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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;
import zhibo.OnlineUser;
import zhibo.HttpRequest;

public class contribution {
	private static final int THREAD_NUMBER = 10;
	private static int LEVEL = 1;
	
	private static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024*1024*8);
	private static BlockingQueue<String> filequeue = new ArrayBlockingQueue<String>(1024*1024*8);
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(THREAD_NUMBER);
	private static FileOutputStream os;
    private static BufferedWriter out;
    private static FileInputStream is;
    private static BufferedReader in;
    
    //获得用户映票贡献榜日榜
	public static List<String> getBoard(String id) {
		int start = 0;
		String sTotalString;
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\"contribution\":(.*?),.*?\"id\":(.*?),");
		Matcher m;
		
		JSONObject user = new JSONObject(); 
		user.put("count", "20");  
        user.put("id", id);    
        user.put("request_id", "251464826"); 
        
		while(true) {
			user.put("start", start);
			sTotalString = HttpRequest.sendPost("http://service.inke.com/api/day_bill_board/board?", user.toString());
	        if(sTotalString.contains("\"count\":0")) {
	        	break;
	        }
//	        System.out.println(sTotalString);
	        m = p.matcher(sTotalString);
	        while(m.find()) {
	        	list.add(m.group(2) + "\t" + m.group(1));
	        }
	        start += 20;
		}
		return list;
	}
	
	public static void writeFile() throws InterruptedException, IOException {
		while(!filequeue.isEmpty()) {
			String tmp = filequeue.take();
			out.write(tmp + "\n");
		}
		out.flush();
	}
	
	
	public static void main(String[] args) throws IOException, Exception {
		
		FileInputStream is = new FileInputStream("./tmp.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Pattern p = Pattern.compile("\"creator\".*?\"id\":(.*?),");
		Matcher m;
		
		long start=System.currentTimeMillis();   //获取开始时间
		while((line = in.readLine()) != null)
		{
			m = p.matcher(line);
			while(m.find()) {
				map.put(m.group(1), 1);
			}
		}
		System.out.println(map.size());
		for(String item: map.keySet()) {
			queue.add(item);
		}
		System.out.println(queue.size() + " users");
		
		os = new FileOutputStream("./file/contribution" + ".txt");
		out = new BufferedWriter(new OutputStreamWriter(os));
		
		for(int i = 0; i < 9; i++) {
			service.scheduleWithFixedDelay(
					() -> {
						String id = "";
						if(queue.size() == 0) {
							System.out.println("queue empty");
							long end=System.currentTimeMillis(); //获取结束时间
							System.out.println("程序运行时间： "+(end-start)+"ms"); 
							try {
								Thread.sleep(3600000);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							id = queue.take();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						List<String> contri = getBoard(id);
						for(String elem: contri) {
							if(!filequeue.offer(id + "\t" + elem)) {
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

