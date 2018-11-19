import lxml
from lxml.etree import *
import json
import re
import HTMLParser
import os
import lxml.html

postcount = 0
snippetcount = 0
python_flag = False
idtotags = {}

outputfile = "python-multiline"

# get all id to tags mapping
with open("2016-12-15/Posts.xml", "r") as f:
	for line in f:
		line = line.strip()
		if line.startswith("<row") and (line.endswith("row>") or line.endswith("/>")):
			postcount += 1
			rowData = fromstring(line)
			id = rowData.attrib["Id"]
			# get all the tags
			if "Tags" in rowData.attrib:
				# this is a Question post
				idtotags.setdefault(id, set())
				tags = set([tag.replace("<", "").replace(">", "").replace(" ", "_").lower() for tag in rowData.attrib["Tags"].split("><")])
				idtotags[id].update(tags)
		if postcount%1000000 == 0:
			print "Tags done with: ", postcount

print "Done loading tag information"

with open("2016-12-15/Posts.xml", "r") as f:
	with open(outputfile, "w") as fw:
		for line in f:
			line = line.decode("utf-8").strip()
			if line.startswith("<row") and (line.endswith("row>") or line.endswith("/>")):
				rowData = fromstring(line)
				id = rowData.attrib["Id"]
				# get all the tags
				tags = None
				if "Tags" in rowData.attrib:
					# this is a Question post
					tags = idtotags[id] 
				elif "ParentId" in rowData.attrib: 
					# this is an Answer post
					parentId = rowData.attrib["ParentId"]
					if parentId in idtotags:
						tags = idtotags[parentId]
					else:
						print "cannot find tags for parentId", id
				# check if it's a question about python
				python_flag = False
				if tags != None:
					for tag in tags:
						if "python" in tag:
							python_flag = True
			
				if python_flag and rowData.attrib["Body"].strip() != "":
					# print tags
					body = rowData.attrib["Body"].strip()
					# get the code snippets
					codes = re.findall("<code>.*?</code>", body, re.DOTALL)
					for snippet in codes:
						# remove html tags and unescape html special characters
						snippet = lxml.html.document_fromstring(snippet).text_content()
						h = HTMLParser.HTMLParser()
						snippet = h.unescape(snippet)			
                                                
						linecount = snippet.count("\n")
                                                if not snippet.endswith("\n"):
                                                        linecount += 1
						if linecount > 1: # multiline snippet
							snippetcount += 1
							idtosnippet = {id : snippet}
							json.dump(idtosnippet, fw)
							fw.write("\n")
							#filename = "python-multiline/" + id + "/" + str(snippetcount) + ".py"
							#if not os.path.exists(os.path.dirname(filename)):
    							#	try:
							#		os.makedirs(os.path.dirname(filename))
							#	except OSError as exc: # Guard against race condition
							#		if exc.errno != errno.EEXIST:
							#				raise
							#with open(filename, "w") as fw:
							#	fw.write(snippet.encode("utf-8") + "\n")
			if snippetcount%10000 == 0:
				print "Snippets done with: ", snippetcount
print "Total number of posts: ", postcount
print "Total number of multiline snippets: ", snippetcount
