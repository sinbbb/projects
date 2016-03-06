# -*- coding: utf-8 -*-
import re
import os
import os.path
import collections

"""

한국어 이중해쉬테이블

"""


def make_dict(sentences):
    kr_dict = {}
    for sentence in sentences:
        s = re.split('\t', sentence[:-1]) #마지막 \n을 제외한 단어를 띄어쓰기 단위로 리스트에 추가
        for i, word in enumerate(s):
            if i == 0:
                kr_dict[word] = s[i+1]
            else: continue
    print("...\n")
    return kr_dict

def make_hashtable(sentences):
    from collections import defaultdict
    kr_ht = defaultdict(lambda : defaultdict(int))
    N = 0
    for sentence in sentences:
        s = re.split(' ', sentence[:-1]) #마지막 \n을 제외한 단어를 띄어쓰기 단위로 리스트에 추가
        for i, word in enumerate(s):  #sentence의 단어가 테이블에 있는지 확인 후 없으면 추가
            word_r = i+3            #단어를 기준으로 오른쪽, 왼쪽 3번째 단어까지 해쉬테이블에 추가하고 freq를 1로 설정
            if word_r >= len(s): word_r = len(s)-1
            word_l = i-3
            if word_l < 0: word_l = 0
            
            if word not in kr_ht.keys():
                for num in range(word_l, word_r+1):
                    if num == i : continue
                    kr_ht[word][s[num]] = 1
                    N += 1
                    #print(kr_ht)
            else:
                for num in range(word_l, word_r+1):
                    if num == i : continue
                    if s[num] not in kr_ht[word].keys(): kr_ht[word][s[num]] = 1
                    else:
                        kr_ht[word][s[num]] += 1
                        N += 1
    N = int(N/2)
    return kr_ht, N


def chi_square(kr_dict, N):
    #chi-square
    from collections import defaultdict
    bi_chi = defaultdict(lambda : defaultdict(float))
    print(N)
    for keys, values in kr_dict.items():
        for k, v in kr_dict[keys].items():
            if v<5:continue
            o_11 = int(v)
            left = keys
            right = k
            o_12 = sum(kr_dict[keys].values()) - v
            o_21 = sum(kr_dict[k].values()) - v
            o_22 = N - (o_11 + o_12 + o_21)
            chi_square = N * ( (( (o_11 * o_22 ) - ( o_12 * o_21 ) ) - (N/2))**2 ) / ( (o_11 + o_12) * ( o_21 + o_22) * ( o_11 + o_21) * ( o_12 + o_22 ) )

            if chi_square > 3.6: 
                bi_chi[keys][k] = chi_square

    return bi_chi


def change_hashtable(kr_ht, kr_en_dict):
    kr_dict = list()
    for keys, values in kr_ht.items():
        for key, value in kr_ht[keys].items():
            if key in kr_en_dict.keys():
                kr_dict.append([kr_en_dict[key], kr_ht[keys][key]])
            else: continue
        kr_ht[keys].clear()
        for k, v in kr_dict:
            kr_ht[keys][k] = v
        kr_dict = []
            

    return kr_ht
            
            

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
        try:
            for key, value in contents[keys].items():
                v_all = sum(contents[keys].values())
                f.write(key + ' ')
                f.write(str(value/v_all) + '\t')
        except: continue
        f.write('\n')
    f.close()

if __name__ == '__main__':
    from collections import defaultdict
    kr_change = defaultdict(lambda : defaultdict(float))
    mkht = defaultdict(lambda : defaultdict(int))
    f_dict = 'C:/Users/SinBee/Desktop/Program/Eclipse/cProject/dict/normalized.KR-EN.dictionary'
    f_in = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/kr_collocation.nontagged'
    f_out = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/kr_col_hashtable_norm.txt'

    dict_s = file_in(f_dict)

    sentences = file_in(f_in)
    print("...\n")
    mkht, N = make_hashtable(sentences)
    #kr_change = chi_square(mkht)
    kr_change = change_hashtable(chi_square(mkht, N), make_dict(dict_s))
    
    file_out(f_out, kr_change)
