# -*- coding: utf-8 -*-
import re
import os
import os.path
import collections

"""



"""

def hashtable(): return collections.defaultdict(hashtable) #이중hashtable

def make_hashtable(sentences):
    kr_ht = hashtable()
    N = 0
    for sentence in sentences:
        s = sentence.strip().split()
        #s = re.split(' ', sentence[:-1]) #마지막 \n을 제외한 단어를 띄어쓰기 단위로 리스트에 추가
        for i, word in enumerate(s):  #sentence의 단어가 테이블에 있는지 확인 후 없으면 추가
            word_r = i+3            #단어를 기준으로 오른쪽, 왼쪽 3번째 단어까지 해쉬테이블에 추가하고 freq를 1로 설정
            if word_r >= len(s): word_r = len(s)-1
            word_l = i-3
            if word_l < 0: word_l = 0
            
            if word not in kr_ht.keys():
                for num in range(word_l, word_r+1):
                    if num == i : continue
                    #if word == s[num] : continue
                    kr_ht[word][s[num]] = 1
                    N += 1
                    #print(kr_ht)
            else:   #이미 단어가 있다면 count++
                for num in range(word_l, word_r+1):
                    if num == i : continue
                    #if word == s[num] : continue
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
            if v<5 : continue   #value값이 5 미만인 단어는 버림
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


def file_in(fp):

    f = open(fp, 'r', encoding = 'utf-8')
    sentences = f.readlines()
    f.close()

    return sentences


def file_out(fp, contents):
    
    f = open(fp, 'w', encoding = 'utf-8')
    for keys, values in contents.items():
        f.write(keys + '\t')
        for key, value in contents[keys].items():
            v_all = sum(contents[keys].values())
            f.write(key + ' ')
            f.write(str(value/v_all) + '\t')
        f.write('\n')
    f.close()
    

if __name__ == '__main__':
    from collections import defaultdict
    en_change = defaultdict(lambda : defaultdict(float))
    f_in = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/eng_collocation.nontagged'
    f_out = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/eng_hashtable_chi_sort_norm.txt'
    sentences = file_in(f_in)
    en_change, N = make_hashtable(sentences)
    file_out(f_out, chi_square(en_change, N))




