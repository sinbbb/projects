# -*- coding: utf-8 -*-
import os
import os.path
import re

"""

연어 추출 프로그램

"""

def preprocessing(root, fp):
    ifile = "{}/{}".format(root, fp)
    f = open(ifile, 'r', encoding = 'utf-8')
    sentences = list()
    word = re.compile(r"(?P<lemma>.*)\/(?P<tag>.*)")
    for line in f:
        sentence = ""
        sentence = line
        if sentence != "" :
            sentences.append(sentence)
            sentence = ""
    f.close()
    return sentences

def tag_col(sentences, bipmi, tripmi, bichi) :
    light_verb = ["do", "have", "give", "make", "take"]    
    check_nv = re.compile(r"(?P<word>.*)\/(?P<tag>.*)")
    new_sentence = []
    s = ""
    for sentence in sentences:
        tagged = sentence.strip().split() #문장 양 옆의 공백을 지우고 공백을 기준으로 잘라서 리스트에 넣음
        for i, t in enumerate(tagged):
            if tagged[i].find('+') != -1 :
                continue
            word_m = check_nv.match(tagged[i])
            if (word_m.group("word") in light_verb and word_m.group("tag")[0] == "V"): #verb+noun
                if tagged[i].find('+') != -1 :
                    print(tagged[i])
                    continue
                j = i+1
                if j >= len(tagged) : continue
                nword_m = check_nv.match(tagged[j])
                w_dst = j-i
                #verb + noun
                while(nword_m.group("tag").find("N") != 0 and j<len(tagged)-1 and w_dst <= 3):
                    j+=1
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                check_dic = tagged[i] + "+" + tagged[j]
                if check_dic in bi_pmi:
                    tagged[i] = check_dic
                    del tagged[j]
                    continue
            
            if(word_m.group("tag")[0] == "V"):
                #verb+adverb
                
                j = i+1
                if j < len(tagged)-1 :
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                else: continue
                while(nword_m.group("tag").find("RB") != 0 and j<len(tagged)-1 and w_dst < 2):
                    j+=1
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                check_dic = tagged[i] + "+" + tagged[j]
                if check_dic in bipmi:
                    tagged[i] = check_dic
                    del tagged[j]
                    continue
                #verb+prepositional phrase
                j = i+1
                if j >= len(tagged) : continue
                nword_m = check_nv.match(tagged[j])
                w_dst = j-i
                while(nword_m.group("tag").find("IN") != 0 and nword_m.group("tag").find("RP") != 0 and j<len(tagged)-1 and w_dst <= 2):
                    j+=1
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                if(nword_m.group("tag").find("RP") == 0 and j<len(tagged)-1):
                    nnword_m = check_nv.match(tagged[j+1])
                    #verb + particle + preposition
                    check_dic2 = tagged[i] + "+" + tagged[j] + "+" + tagged[j+1]
                    check_dic = tagged[i] + "+" + tagged[j]
                    if check_dic2 in tripmi:
                        print(check_dic2)
                        tagged[i] = check_dic2
                        del tagged[j]
                        del tagged[j+1]
                        continue
                    #verb + particle
                    elif check_dic in bipmi:
                        tagged[i] = check_dic
                        del tagged[j]
                        continue
                #verb + preposition
                check_dic = tagged[i] + "+" + tagged[j]
                if check_dic in bi_pmi:
                    tagged[i] = check_dic
                    del tagged[j]
                    continue
            
            else:
                #s += word_m.group("tag")
                continue
                
        if tagged != "":
            new_sentence.append(tagged)
    return new_sentence          

"""
def file_in(fp):

    f = open(fp, 'r', encoding = 'utf-8')
    sentences = f.readlines()
    f.close()

    return sentences

def file_out(fp, contents):

    f = open(fp, 'a', encoding = 'utf-8')
    for content in contents:
        f.write(content)
        f.write('\n')
    f.close()
"""

if __name__ == '__main__':
    from operator import itemgetter
    from collections import defaultdict
    sentences = list()
    print("start Extraction!")
    root = r"C:\Users\SinBee\Desktop\SinB\parallel_test3\en"
    bi_pmi = defaultdict(int)
    tri_pmi = defaultdict(int)
    bi_chi = defaultdict(int)
    
    #f = 'C:/Users/SinBee/Desktop/SinB/collocation/test/sinb.txt'
    f = open('C:/Users/SinBee/Desktop/SinB/en_kr_all/sinb_pmi_en.txt', 'r', encoding='utf8')
    lines = f.readlines()
    for line in lines:
        word, pmi = line.split('\t')
        bi_pmi[word] = pmi
    f.close()

    f = open('C:/Users/SinBee/Desktop/SinB/en_kr_all/sinb_tri_pmi_en.txt', 'r', encoding='utf8')
    lines = f.readlines()
    for line in lines:
        word, pmi =line.split('\t')
        tri_pmi[word] = pmi    
    f.close()

    f = open('C:/Users/SinBee/Desktop/SinB/en_kr_all/sinb_chisquare_en.txt', 'r', encoding='utf8')
    lines = f.readlines()
    for line in lines:
        word, chi = line.split('\t')
        bi_chi[word] = chi    
    f.close()

    f = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/eng_collocation.txt'
    #f2 = 'C:/Users/SinBee/Desktop/SinB/file_name.txt'
    for root, dirs, files, in os.walk(root):
        if not files : continue
        for file in files:
            if not re.search('\.tagtxt2$', file) : continue
            #print(root, '...', file)
            sentences = preprocessing(root, file)
            sents = tag_col(sentences, bi_pmi, tri_pmi, bi_chi)
            if not sents: continue
            fname, ext = os.path.splitext(file)
            #ofile = "{}/{}.tagged".format(root, fname)
            with open(f, 'a', encoding='utf8') as fout:
                for sent in sents:
                    #print (" ".join(sent), file=fout)
                    print(" ".join(sent), file = fout)
            #with open(f2, 'a', encoding='utf8') as fout:
                #print(str(file), file = fout)

    

    print("Extraction is done!")
