from nltk.corpus.reader.bnc import BNCCorpusReader
import os
import os.path 
import nltk.data
import re
from nltk.tokenize import word_tokenize

sent_detector = nltk.data.load('tokenizers/punkt/english.pickle')

def extract_sent_from_MECO(root, fileid):
    ifile = "{}/{}".format(root, fileid)
    codeset = ['cp949', 'utf8', 'cp1252']
    for code in codeset:
        try:
            text = open(ifile, 'r', encoding=code).read()
            return [word_tokenize(sent) for sent in sent_detector.tokenize(text.strip())]
        except UnicodeDecodeError:
            continue
    return None
    
    
    
if __name__ == '__main__':
    root='C:/Users/SinBee/Desktop/SinB/test_file/en/ybm'
    for root, dirs, files in os.walk(root):
        if not files : continue 
        for file in files:
            if not re.search('\.eal$', file) : continue
            print (root, '...', file)
            sents =  extract_sent_from_MECO(root, file)
            if not sents: continue 
            fname, ext = os.path.splitext(file)
            ofile = "{}/{}.tok".format(root, fname)
            with open(ofile, 'w', encoding='utf8') as fout:
                for sent in sents:
                    print (" ".join(sent), file=fout)


