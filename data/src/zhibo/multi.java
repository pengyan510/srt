package zhibo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class multi implements Runnable {
	   private String id;
	   
	   public multi(String message) {
	      this.id = message;
	   }
	   
	   public void run() {
		   int page = 0;
			String urlStr = "http://120.55.238.158/api/live/users?uid=251464826&count=20&id=" + id + "&start=";
			Pattern p = Pattern.compile("\"id\":(.*?),");
			HashMap<String, Integer> dict = new HashMap<String, Integer>();
			while(true) {
				URL url;
				try {
					url = new URL(urlStr + page);
					//System.out.println(url);
					URLConnection URLconnection = url.openConnection();
					HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
					int responseCode = httpConnection.getResponseCode();

					if (responseCode == HttpURLConnection.HTTP_OK) {
						System.err.println("success");
						InputStream urlStream = httpConnection.getInputStream();
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(urlStream, "utf-8"));
						String sCurrentLine = "";
						String sTotalString = "";
						int i = 0;
						while ((sCurrentLine = bufferedReader.readLine()) != null) {
							sTotalString += sCurrentLine;
						}
						if(sTotalString.contains("内部系统错误"))
							break;
						//System.out.println(sTotalString);
						Matcher m = p.matcher(sTotalString);
						if(m.find()) {
							if(dict.containsKey(m.group(1)))
			        			dict.put(m.group(1), dict.get(m.group(1)) + 1);
			        		else
			        			dict.put(m.group(1), 1);
							while(m.find()) {
				        		if(dict.containsKey(m.group(1)))
				        			dict.put(m.group(1), dict.get(m.group(1)) + 1);
				        		else
				        			dict.put(m.group(1), 1);
				        	}
						} else {
							break;
						}
			        	//System.out.println(dict.size());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch blockeb
					e.printStackTrace();
				}
				page += 20;
			}
			//System.out.println(page);
			int count = 0;
			for (Entry<String, Integer> entry : dict.entrySet()) {  
				 count++;
			    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
			}
			System.out.println(id + "\t" + count);
	   }
	   public static void main(String []args) throws IOException {
			multi hello = new multi("1479989298093505");
			  Thread thread1 = new Thread(hello);
			  System.out.println("Starting hello thread...");
			  
			  
			  multi bye = new multi("1479989385150281");
		      Thread thread2 = new Thread(bye);
		      System.out.println("Starting goodbye thread...");
		      thread1.start();
		      thread2.start();
		}
	}