from gensim import corpora, models, similarities
import cPickle, json
import numpy
numpy.random.seed(42)
import random as rm

CORPUS = "wordcorpus_tfidf.mm"
DICTIONARY = "worddict_nostops_list_lemmet.pickle"
PARTITIONFILE = "worddict_nostops_list_lemmet.pickle"
LDAMODEL = "workingset_cleanwords/withoutcode_asymmetric_tfidf_27.lda"
TOPICVIEW = "topicview_asymmetric_tfidf_27.txt"
RAWMODEL = "workingset_cleanwords/withoutcode_asymmetric_tfidf_27.pickle"
corpus = corpora.MmCorpus(CORPUS)
#corpus.load("wordcorpus.mm")
#print(corpus)
#raw_input()
corpuslist = list(corpus)
print("starting")
#for i in range(2, 10):
#  if i == 5:
#    continue
#  print i

str2word = cPickle.load(open(DICTIONARY, "rb"))
id2word = {}
for word in str2word:
  id2word[int(word)] = str2word[word]

indices = set(range(0, len(corpus)))
test = set(rm.sample(indices, 1000000))
train = indices - test
start = set(rm.sample(train, 4000000))
remain = list(train - start)
startcorpus = [corpus[i] for i in start]
json.dump({"start": list(start), "test": list(test), "train": list(train)}, open(PARTITIONFILE, "w"))
lda = models.LdaModel(corpuslist, num_topics = 27, alpha = "asymmetric", id2word=id2word)

updateiters = int(len(remain) / 100000) + 1
print "updateiters: ", updateiters
for x in range(0, updateiters):
#  lda.save("workingset_cleanwords/reruns_" + str(i) + ".lda")  
#for i in range(1,100):
  print x
  lda.update([corpus[i] for i in remain[x*100000:(x+1)*100000]])
print("Saving!!")
lda.save(LDAMODEL)
cPickle.dump(lda, open(RAWMODEL, "w"))
topics =  lda.show_topics(topics=300, formatted=False)
fileout = open(TOPICVIEW, "w")
i = 0
for untopic in topics:
  fileout.write("Topic " + str(i) + "\n")
  i += 1
  topic = sorted(untopic, key = lambda x: x[0], reverse = True)
  #print topic
  #break
  for weight, word in topic:
    fileout.write(word + "\t" + str(weight) + "\n")
  fileout.write("\n")

fileout.write(str(lda.alpha))
fileout.close()
print("DONE!!!!")
