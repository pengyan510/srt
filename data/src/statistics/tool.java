package statistics;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tool {
	public static void getContri() throws IOException {
		FileOutputStream os;
		BufferedWriter out;
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<String, Integer>map = new HashMap();
		is = new FileInputStream("/home/medialab/yyp/hid.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(!map.containsKey(data[0])) {
				map.put(data[0], 1);
			}
		}
		
		File dir = new File("/home/medialab/yyp/data");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contri"))
        		continue;
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		os = new FileOutputStream("/home/medialab/yyp/contri/" + file.getName());
    		out = new BufferedWriter(new OutputStreamWriter(os));
    		
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
				if(map.containsKey(data[0])) {
					out.write(line + "\n");
				}
    		}
    		out.flush();
        }

	}
	public static void fecthData() throws InterruptedException, IOException {
		FileOutputStream os = new FileOutputStream("./followingall.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os1 = new FileOutputStream("./followinglack.txt");
		BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(os1));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Pattern p = Pattern.compile(",\"id\":(.*?),");
		Matcher m;
		
		File dir = new File("./following");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("hotfollowing"))
        		continue;
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
				m = p.matcher(data[1]);
				if(m.find()) {
					out.write(data[0] + "\t" + m.group(1) + "\n");
				} else {
					out1.write(line + "\n");
				}
    		}
        }
        out1.flush();
        out1.close();
		out.flush();
		out.close();
	}
	public static void fecthhotid() throws InterruptedException, IOException {
		FileOutputStream os = new FileOutputStream("./hid.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Pattern p = Pattern.compile("\"creator\".*?\"id\":(.*?),");
		Matcher m;
		Map<String, Long>map = new HashMap();
		
		File dir = new File("./file");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("list"))
        		continue;
        	int d = Integer.parseInt(file.getName().replace("list_20170", "").replace("22.txt", "").replace("10.txt", "").replace("04.txt", ""));
        	if(d <= 101)
        		continue;
        	
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			if(line.contains("online")) {
    				int n = 0;
					m = p.matcher(line);
					while(m.find()) {
//						n++;
						String id = m.group(1).trim().replaceAll(" ", "");
						if(map.containsKey(id))
							map.put(id, map.get(id) + 1);
						else
							map.put(id, (long) 1);
					}
//					System.out.println(n);
    			}
    		}
        }
        for(Entry<String, Long>entry: map.entrySet()) {
        	out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
        }
		out.flush();
		out.close();
	}
	public static void incomecdf() throws Exception {
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/boardpoint.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("/Users/yiping/Desktop/file/boardpointcdf");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String line;
		double x, y;
		int k = 0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			x = Double.parseDouble(data[2]);
			if(x == 0) {
				k++;
				continue;
			}
			y = Math.log10(Math.abs(x));
			if(x < 0) y = -y;
			out.write(line + "\t" + y + "\n");
		}
		System.out.println(k);
		out.flush();
	}
	public static void countFollow() throws Exception {
		FileInputStream is = new FileInputStream("./followingall.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = in.readLine();
		Map<String, Integer>map = new HashMap();
		
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(!map.containsKey(data[0])) {
				map.put(data[0], 1);
			}
		}
		long s = 0, m = 0;
		File dir = new File("./data");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String[]data = line.split("\t");
    			if(map.containsKey(data[1])) {
    				s++;
    				m += Integer.parseInt(data[2]);
    			}
    		}
        }
        System.out.println(s + "\t" + m);
        
        long p = 0, mo = 0;
        is = new FileInputStream("./mid_result");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String[]data = line.split("\t");
			if(map.containsKey(data[0])) {
				p++;
				mo += Integer.parseInt(data[2]);
			}
		}
		System.out.println(p + "\t" + mo);
	}
	public static void test() throws Exception {
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/followinglack.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("/Users/yiping/Desktop/file/tmp");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os1 = new FileOutputStream("/Users/yiping/Desktop/file/tmp1");
		BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(os1));
		String line = in.readLine();
		Pattern p = Pattern.compile("\"id\":(.*?),");
		Matcher m;
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(data[1].contains("感谢亲姐 笨女人")) {
				out.write(data[0] + "\t" + "3305468" + "\n");
				continue;
			}
			m = p.matcher(data[1]);
			if(m.find()) {
				System.out.println(m.group(1));
				out.write(data[0] + "\t" + m.group(1) + "\n");
			} else {
				out1.write(line + "\n");
			}
		}
		out.flush();
		out1.flush();
		out.close();
		out1.close();
	}
	public static void countpair() throws IOException {
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<String, Integer>map = new HashMap();
		String user = "";
		
		File dir = new File("./contri");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_", "").replace(".txt", "");
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			String str = data[0] + "\t" + data[1];
    			if(data[0].compareTo(data[1]) > 0)
    				str = data[1] + "\t" + data[0];
    			if(!map.containsKey(str)) {
    				map.put(str, 1);
    			}
    		}
        }
        System.out.println(map.size());
	}
	
	public static void fetchfrombase() throws Exception {
		FileInputStream is;
		BufferedReader in;
		FileOutputStream os = new FileOutputStream("/Users/yiping/Desktop/file/userfeature.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		is = new FileInputStream("/Users/yiping/Desktop/file/treat.txt");
		in = new BufferedReader(new InputStreamReader(is));
		String line;
		Pattern p = Pattern.compile("\"emotion\":\"(.*?)\",.*?gender\":(.*?),.*?location\":\"(.*?)\",\"birth\":\"(.*?)\",.*?\"level\":(.*?),");
		Matcher m;
		Map<String, Integer>map = new HashMap();
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			map.put(data[0], 1);
		}
		Map<String, Integer>e = new HashMap();
		e.put("同性", 1);
		e.put("恋爱中", 2);
		e.put("已婚", 3);
		e.put("保密", 4);
		e.put("单身", 5);
		
		out.write("point\ttreat\tid\tfans\tfollowing\tweibo\temotion\tgender\tlocation\tbirth\tlevel\n");
		is = new FileInputStream("/Users/yiping/Desktop/file/userinfo.txt");
		in = new BufferedReader(new InputStreamReader(is));
		
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			m = p.matcher(data[1]);
			if(m.find()) {
//				String str = m.group(1) + "\t" + m.group(2) + "\t" + m.group(3) + "\t" + m.group(4) + "\t" + m.group(5);
//				out.write(data[3] + "\t" + data[0] + "\t" + data[4] + "\t" + data[5] + "\t" + data[6] + "\t" + str + "\n");
				String str = "";
				if(m.group(1).length() < 1)
					str = "0";
				else
					str = str + e.get(m.group(1));
				//gender
				str = str + "\t" + m.group(2);
				//age level
				String date = m.group(4).split("-")[0];
				int d = 2017 - Integer.parseInt(date);
				str = str + "\t" + d + "\t" + m.group(5);
				
				String flag = "0";
				if(map.containsKey(data[0])) {
					flag = "1";
				}
				out.write(data[3] + "\t" + flag + "\t" + data[0] + "\t" + data[4] + "\t" + data[5] + "\t" + data[6] + "\t" + str + "\n");
				
			} else {
				System.out.println(line);
			}
		}
		out.flush();
	}
	public static void main(String[] args) throws Exception {
//		getContri();
//		fecthData();
//		cleanContri();
//		fecthhotid();
//		incomecdf();
//		countFollow();
//		test();
//		countpair();
//		fetchfrombase();
	}
}
