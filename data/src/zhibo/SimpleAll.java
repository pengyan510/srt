package zhibo;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import zhibo.HttpRequest;

public class SimpleAll {

	//获得映客首页直播list
	public static List<String> simpleAll() {
		List<String> list = new ArrayList<String>();   
		String urlStr = "http://120.55.238.158/api/live/simpleall";
		Pattern p = Pattern.compile("\"creator\".*?\"id\":(.*?),.*?,\"id\":\"(.*?)\",");
		String sTotalString = HttpRequest.sendGet(urlStr);
		Matcher m = p.matcher(sTotalString);
		while(m.find()) {
			//System.out.println(m.group(1));
			list.add(m.group(1) + "\t" + m.group(2));
		}
		return list;
	}
	public static void main(String[] args) throws IOException, Exception {
		
		FileOutputStream os = new FileOutputStream("./file/today.txt", true);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		
		while(true) {
			List<String> list = simpleAll();
			for(String elem: list) {
				out.write(elem + "\n");
			}
			out.flush();
			Thread.sleep(5000);
		}		
    }
	
}
