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

public class room {
	private static final int THREAD_NUMBER = 10;
	
	private static Map<String, String> map = new ConcurrentHashMap<String, String>();
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024*1024*128);
	private static BlockingQueue<String> filequeue = new ArrayBlockingQueue<String>(2048*1024*64);
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(THREAD_NUMBER);
	private static FileOutputStream os;
    private static BufferedWriter out;
    
   //获得映客首页直播list
	public static void simpleAll() {
		String urlStr = "http://120.55.238.158/api/live/simpleall";
		Pattern p = Pattern.compile("\"creator\":.*?\"id\":(.*?),.*?id\":\"(.*?)\",\"name.*?\"online_users\":(.*?),\"");
		String sTotalString = HttpRequest.sendGet(urlStr), oldValue;
		Matcher m = p.matcher(sTotalString);
		while(m.find()) {
			oldValue = map.get(m.group(2));
	        if (oldValue == null) {
				map.put(m.group(2), m.group(3));
	        	if(!queue.offer(m.group(1) + "\t" + m.group(2))) {
	            	System.out.println("queue error");
	            }
	        }
		}
	}
	
	//获得当前房间号里的用户
	public static List<String> roomUser(String id) {
		int page = 0;
		List<String>list = new ArrayList<String>();
		String urlStr = "http://120.55.238.158/api/live/users?uid=251464826&count=20&id=" + id + "&start=";
		Pattern p = Pattern.compile("\"id\":(.*?),");
		
		while(true) {
			String sTotalString = HttpRequest.sendGet(urlStr + page);
			if(!sTotalString.contains("emotion"))
				break;
//			System.out.println(page + "\t" + sTotalString);
			Matcher m = p.matcher(sTotalString);
			while(m.find()) {
        		list.add(m.group(1));
        	}
			page += 20;
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
	
	//获得当前房间号在线人数
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
	public static void main(String []args) throws IOException {
		os = new FileOutputStream("./file/room.txt");
		out = new BufferedWriter(new OutputStreamWriter(os));
		service.scheduleAtFixedRate(
		        () -> {simpleAll();}, 0, 3, TimeUnit.SECONDS);

		for(int i = 0; i < 8; i++) {
			service.scheduleWithFixedDelay(
					() -> {
						String tmp = "", id = "", roomid = "", cur = "";
						try {
							if(queue.isEmpty()) {
								System.out.println("finish");
							}
							tmp = queue.take();
							id = tmp.split("\t")[0];
							roomid = tmp.split("\t")[1];
							cur = onlineUser(roomid);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!cur.equals(map.get(roomid))) {
							List<String>list = roomUser(roomid);
							for(String elem: list) {
								if(!filequeue.offer(id + "\t" + roomid + "\t" + elem)) {
									System.out.println("file queue full");
								}
							}
							map.put(roomid, cur);
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