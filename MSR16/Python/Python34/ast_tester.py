
import xml.etree.ElementTree as ET1
import xml.etree.ElementTree as ET2
import xml.etree.ElementTree as ET3
import ast

num_snippet = 0
unparsable = 0;

def readXml(filename):
    global unparsable
    global num_snippet
    
    tree = ET1.parse('C:\\StackOverflow\\Python\\unparsablePython27\\post' + filename + '_unparsable.xml')
    root = tree.getroot();

    unparseroot = ET2.Element("DATA");
    parseroot = ET3.Element("DATA");
    
    with open('C:\\StackOverflow\\Python\\parseError34\\post' + filename + '_error.txt', 'w') as f:       
        for row in root:    
            num_snippet = num_snippet+1
        
            snippet = row[1].text
            #print num_snippet
            try:
                
                compile(snippet, '<string>', 'exec', ast.PyCF_ONLY_AST)
                parserow = ET3.SubElement(parseroot, "ROW")
                ET3.SubElement(parserow, "id").text = row[0].text
                ET3.SubElement(parserow, "snippet").text = snippet     
            except Exception as e:
                # store the unparsable ones
                #f.write(row[0].text)
                #f.write('----')
                f.write(str(e))
                f.write('\n')
                unparserow = ET2.SubElement(unparseroot, "ROW")
                ET2.SubElement(unparserow, "id").text = row[0].text
                ET2.SubElement(unparserow, "snippet").text = snippet      
                unparsable = unparsable + 1
    
        unparsetree = ET2.ElementTree(unparseroot)
        unparsetree.write('C:\\StackOverflow\\Python\\unparsablePython34\\post' + filename + '_unparsable.xml');
        parsetree = ET3.ElementTree(parseroot)
        parsetree.write('C:\\StackOverflow\\Python\\parsablePython34\\post' + filename + '_parsable.xml');

     
            
# The main function#

filelist = ['aa', 'ab', 'ac', 'ad', 'ae', 'af', 'ag', 'ah', 'ai', 'aj', 'ak', 'al', 'am', 'an', 'ao', 'ap', 'aq',
            'ar', 'as', 'at', 'au', 'av', 'aw', 'ax', 'ay', 'az',
            'ba', 'bb', 'bc', 'bd', 'be', 'bf', 'bg', 'bh', 'bi', 'bj', 'bk', 'bl', 'bm', 'bn', 'bo','bp', 'bq',
            'br', 'bs', 'bt', 'bu', 'bv', 'bw', 'bx', 'by', 'bz',
            'ca', 'cb', 'cc', 'cd', 'ce', 'cf', 'cg', 'ch', 'ci', 'cj', 'ck', 'cl', 'cm', 'cn', 'co','cp', 'cq',
            'cr', 'cs', 'ct', 'cu', 'cv', 'cw', 'cx', 'cy', 
            'da', 'db', 'dc', 'dd', 'de', 'df', 'dg', 'dh', 'di', 'dj','dk', 'dl', 'dm', 'dn', 'do','dp', 'dq',
            'dr', 'ds', 'dt', 'du', 'dv']
            
#filelist = ['aa']
for file in filelist:
    #print(file)
    num_snippet = 0
    unparsable = 0;
    readXml(file) 
    #print ('Number of Snippets: ' + str(num_snippet))
    print ( str(num_snippet - unparsable))

