package zhibo;
import zhibo.HttpRequest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

public class test {
	public static void test() throws Exception {
		FileInputStream is = new FileInputStream("./file/userinfo.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = in.readLine();
		double count = 0, i = 0, female = 0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			if(data.length < 6) {
				System.out.println(line);
				continue;
			}
			int x = Integer.parseInt(data[4]);
			if(x > 1000) {
				System.out.println(x);
			}
		}
	}
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
	public static void tmp() {
		List<String> list = getBoard("45952027");
		for(String line: list) {
			String time = DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date());
			System.out.println(time + "\t" + line + "\n");
		}
}
	public static void main(String[] args) throws Exception {
//		test();
		tmp();
	}
}
