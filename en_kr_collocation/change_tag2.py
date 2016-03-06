import os
import os.path
import re

"""

TreeTagger 변환
태그를 제외한 단어를 소문자로 변환
word tag lemma로 구성된 형태를
lemma/tag로 변환

"""

def change_tag(root, fileid) :
    fname = re.sub('\.lemma$', '',  fileid)
    ifile = "{}/{}".format(root, fileid)
    ofile = "{}/{}.tagtxt2".format(root, fname)
    find_slash = re.compile(".*\/.*")
    find_plus = re.compile(".*\+.*")
    find_slash_s = re.compile(".*/s.*")
    f = open(ifile, 'r', encoding = 'utf8')
    sentence = ""
    sentences = list()
    for line in f:
        word = line.split('\t')
        w_slash = find_slash.search(word[0])
        w_plus = find_plus.search(word[0])
        if(len(word) != 3) :
            end_ol = find_slash_s.match(word[0])
            if end_ol :
                word = '\n'
                sentence = word
            else : continue
        
        elif w_slash: #문자에 '/'기호가 들어간 단어는 '/' 대신 '@'로 구분
            l_word = word[0].replace("/", "@")
            r_word = word[2].replace("/", "@")
            sentence = r_word[:-1].lower() + "/" + word[1] + " "
            #sentence = l_word + "/" + word[1] + "/" + r_word[:-1].lower() + " "
        elif w_plus:
            l_word = word[0].replace("+", "***")
            r_word = word[2].replace("+", "***")
            sentence = r_word[:-1].lower() + "/" + word[1] + " "
            #sentence = l_word + "/" + word[1] + "/" + r_word[:-1].lower() + " "
        elif(word[1] != "SENT" and word[1] != ":" and word[1] != "''" and word[1] != "," and word[1] != "DT" and word[1] != "``"):
            if(word[1] == "IN/that"): #접속사 that이 IN/that으로 태깅 되는 것을 IN으로 변경
                sentence = word[2][:-1].lower() + "/IN "
                #sentence = word[0].lower() + "/IN/" + word[2][:-1].lower() + " "
            if(word[1][0] == 'V'):
                sentence = word[2][:-1].lower() + "/VV "
            else:
                sentence = word[2][:-1].lower() + "/" + word[1] + " "
                #sentence = word[0].lower() + "/" + word[1] + "/" + word[2][:-1].lower() + " "
                # word[2]의 마지막 문자가 \n이기 때문에 뒤에 한 글자 빼고 합침           
        else:
            continue
            #sentence = word[0] + "/" + word[1] + " "
            #sentence = word[0] + "/" + word[1] + "/" + word[2]
        sentences.append(sentence)
    out_f = open(ofile, 'w', encoding='utf8')
    for s in sentences:
        out_f.write(s)
    f.close()
    out_f.close()


if __name__ == '__main__': 
    root = r"C:\Users\SinBee\Desktop\SinB\parallel_test3\en"
    #root = 'C:/Users/SinBee/Desktop/SinB/test_file/en/ybm'
    for root, dirs, files, in os.walk(root):
        if not files :
            continue
        print(root)
        for file in files:
            if not re.search('\.lemma$', file) : continue
            #print(root, '...', file)
            sents = change_tag(root, file)
