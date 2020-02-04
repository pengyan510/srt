# coding=utf-8

import json, math, codecs, time
import numpy as np
import matplotlib.pyplot as plt
import codecs

fontsize = 22

def platform_online():
    f = open('../data/timeline_data.txt')
    dic = json.load(f)
    f.close()
    l = len(dic['inke']['online'])
    x = [i/(12.0*24.0) for i in range(l)]
    ylabels = ['online', 'gift', 'danmu']
    for label in ylabels:
        plt.plot(x, dic['inke'][label], label='inke', linewidth=2, color='r')
        plt.plot(x, dic['huya'][label], label='huya', linewidth=2, color='b')
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=4, ncol=5, mode="expand", borderaxespad=0.)
        plt.xlabel('time')
        plt.ylabel(label)
        plt.show()

def living_type():
    f = codecs.open('../data/living_types.txt', encoding='UTF-8')
    dic = json.load(f)
    f.close()
    l = 12*24*62
    x = [i/(12.0*24.0) for i in range(l)]
    colors = ['darkviolet', 'g', 'b', 'k', 'lime', 'cyan', 'yellow', 'darkorange', 'peru', 'r']
    count = 0
    for key in dic['huya']['online']:
        shape = '-'
        if count%2==1:
            shape+='-'
        plt.plot(x, dic['huya']['online'][key], label=key, linewidth=2, color=colors[count/2], linestyle=shape)
        count+=1
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=4, ncol=5, mode="expand", borderaxespad=0.)
    plt.xlabel('time')
    plt.ylabel('online')
    plt.show()

def channel_atten_online():
    f = codecs.open('../data/99_channels_atten_and_online.txt', encoding='UTF-8')
    dic = json.load(f)
    f.close()
    l = 12*24*62
    x = [i/(12.0*24.0) for i in range(l)]
    colors = ['r', 'b', 'k', 'cyan', 'peru', 'yellow', 'g', 'lime', 'darkviolet', 'darkorange']
    ii = 1
    for rid in dic:
        count = 0
        for ltype in dic[rid]['atten']:
            xx, yy = [], []
            for i in range(len(x)):
                if dic[rid]['atten'][ltype][i]!=0:
                    xx.append(x[i])
                    yy.append(dic[rid]['atten'][ltype][i])
            plt.plot(xx, yy, label=ltype, linewidth=2, color=colors[count], linestyle='--')
            plt.plot(x, dic[rid]['online'][ltype], label=ltype, linewidth=2, color=colors[count], linestyle='-')
            count+=1
        plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=4, ncol=5, mode="expand", borderaxespad=0.)
        plt.xlabel('time')
        plt.ylabel('atten--/online-')
        plt.savefig('../result/huya/99channels/%d_atten_online.jpg'%(ii))
        plt.close('all')
        ii+=1
        print ii

def gift_cdf():
    f = codecs.open('../data/gift_cdf.txt', encoding='UTF-8')
    dic = json.load(f)
    f.close()
    for rid in dic:
        arr = dic[rid]
        x = [i/float(len(arr)) for i in range(len(arr)+1)]
        y = [0]+arr
        plt.plot(x, y, 'r-', linewidth=1)
    plt.xlabel('user percentage')
    plt.ylabel('CDF')
    plt.show()

def channel_dur_cdf():
    f = codecs.open('../data/channel_dur_distri.txt', encoding='UTF-8')
    dic = json.load(f)
    f.close()
    x = [i for i in range(1441)]
    y1, y2 = [i for i in range(1442)], [i for i in range(1442)]
    arr = dic['huya']
    for a in arr:
        j = abs(int(a))
        if j>1440:
            j=1441
        y1[j]+=1
    s = float(sum(y1))
    y1[0]/=s
    for i in range(1, len(y1)):
        y1[i]=float(y1[i])/s+y1[i-1]
    arr = dic['inke']
    for a in arr:
        j = abs(int(a))
        if j>1440:
            j=1441
        y2[j]+=1
    s = float(sum(y2))
    y2[0]/=s
    for i in range(1, len(y2)):
        y2[i]=float(y2[i])/s+y2[i-1]
    plt.plot(x, y1[:-1], 'b-', linewidth=2, label='huya')
    plt.plot(x, y2[:-1], 'r-', linewidth=2, label='inke')
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=4, ncol=5, mode="expand", borderaxespad=0.)
    plt.xlabel('duration of channel')
    plt.ylabel('CDF')
    plt.show()

def main():
    platform_online()
    #living_type()
    #channel_atten_online()
    #gift_cdf()
    #channel_dur_cdf()

if __name__ == '__main__':
    main()