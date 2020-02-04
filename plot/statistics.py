# coding:utf-8
import codecs, json, os, time

def writejson2file(data, fname):
    out = open(fname, 'w')
    out.write(json.dumps(data))
    out.close()

def platform_timeline(plist):
    f = open('../result/yc/freq_channel.txt')
    rdics = json.loads(f.read())
    f.close()
    S, E, delta = 1512057600, 1517414399, 300
    Range = int((E-S+1)/delta)
    timeline = {}
    base_dir = '../data/201712-201801/each_live/'
    base_dir_danmu = '../data/201712-201801/danmu/'
    base_dir_gift = '../data/201712-201801/gift/'
    for plat in plist:
        print('platform '+plat)
        freq_list = []
        if plat in rdics:
            freq_list = rdics[plat]
        cache_danmu = codecs.open('../result/yc/huya_midfile/%s_danmu.txt'%plat, 'w', encoding='UTF-8')
        cache_gift = codecs.open('../result/yc/huya_midfile/%s_gift.txt'%plat, 'w', encoding='UTF-8')
        timeline[plat] = {'online':[0 for i in range(Range)], 'gift':[0 for i in range(Range)], 'danmu':[0 for i in range(Range)]}
        fnames = sorted(os.listdir(base_dir))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            print(fname)
            f = codecs.open(base_dir+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                rid, ltype, s, e, atten, online = words[1], words[2], int(words[3]), int(words[4]), int(words[5]), int(words[6])
                if s>=e:
                    continue
                if s<S:
                    s = S
                if e>E:
                    e = E
                si = int((s-S)/delta)
                ei = int((e-S)/delta)
                for j in range(si, ei+1):
                    timeline[plat]['online'][j] += online
        fnames = sorted(os.listdir(base_dir_danmu))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            if '2018-02' in fname:
                continue
            print(fname)
            f = codecs.open(base_dir_danmu+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                try:
                    uid, rid, ts, content = words[1], words[2], words[3], words[5]
                except:
                    #print(line)
                    continue
                if uid=='0':
                    continue
                try:
                    t = int(time.mktime(time.strptime(ts, '%Y-%m-%d %H:%M:%S')))
                except:
                    continue
                if t>E or t<S:
                    continue
                ti = int((t-S)/delta)
                timeline[plat]['danmu'][ti] += 1
                if rid in freq_list:
                    cache_danmu.write(line)
        cache_danmu.close()
        fnames = sorted(os.listdir(base_dir_gift))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            print(fname)
            f = codecs.open(base_dir_gift+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                try:
                    uid, rid, ts, num, price = words[1], words[2], words[3], int(words[5]), int(words[6])
                except:
                    #print(line)
                    continue
                try:
                    t = int(time.mktime(time.strptime(ts, '%Y-%m-%d %H:%M:%S')))
                except:
                    continue
                if t>E or t<S:
                    continue
                ti = int((t-S)/delta)
                if rid in freq_list:
                    cache_gift.write(line)
                if plat=='inke':
                    timeline[plat]['gift'][ti] += price
                else:
                    timeline[plat]['gift'][ti] += num*price
        cache_gift.close()
    return timeline

def platforms(plist):
    timeline_data = platform_timeline(plist)
    writejson2file(timeline_data, '../result/yc/timeline_data.txt')

def living_types(plist):
    S, E, delta = 1512057600, 1517414399, 300
    Range = int((E-S+1)/delta)
    timeline = {}
    base_dir = '../data/201712-201801/each_live/'
    for plat in plist:
        print('platform '+plat)
        type_dic = {}
        timelines = []
        timeline[plat] = {'online':{}, 'gift':{}, 'danmu':{}}
        fnames = sorted(os.listdir(base_dir))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            print(fname)
            f = codecs.open(base_dir+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                rid, ltype, s, e, atten, online = words[1], words[2], int(words[3]), int(words[4]), int(words[5]), int(words[6])
                if s>=e:
                    continue
                if s<S:
                    s = S
                if e>E:
                    e = E
                si = int((s-S)/delta)
                ei = int((e-S)/delta)
                if ltype not in type_dic:
                    type_dic[ltype] = [0 for i in range(Range)]
                    type_dic[ltype].append(ltype)
                for j in range(si, ei+1):
                    type_dic[ltype][j] += online
        for key in type_dic:
            timelines.append(type_dic[key])
        timelines = sorted(timelines, key=lambda x:-sum(x[:-1]))
        for i in range(min(20, len(timelines))):
            timeline[plat]['online'][timelines[i][-1]] = timelines[i][:-1]
    writejson2file(timeline, '../result/yc/living_types.txt')

def freq_channel(plist):
    S, E, delta = 1512057600, 1517414399, 300
    times = {}
    farr = {}
    base_dir = '../data/201712-201801/each_live/'
    for plat in plist:
        print('platform '+plat)
        times[plat] = {}
        farr[plat] = []
        fnames = sorted(os.listdir(base_dir))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            print(fname)
            f = codecs.open(base_dir+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                rid, ltype, s, e, atten, online = words[1], words[2], int(words[3]), int(words[4]), int(words[5]), int(words[6])
                if s>=e:
                    continue
                if s<S:
                    s = S
                if e>E:
                    e = E
                if rid not in times[plat]:
                    times[plat][rid] = [[], []] #[shichang, renshu]
                times[plat][rid][0].append(e-s)
                times[plat][rid][1].append(online)
        rid_result = {}
        arr = []
        for rid in times[plat]:
            arr.append([rid, sum(times[plat][rid][0]), sum(times[plat][rid][1])/len(times[plat][rid][1])])
        arr = sorted(arr, key=lambda x:-x[1])
        for i in range(50):
            rid_result[arr[i][0]]=True
        arr = sorted(arr, key=lambda x:-x[2])
        for i in range(50):
            rid_result[arr[i][0]]=True
        for rid in rid_result:
            farr[plat].append(rid)
    writejson2file(farr, '../result/yc/freq_channel.txt')

def freq_channel_stat(plat):
    f = open('../result/yc/freq_channel.txt')
    rdics = json.loads(f.read())
    cdic = {}
    f.close()
    rdic = rdics[plat]
    cdic = {}
    S, E, delta = 1512057600, 1517414399, 300
    Range = int((E-S+1)/delta)
    for rid in rdic:
        cdic[rid] = {'atten':{}, 'online':{}}
        #cdic[rid] = {'atten':{}, 'online':{}, 'gift':[0 for i in range(Range)], 'danmu':[0 for i in range(Range)]}
    base_dir = '../data/201712-201801/each_live/'
    for i in range(1):
        fnames = sorted(os.listdir(base_dir))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            print(fname)
            f = codecs.open(base_dir+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                rid, ltype, s, e, atten, online = words[1], words[2], int(words[3]), int(words[4]), int(words[5]), int(words[6])
                if rid not in cdic:
                    continue
                if ltype not in cdic[rid]['atten']:
                    cdic[rid]['atten'][ltype] = [0 for i in range(Range)]
                    cdic[rid]['online'][ltype] = [0 for i in range(Range)]
                if s>=e:
                    continue
                if s<S:
                    s = S
                if e>E:
                    e = E
                si = int((s-S)/delta)
                ei = int((e-S)/delta)
                for j in range(si, ei+1):
                    cdic[rid]['atten'][ltype][j] = atten
                    cdic[rid]['online'][ltype][j] = online
    writejson2file(cdic, '../result/yc/99_channels_atten_and_online.txt')

def freq_channel_stat_plats(plist):
    for plat in plist:
        freq_channel_stat(plat)

def ltype_distri(plat):
    f = open('../result/yc/freq_channel.txt')
    rdics = json.loads(f.read())
    f.close()
    rdic = rdics[plat]
    dic = {}
    for rid in rdic:
        dic[rid] = {}
    base_dir = '../data/201712-201801/each_live/'
    for i in range(1):
        fnames = sorted(os.listdir(base_dir))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            print(fname)
            f = codecs.open(base_dir+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                rid, ltype, s, e, atten, online = words[1], words[2], int(words[3]), int(words[4]), int(words[5]), int(words[6])
                if rid not in dic:
                    continue
                if ltype not in dic[rid]:
                    dic[rid][ltype] = 1
                else:
                    dic[rid][ltype] += 1
    out = codecs.open('../result/yc/ltypes.txt', 'w', encoding='UTF-8')
    for rid in dic:
        out.write(rid+'\t'+str(len(dic[rid]))+'\t'+json.dumps(dic[rid])+'\n')
    out.close()

def gift_cdf(plist):
    f = open('../result/yc/freq_channel.txt')
    rdics = json.loads(f.read())
    f.close()
    for plat in plist:
        rarr = rdics[plat]
        dic = {rid:{} for rid in rarr}
        f = codecs.open('../result/yc/huya_midfile/%s_gift.txt'%plat, encoding='UTF-8')
        count = 0
        for line in f:
            if count%100000==0:
                print(count)
            count+=1
            words = line.strip().split(',')
            try:
                uid, rid, ts, num, price = words[1], words[2], words[3], int(words[5]), int(words[6])
            except:
                #print(line)
                continue
            if rid not in dic:
                continue
            if uid not in dic[rid]:
                dic[rid][uid] = 0
            dic[rid][uid] += num*price
        res = {}
        for rid in dic:
            tmp = []
            if len(dic[rid])==0:
                continue
            for uid in dic[rid]:
                tmp.append(dic[rid][uid])
            tmp = sorted(tmp, key=lambda x:-x)
            s = float(sum(tmp))
            tmp[0]/=s
            for i in range(1,len(tmp)):
                tmp[i]=tmp[i-1]+tmp[i]/s
            res[rid] = tmp
    print(len(res))
    writejson2file(res, '../result/yc/gift_cdf.txt')

def channel_dayduration_distri(plist):
    base_dir = '../data/201712-201801/each_live/'
    res = {}
    for plat in plist:
        dic = {}
        fnames = sorted(os.listdir(base_dir))
        for fname in fnames:
            if not fname.startswith(plat):
                continue
            print(fname)
            f = codecs.open(base_dir+fname, encoding='UTF-8')
            first_line = True
            for line in f:
                if first_line:
                    first_line = False
                    continue
                words = line.strip().split(',')
                rid, ltype, s, e, atten, online = words[1], words[2], int(words[3]), int(words[4]), int(words[5]), int(words[6])
                if s>=e:
                    continue
                dur = int((e-s)/60)
                if rid not in dic:
                    dic[rid] = []
                dic[rid].append(dur)
        arr = []
        for rid in dic:
            arr.append(int(float(sum(dic[rid]))/float(len(dic[rid]))))
        res[plat] = sorted(arr)
    writejson2file(res, '../result/yc/channel_dur_distri.txt')

def main():
    #ltype_distri('huya')
    #return
    plist = ['huya', 'inke']
    #plist = ['inke']
    #platforms(plist)
    channel_dayduration_distri(plist)
    plist = ['huya']
    #living_types(plist)
    #freq_channel(plist)
    #freq_channel_stat_plats(plist)
    gift_cdf(plist)

if __name__=='__main__':
    main()
