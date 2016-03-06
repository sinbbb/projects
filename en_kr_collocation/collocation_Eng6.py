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
    lines = f.readlines()
    sentences = list()
    word = re.compile(r"(?P<lemma>.*)\/(?P<tag>.*)")
    for line in lines:
        sentence = ""
        sentence = line.strip()
        if sentence != "" :
            sentences.append(sentence)
    f.close()
    return sentences

def frequency_analysis(sentences) :
    light_verb = ["do", "have", "give", "make", "take"]
    from collections import defaultdict
    unigram_freq = defaultdict(int)
    verb_col_freq = defaultdict(int)
    verb_tri_col_freq = defaultdict(int)
    check_nv = re.compile(r"(?P<word>.*)\/(?P<tag>.*)")

    for sentence in sentences:
        tagged = sentence.strip().split() #문장 양 옆의 공백을 지우고 공백을 기준으로 잘라서 리스트에 넣음
        for i in range(0, len(tagged)-2):
            unigram_freq[tagged[i]] = unigram_freq[tagged[i]] + 1 #모든 unigram을 count
            #print(tagged[i], unigram_freq[tagged[i]])
            word_m = check_nv.match(tagged[i])
            if(word_m.group("tag")[0] == "V"):
                #verb+adverb
                j = i+1
                if j < len(tagged)-1:
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                else: continue
                while(nword_m.group("tag").find("RB") != 0 and j<len(tagged)-1 and w_dst < 2):
                    j+=1
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                if (nword_m.group("tag").find("RB") == 0):
                    verb_col_freq[tagged[i]+"+"+tagged[j]] += 1#동사 다음에 부사가 올 경우 dict에 추가하고 count
                #verb+prepositional phrase
                j = i+1
                nword_m = check_nv.match(tagged[j])
                w_dst = j-i
                while(nword_m.group("tag").find("IN") != 0 and nword_m.group("tag").find("RP") != 0 and j<len(tagged)-1 and w_dst <= 2):
                    j+=1
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                if(nword_m.group("tag").find("RP") == 0 and j < len(tagged)-1):
                    nnword_m = check_nv.match(tagged[j+1])
                    #verb + particle + preposition
                    if(nnword_m.group("tag").find("IN") == 0):
                        verb_tri_col_freq[tagged[i] + "+" + tagged[j] + "+" + tagged[j+1]] += 1
                    #verb + particle
                    else:
                        verb_col_freq[tagged[i] + "+" +tagged[j]] += 1
                #verb + preposition
                if(nword_m.group("tag").find("IN") == 0):
                    verb_col_freq[tagged[i] + "+" +tagged[j]] += 1 #동사 다음에 전치사가 올 경우 dict에 추가하고 count

            if (word_m.group("word") in light_verb and word_m.group("tag")[0] == "V"): #verb+noun
                j = i+1
                nword_m = check_nv.match(tagged[j])
                w_dst = j-i
                #verb + noun
                while(nword_m.group("tag").find("N") != 0 and j<len(tagged)-1 and w_dst <= 3):
                    j+=1
                    nword_m = check_nv.match(tagged[j])
                    w_dst = j-i
                if (nword_m.group("tag").find("N") == 0):
                    verb_col_freq[tagged[i]+"+"+tagged[j]] += 1 #경동사 다음에 명사가 올 경우 dict에 추가하고 count

    return unigram_freq, verb_col_freq, verb_tri_col_freq                   

def chi_square(unigram_freq, bigram_freq):
    #chi-square
    from collections import defaultdict
    
    bi_chi = defaultdict(float)
    N = sum(bigram_freq.values())
    for k, v in bigram_freq.items():
        if v < 5: continue
        else:
            o_11 = v
            left, right = k.split('+')
            o_12 = unigram_freq[right] - v
            o_21 = unigram_freq[left] - v
            o_22 = N - (o_11 + o_12 + o_21)
        
            chi_square = N * ( (( (o_11 * o_22 ) - ( o_12 * o_21 ) ) - (N/2))**2 ) / ( (o_11 + o_12) * ( o_21 + o_22) * ( o_11 + o_21) * ( o_12 + o_22 ) )
            bi_chi[k] = chi_square

    return bi_chi

def pmi(unigram_freq, bigram_freq):
    #PMI(pointewise mutual information)
    from collections import defaultdict
    import math
    
    bi_pmi = defaultdict(float)
    N = sum(bigram_freq.values())
    for k, v in bigram_freq.items():
        if v < 5: continue
        else:
            o_11 = v
            left, right = k.split('+')
            o_12 = unigram_freq[right] - v
            o_21 = unigram_freq[left] - v
            o_22 = N - (o_11 + o_12 + o_21)

            pmi = math.log( (o_11 * N)/( (o_11+o_12)*(o_11+o_21) ) )
            bi_pmi[k] = pmi

    return bi_pmi

def tri_pmi(unigram_freq, tri_freq):
    from collections import defaultdict
    import math
    tri_pmi = defaultdict(float)
    N = sum(tri_freq.values())
    for k, v in tri_freq.items():
        if v < 5: continue
        else:
            o_111 = v
            left, mid, right = k.split('+')

            pmi = math.log( (o_111 * N * N)/( unigram_freq[left]*(unigram_freq[mid])*(unigram_freq[right]) ) )

            tri_pmi[k] = pmi

    return tri_pmi

"""
def file_in(fp):

    f = open(fp, 'r', encoding = 'utf-8')
    sentences = f.readlines()
    f.close()

    return sentences
"""
def file_out(fp, contents):

    f = open(fp, 'w', encoding = 'utf-8')
    for content in contents:
        if content != "" :
            f.write(content + "\n")
        else : continue
    f.close()


if __name__ == '__main__':
    from operator import itemgetter
    sentences = list()
    print("start Extraction!")
    root = r"C:\Users\SinBee\Desktop\SinB\parallel_test3\en"
    for root, dirs, files, in os.walk(root):
        if not files : continue
        for file in files:
            if not re.search('\.tagtxt2$', file) : continue
            #print(root, '...', file)
            sentences.extend(preprocessing(root, file))
    #sentences = file_in(r"C:\Users\NLPLAB\Desktop\SinB\collocation\sejong2002.txt")
    #print(sentences)
    f = 'C:/Users/SinBee/Desktop/SinB/en_kr_all/eng_all.txt'
    file_out(f,sentences)
    unigram_freq, bigram_freq, trigram_freq = frequency_analysis(sentences)
    bi_pmi = pmi(unigram_freq, bigram_freq)
    tri_pmi = tri_pmi(unigram_freq, trigram_freq)
    bi_chi = chi_square(unigram_freq, bigram_freq)

    #f = 'C:/Users/SinBee/Desktop/SinB/collocation/test/sinb.txt'
    f = open('C:/Users/SinBee/Desktop/SinB/en_kr_all/sinb_pmi_en.txt', 'w', encoding='utf-8')
    for key in sorted(bi_pmi, key = bi_pmi.get, reverse = True):
        left, right = key.split('+')
        if bi_pmi[key] < 0 : del bi_pmi[key]
        else:
            #f.write(key + "\t" + str(bigram_freq[key]) + "\t" + str(unigram_freq[left]) + "\t" + str(unigram_freq[right]) + "\t" + str(bi_pmi[key])+"\n")
            f.write(key + "\t" + str(bi_pmi[key])+"\n")
    f.close()

    f = open('C:/Users/SinBee/Desktop/SinB/en_kr_all/sinb_tri_pmi_en.txt', 'w', encoding='utf-8')
    for key in sorted(tri_pmi, key = tri_pmi.get, reverse = True):
        left, mid, right = key.split('+')
        if tri_pmi[key] < 0 : del tri_pmi[key]
        else:
            #f.write(key + "\t" + str(trigram_freq[key]) + "\t" + str(unigram_freq[left]) + "\t" + str(unigram_freq[mid]) + "\t" + str(unigram_freq[right]) + "\t" + str(tri_pmi[key])+"\n")
            f.write(key + "\t" + str(tri_pmi[key])+"\n")
    f.close()

    f = open('C:/Users/SinBee/Desktop/SinB/en_kr_all/sinb_chisquare_en.txt', 'w', encoding='utf-8')
    for key in sorted(bi_chi, key = bi_chi.get, reverse = True):
        left, right = key.split('+')
        if bi_chi[key] < 3 : del bi_chi[key]
        else:
            f.write(key + "\t" + str(bi_chi[key])+"\n")
    f.close()

    #f = 'C:/Users/SinBee/Desktop/SinB/eng_all2.txt'
    #file_out(f, sentences)
    

    print("Extraction is done!")
