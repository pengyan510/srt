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

public class ZhiboList {
	
	public static void main(String[] args) throws IOException, Exception {
		
		FileOutputStream os = new FileOutputStream("./file/today.txt", true);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String urlStr1 = "http://120.55.238.158/api/live/simpleall";
		String urlStr2 = "http://120.55.238.158/api/live/near_flow_old?uid=251464826";
		
		//每隔5秒获得首页和附近直播list
		while(true) {
			String sTotalString = HttpRequest.sendGet(urlStr1);
			out.write(sTotalString + "\n");
			sTotalString = HttpRequest.sendGet(urlStr2);
			out.write(sTotalString + "\n");
			out.flush();
			Thread.sleep(5000);
		}		
    }
	
}
