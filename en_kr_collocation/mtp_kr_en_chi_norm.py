# -*- coding: utf-8 -*-
import re
import os
import os.path
import collections
import math

"""

한국어 해쉬테이블과 영어 해쉬테이블 결합

"""

def hashtable(): return collections.defaultdict(hashtable) #이중hashtable

def make_dict(sentences):
    kr_en_dict = hashtable()
    for sentence in sentences:
        s = re.split('\t', sentence[:-1]) #단어를 탭 단위로 리스트에 추가
        for i, word in enumerate(s):
            if i == 0:
                keyword = word
            else:
                try:
                    w, f = word.split(' ')
                except:
                    continue
                kr_en_dict[keyword][w] = f
    return kr_en_dict


def mtp_kr_en(kr_dict, en_dict):
    check_overlap = list()
    word_distance = hashtable()
    i = 0
    for kr_keys in kr_dict.keys():
        print("\n***%d : %s***\n" % (i, kr_keys))
        for kr_key in kr_dict[kr_keys].keys():
            if kr_key in en_dict.keys():
                for en_key in en_dict[kr_key].keys():
                    check_overlab = make_list(kr_dict[kr_keys], en_dict[en_key])
                    if en_key not in word_distance[kr_keys]:
                        kr_en_distance = cosin_sim(check_overlab, kr_dict[kr_keys], en_dict[en_key])
                        word_distance[kr_keys][en_key] = kr_en_distance
                    else:
                        continue
                check_overlab = []
        i+=1
    return word_distance


def make_list(kr_dict, en_dict):
    list_set = list()

    for kr_key in kr_dict.keys():
        list_set.append(kr_key)
    for en_key in en_dict.keys():
        if en_key not in list_set:  list_set.append(en_key)

    return list_set


def cosin_sim(word_list, kr_dict, en_dict):
    kr_w_list = {}
    en_w_list = {}

    for w in word_list:
        if w not in kr_dict.keys():
            kr_w_list[w] = 0
        else: kr_w_list[w] = float(kr_dict[w])

        if w not in en_dict.keys():
            continue
            #en_w_list[w] = 0
        else: en_w_list[w] = float(en_dict[w])
    
    """
    for key, value in kr_dict.items():
        if key not in kr_w_list.keys():
            kr_w_list[key] = 0
        else:
            kr_w_list[key] = value
    for key, value in en_dict.items():
        if key not in en_w_list.keys():
            en_w_list[key] = 0
        else:
            en_w_list[key] = value
    """

    
    numerator = 0
    kr_v = 0
    en_v = 0
    #cos_sim = 0
    for value in kr_w_list.values():
        kr_v += pow(value, 2)
    for value in en_w_list.values():
        en_v += pow(value, 2)
    for kr_key, kr_value in kr_w_list.items():
        for en_key, en_value in en_w_list.items():
            if kr_key == en_key:
                numerator += (kr_value * en_value)
    try:
        cos_sim = numerator/(math.sqrt(kr_v) * math.sqrt(en_v))
        #print(cos_sim)
    except:
        print(kr_v)
        print(en_v)
        return 0
    return cos_sim            
            

def file_in(fp):

    f = open(fp, 'r', encoding = 'utf-8')
    sentences = f.readlines()
    f.close()

    return sentences


def file_out(fp, contents):
    f = open(fp, 'w', encoding = 'utf-8')
    for keys, values in contents.items():
        if keys.find('+') == -1 : continue
        f.write(keys + '\t')
        count = 0
        try:
            for k in sorted(contents[keys], key=contents[keys].get, reverse=True):
                count += 1
                if count <= 25:
                    f.write(k + ' ')
                    f.write(str(contents[keys][k]) + '\t')
                else:
                    continue
        except: continue
        f.write('\n')
    f.close()
    """
    f = open(fp, 'w', encoding = 'utf-8')
    for keys, values in contents.items():
        f.write(keys + '\t')
        try:
            for key, value in contents[keys].items():
                f.write(key + ' ')
                f.write(str(value) + '\t')
        except: continue
        f.write('\n')
    f.close()
    """

    
if __name__ == '__main__':
    kr_en_distance = hashtable()
    kr_dict = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/kr_col_hashtable_norm.txt'
    en_dict = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/eng_hashtable_chi_sort_norm.txt'
    f_out = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/kr_en_col_sorted_chi_norm.txt'

    dict_kr = file_in(kr_dict)
    dict_en = file_in(en_dict)

    print("...\n")
    kr_en_distance = mtp_kr_en(make_dict(dict_kr), make_dict(dict_en))
    print("...\n")
    file_out(f_out, kr_en_distance)
