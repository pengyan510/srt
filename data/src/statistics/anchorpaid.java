package statistics;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class anchorpaid {
	public static void boardperson() throws IOException {
		FileOutputStream os = new FileOutputStream("./boardperson.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		
		File dir = new File("/home/medialab/yyp/contri");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_", "").replace(".txt", "");
        	String date = filename.substring(0, 4) + "/" + filename.substring(4, 6) + "/" + filename.substring(6);
        	int x = Integer.parseInt(filename);
        	if(x < 20170103 || x > 20170309)
        		continue;
        	Map<String, Integer> dic = new HashMap();
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		Long sum = (long) 0;
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			if(dic.containsKey(data[0])) {
    				dic.put(data[0], dic.get(data[0]) + 1);
    			} else {
    				dic.put(data[0],  1);
    			}
    			sum += Integer.parseInt(data[2]);
    		}
//    		for(Entry<String, Integer> entry: dic.entrySet()) {
//    			if(entry.getValue() > 1)
//    				System.out.println(entry.getValue());
//    		}
    		out.write(filename + "\t" + date + "\t" + sum + "\t" + dic.size() + "\n");
        }
		out.flush();
		out.close();
	}
	public static void paid_hot_perc() throws IOException {
		FileOutputStream os = new FileOutputStream("./paid_hot_per.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<String, Long>dic = new HashMap();
		
		File dir = new File("/home/medialab/yyp/contri");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_20170", "").replace(".txt", "");
        	int d = Integer.parseInt(filename);
        	if(d < 109 || d > 309) 
        		continue;
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			int money = Integer.parseInt(data[2]);
    			if(dic.containsKey(data[0])) {
    				dic.put(data[0], dic.get(data[0]) + money);
    			} else {
    				dic.put(data[0],  (long) money);
    			}
    		}
        }
        
        Map<String, Long>pay = new HashMap();
        Map<String, Long>paygold = new HashMap();
    	is = new FileInputStream("/home/medialab/yyp/point/0109");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(dic.containsKey(data[0])) {
				int point = Integer.parseInt(data[2]);
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0],  (long) point);
				paygold.put(data[0], (long)gold);
			}
		}
		
		is = new FileInputStream("/home/medialab/yyp/point/0309");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(dic.containsKey(data[0])) {
				int point = Integer.parseInt(data[2]);
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0], (long)point - pay.get(data[0]));
				paygold.put(data[0], (long)gold - paygold.get(data[0]));
			}
		}
        out.write("id\tgold\tpoint\tdif\n");
        for(Entry<String, Long> entry: pay.entrySet()) {
        	String id = entry.getKey();
//        	long gold = entry.getValue();
//        	long point = dic.get(id);
//        	double per = 0.0;
//        	if(gold >= point) 
//        		per = (double)(point) / gold;
        	long income = entry.getValue(), payout = paygold.get(id);
        	out.write(id + "\t" + income  + "\t" + payout + "\t" + (income-payout) + "\n");
		}
		out.flush();
		out.close();
	}
	
	public static void paidmoney() throws IOException {
		FileOutputStream os = new FileOutputStream("./paidmoney.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<String, Integer>dic = new HashMap();
		
		is = new FileInputStream("/home/medialab/yyp/hid.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(!dic.containsKey(data[0])) {
				dic.put(data[0], 1);
			}
		}
        
        Map<String, Long>pay = new HashMap();
        Map<String, Long>paygold = new HashMap();
    	is = new FileInputStream("/home/medialab/yyp/point/0109");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(dic.containsKey(data[0])) {
				int point = Integer.parseInt(data[2]);
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0],  (long) point);
				paygold.put(data[0], (long)gold);
			}
		}
		
		is = new FileInputStream("/home/medialab/yyp/point/0309");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(dic.containsKey(data[0])) {
				int point = Integer.parseInt(data[2]);
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0], (long)point - pay.get(data[0]));
				paygold.put(data[0], (long)gold - paygold.get(data[0]));
			}
		}
        for(Entry<String, Long> entry: pay.entrySet()) {
        	String id = entry.getKey();
//        	long gold = entry.getValue();
//        	long point = dic.get(id);
//        	double per = 0.0;
//        	if(gold >= point) 
//        		per = (double)(point) / gold;
        	long income = entry.getValue(), payout = paygold.get(id);
        	out.write(id + "\t" + income  + "\t" + payout + "\t" + (income-payout) + "\n");
		}
		out.flush();
		out.close();
	}
	public static void dayPayNumber() throws IOException {
		FileOutputStream os = new FileOutputStream("./day_paid_num.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os1 = new FileOutputStream("./day_paid_numcdf.txt");
		BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(os1));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<Long, Long>map = new HashMap();
		String user = "";
		
		File dir = new File("/home/medialab/yyp/contri");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_", "").replace(".txt", "");
        	Map<String, Long> dic = new HashMap();
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			if(dic.containsKey(data[0])) {
    				dic.put(data[0], dic.get(data[0]) + 1);
    			} else {
    				dic.put(data[0],  (long) 1);
    			}
    		}
    		for(Entry<String, Long> entry: dic.entrySet()) {
    			String u = entry.getKey();
    			long n = entry.getValue();
    			out1.write(n + "\n");
//    			if(n >= 5000) {
//    				System.out.println(u + "\t" + n);
//    				user = user + "\n" + (u + "\t" + n);
//    			}
    			if(map.containsKey(n)) {
    				map.put(n, map.get(n) + 1);
    			} else {
    				map.put(n, (long) 1);
    			}
    		}
        }
        for(Entry<Long, Long> entry: map.entrySet()) {
			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
        out.write("\n" + user);
		out.flush();
		out.close();
		out1.flush();
		out1.close();
	}

	public static void dayPayMoney() throws IOException {
		FileOutputStream os = new FileOutputStream("./day_paid_money.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os1 = new FileOutputStream("./day_paid_moneycdf.txt");
		BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(os1));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<Long, Long>map = new HashMap();
		String user = "";
		
		File dir = new File("/home/medialab/yyp/contri");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_", "").replace(".txt", "");
        	Map<String, Long> dic = new HashMap();
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			long money = Integer.parseInt(data[2]);
    			if(dic.containsKey(data[0])) {
    				dic.put(data[0], dic.get(data[0]) + money);
    			} else {
    				dic.put(data[0],  money);
    			}
    		}
    		for(Entry<String, Long> entry: dic.entrySet()) {
    			String u = entry.getKey();
    			long n = entry.getValue();
    			out1.write(n + "\n");
//    			if(n >= 10218210) {
//    				System.out.println(u + "\t" + n);
//    				user = user + "\n" + (u + "\t" + n);
//    			}
    			if(map.containsKey(n)) {
    				map.put(n, map.get(n) + 1);
    			} else {
    				map.put(n, (long) 1);
    			}
    		}
        }
        for(Entry<Long, Long> entry: map.entrySet()) {
			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
        out.write("\n" + user);
		out.flush();
		out.close();
		out1.flush();
		out1.close();
	}
	
	public static void board_paid() throws InterruptedException, IOException {
		FileOutputStream os = new FileOutputStream("board_paid.txt");
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
        	if(d < 109 || d > 309)
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

        Map<String, Long>pay = new HashMap();
        Map<String, Long>paygold = new HashMap();
    	is = new FileInputStream("/home/medialab/yyp/point/0109");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(map.containsKey(data[0])) {
				int point = Integer.parseInt(data[2]);
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0],  (long) point);
				paygold.put(data[0], (long)gold);
			}
		}
		
		is = new FileInputStream("/home/medialab/yyp/point/0309");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(map.containsKey(data[0])) {
				int point = Integer.parseInt(data[2]);
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0], (long)point - pay.get(data[0]));
				paygold.put(data[0], (long)gold - paygold.get(data[0]));
			}
		}
        for(Entry<String, Long> entry: pay.entrySet()) {
        	String id = entry.getKey();
        	long income = entry.getValue(), payout = paygold.get(id);
        	out.write(id + "\t" + map.get(id) + "\t" + income  + "\t" + payout + "\t" + (income-payout) + "\n");
		}
		out.flush();
		out.close();
	}

	public static void main(String[] args) throws Exception {
//		boardperson();
//		paid_hot_perc();
//		paidmoney();
//		dayPayNumber();
//		dayPayMoney();
		board_paid();
	}
}
