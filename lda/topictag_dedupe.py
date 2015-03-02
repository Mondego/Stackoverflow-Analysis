import json
topictag = json.load(open("topictag_distrib.json", "r"))
tagtopic = {}
for topic in topictag:
  for tag in topictag[topic]["tags"]:
    tagtopic.setdefault(tag, set())
    tagtopic[tag].add(topic)

finaltopictag = {}
for topic in topictag:
  finaltopictag[topic] = []
  for tag in topictag[topic]["tags"]:
    #print (len(tagtopic[tag]) / 300)
    if len(tagtopic[tag]) < 5:
      finaltopictag[topic].append(tag)

json.dump(finaltopictag, open("topictag_distrib_less_2.json", "w"), sort_keys = True, indent = 4, separators = (',', ': '))


tagtopiccount = {}
for tag in tagtopic:
  tagtopiccount[tag] = len(tagtopic[tag])

json.dump(tagtopiccount, open("topictag_count.json", "w"), sort_keys = True, indent = 4, separators = (',', ': '))

topictag_re = {}
topictag_reordered = {}
for topic in topictag:
  topictag_re[topic] = []
  for i in range(len(topictag[topic]["tags"])):
    topictag_re[topic].append((topictag[topic]["tags"][i], float(1/len(tagtopic[tag]))))
    
for topic in topictag_re:
  topictag_re[topic].sort(key = lambda x: x[1], reverse = True)

json.dump(topictag_re, open("topictag_redistrib.json", "w"), sort_keys = True, indent = 4, separators = (',', ': '))
    
