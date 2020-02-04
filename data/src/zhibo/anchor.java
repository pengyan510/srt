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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

public class anchor {
	
	public static void type() throws Exception {
		FileInputStream is = new FileInputStream("./anchor/list_2017022022.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("./anchor/list_type.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String line;
		Map<String, Integer>map = new HashMap();
		Map<String, Integer>city = new HashMap();
		Pattern p = Pattern.compile("\"tab_name\":\"(.*?)\",");
		Matcher m;
		Pattern l = Pattern.compile("\"cover\":(.*?)\\]}\\]}");
		Matcher lm;
		while((line = in.readLine()) != null)
		{
			lm = l.matcher(line);
			while(lm.find()) {
				String curline = lm.group(1);
				m = p.matcher(curline);
				if(!m.find()) {
					continue;
				} else {
					while(true) {
						Boolean flag = false;
						String str = m.group(1);
						if(!m.find()) {
							if(city.containsKey(str)) {
								city.put(str, city.get(str) + 1);
							} else {
								city.put(str, 1);
							}
							break;
						}
						if(!flag){
//							if(str.contains("市"))
//								System.out.println(str + "\t" + curline);
							if(map.containsKey(str)) {
								map.put(str, map.get(str) + 1);
							} else {
								map.put(str, 1);
							}
						}
					}
				}
			}
		} 
		for(Entry<String, Integer>entry: map.entrySet()) {
			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		out.write("--------------\n");
		for(Entry<String, Integer>entry: city.entrySet()) {
			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		
		out.flush();
	}
	public static void test() {
		String s = "投资学";
		System.out.println(s.length() + "\t" + s.charAt(0));
	}

	public static void main(String[] args) throws Exception {
		type();
	}
}
