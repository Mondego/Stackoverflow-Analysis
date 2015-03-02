from gensim import corpora, models
import cPickle
import json

DICTIONARY = "worddict_nostops_list_lemmet.pickle"
LDAMODEL = "workingset_cleanwords/withoutcode_asymmetric_tfidf_27.lda"
CORPUS = "wordcorpus_tfidf.mm"
DOCIDMAP = "docidmap_tfidf.json"
DOCIDTOTAGMAP = "docid_to_tags.json"
PARTITIONFILE = "partition_1_27.json"
TOPICTAGDISTRIB = "topictag_distrib_27.json"

str2word = cPickle.load(open(DICTIONARY, "rb"))
id2word = {}
for word in str2word:
  id2word[int(word)] = str2word[word]

model = models.LdaModel.load(LDAMODEL)
print "loaded model"
corpus = list(corpora.MmCorpus(CORPUS))
print "loaded corpus"
docidmap = json.load(open(DOCIDMAP, "r"))
print "loaded docidmap"
docidtagsmap = json.load(open(DOCIDTOTAGMAP, "r"))
print "loaded docid tags"
docidtopic = {}
topic_tag = {}
partitions = json.load(open(PARTITIONFILE, "r"))

docid_to_topics = {}
print "getting topics"
for i in (partitions["start"] + partitions["train"]):
  key = str(i)
  topics = model[corpus[i]]
  topics.sort(key = lambda x:x[1], reverse = True)
  docidtopic[key] = topics[0][0]
  strtopic = str(topics[0][0])
  topic_tag.setdefault(strtopic, {})
  if key in docidmap and docidmap[key] in docidtagsmap:
    for tag in docidtagsmap[docidmap[key]]:
      topic_tag[strtopic].setdefault(tag, 0)
      topic_tag[strtopic][tag] += 1
#  else:
#    print "no tag/id", key

  if i%10000 == 0:
    print i

print "transforming"
finaljson = {}
for topic in topic_tag:
  taglist = [(key, topic_tag[topic][key]) for key in topic_tag[topic]]
  taglist.sort(key = lambda x:x[1], reverse = True)
  tags = [tag for tag, count in taglist]
  counts = [count for tag, count in taglist]
  finaljson[topic] = {"tags": tags, "counts": counts}

json.dump(finaljson, open(TOPICTAGDISTRIB, "w"), sort_keys = True, indent = 4, separators = (',', ': '))
print "Done!!"

