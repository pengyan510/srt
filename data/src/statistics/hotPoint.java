package statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class hotPoint {
	
	public static void cleanData() throws InterruptedException, IOException {
		FileOutputStream os;
		BufferedWriter out;
		FileInputStream is;
		BufferedReader in;
		String line = null;
		
		is = new FileInputStream("./hotId.txt");
		in = new BufferedReader(new InputStreamReader(is));
		Map<String, Integer>map = new HashMap();
		while((line = in.readLine()) != null) {
			map.put(line, 0);
		}

		File dir = new File("/home/medialab/yyp/point/file");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	String filename = file.getName();
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		os = new FileOutputStream("./data/" + filename);
    		out = new BufferedWriter(new OutputStreamWriter(os));
    		out.write("id	gold	point\n");
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			if(map.containsKey(data[0])) {
    				out.write(line + "\n");
    			}
    		}
    		out.flush();
        }
	}
	public static void findid() throws InterruptedException, IOException {
		FileOutputStream os = new FileOutputStream("./tmp.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("./hot.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Map<String, Integer>map = new HashMap();
		
		while((line = in.readLine()) != null) {
			map.put(line, 0);
		}
		is = new FileInputStream("./near.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			map.put(line, 0);
		}
		
		is = new FileInputStream("./point/id.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			if(map.containsKey(line)) {
				out.write(line + "\n");
			}
		}
		out.flush();

	}
	
	public static void earnpoint() throws InterruptedException, IOException {
		FileOutputStream os = new FileOutputStream("./earn.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("./point_0109");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = in.readLine();
		Map<String, String>map = new HashMap();
		
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			map.put(data[0], data[1] + "\t" + data[2]);
		}
		
		is = new FileInputStream("./point_0409");
		in = new BufferedReader(new InputStreamReader(is));
		out.write("user\tgold\tpoint\n");
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			String []pre = map.get(data[0]).split("\t");
			long gold = Long.parseLong(data[1]) - Long.parseLong(pre[0]), point = Long.parseLong(data[2]) - Long.parseLong(pre[1]);
			out.write(data[0] + "\t" + gold + "\t" + point + "\n");
		}
		out.flush();

	}
	public static void hotnear() throws InterruptedException, IOException {
		FileOutputStream os = new FileOutputStream("./earn_hot.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os1 = new FileOutputStream("./earn_near.txt");
		BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(os1));
		String line = null;
		Map<String, Integer>map = new HashMap();
		Map<String, Integer>map1 = new HashMap();
		
		FileInputStream is = new FileInputStream("./hot.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String[]data = line.split("\t");
			map.put(data[0], 0);
		}
		is = new FileInputStream("./near.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String[]data = line.split("\t");
			map1.put(data[0], 1);
		}
		
		is = new FileInputStream("./earn.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(map.containsKey(data[0])) {
				out.write(line + "\n");
			} else if(map1.containsKey(data[0])){
				out1.write(line + "\n");
			} else {
				System.out.println(line);
			}
		}
		out.flush();
		out1.flush();
	}
	
	
	public static void main(String[] args) throws IOException, Exception {

//		findid();
//		cleanData();
//		earnpoint();
		hotnear();
	}
}
