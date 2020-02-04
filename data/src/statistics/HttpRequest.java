package statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;


public class HttpRequest {
	public static String sendPost(String urlStr, String param) {
		String sTotalString = "";
		URL url;
		try {
			url = new URL(urlStr);
			//System.out.println(url);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			
			httpConnection.setRequestMethod("POST");// 设置请求方法为post
			httpConnection.setReadTimeout(5000);// 设置读取超时为5秒
			httpConnection.setConnectTimeout(10000);// 设置连接网络超时为10秒
			httpConnection.setDoOutput(true);// 设置此方法,允许向服务器输出内容

            // 获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
            OutputStream out = httpConnection.getOutputStream();// 获得一个输出流,向服务器写数据
            out.write(param.getBytes());
            out.flush();
            out.close();
			
			int responseCode = httpConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				//System.err.println("success");
				InputStream urlStream = httpConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(urlStream, "utf-8"));
				String sCurrentLine = "";
				int i = 0;
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				return sTotalString;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch blockeb
			e.printStackTrace();
		}
		return sTotalString;
	}
	public static String sendGet(String urlStr) {
		String sTotalString = "";
		URL url;
		try {
			url = new URL(urlStr);
			//System.out.println(url);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			int responseCode = httpConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				//System.err.println("success");
				InputStream urlStream = httpConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(urlStream, "utf-8"));
				String sCurrentLine = "";
				int i = 0;
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				return sTotalString;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch blockeb
			e.printStackTrace();
		}
		return sTotalString;
	}
}