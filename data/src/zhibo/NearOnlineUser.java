package zhibo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zhibo.HttpRequest;
public class NearOnlineUser {
	private static final int THREAD_NUMBER = 10;
	
	private static Map<String, String> map = new ConcurrentHashMap<String, String>();
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);
	private static BlockingQueue<String> filequeue = new ArrayBlockingQueue<String>(2048);
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(THREAD_NUMBER);
	private static FileOutputStream os;
    private static BufferedWriter out;
    
    //获得映客附近直播list
	public static void simpleAll() {
		String urlStr = "http://120.55.238.158/api/live/near_flow_old?uid=251464826";
		Pattern p = Pattern.compile("\"id\":(.*?),\"level\":(.*?),.*?\"nick\":\"(.*?)\",.*?,\"id\":\"(.*?)\",");
		String sTotalString = HttpRequest.sendGet(urlStr), oldValue;
		Matcher m = p.matcher(sTotalString);
		while(m.find()) {
			System.out.println(m.group(1));
			oldValue = map.get(m.group(1));
	        if (oldValue == null) {
	        	//if(!map.containsKey(m.group(1))) {
					map.put(m.group(1), "0\t0");
				//}
				if(!queue.offer(m.group(1) + "\t" + m.group(4) + "\t" + m.group(2) + "\t" + m.group(3))) {
	            	System.out.println("queue error");
	            }
	        }
		}
	}
	
	//获得当前房间号的在线人数
	public static String onlineUser(String id) {
		String usercnt = "0";
		String urlStr = "http://120.55.238.158/api/live/info?uid=251464826&id=" + id;
		Pattern p = Pattern.compile("\"online_users\": (.*?), \"");
		String sTotalString = HttpRequest.sendGet(urlStr);
		Matcher m = p.matcher(sTotalString);
		if(m.find()) {
			usercnt = m.group(1);
		}
		return usercnt;
	}
	public static void writeFile() throws InterruptedException, IOException {
		while(!filequeue.isEmpty()) {
			String tmp = filequeue.take();
			out.write(tmp + "\n");
			out.flush();
		}
		out.flush();
	}
	public static void main(String []args) throws IOException {
		os = new FileOutputStream("./file/zhibo_fujin.txt");
		out = new BufferedWriter(new OutputStreamWriter(os));
		service.scheduleAtFixedRate(
		        () -> {simpleAll();}, 0, 20, TimeUnit.SECONDS);

		for(int i = 0; i < 8; i++) {
			service.scheduleWithFixedDelay(
					() -> {
						String id = "", roomid = "", tmp = "";
						try {
							tmp = queue.take();
							System.out.println(tmp);
							id = tmp.split("\t")[0];
							roomid = tmp.split("\t")[1];
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String usercnt = onlineUser(roomid);
						if(!filequeue.offer(tmp + "\t" + usercnt)) {
							System.out.println("file queue full");
						}
						
						String oldvalue = map.get(id).split("\t")[0];
						String cnt = map.get(id).split("\t")[1];
						if(oldvalue.equals(usercnt)) {
							if(cnt.equals("0")) {
								map.put(id, usercnt + "\t1");
								queue.offer(tmp);
							} else if(cnt.equals("1")) {
								map.remove(id);
							}
						} else {
							map.put(id, usercnt + "\t0");
							queue.offer(tmp);
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
				}}, 10, 10, TimeUnit.SECONDS);
	}
}