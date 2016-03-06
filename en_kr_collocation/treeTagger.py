# -*- coding: utf-8 -*-
from nltk.corpus.reader.bnc import BNCCorpusReader
import os
import os.path 
import nltk.data
import nltk
import re
import subprocess


def treetagger(root, fileid):
    fname = re.sub('\.eal$', '', fileid)
    ifile = "{}/{}".format(root, fileid)
    ofile = "{}/{}.lemma".format(root, fname)
    cmd = 'c:/TreeTagger/bin/tag-english.bat "{}" > "{}"'.format(ifile, ofile)
    f = os.popen(cmd)
    for e in f.read():
        print("Error: ", s)
  
if __name__ == '__main__':
    root = 'C:/Users/SinBee/Desktop/SinB/en_kr_test/time_en'
    for root, dirs, files in os.walk(root):
        if not files :
            continue
        for file in files:
            if not re.search('\.eal$', file) : continue
            print (root, '...', file)
            sents = treetagger(root, file)
