import json, lxml, re
from lxml.etree import *
import shelve, cPickle
import nltk

#wml = nltk.WordNetLemmatizer()
stops = dict([(i , 1) for i in "a a's   able    about   above   according accordingly   across  actually    after   afterwards again    against ain't   all allow allows    almost  alone   along   already also    although    always  am  among amongst   an  and another any anybody anyhow  anyone  anything    anyway anyways  anywhere    apart   appear  appreciate appropriate  are aren't  around  as aside    ask asking  associated  at available    away    awfully be  became because  become  becomes becoming    been before beforehand  behind  being   believe below   beside  besides best    better between  beyond  both    brief   but by  c'mon   c's came    can can't   cannot  cant    cause   causes certain  certainly   changes clearly co com  come    comes   concerning  consequently consider   considering contain containing  contains corresponding  could   couldn't    course  currently definitely    described   despite did didn't different    do  does    doesn't doing don't done    down    downwards   during each edu eg  eight   either else elsewhere   enough  entirely    especially et   etc even    ever    every everybody everyone    everything  everywhere  ex exactly  example except  far few fifth   first   five    followed    following follows   for former  formerly    forth four  from    further furthermore get gets    getting given   gives   go goes going   gone    got gotten greetings    had hadn't  happens hardly has  hasn't  have    haven't having he   he's    hello   help    hence her   here    here's  hereafter   hereby herein   hereupon    hers    herself hi him  himself his hither  hopefully how   howbeit however i'd i'll i'm    i've    ie  if  ignored immediate   in  inasmuch    inc indeed indicate indicated   indicates   inner   insofar instead into    inward  is  isn't it    it'd    it'll   it's    its itself  just    keep    keeps   kept know   known   knows   last    lately later    latter  latterly    least   less lest   let let's   like    liked likely    little  look    looking looks ltd   mainly  many    may maybe me    mean    meanwhile   merely  might more  moreover    most    mostly  much must   my  myself  name    namely nd   near    nearly  necessary   need needs  neither never   nevertheless    new next    nine    no  nobody  non none    noone   nor normally    not nothing novel   now nowhere obviously of    off often   oh  ok okay old on  once    one ones    only    onto    or  other others    otherwise   ought   our ours ourselves  out outside over    overall own particular  particularly    per perhaps placed  please  plus    possible    presumably probably provides    que quite   qv rather   rd  re  really  reasonably regarding    regardless  regards relatively  respectively right  said    same    saw say saying  says    second  secondly    see seeing  seem    seemed  seeming seems seen  self    selves  sensible    sent serious    seriously   seven   several shall she   should  shouldn't   since   six so  some    somebody    somehow someone something   sometime    sometimes   somewhat    somewhere soon  sorry   specified   specify specifying still    sub such    sup sure t's    take    taken   tell    tends th    than    thank   thanks  thanx that  that's  thats   the their theirs    them    themselves  then    thence there    there's thereafter  thereby therefore therein   theres  thereupon   these   they they'd they'll they're they've think third this    thorough    thoroughly  those though    three   through throughout  thru thus   to  together    too took toward towards tried   tries   truly try   trying  twice   two un under    unfortunately   unless  unlikely    until unto  up  upon    us  use used    useful  uses    using   usually value   various very    via viz vs  want    wants   was wasn't way  we  we'd    we'll   we're we've welcome well    went    were weren't    what    what's  whatever    when whence whenever    where   where's whereafter whereas  whereby wherein whereupon   wherever whether    which   while   whither who who's   whoever whole   whom    whose why   will    willing wish    with within without won't   wonder  would wouldn't  yes yet you you'd you'll    you're  you've  your    yours yourself  yourselves  zero".split()])
from gensim import corpora, models, similarities

TOKENSHELVE = "tokenizer_nostops_list.shelve"
XMLFILE = "stackoverflow.com-Posts.xml"
DICTIONARY = "worddict_nostops_list.pickle"
CORPUS = "wordcorpus_latest_clean_list.mm"
DOCIDMAP = "docidmap_nostops_list.json"

shelveobj = shelve.open(TOKENSHELVE)
tokenmap = {}
#shelvemap = shelve.open("tokens_latest.shelve")
docidcorpus = {}
tokenre = re.compile("[a-zA-Z][a-zA-Z-]+[a-zA-Z]$")
#pascalcamelre = re.compile("([a-z]+|[A-Z][a-z]*)([A-Z][a-z]*)*")
docidcount = 0
allworddict = corpora.Dictionary()
def Update(item):
    global docidcount
    docidcorpus[docidcount] = item
    docidcount += 1
    return True

def isvalid(token):
    #print tokenre.match(token), token
    return tokenre.match(token)
        

def Tokenize(para):
    #print para
    #try:
    body = fromstring(para, parser=HTMLParser())
    #except:
    #    print para
    #    raw_input()
    for code in body.xpath("//code"):
        code.getparent().remove(code)
    para = tostring(body)
    #print para
    result = []
    for nicetoken in  set([token for token in nltk.word_tokenize(nltk.clean_html(para)) if isvalid(token.lower().strip())]):
        result.extend(re.sub(r'((?<=[a-z])[A-Z]|(?<!\A)[A-Z](?=[a-z]))', r' \1', nicetoken).split())
#    print set(result)
    return [res.lower() for res in result if res.lower() not in stops]

def processNodeAsAns(rowData, id):
    if rowData["Body"].strip() != "":
        x = Tokenize(rowData["Body"])
        if rowData["ParentId"] not in shelveobj:
            shelveobj[rowData["ParentId"].encode("utf-8")] = x
        else:
            shelveobj[rowData["ParentId"].encode("utf-8")].extend(x)
        shelveobj.sync()
    #print x, "|", shelveobj[rowData["ParentId"].encode("utf-8")]

def processNodeAsQ(rowData, id):
    if rowData["Body"].strip() != "":
        x = Tokenize(rowData["Body"])
        if id not in shelveobj:
            shelveobj[id.encode("utf-8")] = x
        else:
            shelveobj[id.encode("utf-8")].extend(x)
        shelveobj.sync()
    #print x, "|", shelveobj[id.encode("utf-8")]

xmlfile = open(XMLFILE, "r")
count = 0

for line in xmlfile.readlines():
#    print "line: ", line,"\n" 
#for line in xmlfile:
    line = line.strip()
    if line.startswith("<row") and (line.endswith("row>") or line.endswith("/>")):
        rowData = fromstring(line)
        id = rowData.attrib["Id"]
        if "ParentId" in rowData.attrib:
            processNodeAsAns(rowData.attrib, id)
        else:
            processNodeAsQ(rowData.attrib, id)
    else:
        print line
    count +=1
#    if count == 10:
#`        break
    if not count%1000:
        print count

'''
for item in shelveobj:
    jsonmap[item] = list(tokenmap[item])
    shelvemap[item] = list(tokenmap[item])
    shelvemap.sync()
'''
allworddict = corpora.Dictionary([list(shelveobj[item]) for item in shelveobj])

allworddict.filter_extremes(no_below=100, no_above=0.5, keep_n=None)
print("Dictionary")
print(shelveobj)
print "\n"
print(allworddict.values())
cPickle.dump(allworddict, open(DICTIONARY, "wb"))
#allworddict = allworddict.load("words.dict")
corpus = [allworddict.doc2bow(shelveobj[item]) for item in shelveobj if Update(item)]

corpora.MmCorpus.serialize(CORPUS, corpus)
json.dump(docidcorpus, open(DOCIDMAP, "w"))
#lda = models.LdaModel(corpus)
#lda.save("vanilla.lda")

