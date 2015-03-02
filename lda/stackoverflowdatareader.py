from lxml.etree import *
import json

XMLFILE = "stackoverflow.com-Posts.xml"
TAGDATASET = "tagdataset.dat"
TAGCOUNTDATASET = "tag_count_asc_dataset.dat"
DOCIDTOTAGS = "docid_to_tags.json"
TAGSTODOCIDMAP = "tag_to_docids.json"
TAGCOUNTJSON = "tagcount.json"

xmlfile = open(XMLFILE, "r")
count = 0
alltags = set()
docidtotagsmap = {}
tagcount = {}
tagcounttup = []
tagtodocidsmap = {}
for line in xmlfile.readlines():
#for line in xmlfile:
  line = line.strip()
  if line.startswith("<row") and (line.endswith("row>") or line.endswith("/>")):
    rowData = fromstring(line)
    #for item in rowData.attrib:
    #  print item
    #print rowData.attrib["Tags"]
    if "Tags" in rowData.attrib and "Id" in rowData.attrib and "ParentId" not in rowData.attrib:
      id = rowData.attrib["Id"]
      tags = set([tag.replace("<", "").replace(">", "").replace(" ", "_") for tag in rowData.attrib["Tags"].split("><")])
      alltags.update(tags)
      docidtotagsmap.setdefault(id, set())
      docidtotagsmap[id].update(tags)
      for tag in tags:
        tagcount.setdefault(tag, 0)
        tagcount[tag] += 1
        
        tagtodocidsmap.setdefault(tag, set()).add(id)
    count += 1
    if count % 10000 == 0:
      #break
      print count
tagcounttup = [(tag, tagcount[tag]) for tag in tagcount]
print len(alltags)
print len(docidtotagsmap)
tagcounttup.sort(key = lambda x: x[1])
print (tagcounttup[0], tagcounttup[-1])
tagdataset = open(TAGDATASET, "w")
for tag, count in tagcounttup:
  tagdataset.write(tag + "\t" + str(count) + "\n")
docidtotags = {}
totaldocs = len(docidtotagsmap)
print "total docs", totaldocs
#tagcounttup.reverse()
discovered = set()
tagpercentdataset = open(TAGCOUNTDATASET, "w")
count = 0
Flag = True
for tag, count in tagcounttup:
  discovered.update(tagtodocidsmap[tag])
  #print len(discovered)
  percent = float(float(len(discovered) * 100) / float(totaldocs))
  tagpercentdataset.write(tag + "\t" + str(percent) + "\n")
  if len(discovered) == totaldocs and Flag:
    print "reached 100% at: ", tag, count
    Flag = False
  count += 1

for docid in docidtotagsmap:
  docidtotags[docid] = list(docidtotagsmap[docid])

for tag in tagtodocidsmap:
  tagtodocidsmap[tag] = list(tagtodocidsmap[tag])

json.dump(docidtotags, open(DOCIDTOTAGS, "w"))
json.dump(tagtodocidsmap, open(TAGSTODOCIDMAP, "w"))
json.dump(tagcount, open(TAGCOUNTJSON, "w")) 
