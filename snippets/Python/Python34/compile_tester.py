
import xml.etree.ElementTree as ET


num_snippet = 0
uncompilable = 0;

def readXml(filename):
    global uncompilable
    global num_snippet
    
    tree = ET.parse('C:\\StackOverflow\\Python\\parsablePython34\\post' + filename + '_parsable.xml')
    root = tree.getroot();

    with open('C:\\StackOverflow\\Python\\compileError34\\post' + filename + '_error.txt', 'w') as f:
        for row in root:    
            num_snippet = num_snippet+1
    
            try:
                snippet = row[1].text
#               print snippet
                compile(snippet, '<string>', 'exec')
            except Exception as e:
                f.write(str(e))
                f.write('\n')
                uncompilable = uncompilable + 1;  

     
            
# The main function#

filelist = ['aa', 'ab', 'ac', 'ad', 'ae', 'af', 'ag', 'ah', 'ai', 'aj', 'ak', 'al', 'am', 'an', 'ao', 'ap', 'aq',
            'ar', 'as', 'at', 'au', 'av', 'aw', 'ax', 'ay', 'az',
            'ba', 'bb', 'bc', 'bd', 'be', 'bf', 'bg', 'bh', 'bi', 'bj', 'bk', 'bl', 'bm', 'bn', 'bo','bp', 'bq',
            'br', 'bs', 'bt', 'bu', 'bv', 'bw', 'bx', 'by', 'bz',
            'ca', 'cb', 'cc', 'cd', 'ce', 'cf', 'cg', 'ch', 'ci', 'cj', 'ck', 'cl', 'cm', 'cn', 'co','cp', 'cq',
            'cr', 'cs', 'ct', 'cu', 'cv', 'cw', 'cx', 'cy', 
            'da', 'db', 'dc', 'dd', 'de', 'df', 'dg', 'dh', 'di', 'dj','dk', 'dl', 'dm', 'dn', 'do','dp', 'dq',
            'dr', 'ds', 'dt', 'du', 'dv']
            
#filelist = ['cz']
for file in filelist:
    #print(file)
    num_snippet = 0
    uncompilable = 0;
    readXml(file) 
    #print ('Number of Snippets: ' + str(num_snippet))
    print (str(num_snippet - uncompilable))

