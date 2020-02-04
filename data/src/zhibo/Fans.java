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

public class Fans {
	private static final int THREAD_NUMBER = 10;
	
	private static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024*1024*8);
	private static BlockingQueue<String> filequeue = new ArrayBlockingQueue<String>(1024*1024*8);
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(THREAD_NUMBER);
	private static FileOutputStream os;
    private static BufferedWriter out;
    private static FileInputStream is;
    private static BufferedReader in;
    
    //获得用户粉丝
	public static List<String> getFans(String id) throws Exception {
		int start = 0;
		String sTotalString = "";
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\"user\":(.*?)}");
		Matcher m;

		while(true) {
			try {
				sTotalString = HttpRequest.sendGet("http://service.inke.com/api/user/relation/fans?uid=251464826&sid=20i1SDX7Hf45qvNsi0JleL0TD8i2elR4rRyx3XIceUhS4laNkRsse&id=" + id + "&start=" + start + "&count=20");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("sendget error");
					Thread.sleep(10000);
					start -= 20;
				}
			if(!sTotalString.contains("\"users\"")) {
	        	break;
	        }
	        m = p.matcher(sTotalString);
	        while(m.find()) {
	        	list.add(m.group(1));
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
	
	public static void getUsers() throws InterruptedException, IOException {
		System.out.println("read file");
		String line = null;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			queue.put(data[0]);
//			while(!queue.offer(data[0])) {
//				System.out.println("queue full");
//				Thread.sleep(10000);
//			}
		}
	}
	
	public static void main(String[] args) throws IOException, Exception {
		is = new FileInputStream("./file/id1.txt");
		in = new BufferedReader(new InputStreamReader(is));
		
		service.scheduleAtFixedRate(
		        () -> {
		        	try {
					getUsers();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("read file error");
					}}, 0, 20, TimeUnit.SECONDS);
					
//		String line = null;
//		while((line = in.readLine()) != null)
//		{
//			String []data = line.split("\t");
//			queue.put(data[0]);
//		}
		System.out.println(queue.size());
		
		os = new FileOutputStream("./file/fans1" + ".txt");
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
							System.out.println("take element error");
							e.printStackTrace();
						}

						List<String> contri;
						try {
							contri = getFans(id);
							for(String elem: contri) {
								while(!filequeue.offer(id + "\t" + elem)) {
									System.out.println("file queue full");
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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

