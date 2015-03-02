import shelve, json, nltk, lxml, cPickle, sys
from gensim import corpora, models, similarities
# Get tag from arguments

TOKENIZERFILE = "tokenizer_nostops_list.shelve"
TAGTODOCIDMAP = "tag_to_docids.json"

tagtype = sys.argv[1]
shelveobj = shelve.open(TOKENIZERFILE)
tagdocidmap = json.load(open(TAGTODOCIDMAP, "r"))
docidcount = 0
docidcorpus = {}
def Update(item):
    global docidcount
    docidcorpus[docidcount] = item
    docidcount += 1
    return True

allworddict = corpora.Dictionary([list(shelveobj[str(item)]) for item in tagdocidmap[tagtype]])

allworddict.filter_extremes(no_below=100, no_above=0.5, keep_n=None)
print "\n"
cPickle.dump(allworddict, open(tagtype + "/worddict.pickle", "wb"))
#allworddict = allworddict.load("words.dict")
corpus = [allworddict.doc2bow(shelveobj[str(item)]) for item in tagdocidmap[tagtype] if Update(item)]
tfidf = models.TfidfModel(corpus)
corpus_tfidf = tfidf[corpus]
corpora.MmCorpus.serialize(tagtype + '/wordcorpus.mm', corpus_tfidf)
json.dump(docidcorpus, open(tagtype + "/docidmap.json", "w"))

