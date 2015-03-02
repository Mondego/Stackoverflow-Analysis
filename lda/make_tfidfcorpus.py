import json, lxml, re
from lxml.etree import *
import shelve, cPickle
import nltk
from gensim import corpora, models, similarities

TOKENSHELVE = "tokenizer_nostops_list_lemmet.shelve"
DICTIONARY = "worddict_nostops_list_lemmet.pickle"
CORPUS = "wordcorpus_tfidf.mm"
DOCIDMAP = "docidmap_tfidf.json"

docidcorpus = {}
#tokenre = re.compile("[a-zA-Z][a-zA-Z-]+[a-zA-Z]$")
#pascalcamelre = re.compile("([a-z]+|[A-Z][a-z]*)([A-Z][a-z]*)*")
docidcount = 0
#allworddict = corpora.Dictionary()
def Update(item):
    global docidcount
    docidcorpus[docidcount] = item
    docidcount += 1
    return True
shelveobj = dict(shelve.open(TOKENSHELVE))

allworddict = cPickle.load(open(DICTIONARY, "r"))
corpus = [allworddict.doc2bow(shelveobj[item]) for item in shelveobj if Update(item)]
tfidf = models.TfidfModel(corpus)
corpus_tfidf = tfidf[corpus]
corpora.MmCorpus.serialize(CORPUS, corpus_tfidf)
json.dump(docidcorpus, open(DOCIDMAP, "w"))

