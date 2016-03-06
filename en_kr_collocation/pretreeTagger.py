# -*- coding: utf-8 -*-
import os
import os.path
import re

def file_in(fp):

    f = open(fp, 'r', encoding = 'utf-8')
    sentences = f.readlines()
    f.close()

    return sentences

def file_out(fp, contents):
    i = 0
    f = open(fp, 'w', encoding = 'utf-8')
    for content in contents:
        if content != "" :
            i+=1
            f.write("<s id = %d>" % i)
            f.write(content[:-1] + " ")
            f.write("</s>\n")
        else : continue
    f.close()

if __name__ == '__main__':
    in_f = 'C:/Users/SinBee/Desktop/SinB/en_kr/43.news.en'
    out_f = 'C:/Users/SinBee/Desktop/SinB/en_kr/43.news_linetag.en.'
    sentences = file_in(in_f)
    file_out(out_f, sentences)
