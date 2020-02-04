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

public class statistics {
	public static void follow() throws Exception {
		FileInputStream is = new FileInputStream("./file/userinfo.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("./file/anchor.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		FileOutputStream os2 = new FileOutputStream("./file/audience.txt");
		BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(os2));
		String line = in.readLine();
		double count = 0, i = 0, female = 0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			if(data[2].equals("gold")) continue;
			int gold = Integer.parseInt(data[2]), point = Integer.parseInt(data[3]);
			if(!(gold < point && (point - gold) > 100)) {
				out.write(line + "\n");
			} else {
				out2.write(line + "\n");
			}
		}
		out.flush();
		out2.flush();
	}
	public static void gender() throws Exception {
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/userinfo_20170429.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = in.readLine();
		long count = 0, i = 0, female = 0, male = 0, fe_point = 0, fe_gold = 0, ma_point = 0, ma_gold = 0;
		long fe_in = 0, ma_in = 0, fe_enum = 0, ma_enum = 0;
		Pattern p = Pattern.compile(",\"gender\":(.*?),");
		Matcher m;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			if(data.length < 3 || data[2].equals("gold")) continue;
			long gold = Integer.parseInt(data[2]), point = Integer.parseInt(data[3]);
//			System.out.println(gold + "\t" + point);
			m = p.matcher(data[1]);
			if(!m.find()) {
//					System.out.println(line);
				continue;
			}
			if(m.group(1).equals("0")) {
				female++;
				fe_point += Integer.parseInt(data[3]);
				fe_gold += Integer.parseInt(data[2]);
				fe_in += Integer.parseInt(data[3]) - Integer.parseInt(data[2]);
				if(Integer.parseInt(data[3]) - Integer.parseInt(data[2]) > 0)
					fe_enum++;
			} else {
				male++;
				ma_point += Integer.parseInt(data[3]);
				ma_gold += Integer.parseInt(data[2]);
				ma_in += Integer.parseInt(data[3]) - Integer.parseInt(data[2]);
				if(Integer.parseInt(data[3]) - Integer.parseInt(data[2]) > 0)
					ma_enum++;
			}
		}
		System.out.println(female + "\t" + fe_point + "\t" + fe_gold + "\t" + fe_in + "\t" + fe_enum);
		System.out.println(male + "\t" + ma_point + "\t" + ma_gold + "\t" + ma_in + "\t" + ma_enum);
	}
	public static void point_gold() throws Exception {
		FileInputStream is = new FileInputStream("./file/userinfo.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("./file/point.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>(
                new Comparator<Integer>() {
                    public int compare(Integer obj1, Integer obj2) {
                        // 降序排序
                        return obj2.compareTo(obj1);
                    }
                });
		String line = in.readLine();
		double sum = 0.0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			if(data[2].equals("gold")) continue;
			int gold = Integer.parseInt(data[2]), point = Integer.parseInt(data[3]);
//			if(!(gold < point && (point - gold) > 100)) 
			{
				if(map.containsKey(point)) {
					map.put(point, map.get(point) + 1);
				} else {
					map.put(point, 1);
				}
			}
		}
		
		for(Entry<Integer, Integer> entry: map.entrySet()) {
//			out.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			int cnt = entry.getValue();
			for(int i = 0; i < cnt; i++)
				out.write(entry.getKey() + "\n");
			sum += cnt * entry.getKey();
		}
		System.out.println(sum + "\t" + sum / 5.0);
		out.flush();
		/*
		//这里将map.entrySet()转换成list
        List<Map.Entry<String,Integer> > list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,Integer> >() {
            //升序排序
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
            
        });
        
        for(Entry<String, Integer> mapping:list){ 
               out.write(mapping.getKey()+"\t"+mapping.getValue() + "\n"); 
          }
	*/
		
		
		is = new FileInputStream("./file/point.txt");
		in = new BufferedReader(new InputStreamReader(is));
		double x = 0.0, y = 0.0;
		int i = 0;
		while((line = in.readLine()) != null)
		{
			i++;
			int point = Integer.parseInt(line);
			x += point;
			y += point;
			if(sum / 5.0 - x < 0.000001) {
				System.out.println(i + "\t" + x + "\t" + sum / 5.0);
				break;
			}
		}
		
	}
	public static void level() throws Exception {
		FileInputStream is = new FileInputStream("./file/anchor.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("./file/level_anchor.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String line = in.readLine();
		Pattern p = Pattern.compile(",\"level\":(.*?),");
		Map<Integer, Integer>map = new HashMap();
		Matcher m;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			m = p.matcher(data[1]);
			if(!m.find()) {
//				System.out.println(line);
				continue;
			}
			int l = Integer.parseInt(m.group(1));
			if(map.containsKey(l)) {
				map.put(l, map.get(l) + 1);
			} else {
				map.put(l, 1);
			}
			if(l > 300) {
				System.out.println(line);
			}
//			out.write(m.group(1) + "\n");
		}
		double count = 0;
		for(Entry<Integer, Integer> elem: map.entrySet()) {
			count += elem.getValue();
			System.out.println(count);
			out.write(elem.getKey() + "\t" + elem.getValue() + "\t" + count / 12788443.0 + "\n");
		}
		out.flush();
	}
	public static void location() throws Exception {
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/userinfo_20170429.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("/Users/yiping/Desktop/file/location.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String line = in.readLine();
		Pattern p = Pattern.compile(",\"location\":\"(.*?)\",");
		Map<String, Long>map = new HashMap();
		Map<String, Long>mappoint = new HashMap();
		Map<String, Long>mapgold = new HashMap();
		Map<String, Long>mapincome = new HashMap();
		Map<String, Long>mapearn = new HashMap();
		Matcher m;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			m = p.matcher(data[1]);
			if(!m.find()) {
//				System.out.println(line);
				continue;
			}
			long gold = Integer.parseInt(data[2]), point = Integer.parseInt(data[3]);
			String l = m.group(1);
			if(map.containsKey(l)) {
				map.put(l, map.get(l) + 1);
				mappoint.put(l, mappoint.get(l) + point);
				mapgold.put(l, mapgold.get(l) + gold);
				mapincome.put(l, mapincome.get(l) + (point - gold));
				long tmp = 1;
				if(point > gold) {
					if(mapearn.containsKey(l))
						tmp += mapearn.get(l);
					mapearn.put(l, tmp);
				}
			} else {
				map.put(l, (long) 1);
				mappoint.put(l, point);
				mapgold.put(l, gold);
				mapincome.put(l, (point - gold));
				if(point > gold)
					mapearn.put(l, (long) 1);
			}
//			out.write(m.group(1) + "\n");
		}
		for(Entry<String, Long> elem: map.entrySet()) {
			String key = elem.getKey();
			out.write(key + "\t" + elem.getValue() +  "\t" + mappoint.get(key) + "\t" + mapgold.get(key) + "\t");
			out.write(mapincome.get(key) + "\t" + mapearn.get(key) + "\n");
		}
		out.flush();
	}
	
	public static void locationPro() throws Exception {
		//process location
		FileInputStream is = new FileInputStream("./file/location_anchor.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("./file/location_anchor-.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String line = in.readLine();
		String tmp = in.readLine();
		String pre = tmp.split("\t")[0], cur = "";
		long prex = Long.parseLong(tmp.split("\t")[1]), curx, prey = Long.parseLong(tmp.split("\t")[2]), cury;
		while((line = in.readLine()) != null) {
			if(line.split("\t").length < 2) {
				System.out.println(line);
				continue;
			}
			cur = line.split("\t")[0];
			curx = Long.parseLong(line.split("\t")[1]);
			cury = Long.parseLong(line.split("\t")[2]);
			if(cur.contains(pre)) {
				curx += prex;
				cury += prey;
			} else {
				out.write(pre + "\t" + prex + "\t" + prey + "\n");
			}
			pre = cur;
			prex = curx;
			prey = cury;
		}
		out.flush();
	}
	
	public static void locationNum() throws Exception {
		//process location
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/userinfo_20170429.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = null;
		long []n = {0, 0, 0, 0, 0, 0};
		long []p = {0, 0, 0, 0, 0, 0};
		long []g = {0, 0, 0, 0, 0, 0};
		long []i = {0, 0, 0, 0, 0, 0};
		long []e = {0, 0, 0, 0, 0, 0};
		String yi = "北京、上海、广州、深圳、成都市、杭州市、武汉市、天津市、南京市、重庆市、西安市、长沙市、青岛市、沈阳市、大连市、厦门市、苏州市、宁波市、无锡市";
		String er = "福州市、合肥市、郑州市、哈尔滨、佛山市、济南市、东莞市、昆明市、太原市、南昌市、南宁市、温州市、石家庄市、长春市、泉州市、贵阳市、常州市、珠海市、金华市、烟台市、海口市、惠州市、乌鲁木齐市、徐州市、嘉兴市、潍坊市、洛阳市、南通市、扬州市、汕头市";
		String san = "兰州市、桂林市、三亚市、呼和浩特市、绍兴市、泰州市、银川市、中山市、保定市、西宁市、芜湖市、赣州市、绵阳市、漳州市、莆田市、威海市、邯郸市、临沂市、唐山市、台州市、宜昌市、湖州市、包头市、济宁市、盐城市、鞍山市、廊坊市、衡阳市、秦皇岛市、吉林市、大庆市、淮安市、丽江市、揭阳市、荆州市、连云港市、张家口市、遵义市、上饶市、龙岩市、衢州市、赤峰市、湛江市、运城市、鄂尔多斯市、岳阳市、安阳市、株洲市、镇江市、淄博市、郴州市、南平市、齐齐哈尔市、常德市、柳州市、咸阳市、南充市、泸州市、蚌埠市、邢台市、舟山市、宝鸡市、德阳市、抚顺市、宜宾市、宜春市、怀化市、榆林市、梅州市、呼伦贝尔市";
		String si = "临汾市、 南阳市、 新乡市、 肇庆市、 丹东市、 德州市、菏泽市、 九江市、江门市 、 黄山市、 渭南市、 营口市、 娄底市、永州市 、 邵阳市、 清远市、 大同市、 枣庄市、 北海市、 丽水市、 孝感市、 沧州市、 马鞍山、 聊城市、 三明市、 开封市、 锦州市、 汉中市、 商丘市、 泰安市、 通辽市、 牡丹江、 曲靖市、 东营市、 韶关市、 拉萨市、 襄阳市、 湘潭市、 盘锦市、 驻马店市、 酒泉市、 安庆市、 宁德市、 四平市、 晋中市、 滁州市、 衡水市、 佳木斯、 茂名市、 十堰市、 宿迁市、 潮州市、 承德市、 葫芦岛市、 黄冈市、 本溪市、 绥化市、 萍乡市、 许昌市、 日照市、 铁岭市、 大理州、 淮南市、 延边州、 咸宁市、 信阳市、 吕梁市、 辽阳市、 朝阳市、 恩施州、 达州市 、 益阳市 、 平顶山、 六安市、 延安市、 梧州市、 白山市、 阜阳市、 铜陵市 、 河源市、 玉溪市 、 黄石市、 通化市、 百色市、 乐山市 、 抚州市 、 钦州市、 阳江市、 池州市 、广元市";
		String wu = "滨州市、 阳泉市、 周口市 、 遂宁市、 吉安市、 长治市、 铜仁市、 鹤岗市、 攀枝花市、 昭通市、 云浮市、 伊犁州、 焦作市、 凉山州、 黔西南州、 广安市、 新余市、 锡林郭勒、 宣城市、 兴安盟、 红河州 、 眉山市、 巴彦淖尔、 双鸭山市 、景德镇市 、 鸡西市、 三门峡市、 宿州市、 汕尾市、 阜新市、 张掖市、 玉林市、 乌兰察布、 鹰潭市、 黑河市、 伊春市、 贵港市 、 漯河市、 晋城市、 克拉玛依、 随州市、 保山市、 濮阳市、 文山州 、 嘉峪关市、 六盘水市、 乌海市、 自贡市、 松原市、 内江市、 黔东南州、 鹤壁市、 德宏州、 安顺市、 资阳市、 鄂州市、 忻州市、 荆门市、 淮北市、 毕节市、 巴音郭楞、 防城港、 天水市、 黔南州、 阿坝州、 石嘴山市、 安康市、 亳州市 、 昌吉州、 普洱市、 楚雄州、 白城市、 贺州市、 哈密市、 来宾市、 庆阳市、 河池市、 张家界、雅安市、 辽源市、 湘西州、 朔州市、 临沧市、 白银市、 塔城地区、 莱芜市、 迪庆州、 喀什地区、 甘孜州、 阿克苏、 武威市、 巴中市、 平凉市、 商洛市、 七台河、 金昌市、 中卫市、 阿勒泰、 铜川市、 海西州、 吴忠市、 固原市、 吐鲁番、 阿拉善盟、 博尔塔拉州、 定西市、 西双版纳、 陇南市、 大兴安岭、 崇左市、 日喀则、 临夏州、 林芝市、 海东市、 怒江州、 和田地区、 昌都市、 儋州市、 甘南州、 山南市、 海南州、 海北州、 玉树州、 阿里地区、 那曲地区、 黄南州、 克孜勒苏州 、 果洛州、 三沙市";
		Pattern pa = Pattern.compile(",\"location\":\"(.*?)\",");
		Matcher m;
		while((line = in.readLine()) != null) {
			String []data = line.split("\t");
			m = pa.matcher(data[1]);
			if(!m.find()) {
//				System.out.println(line);
				continue;
			}
			long gold = Integer.parseInt(data[2]), point = Integer.parseInt(data[3]);
			String l = m.group(1);
			if(l.length() < 1) {
				continue;
			}
			if(yi.contains(l)) {
				n[0]++;
				p[0] += point;
				g[0] += gold;
				i[0] = (point - gold) + i[0];
				if(point - gold > 0)
					e[0] += 1;
			} else if(er.contains(l)) {
				n[1]++;
				p[1] += point;
				g[1] += gold;
				i[1] = i[1] + (point - gold);
				if(point - gold > 0)
					e[1] += 1;
			}else if(san.contains(l)) {
				n[2]++;
				p[2] += point;
				g[2] += gold;
				i[2] = i[2] + (point - gold);
				if(point - gold > 0)
					e[2] += 1;
			}else if(si.contains(l)) {
				n[3]++;
				p[3] += point;
				g[3] += gold;
				i[3] = i[3] + (point - gold);
				if(point - gold > 0)
					e[3] += 1;
			}else if(wu.contains(l)) {
				n[4]++;
				p[4] += point;
				g[4] += gold;
				i[4] = i[4] + (point - gold);
				if(point - gold > 0)
					e[4] += 1;
			} else {
				n[5]++;
				p[5] += point;
				g[5] += gold;
				i[5] = i[5] + (point - gold);
				if(point - gold > 0)
					e[5] += 1;
			}
			System.out.println(i[0] + "\t" + i[1] + "\t" + i[2] + "\t" + i[3] + "\t" + i[4]);
		}
		for(int j = 0; j < 6; j++) {
			System.out.println(n[j] + "\t" + p[j] + "\t" + g[j] + "\t" + i[j] + "\t" + e[j]);
		}
	}
	
	public static void nick() throws Exception {
		FileInputStream is = new FileInputStream("./file/userinfo.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
//		FileOutputStream os = new FileOutputStream("./file/nick.txt");
//		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String line = in.readLine();
		Pattern p = Pattern.compile(",\"nick\":\"(.*?)\",");
		Map<String, Integer>map = new HashMap();
		Matcher m;
		int cnt = 0, sum = 0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			m = p.matcher(data[1]);
			if(!m.find()) {
//				System.out.println(line);
				continue;
			}
			sum++;
			String nick = m.group(1);
			if(nick.contains("哥") || nick.contains("姐") || nick.contains("宝")  || nick.contains("妹")) {
				cnt++;
			}
//			for(int i = 0; i < nick.length(); i++) {
//				String tmp = "" + nick.charAt(i);
//				if(map.containsKey(tmp)) {
//					map.put(tmp, map.get(tmp) + 1);
//				} else {
//					map.put(tmp, 1);
//				}
//			}
		}
		/*
	    //这里将map.entrySet()转换成list
        List<Map.Entry<String,Integer> > list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,Integer> >() {
            //升序排序
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
            
        });
        
        for(Entry<String, Integer> mapping:list){ 
               out.write(mapping.getKey()+"\t"+mapping.getValue() + "\n"); 
          }
	
		out.flush();*/
		System.out.println(cnt);
		System.out.println(sum);
	}
	public static void birth() throws Exception {
		FileInputStream is = new FileInputStream("./file/userinfo.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("./file/birth_audience.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));

		String line = in.readLine();
		Pattern p = Pattern.compile(",\"birth\":\"(.*?)\",");
		Map<String, Double>pmap = new HashMap();//收入
		Map<String, Integer>mapnum = new HashMap();//人数
		Map<String, Double>gmap = new HashMap();//支出
		Map<String, Double>earn = new HashMap();//净收入
		
		Matcher m;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			if(data[2].equals("gold")) continue;
			double gold = Math.log(Integer.parseInt(data[2])), point = Math.log(Integer.parseInt(data[3]));//Integer.parseInt(data[3]);//
			if(point < 0.0) point = 0.0;
			if(gold < 0.0) gold = 0.0;

			m = p.matcher(data[1]);
			if(!m.find()) {
				continue;
			}
			String date = "";
			if(m.group(1).length() < 1) {
				date = "null";
			} else {
				date = m.group(1).split("-")[0];
			}
			if(mapnum.containsKey(date)) {
				mapnum.put(date, mapnum.get(date) + 1);
				pmap.put(date, pmap.get(date) + point);
				gmap.put(date, gmap.get(date) + gold);
				earn.put(date, earn.get(date) + (point - gold));
			} else {
				mapnum.put(date,1);
				pmap.put(date, point);
				gmap.put(date, gold);
				earn.put(date, point - gold);
			}
		} 
			
		for(Entry<String, Integer> item: mapnum.entrySet()) {
			String date = item.getKey();
			if(item.getKey().equals("null")) {
				out.write("null\t" + mapnum.get(item.getKey()) + "\t" + pmap.get(date) + "\t" + gmap.get(date) + "\t" + earn.get(date) + "\n");
				continue;
			}
			int d = 2017 - Integer.parseInt(date);
			int n = mapnum.get(item.getKey());
			out.write(d + "\t" + n + "\t" + pmap.get(date) + "\t" + gmap.get(date) + "\t" + earn.get(date) + "\t");
			out.write(pmap.get(date)/n + "\t" + gmap.get(date)/n + "\t" + earn.get(date)/n + "\n");
		}
		out.flush();
	}
	public static void weibo() throws Exception {
		FileInputStream is = new FileInputStream("/Users/yiping/Desktop/file/userinfo_20170429.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = in.readLine();
		Map<String, Integer>map = new HashMap();
		long relation = 0, nonrelation = 0;
		long re_gold = 0, re_point = 0, re_income = 0, re_enum = 0, no_gold = 0, no_point = 0, no_income = 0, no_enum = 0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			if(data[2].equals("gold")) continue;
			long gold = Integer.parseInt(data[2]), point = Integer.parseInt(data[3]);
//			System.out.println(gold + "\t" + point);
			if(data[6].equals("1")) {
				relation++;
				re_gold += gold;
				re_point += point;
				re_income += (point - gold);
				if(point > gold)
					re_enum++;
			} else if(!data[6].equals("0")) {
				System.out.println(line);
			} else {
				nonrelation++;
				no_gold += gold;
				no_point += point;
				no_income += (point - gold);
				if(point > gold)
					no_enum++;
			}
		}
		System.out.println(relation + "\t" + re_gold + "\t" + re_point + "\t" + re_income + "\t" + re_enum);
		System.out.println(nonrelation + "\t" + no_gold + "\t" + no_point + "\t" + no_income + "\t" + no_enum);
	}
	public static void emotion() throws Exception {
		FileInputStream is = new FileInputStream("./file/userinfo.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = in.readLine();
		Pattern p = Pattern.compile("\"emotion\":\"(.*?)\",");
		Map<String, Integer>map = new HashMap();
		Matcher m;
		long single = 0, nonsingle = 0, unknown = 0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			if(data[2].equals("gold")) continue;
			long gold = Integer.parseInt(data[2]), point = Integer.parseInt(data[3]);
//			System.out.println(gold + "\t" + point);
			if((gold < point && (point - gold) > 100)) 
			{
				
				m = p.matcher(data[1]);
				if(!m.find()) {
//					System.out.println(line);
					continue;
				}
				String e = m.group(1);
				if(e.equals("")) e = "null";
				if(map.containsKey(e)) {
					map.put(e, map.get(e) + 1);
				} else {
					map.put(e, 1);
				}
			}
		}
		for(Entry<String, Integer> item: map.entrySet()) {
			System.out.println(item.getKey()+ "\t" + item.getValue());
		}
	}
	public static void board() throws Exception {
		FileInputStream is = new FileInputStream("./file/anchor_board.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		FileOutputStream os = new FileOutputStream("./file/board_percent_anchor.txt");
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
		String line;
//		is = new FileInputStream("/Users/yiping/Desktop/file/anchor.txt");
//		in = new BufferedReader(new InputStreamReader(is));
//		Map<String, Integer>map = new HashMap();
//		while((line = in.readLine()) != null)
//		{
//			String []data = line.split("\t");
//			map.put(data[0], 0);
//		}
		is = new FileInputStream("./file/anchor_board.txt");
		in = new BufferedReader(new InputStreamReader(is));
		String pre = "", cur;
		long p = 0, curp = 0;
		int cnt = 0;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
//			if(!map.containsKey(data[0]))
//				continue;
			cur = data[0];
			long tmp = Long.parseLong(data[3]); 
			if(cur.equals(pre)) {
				if(cnt < 3)
					curp += tmp;
				cnt++;
			} else {
				out.write(pre + "\t" + p + "\t" + curp + "\t" + curp / (p*1.0) + "\n");
				cnt = 1;
				curp = tmp;
			}
			pre = cur;
			p = Long.parseLong(data[1]);
		} 
		out.write(pre + "\t" + p + "\t" + curp + "\t" + curp / (p*1.0) + "\n");
		out.flush();
	}

	public static void count() throws Exception {
		FileInputStream is = new FileInputStream("./file/userinfo.txt");//"/Users/yiping/Desktop/file/userinfo_20170429.txt"
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line = in.readLine();
		long cnt = 0, n1 = 0, n2 = 0, n3 = 0;
//		Map<String, Integer>map = new HashMap();
		Pattern p = Pattern.compile("\"location\":\"(.*?)\",");//"verified":0,//,\"inke_verify\":(.*?),
		Matcher m;
		while((line = in.readLine()) != null)
		{
			String []data = line.split("\t");
			
			if(line.contains("gold") || data.length < 4) {
//				System.out.println(line);
				continue;
			}
			m = p.matcher(data[1]);
			if(m.find()){
//				System.out.println(m.group(1));
				if(m.group(1).length() < 1) {
					n1++;
					System.out.println(m.group(1));
				} else{
					n2++;
//					System.out.println(m.group(1));
				}
			}
		}
		System.out.println(n1 + "\t" + n2);
	}
	public static void test() {
		String s = "投资学";
		System.out.println(s.length() + "\t" + s.charAt(0));
	}

	public static void main(String[] args) throws Exception {
//		follow();
//		gender();
//		point_gold();
//		level();
//		location();
//		locationPro();
//		locationNum();
//		nick();
//		birth();
//		weibo();
//		emotion();
		board();
//		test();
//		count();
	}
}
