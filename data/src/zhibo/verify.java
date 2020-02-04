package zhibo;
import zhibo.HttpRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;
import zhibo.OnlineUser;
import zhibo.HttpRequest;

public class verify {
	private static final int THREAD_NUMBER = 10;
	private static int LEVEL = 1;
	
//	private static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024*1024);
	private static BlockingQueue<String> filequeue = new ArrayBlockingQueue<String>(1024*128);
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(THREAD_NUMBER);
	private static FileOutputStream os;
    private static BufferedWriter out;
	
	public static List<String> getBoard(String id) {
		int start = 0;
		String sTotalString;
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\"contribution\":(.*?),.*?\"id\":(.*?),");
		Matcher m;
		
		while(true) {
			sTotalString = HttpRequest.sendGet("http://120.55.238.158/api/statistic/contribution?uid=251464826&count=200&id=" + id + "&start=" + start);
	        if(!sTotalString.contains("\"contributions\":[{\"contribution\"")) {
	        	break;
	        }
	        m = p.matcher(sTotalString);
	        while(m.find()) {
	        	list.add(m.group(2) + "\t" + m.group(1));
	        }
	        start += 200;
	        //只取第一页
	        break;
		}
		return list;
	}
	
	public static String getPoint(String id) {
		String res = "0\t0";
		String urlStr = "http://120.55.238.158/api/statistic/inout?uid=251464826&id=" + id;
		Pattern p = Pattern.compile("\"gold\": (.*?), \"point\": (.*?)}");
		String sTotalString = HttpRequest.sendGet(urlStr);
		Matcher m = p.matcher(sTotalString);
		if(m.find()) {
			res = m.group(2);
		}
		return res;
	}
	
	public static void writeFile() throws InterruptedException, IOException {
		while(!filequeue.isEmpty()) {
			String tmp = filequeue.take();
			out.write(tmp + "\n");
		}
		out.flush();
	}
	
	public static void main(String[] args) throws IOException {
		
		FileInputStream is = new FileInputStream("./file/anchor.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
//			if(!map.containsKey(data[0])) 
			{
//				map.put(data[0], 1);
				queue.offer(data[0]);
			}
		}
		
		os = new FileOutputStream("./file/anchor_board"+ ".txt");
		out = new BufferedWriter(new OutputStreamWriter(os));
		
		for(int i = 0; i < 9; i++) {
			service.scheduleWithFixedDelay(
					() -> {
						String id = "";
						if(queue.size() == 0) {
							System.out.println("queue empty");
						}
						try {
							id = queue.take();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						String point = getPoint(id);
						List<String> contri = getBoard(id);
						for(String elem: contri) {
							//write the contribution record to log
							if(!filequeue.offer(id + "\t" + point + "\t" + elem)) {
								System.out.println("file queue full");
							}
						}
					}, 3, 1, TimeUnit.MILLISECONDS);
		}
		
		service.scheduleWithFixedDelay(
		        () -> {
		        try {
					writeFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}, 10, 1, TimeUnit.SECONDS);
		
    }
}
