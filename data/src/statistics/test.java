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
import java.util.Map.Entry;

public class test {
	
	public static void cleanData() throws InterruptedException, IOException {
		FileOutputStream os;
		BufferedWriter out;
		FileInputStream is;
		BufferedReader in;
		String line = null;

		File dir = new File("/home/medialab/yyp/file");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	String filename = file.getName();
        	if(!filename.contains("contribution"))
        		continue;
        	
        	Map<String, Integer>map = new HashMap();
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		os = new FileOutputStream("./data/" + filename);
    		out = new BufferedWriter(new OutputStreamWriter(os));
    		while((line = in.readLine()) != null) {
    			String tmp = line.replaceAll(" ", "").trim();
    			String []data = tmp.split("\t");
    			String key = data[0] + "\t" + data[1];
    			int m = Integer.parseInt(data[2]);
    			if(!map.containsKey(key)) {
    				map.put(key, m);
    			} else {
    				if(m > map.get(key))
    					map.put(key, m);
    			}
    		}
    		for(Entry<String, Integer>entry: map.entrySet()) {
    			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
    		}
    		out.flush();
        }
	}

	public static void findid() throws InterruptedException, IOException {
		FileOutputStream os = new FileOutputStream("./tmp.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("./hotid.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Map<String, Integer>map = new HashMap();
		
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
	public static void countjiao() throws InterruptedException, IOException {
//		FileOutputStream os = new FileOutputStream("./tmp.txt");
//		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/userfeature.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Map<String, Integer>map = new HashMap();
		
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			map.put(data[2], 0);
		}
		int i = 0;
		is = new FileInputStream("/Users/yiping/Documents/workspace/zhibo/file/anchor_board.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(map.containsKey(data[0])) {
				i++;
			}
		}
		System.out.println(i);

	}
	
	public static void tmp() throws InterruptedException, IOException {
		String s = "20170101";
		String t = s.substring(0, 4) + "/" + s.substring(4, 6) + "/" + s.substring(6);
		System.out.println(t);
				
	}
	public static void main(String[] args) throws IOException, Exception {
//		cleanData();
//		tmp();
//		findid();
		countjiao();
	}
}
