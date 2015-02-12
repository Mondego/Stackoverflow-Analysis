
import xml.etree.ElementTree as di_ET
import xml.etree.ElementTree as di_ET2
import sys

di_num_snippet = 0
di_unrunnable = 0;

def readXml(di_filename):
    global di_unrunnable
    global di_num_snippet
    
    di_tree = di_ET.parse('C:\\StackOverflow\\Python\\parsablePython27Run\\post' + di_filename + '_parsable.xml')
    di_root = di_tree.getroot();
    
    di_unrunnableroot = di_ET2.Element("DATA");

    with open('C:\\StackOverflow\\Python\\runError27\\post' + di_filename + '_error.txt', 'w') as di_outputFile:
        for di_row in di_root:    
            di_num_snippet = di_num_snippet+1
           # print str(num_snippet)
           
            try:
                di_snippet = di_row[1].text
#               print snippet
                print di_row[0].text
                if 'raw_input(' not in di_snippet and 'input(' not in di_snippet and 'input (' not in di_snippet and 'sys.stdin' not in di_snippet and 'code.interact' not in di_snippet\
                and 'exit' not in di_snippet and 'help()' not in di_snippet and 'atexit' not in di_snippet and 'unittest.main(' not in di_snippet and 'quit(' not in di_snippet \
                and 'getpass.getpass(' not in di_snippet and 'import subprocess' not in di_snippet and 'from multiprocessing ' not in di_snippet and 'import socket' not in di_snippet\
                and 'str' not in di_snippet and 'pdb' not in di_snippet and ' Tkinter' not in di_snippet and 'setup(' not in di_snippet and 'BaseHTTPServer' not in di_snippet \
                and 'thread' not in di_snippet and 'while True' not in di_snippet and 'while(1)' not in di_snippet and 'while 1' not in di_snippet and 'while(True)' not in di_snippet \
                and 'argparse' not in di_snippet and 'serve_forever()' not in di_snippet and 'import itertools' not in di_snippet:
                    exec di_snippet
                
            except Exception as di_e: 
                di_outputFile.write(str(di_e))
                di_outputFile.write('\n')
                
                di_unrunnablerow = di_ET2.SubElement(di_unrunnableroot, "ROW")
                di_ET2.SubElement(di_unrunnablerow, "id").text = di_row[0].text
                di_ET2.SubElement(di_unrunnablerow, "snippet").text = di_snippet 
                di_unrunnable = di_unrunnable + 1 
                print di_num_snippet - di_unrunnable
                continue
            
            
        di_unrunnabletree = di_ET2.ElementTree(di_unrunnableroot)
        di_unrunnabletree.write('C:\\StackOverflow\\Python\\unrunnablePython27\\post' + di_filename + '_unrunnable.xml');
     
# The main function#

'''
filelist = ['aa', 'ab', 'ac', 'ad', 'ae', 'af', 'ag', 'ah', 'ai', 'aj', 'ak', 'al', 'am', 'an', 'ao', 'ap', 'aq',
            'ar', 'as', 'at', 'au', 'av', 'aw', 'ax', 'ay', 'az',
            'ba', 'bb', 'bc', 'bd', 'be', 'bf', 'bg', 'bh', 'bi', 'bj', 'bk', 'bl', 'bm', 'bn', 'bo','bp', 'bq',
            'br', 'bs', 'bt', 'bu', 'bv', 'bw', 'bx', 'by', 'bz',
            'ca', 'cb', 'cc', 'cd', 'ce', 'cf', 'cg', 'ch', 'ci', 'cj', 'ck', 'cl', 'cm', 'cn', 'co','cp', 'cq',
            'cr', 'cs', 'ct', 'cu', 'cv', 'cw', 'cx', 'cy', 
            'da', 'db', 'dc', 'dd', 'de', 'df', 'dg', 'dh', 'di', 'dj','dk', 'dl', 'dm', 'dn', 'do','dp', 'dq',
            'dr', 'ds', 'dt', 'du', 'dv']
'''
di_filelist = ['dn']


for di_file in di_filelist:
    #print(file)
    di_num_snippet = 0
    di_unrunnable = 0;
    readXml(di_file) 
    print str(di_num_snippet)
    print str(di_num_snippet - di_unrunnable)

