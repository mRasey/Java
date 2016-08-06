# -*- coding:gb2312 -*-

import zipfile
from lxml import etree
import re
import sys
import shutil
import time
import os
import tempfile

Data_DirPath = sys.argv[1]#输入文件夹的路径,命令行的第二个参数

Docx_Filename = Data_DirPath + 'origin.docx'
Checkout_Filename = Data_DirPath + 'check_out1.txt'
ModifySpace_FileName = Data_DirPath + 'space.txt'

word_schema='{http://schemas.openxmlformats.org/wordprocessingml/2006/main}'
Unicode_bt = 'gb2312'#中文字符编码方式，我的机器上是gb2312，服务器上是utf-8，还有个别机器上是GBK

#以下4个函数是关于 解压缩word文档和读取xml内容并按节点标签对树形结构进行迭代
def get_word_xml(docx_filename):
    zipF = zipfile.ZipFile(docx_filename)
    xml_content = zipF.read('word/document.xml')
    style_content=zipF.read('word/styles.xml')
    return xml_content,style_content

def get_xml_tree(xml_string):
    return etree.fromstring(xml_string)

def _iter(my_tree,type_char):
    for node in my_tree.iter(tag=etree.Element):
        if _check_element_is(node,type_char):
            yield node

def _check_element_is(element,type_char):
    return element.tag == '%s%s' % (word_schema,type_char)
 
#获取节点包含的文本内容
def get_ptext(w_p):
    ptext = ''
    for node in w_p.iter(tag=etree.Element):
        if _check_element_is(node,'t'):
            ptext += node.text
    return ptext.encode(Unicode_bt,'ignore')#被检测的论文中可能出现奇怪的根本无法解码字符比如该同学从其他地方乱粘贴东西，因此加上个ignore参数忽略非法字符


def has_key(node,attribute):
    return '%s%s' %(word_schema,attribute) in node.keys()

def get_val(node,attribute):
    if has_key(node,attribute):
        return node.get('%s%s' %(word_schema,attribute))
    else:
        return '未获取属性值'
def read_error(filename):
    #因为文件里段落可能不是递增的，这里最好写一个排序。对以后改错有帮助
    rp = open(filename,'r')
    error_dct=[]
    for line in rp:
        group = line.split('_')
        group1 = {}
        group1['paraNum'] = group[0]
        group1['location'] = group[1]
        group1['kind'] = group[2]
        group1['type'] = group[3]
        group1['rightValue'] = group[4][:-1].decode('gbk')#去掉'\n'
        error_dct.append(group1)
    return error_dct
def modify(xml_tree,errorlist):
    paraNum = 0
    listCount = 0
    spacelistCount = 0
    for paragr in _iter(xml_tree,'p'):
        paraNum +=1
        if spacelistCount< len(spacelist) and paraNum == spacelist[spacelistCount]:
            spacelistCount = spacelistCount + 1
            pat = re.compile('[0-9]')
            for r in paragr.iter(tag = etree.Element):
                if _check_element_is(r,'r'):
                    for t in r.iter(tag=etree.Element):
                        if _check_element_is(t,'t'):
                            if pat.match(t.text):
                                pos = 0
                                for char in t.text:
                                    if char.isdigit() or char == '.':
                                        pos = pos+1
                                        continue
                                    else:
                                        break
                                txt=t.text
                                t.text=txt[:pos]+' '+txt[pos:]
                                t.set('%s%s' % ('{http://www.w3.org/XML/1998/namespace}','space'), 'preserve')
                                print t.text
        if paraNum != int(errorlist[listCount]['paraNum']):
            continue
        if errorlist[listCount]['type'] == 'paraAlign':
            for pPr in paragr:
                if _check_element_is(pPr,'pPr'):
                    found_jc = False
                    #print etree.tostring(pPr,pretty_print = True)
                    for jc in pPr:
                        if _check_element_is(jc,'jc'):
                            found_jc = True
                            jc.set('%s%s' %(word_schema,'val'),errorlist[listCount]['rightValue'])
                            break
                    if found_jc == False:
                        pPr.insert(0,etree.Element('%s%s' %(word_schema,'jc')))
                        jc = pPr[0]
                        jc.set('%s%s' %(word_schema,'val'),errorlist[listCount]['rightValue'])
                    #print etree.tostrin(pPr,pretty_print = True)
                    break
            listCount = listCount + 1
        elif errorlist[listCount]['type'] == 'paraIsIntent' or errorlist[listCount]['type'] == 'paraIsIntent1':
            for pPr in paragr:
                if _check_element_is(pPr,'pPr'):
                    found_ind = False
                    #print etree.tostring(pPr,pretty_print = True)
                    for ind in pPr:
                        if _check_element_is(ind,'ind'):
                            found_ind = True
                            if errorlist[listCount]['rightValue'] != '0':
                                ind.set('%s%s' %(word_schema,'firstLineChars'),'200')
                            else:
                                ind.set('%s%s' %(word_schema,'firstLineChars'),'0')
                                ind.set('%s%s' %(word_schema,'firstLine'),'0')
                            break
                    if found_ind == False:
                        pPr.insert(0,etree.Element('%s%s' %(word_schema,'ind')))
                        ind = pPr[0]
                        if errorlist[listCount]['rightValue'] != '0':
                            ind.set('%s%s' %(word_schema,'firstLineChars'),'200')
                        else:
                            ind.set('%s%s' %(word_schema,'firstLineChars'),'0')
                            ind.set('%s%s' %(word_schema,'firstLine'),'0')
                    #print etree.tostring(pPr,pretty_print = True)
                    break
            listCount = listCount + 1
        elif errorlist[listCount]['type'] == 'paraSpace':
            for pPr in paragr:
                if _check_element_is(pPr,'pPr'):
                    found_node = False
                    #print etree.tostring(pPr,pretty_print = True)
                    for node in pPr:
                        if _check_element_is(node,'spacing'):
                            found_node = True
                            node.set('%s%s' %(word_schema,'line'),errorlist[listCount]['rightValue'])
                            break
                    if found_node == False:
                        pPr.insert(0,etree.Element('%s%s' %(word_schema,'spacing')))
                        node = pPr[0]
                        node.set('%s%s' %(word_schema,'line'),errorlist[listCount]['rightValue'])
                    #print etree.tostring(pPr,pretty_print = True)
                    break
            listCount = listCount + 1
        elif errorlist[listCount]['type'] == 'paraFrontSpace':
            for pPr in paragr:
                if _check_element_is(pPr,'pPr'):
                    found_node = False
                    #print etree.tostring(pPr,pretty_print = True)
                    for node in pPr:
                        if _check_element_is(node,'spacing'):
                            found_node = True
                            node.set('%s%s' %(word_schema,'before'),errorlist[listCount]['rightValue'])
                            break
                    if found_node == False:
                        pPr.insert(0,etree.Element('%s%s' %(word_schema,'spacing')))
                        node = pPr[0]
                        node.set('%s%s' %(word_schema,'before'),errorlist[listCount]['rightValue'])
                    #print etree.tostring(pPr,pretty_print = True)
                    break
            listCount = listCount + 1
        elif errorlist[listCount]['type'] == 'paraAfterSpace':
            for pPr in paragr:
                if _check_element_is(pPr,'pPr'):
                    found_node = False
                    #print etree.tostring(pPr,pretty_print = True)
                    for node in pPr:
                        if _check_element_is(node,'spacing'):
                            found_node = True
                            node.set('%s%s' %(word_schema,'after'),errorlist[listCount]['rightValue'])
                            break
                    if found_node == False:
                        pPr.insert(0,etree.Element('%s%s' %(word_schema,'spacing')))
                        node = pPr[0]
                        node.set('%s%s' %(word_schema,'after'),errorlist[listCount]['rightValue'])
                    #print etree.tostring(pPr,pretty_print = True)
                    break
            listCount = listCount + 1
        elif errorlist[listCount]['type'] == 'fontCN':
            rPr_first = True
            for rPr in paragr.iter(tag=etree.Element):
                found_node = False
                if _check_element_is(rPr,'rPr'):
                    for node in rPr:
                        if _check_element_is(node,'rFonts'):
                            found_node = True
                            node.set('%s%s' %(word_schema,'eastAsia'),errorlist[listCount]['rightValue'])
                            break
                    if rPr_first == True and found_node == False:
                        rPr.insert(0,etree.Element('%s%s' %(word_schema,'rFonts')))
                        node = rPr[0]
                        node.set('%s%s' %(word_schema,'eastAsia'),errorlist[listCount]['rightValue'])
                    rPr_first = False  
            listCount = listCount + 1 
        elif errorlist[listCount]['type'] == 'fontSize':
            rPr_first = True
##            print 'a'
            for rPr in paragr.iter(tag=etree.Element):
                found_node = False
##                print rPr.tag
                if _check_element_is(rPr,'rPr'):
##                    print 'aa',rPr.tag
                    for node in rPr:
                        if _check_element_is(node,'sz'):
                            found_node = True
##                            print 'a'
                            node.set('%s%s' %(word_schema,'val'),errorlist[listCount]['rightValue'])
                            break
                    if rPr_first == True and found_node == False:
                        rPr.insert(0,etree.Element('%s%s' %(word_schema,'sz')))
                        node = rPr[0]
                        node.set('%s%s' %(word_schema,'val'),errorlist[listCount]['rightValue'])
                    rPr_first = False      
            listCount = listCount + 1                  
        elif errorlist[listCount]['type'] == 'fontShape':
            rPr_first = True
            for rPr in paragr.iter(tag=etree.Element):
                found_node = False
                if _check_element_is(rPr,'rPr'):
                    for node in rPr:
                        if _check_element_is(node,'b'):
                            found_node = True
                            node.set('%s%s' %(word_schema,'val'),errorlist[listCount]['rightValue'])
                            break
                    if rPr_first == True and found_node == False:
                        rPr.insert(0,etree.Element('%s%s' %(word_schema,'b')))
                        node = rPr[0]
                        node.set('%s%s' %(word_schema,'val'),errorlist[listCount]['rightValue'])
                    rPr_first = False
            listCount = listCount + 1
startTime=time.time()  
#主程序__main__入口差不多在这里了
xml_from_file,style_from_file = get_word_xml(Docx_Filename)
xml_tree = get_xml_tree(xml_from_file)
#sys.exit()
errorlist = read_error(Checkout_Filename)
spacelist=[]
spacefile = open(ModifySpace_FileName,'r')
for line in spacefile:
    spacelist.append(int(line))
i = 0
s = ''
modify(xml_tree,errorlist)
endTime=time.time()

#---zqd 新增 20160121-------------------------------------------------
zipF = zipfile.ZipFile(Docx_Filename)
#创建临时目录
tmp_dir = tempfile.mkdtemp()
#把原来的docx解压到临时目录
zipF.extractall(tmp_dir)
#把新的xml写到里面
with open(os.path.join(tmp_dir,'word/document.xml'),'w') as f:
    xmlstr = etree.tostring (xml_tree, pretty_print=False,encoding="UTF-8")#此处不识别'gb2312'
    f.write(xmlstr)

# Get a list of all the files in the original docx zipfile
filenames = zipF.namelist()
# Now, create the new zip file and add all the filex into the archive
zip_copy_filename = Data_DirPath + 'result.docx'
with zipfile.ZipFile(zip_copy_filename, "w") as docx:
    for filename in filenames:
        docx.write(os.path.join(tmp_dir,filename),  filename)

# Clean up the temp dir
shutil.rmtree(tmp_dir)
#-----------------------------------------------             
print endTime - startTime
