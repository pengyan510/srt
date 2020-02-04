package statistics;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day_userpay {
	public static void day_pay() throws IOException {
		FileOutputStream os = new FileOutputStream("./day_pay_all.txt");
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
    			if(dic.containsKey(data[1])) {
    				dic.put(data[1], dic.get(data[1]) + 1);
    			} else {
    				dic.put(data[1],  1);
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
	public static void dayPayNumber() throws IOException {
		FileOutputStream os = new FileOutputStream("./day_pay_num.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os1 = new FileOutputStream("./day_pay_numcdf.txt");
		BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(os1));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<Integer, Long>map = new HashMap();
		String user = "";
		
		File dir = new File("/home/medialab/yyp/contri");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_", "").replace(".txt", "");
        	Map<String, Integer> dic = new HashMap();
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			if(dic.containsKey(data[1])) {
    				dic.put(data[1], dic.get(data[1]) + 1);
    			} else {
    				dic.put(data[1],  1);
    			}
    		}
    		for(Entry<String, Integer> entry: dic.entrySet()) {
    			String u = entry.getKey();
    			int n = entry.getValue();
    			out1.write(n + "\n");
    			if(n >= 5000) {
    				System.out.println(u + "\t" + n);
    				user = user + "\n" + (u + "\t" + n);
    			}
    			if(map.containsKey(n)) {
    				map.put(n, map.get(n) + 1);
    			} else {
    				map.put(n, (long) 1);
    			}
    		}
        }
        for(Entry<Integer, Long> entry: map.entrySet()) {
			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
        out.write("\n" + user);
		out.flush();
		out.close();
		out1.flush();
		out1.close();
	}

	public static void dayPayMoney() throws IOException {
		FileOutputStream os = new FileOutputStream("./day_pay_money.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os1 = new FileOutputStream("./day_pay_moneycdf.txt");
		BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(os1));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<Integer, Long>map = new HashMap();
		String user = "";
		
		File dir = new File("/home/medialab/yyp/contri");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_", "").replace(".txt", "");
        	Map<String, Integer> dic = new HashMap();
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			int money = Integer.parseInt(data[2]);
    			if(dic.containsKey(data[1])) {
    				dic.put(data[1], dic.get(data[1]) + money);
    			} else {
    				dic.put(data[1],  money);
    			}
    		}
    		for(Entry<String, Integer> entry: dic.entrySet()) {
    			String u = entry.getKey();
    			int n = entry.getValue();
    			out1.write(n + "\n");
    			if(n >= 10218210) {
    				System.out.println(u + "\t" + n);
    				user = user + "\n" + (u + "\t" + n);
    			}
    			if(map.containsKey(n)) {
    				map.put(n, map.get(n) + 1);
    			} else {
    				map.put(n, (long) 1);
    			}
    		}
        }
        for(Entry<Integer, Long> entry: map.entrySet()) {
			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
        out.write("\n" + user);
		out.flush();
		out.close();
		out1.flush();
		out1.close();
	}
	
	public static void payMoney() throws IOException {
		FileOutputStream os = new FileOutputStream("./pay_money.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<String, Long>dic = new HashMap();
		
		File dir = new File("/home/medialab/yyp/data");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	String filename = file.getName().replace("contribution_", "").replace(".txt", "");
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			int money = Integer.parseInt(data[2]);
    			if(dic.containsKey(data[1])) {
    				dic.put(data[1], dic.get(data[1]) + money);
    			} else {
    				dic.put(data[1],  (long) money);
    			}
    		}
        }
        for(Entry<String, Long> entry: dic.entrySet()) {
			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		out.flush();
		out.close();
	}
	
	public static void payMoneyTop() throws IOException {
		FileOutputStream os = new FileOutputStream("./pay_money_top.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is;
		BufferedReader in;
		String line = null;
		Map<String, Long>map = new HashMap();
		Map<String, Map<String, Long> >dic = new HashMap<String,Map<String,Long> >();
		
		File dir = new File("/home/medialab/yyp/data");  
		File[] files = dir.listFiles();  
        for (File file: files) {  
        	if(!file.getName().contains("contribution"))
        		continue;
        	
        	is = new FileInputStream(file);
    		in = new BufferedReader(new InputStreamReader(is));
    		while((line = in.readLine()) != null) {
    			String []data = line.split("\t");
    			String str = data[0];
    			
    			int money = Integer.parseInt(data[2]);
    			if(map.containsKey(data[1])) {
    				map.put(data[1], map.get(data[1]) + money);
    				Map<String, Long>tmp = dic.get(data[1]);
    				if(tmp.containsKey(str)) {
    					tmp.put(str, tmp.get(str) + money);
    				} else {
    					tmp.put(str, (long) money);
    				}
    				dic.put(data[1], tmp);
    			} else {
    				map.put(data[1],  (long) money);
    				Map<String, Long>tmp = new HashMap();
    				tmp.put(str, (long) money);
    				dic.put(data[1], tmp);
    			}
    		}
        }

        for(Entry<String, Long> entry: map.entrySet()) {
        	Long sum = entry.getValue();
        	Map<String, Long>tmp = dic.get(entry.getKey());
        	
        	List<Map.Entry<String,Long> > list = new ArrayList<Map.Entry<String,Long> >(tmp.entrySet());
            Collections.sort(list,new Comparator<Map.Entry<String,Long> >() {
                //升序排序
                public int compare(Entry<String, Long> o1,
                        Entry<String, Long> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            int i = 0;
            Long top = (long) 0;
            String top3 = "";
            for(Map.Entry<String,Long> mapping:list){ 
            	if(i >= 3) break;
            	i++;
                top += mapping.getValue();
                top3 = top3 + mapping.getKey() + "\t";
            } 
			out.write(entry.getKey() + "\t" + sum + "\t" + (double)(top / (double)sum) + "\t" + top3 + "\n");
		}
		out.flush();
		out.close();
	}
	
	public static void test() throws IOException {
			FileInputStream is;
			BufferedReader in;
			String line = null;
			Map<String, Long>map = new HashMap();
			
			File dir = new File("/home/medialab/yyp/data");  
			File[] files = dir.listFiles();  
	        for (File file: files) {  
	        	if(!file.getName().contains("contribution"))
	        		continue;
	        	
	        	is = new FileInputStream(file);
	    		in = new BufferedReader(new InputStreamReader(is));
	    		while((line = in.readLine()) != null) {
	    			String []data = line.split("\t");
	    			String str = data[1] + "\t" + data[0];
	    			if(!map.containsKey(str)) {
	    				map.put(str, (long) 1);
	    			} 
	    		}
	        }
	        System.out.println(map.size());
	}
	public static void all_pay() throws IOException {
		FileOutputStream os = new FileOutputStream("./all_pay.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("./anchor_board.txt");;
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Map<String, List<Integer> >map = new HashMap();
		
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			List<Integer>tmp = new ArrayList();
			if(map.containsKey(data[1])) {
				tmp = map.get(data[1]);
			}
			tmp.add(Integer.parseInt(data[2]));
			map.put(data[1],  tmp);
		}
		for(Entry<String, List<Integer> > entry: map.entrySet()) {
			List<Integer>tmp = entry.getValue();
			Collections.sort(tmp);
			out.write(entry.getKey() + "\t" + tmp.size());
			for(int i = 0; i < tmp.size(); i++) {
				out.write("\t" + tmp.get(i));
			}
			out.write("\n");
		}
		out.flush();
		out.close();
	}
	public static void sortPay() throws IOException {
		FileOutputStream os = new FileOutputStream("./top2000.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("./paymoney.txt");;
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Map<String, Integer >map = new HashMap();
		
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			int tmp = Integer.parseInt(data[2]);
			map.put(data[0],  tmp);
		}
		
		List<Map.Entry<String,Integer> > list = new ArrayList<Map.Entry<String,Integer> >(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            //升序排序
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int i = 0;
        for(Map.Entry<String,Integer> mapping: list) { 
        	i++;
        	out.write(mapping.getKey() + "\n");                                                                       
        	if(i >= 300) break;
        } 

		out.flush();
		out.close();
	}
	public static void relation() throws IOException {
		FileOutputStream os = new FileOutputStream("./rela_200.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("./top2000.txt");;
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Map<String, Integer >map = new HashMap();
		
		while((line = in.readLine()) != null) {
			map.put(line,  1);
		}
		
		is = new FileInputStream("./mid_result.txt");
		in = new BufferedReader(new InputStreamReader(is));
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(map.containsKey(data[0]) && map.containsKey(data[1])) {
				out.write(line + "\n");
			}
		}
		out.flush();
		out.close();
	}
	public static void top1_per() throws IOException {
		FileOutputStream os = new FileOutputStream("/Users/yiping/Desktop/file/top1.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/top3pay");;
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Map<String, Integer >map = new HashMap();
		
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			String top1 = data[4].split(" ")[0];
			Long s = Long.parseLong(data[2]);
			Long t = Long.parseLong(top1);
			double p = (double)t / (double)s;
			out.write(p + "\n");
		}
		out.flush();
		out.close();
	}
	
	public static void pay_hot_perc() throws IOException {
		FileOutputStream os = new FileOutputStream("./pay_hot_per.txt");
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
    			if(dic.containsKey(data[1])) {
    				dic.put(data[1], dic.get(data[1]) + money);
    			} else {
    				dic.put(data[1],  (long) money);
    			}
    		}
        }
        
        Map<String, Long>pay = new HashMap();
    	is = new FileInputStream("/home/medialab/yyp/point/0109");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(dic.containsKey(data[0])) {
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0],  (long) gold);
			}
		}
		
		is = new FileInputStream("/home/medialab/yyp/point/0309");
		in = new BufferedReader(new InputStreamReader(is));
		line = in.readLine();
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			if(dic.containsKey(data[0])) {
				int gold = Integer.parseInt(data[1]);
				pay.put(data[0], (long)gold - pay.get(data[0]));
			}
		}
        out.write("id\tgold\tpoint\tdif\n");
        for(Entry<String, Long> entry: pay.entrySet()) {
        	String id = entry.getKey();
        	long gold = entry.getValue();
        	long point = dic.get(id);
        	double per = 0.0;
        	if(gold >= point) 
        		per = (double)(point) / gold;
        	out.write(id + "\t" + gold  + "\t" + point + "\t" + (gold - point) + "\t" + per + "\n");
		}
		out.flush();
		out.close();
	}

	
	public static void main(String[] args) throws Exception {
//		day_pay();
//		dayPayNumber();
//		dayPayMoney();
//		test();
//		all_pay();
//		payMoney();
//		payMoneyTop();
//		sortPay();
//		relation();
//		top1_per();
		
//		pay_hot_perc();
		
	}
}
