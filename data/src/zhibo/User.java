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
import java.text.DateFormat;
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

import org.json.JSONObject;

import zhibo.HttpRequest;

//给定用户id，监控该id的所有活动，以及其直播间的所有信息
public class User {
	private static final int THREAD_NUMBER = 7;
	
	private static Boolean ispub = false;
	private static String roomid = null;
	private static Map<String, String> map = new ConcurrentHashMap<String, String>();
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024*1024*128);
	private static BlockingQueue<String> filequeue = new ArrayBlockingQueue<String>(2048*1024*64);
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(THREAD_NUMBER);
    
	//获得用户point和gold
    public static String getPoint(String id) {
		String res = "0\t0";
		String urlStr = "http://120.55.238.158/api/statistic/inout?uid=251464826&id=" + id;
		Pattern p = Pattern.compile("\"gold\": (.*?), \"point\": (.*?)}");
		String sTotalString = HttpRequest.sendGet(urlStr);
		Matcher m = p.matcher(sTotalString);
		if(m.find()) {
			res = m.group(1) + "\t" + m.group(2);
		}
		return res;
	}
	
   //获得用户基本信息
	public static String getInfo(String id) {
		String res = "0\t0";
		String urlStr = "http://120.55.238.158/api/user/info?uid=251464826&id=" + id;
		String sTotalString = HttpRequest.sendGet(urlStr);
		res = sTotalString;
		return res;
	}
	
	//获得用户fans和following
	public static String getFans(String id) {
		String res = "0\t0";
		String urlStr = "http://120.55.238.158/api/user/relation/numrelations?uid=251464826&id=" + id;
		Pattern p = Pattern.compile("\"num_followers\":(.*?),\"num_followings\":(.*?)}");
		String sTotalString = HttpRequest.sendGet(urlStr);
		Matcher m = p.matcher(sTotalString);
		if(m.find()) {
			res = m.group(1) + "\t" + m.group(2);
		}
		return res;
	}
	
	//获得用户贡献榜
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
	        m = p.matcher(sTotalString);
	        while(m.find()) {
	        	list.add(m.group(2) + "\t" + m.group(1));
	        }
	        start += 20;
		}
		return list;
	}
	 
	//获得当前房间号里的用户
	public static List<String> roomUser(String roomid) {
		int page = 0;
		List<String>list = new ArrayList<String>();
		String urlStr = "http://120.55.238.158/api/live/users?uid=251464826&count=20&id=" + roomid + "&start=";
		
		while(true) {
			String sTotalString = HttpRequest.sendGet(urlStr + page);
			if(!sTotalString.contains("emotion"))
				break;
			list.add(sTotalString);
			page += 20;
		}
		return list;
	}
	
	//获得当前房间号在线用户数
	public static String onlineUser(String roomid) {
		String usercnt = "0";
		String urlStr = "http://120.55.238.158/api/live/info?uid=251464826&id=" + roomid;
		Pattern p = Pattern.compile("\"online_users\": (.*?), \"");
		String sTotalString = HttpRequest.sendGet(urlStr);
		Matcher m = p.matcher(sTotalString);
		if(m.find()) {
			usercnt = m.group(1);
		}
		return usercnt;
	}
	
	//获得当前用户状态（是否直播）
	public static String nowPublish(String id) {
		String res = "0";
		String urlStr = "http://service.inke.com/api/live/now_publish?cv=IK3.7.20_Android&uid=251464826&id=" + id;
		String sTotalString = HttpRequest.sendGet(urlStr);
		if(sTotalString.contains("live")) {
			res = sTotalString;
		}
		return res;
	}
	
	//获得映客首页直播list
	public static Boolean simpleAll(String id) {
		String urlStr = "http://120.55.238.158/api/live/simpleall";
		String sTotalString = HttpRequest.sendGet(urlStr), oldValue;
		if(sTotalString.contains("\"id\": " + id + ",") || sTotalString.contains("\"id\":" + id + ",")) {
			return true;
		}
		return false;
	}
	
	//获得好声音直播list
	public static Boolean goodVoice(String id) {
		String urlStr = "http://service.inke.com/api/live/themesearch?uid=251464826&keyword=666ABA8214206E5B";
		String sTotalString = HttpRequest.sendGet(urlStr), oldValue;
		if(sTotalString.contains("\"id\": " + id + ",") || sTotalString.contains("\"id\":" + id + ",")) {
			return true;
		}
		return false;
	}
	
	//获得才艺直播list
	public static Boolean skill(String id) {
		String urlStr = "http://service.inke.com/api/live/themesearch?uid=251464826&keyword=AFCC0BC263924F20";
		String sTotalString = HttpRequest.sendGet(urlStr), oldValue;
		if(sTotalString.contains("\"id\": " + id + ",") || sTotalString.contains("\"id\":" + id + ",")) {
			return true;
		}
		return false;
	}
	
	public static void main(String []args) throws IOException {
		String []uid = {"9028876", "39657083", "631180", "332490251", "4898129", "46147663", "323285590", "262960", "64725155", "57096255"};
		String id = uid[9];
		//检测是否直播
		service.scheduleWithFixedDelay(
		        () -> {
		        	String res = nowPublish(id);
		        	if(res.equals("0")) {
		        		ispub = false;
		        	} else {
		        		Pattern p = Pattern.compile(",\"creator\":(.*?),\"id\":\"(.*?)\",");
		        		Matcher m = p.matcher(res);
		        		if(m.find()) {
		        			ispub = true;
		        			roomid = m.group(2);
		        		}
		        		FileOutputStream ostmp;
						try {
							ostmp = new FileOutputStream("./file/ispub.txt", true);
							BufferedWriter outtmp = new BufferedWriter(new OutputStreamWriter(ostmp));
			        		String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			        		outtmp.write(time + "\t" + res + "\n");
			        		outtmp.flush();
			        		outtmp.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("nowpublish\n");
						}
		        	}
		        }, 0, 60, TimeUnit.SECONDS);
		
		//直播时获得房间人数
		service.scheduleWithFixedDelay(
		        () -> {
		        	if(ispub) {
		        		String online = onlineUser(roomid);
		        		FileOutputStream ostmp;
						try {
							ostmp = new FileOutputStream("./file/room_number.txt", true);
							BufferedWriter outtmp = new BufferedWriter(new OutputStreamWriter(ostmp));
			        		String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			        		outtmp.write(time + "\t" + online + "\n");
			        		outtmp.flush();
			        		outtmp.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("onlineuser\n");
						}
		        		
		        	}
		        }, 1, 30, TimeUnit.SECONDS);
		
		//直播时获得房间用户
		service.scheduleWithFixedDelay(
		        () -> {
		        	if(ispub) {
		        		List<String> user = roomUser(roomid);
		        		FileOutputStream ostmp;
						try {
							ostmp = new FileOutputStream("./file/room_user.txt", true);
							BufferedWriter outtmp = new BufferedWriter(new OutputStreamWriter(ostmp));
							String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
							for(String line: user) {
			        			outtmp.write(time + "\t" + line + "\n");
			        		}
			        		outtmp.write("\n\n");
			        		outtmp.flush();
			        		outtmp.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("roomuser\n");
						}
		        		
		        	}
		        }, 1, 60, TimeUnit.SECONDS);
		
		//检查是否在榜上
		service.scheduleWithFixedDelay(
		        () -> {
		        	if(ispub) {
		        		FileOutputStream ostmp;
						try {
							ostmp = new FileOutputStream("./file/on_board.txt", true);
							BufferedWriter outtmp = new BufferedWriter(new OutputStreamWriter(ostmp));
			        		Boolean a = simpleAll(id), b = goodVoice(id), c = skill(id);
			        		if(a || b || c) {
			        			String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			        			outtmp.write(time + "\t" + a + "\t" + b + "\t" + c + "\n");
			        		}
			        		outtmp.flush();
			        		outtmp.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("onboard\n");
						}
		        		
		        	}
		        }, 1, 60, TimeUnit.SECONDS);
		//获得日榜
		service.scheduleWithFixedDelay(
		        () -> {
		        	if(ispub) {
		        		FileOutputStream ostmp;
						try {
							ostmp = new FileOutputStream("./file/day_board.txt", true);
							BufferedWriter outtmp = new BufferedWriter(new OutputStreamWriter(ostmp));
			        		List<String> list = getBoard(id);
//			        		System.out.println(list.size());
			        		String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			        		for(String line: list) {
			        			outtmp.write(time + "\t" + line + "\n");
			        		}
			        		outtmp.write("\n\n");
			        		outtmp.flush();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("dayboard\n");
						}
		        		
		        	}
		        }, 1, 180, TimeUnit.SECONDS);
		//获得关系和收支
		service.scheduleWithFixedDelay(
		        () -> {
		        	if(ispub) {
		        		FileOutputStream ostmp;
						try {
							ostmp = new FileOutputStream("./file/" + id + "_relation.txt", true);
							BufferedWriter outtmp = new BufferedWriter(new OutputStreamWriter(ostmp));
			        		String res = getFans(id);
			        		String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			        		outtmp.write(time + "\t" + res + "\t");
			        		res = getPoint(id);
//			        		time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			        		outtmp.write(time + "\t" + res + "\n");
			        		outtmp.flush();
			        		outtmp.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("relationpoint\n");
						}
		        		
		        	}
		        }, 1, 180, TimeUnit.SECONDS);
		//获得基本信息
		service.scheduleWithFixedDelay(
		        () -> {
		        	if(ispub) {
		        		FileOutputStream ostmp;
						try {
							ostmp = new FileOutputStream("./file/" + id + "_info.txt", true);
							BufferedWriter outtmp = new BufferedWriter(new OutputStreamWriter(ostmp));
			        		String res = getInfo(id);
			        		String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			        		outtmp.write(time + "\t" + res + "\n");
			        		outtmp.flush();
			        		outtmp.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("info\n");
						}
		        		
		        	}
		        }, 1, 300, TimeUnit.SECONDS);
		
	}
}