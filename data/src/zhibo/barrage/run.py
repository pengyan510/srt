# coding: utf-8
import urllib
import urllib2
import re
import time
import subprocess

def main(page_num):
    url = "http://www.inke.cn/hotlive_list.html?page="+str(page_num)
    request = urllib2.Request(url)
    page = urllib2.urlopen(request, data=None, timeout=5)
    text = page.read()
    #print text
    rooms = re.findall("<a href=\"\./live\.html\?uid=\d+&id=\d+\">", text)#"\"sio_ip\":\".*:81\"", text)
    #print rooms
    for room in rooms:
        roomid = room.split('=')[-1][:-2]
        print roomid
        subprocess.Popen('nohup python getcomments.py %s &'%roomid, shell=True)
        #break

if __name__ == "__main__":
    while True:
        for i in range(1,3):
            main(i)
        time.sleep(3600*3)
