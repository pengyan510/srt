package statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Point {
	private static final int THREAD_NUMBER = 10;
	
	private static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024*128);
	private static BlockingQueue<String> filequeue = new ArrayBlockingQueue<String>(1024*1024);
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(THREAD_NUMBER);
	
	private static FileOutputStream os;
    private static BufferedWriter out;
    private static FileInputStream is;
    private static BufferedReader in;
    
    
	public static String getPoint(String id) {
		String res = "0\t0";
		String urlStr = "http://120.55.238.158/api/statistic/inout?uid=251464826&id=" + id;
		Pattern p = Pattern.compile("\"gold\": (.*?), \"point\": (.*?)}");
		String sTotalString = HttpRequest.sendGet(urlStr);
		Matcher m = p.matcher(sTotalString);
		if(m.find()) {
			res = m.group(1) + "\t" + m.group(2);
		}
		return res;
	}
	public static void getUsers() throws InterruptedException, IOException {
		System.out.println("read file");
		String line = null;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			queue.put(data[0]);
//			while(!queue.offer(data[0])) {
//				System.out.println("queue full");
//				Thread.sleep(10000);
//			}
		}
	}
	public static void writeFile() throws InterruptedException, IOException {
		while(!filequeue.isEmpty()) {
			String tmp = filequeue.take();
			out.write(tmp + "\n");
		}
		out.flush();
	}
	public static void main(String[] args) throws IOException {
		
		is = new FileInputStream("./hotid.txt");
		in = new BufferedReader(new InputStreamReader(is));
		os = new FileOutputStream("./file/hotid_point.txt", true);
		out = new BufferedWriter(new OutputStreamWriter(os));
		out.write("id\tgold\tpoint\n");
		
//		String line = null;
//		while((line = in.readLine()) != null)
//		{
//			String []data = line.split("\t");
//			String info = getInfo(data[0]), point = getPoint(data[0]), fans = getFans(data[0]), weibo = getWeibo(data[0]);
//			out.write(data[0] + "\t" + info + "\t" + point + "\t" + fans + "\t" + weibo + "\n");
//			out.flush();
//		}
//		out.flush();
		long start=System.currentTimeMillis();   //获取开始时间
		service.scheduleAtFixedRate(
		        () -> {
		        	try {
					getUsers();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("read file error");
					}}, 0, 20, TimeUnit.SECONDS);
		
		for(int i = 0; i < 9; i++) {
			service.scheduleWithFixedDelay(
					() -> {
						String id = "";
						if(queue.size() == 0) {
							System.out.println("queue empty");
							long end=System.currentTimeMillis(); //获取结束时间
							System.out.println("程序运行时间： "+(end-start)+"ms"); 
						}
						try {
							id = queue.take();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("take element error");
							e.printStackTrace();
						}

						String point = getPoint(id);
						while(!filequeue.offer(id + "\t" + point)) {
								System.out.println("file queue full");
						}
					}, 3, 1, TimeUnit.MILLISECONDS);
		}
		
		service.scheduleWithFixedDelay(
		        () -> {
		        try {
					writeFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}, 10, 1, TimeUnit.SECONDS);
		
    }
}
