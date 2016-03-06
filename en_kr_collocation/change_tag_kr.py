import os
import os.path
import re

"""

불필요한 단어 삭제 or 변환

"""

def change_tag(root, fileid) :
    fname = 'kr_all_changetag'
    ifile = "{}/{}".format(root, fileid)
    ofile = "{}/{}.tagtxt".format(root, fname)
    find_slash = re.compile(".*\/\/.*") #텍스트에 /가 있는지 탐색
    find_pluss = re.compile(".*\+\/.*")  #텍스트에 +가 있는지 탐색
    find_plus = re.compile("\+")
    find_num = re.compile("__[0-9][0-9]")   #텍스트에 __[0-9][0-9]가 있는지 탐색
    f = open(ifile, 'r', encoding = 'utf8')
    sentence = ""
    sentences = list()
    for line in f:
        words = line.split(' ')
        for word in words:
            """
            w_slash = find_slash.search(word)
            w_pluss = find_pluss.search(word)
            w_num = find_num.search(word)
            if w_slash: #문자에 '/'기호가 들어간 단어는 '/' 대신 '@'로 구분
                word = find_slash.sub('@/', word)
            if w_pluss:
                word = find_pluss.sub('***/', word)
            if w_num:
                word = find_num.sub('', word)
                """
            word = find_slash.sub('@/', word)
            word = find_pluss.sub('***/', word)
            word = find_num.sub('', word)
            word = find_plus.sub(' ', word)
            sentence = word + " "

            sentences.append(sentence)
    out_f = open(ofile, 'w', encoding='utf8')
    for s in sentences:
        out_f.write(s)
    f.close()
    out_f.close()


if __name__ == '__main__': 
    root = 'C:/Users/SinBee/Desktop/SinB/en_kr_all'
    file = 'kr_all.txt.tag'
    sents = change_tag(root, file)
