# -*- coding:gb2312 -*-

import zipfile
from lxml import etree
import re
import sys
import shutil
import time
import os
import tempfile

Rule_DirPath = sys.argv[1]#输入文件夹的路径,命令行的第二个参数
Data_DirPath = sys.argv[2]#输出文件夹的路径，命令行的第三个参数

Rule_Filename = Rule_DirPath + 'rules.txt'
Docx_Filename = Data_DirPath + 'origin.docx'

word_schema='{http://schemas.openxmlformats.org/wordprocessingml/2006/main}'
Unicode_bt ='gb2312'#中文字符编码方式，我的机器上是gb2312，服务器上是utf-8，还有个别机器上是GBK

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
#-modified by zqd 20160125-----------------------
    for node in w_p.iter(tag=etree.Element):
        if _check_element_is(node,'t'):
#-----------------------------------------------
            ptext += node.text
    return ptext.encode(Unicode_bt,'ignore')#被检测的论文中可能出现奇怪的根本无法解码字符比如该同学从其他地方乱粘贴东西，因此加上个ignore参数忽略非法字符
def get_level(w_p):
    for pPr in w_p:
        if _check_element_is(pPr,'pPr'):
            for pPr_node in pPr:
                if _check_element_is(pPr_node,'outlineLvl'):
                    return pPr_node.get('%s%s' %(word_schema,'val'))
                
                if _check_element_is(pPr_node,'pStyle'):
                    style_xml = etree.fromstring(zipfile.ZipFile(Docx_Filename).read('word/styles.xml'))
                    styleID = pPr_node.get('%s%s' %(word_schema,'val'))
                    flag = 1
                    while flag == 1 :
                        #print 'style',styleID
                        flag = 0
                        for style in _iter(style_xml,'style'):
                            if style.get('%s%s' %(word_schema,'styleId')) == styleID:
                                for style_node in style:
                                    if _check_element_is(style_node,'pPr'):
                                        for pPr_node in style_node:
                                            if _check_element_is(pPr_node,'outlineLvl'):
                                                return pPr_node.get('%s%s' %(word_schema,'val'))
                                    if _check_element_is(style_node,'basedOn'):
                                        styleID = style_node.get('%s%s' %(word_schema,'val'))
                                        flag = 1               

#-----------------------------    
#以下5个函数用来 获取文本对应的格式信息
#初始化一个格式字典，字段值的类型为方便处理均为str，值的定义和范围可以参考文档
def init_fd(d):
    d['fontCN']='宋体'
    d['fontEN']='Times New Roman'
    d['fontSize']='21'#因为word里默认是21
    d['paraAlign']='both'
    d['fontShape']='0'
    d['paraSpace']='240'
    d['paraIsIntent']='0'
    d['paraIsIntent1']='0'
    d['paraFrontSpace']='100'
    d['paraAfterSpace']='100'
    d['paraGrade']='0'
    return d

def has_key(node,attribute):
    return '%s%s' %(word_schema,attribute) in node.keys()

def get_val(node,attribute):
    if has_key(node,attribute):
        return node.get('%s%s' %(word_schema,attribute))
    else:
        return '未获取属性值'

#获取的格式信息赋给当前节点的格式字典
def assign_fd(node,d):
    for detail in node.iter(tag=etree.Element):
#------20160314 zqd----------------------------------
        if _check_element_is(detail,'rFonts'):
            if has_key(detail,'eastAsia'):#有此属性
                d['fontCN'] = get_val(detail,'eastAsia').encode(Unicode_bt)
            elif has_key(detail,'ascii'):
                #print get_val(detail,'hAnsi')
                d['fontEN'] = get_val(detail,'ascii').encode(Unicode_bt)
#--------------------------------------------
        
        elif _check_element_is(detail,'sz'):
            d['fontSize'] = get_val(detail,'val')

        elif _check_element_is(detail,'jc'):
            d['paraAlign'] = get_val(detail,'val')

        elif _check_element_is(detail,'b'):
            if has_key(detail,'val'):
                if get_val(detail, 'val') != '0' and get_val(detail, 'val') != 'false':
                    d['fontShape'] = '1'#表示bold
                else:
                    #print 'not blod'
                    d['fontShape'] = '0'
            else:
                d['fontShape'] = '1'#表示bold

        elif _check_element_is(detail,'spacing'):
            if has_key(detail,'line'):
                d['paraSpace'] = get_val(detail,'line')
            if has_key(detail,'before'):
                d['paraFrontSpace']= get_val(detail,'before')
            if has_key(detail,'after'):
                d['paraAfterSpace']= get_val(detail,'after')
#--------20160313 zqd----------------------------------------
        elif _check_element_is(detail,'ind'):
            if has_key(detail,'firstLine'):
                d['paraIsIntent']=get_val(detail,'firstLine')
            if has_key(detail,'firstLine'):
                d['paraIsIntent1']=get_val(detail,'firstLineChars')
                #print d['paraIsIntent']
                #在这里两种缩进是不同的，具体看xml文档，firstLineChars优先级高
#-------------------------------------------------
        elif _check_element_is(detail,'outlineLvl'):
            d['paraGrade'] = get_val(detail,'val')

    return d
#----20160314 zqd------------
def get_style_format(styleID,d):
    for style in _iter(style_tree,'style'):
        if get_val(style,'styleId') == styleID:#get_val是属性
            for detail in style.iter(tag=etree.Element):
                if _check_element_is(detail,'basedOn'):
                    styleID1 = get_val(detail,'val')
                    get_style_format(styleID1,d)
                if _check_element_is(detail,'pPr'):
                    assign_fd(detail,d)
                if _check_element_is(detail,'rPr'):
                    assign_fd(detail,d)
            
#获取格式
def get_format(node,d):
    init_fd(d)
    for pPr in _iter(node,'pPr'):
        for pstyle in _iter(pPr,'pStyle'):
            styleID = get_val(pstyle,'val')
            get_style_format(styleID,d)
        assign_fd(pPr,d)

    return d
#--------------------------------------------------
def first_locate():
    paraNum = 0
    part[1] = 'cover'
    reference = []
    current_part = ''
    for paragr in _iter(xml_tree,'p'):
        paraNum +=1
        text=get_ptext(paragr)
        if not text or text == '' or text == '':
            continue
        #print text
        for r in paragr.iter(tag=etree.Element):
            if _check_element_is(r, 'r'):
                for instr in r.iter(tag=etree.Element):
                    if _check_element_is(instr, 'instrText'):
                        refer = False
                        if "REF _Ref" in instr.text:
                            refer = True
                        if refer is True:
                            reference.append((instr.text[9:].split())[0])
        if '本人声明' in text:
            current_part = part[paraNum] = 'statement'
        elif '北京航空航天大学' in text  or '本科生毕业设计（论文）任务书' in text:
            current_part = part[paraNum] = 'taskbook'
        elif '论文封面书脊' in text:
            current_part = part[paraNum] = 'spine'
        elif re.compile(r'摘 *要').match(text):
            current_part = part[paraNum] = 'abstract'
        elif 'Abstract' in text or 'abstract' in text or 'ABSTRACT' in text:
            current_part = part[paraNum] = 'abstract_en'
        elif re.compile(r'目 *录').match(text) or re.compile(r'图目录').match(text) or re.compile(r'表目录').match(text) or re.compile(r'图表目录').match(text):
            current_part = part[paraNum] = 'menu'
        elif (current_part == 'menu' and not text[-1].isdigit()) or( re.compile(r'.*绪 *论').match(text) and not text[-1].isdigit()):
            current_part = part[paraNum] = 'body'
        elif text == '参考文献':
            current_part = part[paraNum] = 'refer'
        elif text.startswith('附') and text.endswith('录') :
            current_part = part[paraNum] = 'appendix'
    if not 'statement' in part.values():
        print 'warning statement doesnot exsit'
    if not 'spine' in part.values():
        print 'warning spine'
    if not 'abstract' in part.values():
        print 'warning abstract'
    if not 'body' in part.values():
        print 'warning body'
    if not 'menu' in part.values():
        print 'warning menu'
    return reference
def second_locate():
    paraNum = 0
    locate[1] = 'cover1'
    cur_part = ''
    cur_state = 'cover1'
    title = ''
    warnInfo=[]
    mentioned = []
    last_text = ''
    for paragr in _iter(xml_tree,'p'):
        paraNum +=1
        #------hsy add object detection July.13.2016--
        for node in paragr.iter(tag = etree.Element):
            if _check_element_is(node,'r'):
                for innode in node.iter(tag = etree.Element):
                    if _check_element_is(innode,'object'):
                        cur_state = locate[paraNum] = 'object'
            if _check_element_is(node,'bookmarkStart'):
                if node.values()[1][:4] == '_Ref':
                    if node.values()[1][4:] in reference:
                            mentioned.append(node.values()[1][4:])
        #------end
        text=get_ptext(paragr)
        if text == '' or text == '':
            continue
        if paraNum in part.keys():
            cur_part = part[paraNum]
        if cur_part == 'cover':
            if '毕业设计'in text:
                cur_state = locate[paraNum] = 'cover2'
            elif  cur_state == 'cover2':
                cur_state = locate[paraNum] = 'cover3'
                title = text
                #print title
            elif '院'in text and'系'in text or '名称'in text:
                cur_state = locate[paraNum] = 'cover4'
            elif '年'in text and '月'in text:
                cur_state = locate[paraNum] = 'cover5'
        elif cur_part == 'spine':
            if '论文封面书脊' in text:
                cur_state = locate[paraNum] = 'cover6'
            else:
                cur_state = locate[paraNum] = 'spine1'#不用处理了
        elif cur_part == 'statement':
            if text == '本人声明':
                cur_state = locate[paraNum] = 'statm1'
            elif text.startswith('我声明'):
                cur_state = locate[paraNum] = 'statm2'
            elif '作者'in text:
                cur_state = locate[paraNum] = 'statm3'
            elif '时间' in text and  '年'in text  and '月' in text:
                last_text = text
            elif '时间'in last_text and  '年' in last_text and '月' in last_text and title in text:
                last_text = ''
                cur_state = locate[paraNum] = 'abstr1'
                
            elif '学'and'生'in text:
                cur_state = locate[paraNum] = 'abstr2'
        elif cur_part == 'abstract':
            if re.match(r'摘 *要',text):
                cur_state = locate[paraNum] = 'abstr3'
                last_text = text
            elif re.match(r'摘 *要',last_text):
                last_text = ''
                cur_state = locate[paraNum] = 'abstr4'
            elif '关键词'in text or '关键字'in text:
                cur_state = locate[paraNum] = 'abstr5'
                last_text = text
            elif cur_state == 'abstr5':
                last_text = ''
                cur_state = locate[paraNum] = 'abstr1'
            elif 'Author' in text:
                cur_state = locate[paraNum] = 'abstr2'
        elif cur_part == 'abstract_en':
            if text == 'ABSTRACT':
                cur_state = locate[paraNum] = 'abstr3'
                last_text = text
            elif last_text == 'ABSTRACT' and 'Author'not in text and 'Tutor'not in text:
                cur_state = locate[paraNum] = 'abstr4'
                last_text = ''
            elif ('KEY'in text or 'key' in text and 'WORD'in text or'word' in text )\
                 or ('keyword'in text or 'Keyword'in text or'KEYWORD'in text): 
                cur_state = locate[paraNum] = 'abstr5'
        elif cur_part == 'menu':
            if re.match(r'目 *录',text)or re.compile(r'图目录').match(text) or re.compile(r'表目录').match(text) or re.compile(r'图表目录').match(text):
                cur_state = locate[paraNum] = 'menuTitle'
                cur_state = locate[paraNum] ='menuFirst'
            elif analyse(text) == 'secondLv':
                cur_state = locate[paraNum] = 'menuSecond'
            elif analyse(text) == 'thirdLv':
                cur_state = locate[paraNum] = 'menuThird'
            else :
                cur_state = locate[paraNum] ='menuFirst'#以汉字开头的标题都认为是一级标题
            if locate[paraNum] != 'menuTitle' and not text[-1].isdigit():
                cur_state = part[paraNum] = 'body'
                cur_part = 'body'
        elif cur_part == 'body':
            #得到级别，先按级别走，如果级别为普通，则按正则走。
            #print paraNum
            level = get_level(paragr)
            analyse_result = analyse(text)
            if analyse_result in['firstLv_e','secondLv_e','thirdLv_e']:
                warnInfo.append('    warning: 标题标号需要和标题之间用空格隔开')
                spaceNeeded.append(paraNum)
        #-------follow----hsy--modifies on July.13.2016
            if analyse_result is 'objectT':
                if cur_state != 'object':
                    #print 'warning',text
                    warnInfo.append('   warning: 图标题前没有直接对应的图')
            if cur_state is 'object':
                if analyse_result != 'objectT':
                    #print 'warning',text
                    warnInfo.append('   warning: 图后没有对应的图注。')
        #------end---------------------
            if level == '0':
                cur_state = locate[paraNum] = 'firstTitle'
                if analyse_result != 'firstLv' or analyse_result != 'firstLv_e':
                    #print 'warning',text
                    warnInfo.append('    warning: 标题级别和标题标号代表的级别不一致')
            elif level == '1':
                cur_state = locate[paraNum] = 'secondTitle'
                if analyse_result != 'secondLv' or analyse_result != 'secondLv_e':
                    #print 'warning',text
                    warnInfo.append('    warning: 标题级别和标题标号代表的级别不一致')
            elif level == '2':
                cur_state = locate[paraNum] = 'thirdTitle'
                if analyse_result != 'thirdLv' or analyse_result != 'thirdLv_e':
                    #print 'warning',text
                    warnInfo.append('    warning: 标题级别和标题标号代表的级别不一致')
            else:
                if analyse_result == 'firstLv':
                    cur_state = locate[paraNum] = 'firstTitle'
                elif analyse_result == 'secondLv' or analyse_result == 'secondLv_e':
                    cur_state = locate[paraNum] = 'secondTitle'
                elif analyse_result == 'thirdLv'or analyse_result == 'thirdLv_e':
                    cur_state = locate[paraNum] = 'thirdTitle'
                elif analyse_result == 'objectT':
                    cur_state = locate[paraNum] = 'objectTitle'
                elif analyse_result == 'tableT':
                    cur_state = locate[paraNum] = 'tableTitle'
                elif re.match(r'结 *论',text):
                    cur_state = locate[paraNum] = 'firstTitle'
                elif re.match(r'致 *谢',text):
                    cur_state = locate[paraNum] = 'firstTitle'
                elif paragr.getparent().tag != '%s%s'% (word_schema,'body'): #当paragr的父节点不是body时，该para的文本不属于正文（可能是表格、图形或文本框内的文字
                    cur_state = locate[paraNum] = 'tableText'
                else:
                    cur_state = locate[paraNum] = 'body'
        elif cur_part == 'refer':
            if text == '参考文献':
                cur_state = locate[paraNum] = 'firstTitle'
            else :
                cur_state = locate[paraNum] = 'reference'
                if not re.match('\\[[0-9]+\\]',text):
                    warnInfo.append('    warning: 参考文献必须以[num]标号开头！')
                
        elif cur_part == 'appendix':
            if text.startswith('附') and text.endswith('录'):
                cur_state = locate[paraNum] = 'firstTitle'
            else:
                cur_state = locate[paraNum] = 'body'
##        if paraNum in locate.keys():
##            print locate[paraNum],text[0:(100 if len(text) > 100 else len(text))]
##        else:
##            print '\t\t',text
    for val in mentioned:
        if val in reference:
            reference.remove(val)
    return warnInfo


#在判别文本以什么开头的方法上使用了正则表达式
def analyse(text):
    text=text.strip(' ')
    if text.isdigit():
        return 'body'
    pat1 = re.compile('[0-9]+')#以数字开头的正则表达式
    pat2 = re.compile('[0-9]+\\.[0-9]')#以X.X开头的正则表达式
    pat3 = re.compile('[0-9]+\\.[0-9]\\.[0-9]')#以X.X.X开头的正则表达式
    pat4 = re.compile('图(\s)*[0-9]+(\\.|-)[0-9]')#图标题的正则表达式
    pat5 = re.compile('表(\s)*[0-9]+(\\.|-)[0-9]')#表标题的正则表达式

#20160107 zqd -----------------------------------------------------------------------
    if pat1.match(text) and len(text)<70:
        if  pat1.sub('',text)[0] == ' ':
            sort = 'firstLv'
            #print 'the first LV length is',len(text)
        elif  pat1.sub('',text)[0] =='.':
            if pat2.match(text):
                if pat2.sub('',text)[0] == ' ':
                    sort = 'secondLv'
                elif pat2.sub('',text)[0]=='.':
                    if pat3.match(text):
                        if pat3.sub('',text)[0]==' ':
                            sort = 'thirdLv'
                        elif pat3.sub('',text)[0]=='.':
                            sort = 'overflow'
                            #print '    warning: 不允许出现四级标题！'
                        else:
                            sort ='thirdLv_e'
                    else:
                        sort='secondLv_e2'
                        #print '    warning: 二级标题正确的标号格式为X.X！'
                else:
                    sort = 'secondLv_e'
            else:
                sort = 'body'
        else:
            sort = 'firstLv_e'
    elif pat4.match(text) and len(text)<125:
        sort = 'objectT'
    elif pat5.match(text) and len(text)<125:
        sort = 'tableT'
    else :
        sort ='body'
#  zqd--------------------------------------------------------------------------------
    return sort

#读取规则接口
def read_rules(filename):
    f=open(filename,'r')
    #该字典主要是由于与前端接口定义不一致为了避免大片修改代码而定义的(已更新一致
    keyNameDc={'封面_单位代码':'cover1',
               '封面_毕业设计':'cover2',
               '封面_论文标题':'cover3',
               '封面_个人信息':'cover4',
               '封面_日期':'cover5',
               '封面_书脊':'cover6',
               '个人声明_标题':'statm1',
               '个人声明_正文':'statm2',
               '个人声明_签名':'statm3',
               '摘要_论文题目':'abstr1',
               '摘要_学生及教师姓名':'abstr2',
               '摘要_标题':'abstr3',
               '摘要_内容':'abstr4',
               '摘要_关键词':'abstr5',
               '摘要_关键词内容':'abstr6',
               '目录_标题':'menuTitle',
               '目录_一级标题':'menuFirst',
               '目录_二级标题':'menuSecond',
               '目录_三级标题':'menuThird',
               '正文_一级标题':'firstTitle',
               '正文_二级标题':'secondTitle',
               '正文_三级标题':'thirdTitle',
               '正文_段落':'body',
               '致谢_标题':'unknown',#
               '致谢_内容':'unknown',#
               '附录_标题':'extentTitle',
               '附录_内容':'extentContent',
               '图标题':'objectTitle',
               '表标题':'tableTitle',
               '参考文献_条目':'reference'
               }
    
    rules_dct={}        
    for line in f:
        if line.startswith('{'):
            group=line[1:-3].split(',')
            for factor in group:
                _key = factor[:factor.index(':')]
                _val = factor[factor.index(':')+1:]
                if _key == 'key':
                    rule_dkey = _val
                    rules_dct.setdefault(_val,{})
                if _key!= 'key':
                    rules_dct[rule_dkey].setdefault(_key,_val)
    f.close()
    return rules_dct

#检查格式并输出结果
def check_out(rule,to_check,locate,paraNum):
    errorInfo=[]
    #这个字典的定义主要是由于前台那个同学规则字段和错误类型字段的名称不一致，神烦
    errorTypeName={'fontCN':'font',
                   'fontEN':'font',
                   'fontSize':'fontsize',
                   'fontShape':'fontshape',
                   'paraAlign':'gradeAlign',
                   
                   'paraSpace':'gradeSpace',
                   'paraFrontSpace':'gradeFrontSpace',
                   'paraAfterSpace':'gradeAfterSpace',
                   'paraIsIntent':'FLind'
                   }
    errorTypeDescrip={'fontCN':'中文字体',
                   'fontEN':'英文字体',
                   'fontSize':'字号',
                   'fontShape':'字形',
                   'paraAlign':'对齐方式',
                   
                   'paraSpace':'行间距',
                   'paraFrontSpace':'段前间距',
                   'paraAfterSpace':'段后间距',
                   'paraIsIntent':'首行缩进'
                      }
    
    #这个字典的定义是为了避免对每个para都把规则字典里十个字段检查一遍，根据para的位置有选择有针对性的检查
    checkItemDct={'cover1':['fontCN','fontSize'],
                  'cover2':['fontCN','fontSize','paraAlign'],
                  'cover3':['fontCN','fontSize','paraAlign'],
                  'cover4':['fontCN','fontSize','paraAlign'],
                  'cover5':['fontCN','fontSize','paraAlign'],
                  'cover6':[],
                  'statm1':['fontCN','fontSize','paraAlign','fontShape'],
                  'statm2':['fontCN','fontSize','paraAlign','paraSpace','paraIsIntent'],
                  'statm3':['fontCN','fontSize'],
                  'abstr1':['fontCN','fontSize','paraAlign','fontEN','paraIsIntent'],
                  'abstr2':['fontCN','fontSize','fontEN'],
                  'abstr3':['fontCN','fontSize','paraAlign','fontEN','paraIsIntent'],
                  'abstr4':['fontCN','fontSize','paraAlign','paraSpace','paraIsIntent','fontEN'],
                  'abstr5':['fontCN','fontSize','paraAlign','fontShape','fontEN'],
                  'abstr6':['fontCN','fontSize','fontEN'],
                  'menuTitle':['fontCN','fontSize','paraAlign','paraSpace','fontShape','paraFrontSpace','paraAfterSpace','paraIsIntent'],
                  'menuFirst':['fontCN','fontSize'],
                  'menuSecond':['fontCN','fontSize'],
                  'menuThird':['fontCN','fontSize'],
                  'firstTitle':['fontCN','fontSize','paraAlign','paraSpace','fontShape','paraFrontSpace','paraAfterSpace','paraIsIntent'],
                  'secondTitle':['fontCN','fontSize','paraAlign','paraSpace','fontShape','paraFrontSpace','paraAfterSpace','paraIsIntent'],
                  'thirdTitle':['fontCN','fontSize','paraAlign','paraSpace','fontShape','paraFrontSpace','paraAfterSpace','paraIsIntent'],
                  'body':['fontCN','fontEN','fontSize','fontShape','paraIsIntent'],
                  'tableText':['fontCN','fontEN','fontSize','fontShape'],
                  'thankTitle':['fontCN','fontSize'],
                  'thankContent':['fontCN','fontEN','fontSize'],
                  'extentTitle':['fontCN','fontSize'],
                  'extentContent':['fontCN','fontEN','fontSize'],
                  'objectTitle':['fontCN','fontSize'],
                  'tableTitle':['fontCN','fontSize'],
                  'reference':['fontCN','fontEN','fontSize','paraAlign','fontShape','paraIsIntent']
                  }
    if locate in checkItemDct.keys():
        for key in checkItemDct[locate]:
            if key == 'paraIsIntent':#对于缩进，特别处理
                #print '00000000000000000',to_check['paraIsIntent1'],to_check['paraIsIntent']
                if to_check['paraIsIntent1'] != '未获取属性值' and to_check['paraIsIntent1'] != '0':
                    if to_check['paraIsIntent1'] != '200' and rule['paraIsIntent'] == '1':
                        rp1.write(str(paraNum)+'_'+locate+'_'+'error_paraIsIntent1_200\n')
                    elif rule['paraIsIntent'] == '0':
                        rp1.write(str(paraNum)+'_'+locate+'_'+'error_paraIsIntent1_0\n')
                else:
                    #if to_check['paraIsIntent'] != str(int(rule['paraIsIntent'])*int(rule[key])*20):
                    if int(to_check['paraIsIntent']) < 100:#这里做一个粗略的设定，因为要是按照上面注释的一行来执行，错误率太高了
                        rp1.write(str(paraNum)+'_'+locate+'_'+'error_paraIsIntent_'+str(20*int(to_check['fontSize'])*int(rule[key]))+'\n')
                continue
            else:
                if to_check[key] != rule[key]:
                    #print '    格式：',key,to_check[key]
                    rp.write('    '+errorTypeDescrip[key]+'是'+to_check[key]+'  正确应为：'+rule[key]+'-------->'+ptext+'\n')
                    errorInfo.append('\'type\':\''+errorTypeName[key]+'\',\'correct\':\''+rule[key]+'\'')
                    rp1.write(str(paraNum)+'_'+locate+'_error_'+key+'_'+rule[key]+'\n')
                    #if key == 'fontSize':
                        #print '---------',to_check[key],rule[key]
##    else:
##        for key in errorTypeName:
##            if to_check[key] != rule[key]:
##                #print '    格式：',key,to_check[key]
##                #为满足前台那个同学读JS格式的要求，每个键和值都加上了单引号
##                errorInfo.append('\'type\':\''+errorTypeName[key]+'\',\'correct\':\''+rule[key]+'\'')

    return errorInfo

def grade2num():
    # 20160121 zqd
    # modified: xml_tree
    numPrIlvl = [0,0,0,0]
    for paragr in _iter(xml_tree,'p'):
        for pPr in paragr.iter(tag=etree.Element):
            if _check_element_is(pPr,'pPr'):
                for numPr in pPr.iter(tag=etree.Element):
                    if _check_element_is(numPr,'numPr'):
                        for ilvl in numPr.iter(tag=etree.Element):
                            if _check_element_is(ilvl,'ilvl'):
                                if has_key(ilvl,'val'):#有此属性
                                    
                                    result = get_val(ilvl,'val')
                                    if result == "0":
                                        numPrIlvl[0] += 1
                                        numPrIlvl[1] = numPrIlvl[2] = numPrIlvl[3] = 0
                                    elif result == "1":
                                        numPrIlvl[1] += 1
                                        numPrIlvl[2] = numPrIlvl[3] = 0
                                    elif result == "2":
                                        numPrIlvl[2] += 1
                                        numPrIlvl[3] = 0
                                    elif result == "3":
                                        numPrIlvl[3] += 1
                                    strNumPr = ""
                                    i = 0
                                    while numPrIlvl[i] != 0 and i < 4:
                                        strNumPr += str(numPrIlvl[i])+"."
                                        i+=1
                                    pPr.remove(numPr)
                                    for node in paragr.iter(tag=etree.Element):
                                        if _check_element_is(node,'t'):
                                            node.text = strNumPr+" "+node.text
                                            break
                                    #print etree.tostring(paragr,encoding="UTF-8",pretty_print=True)


        
startTime=time.time()  
#主程序__main__入口差不多在这里了
xml_from_file,style_from_file = get_word_xml(Docx_Filename)
xml_tree   = get_xml_tree(xml_from_file)
style_tree = get_xml_tree(style_from_file)

rules_dct=read_rules(Rule_Filename)#想想这个读取规则只调用了一次，写成函数也是醉了


Part='start'
previousL='unknown'
part = {}
locate = {}
paraNum=0
reference = []
spaceNeeded = []
empty_para=0
#sys.exit()
reference = first_locate()
warninglist = second_locate()
#print locate
#sys.exit()
Report='['
Report1 = '['
rp = open(Data_DirPath + 'check_out.txt','w')
rp1 = open(Data_DirPath + 'check_out1.txt','w')
rp2 = open(Data_DirPath + 'space.txt','w')
eInfo = ''
section_seq = 0
rp.write('''论文格式检查报告文档使用说明：
*****此版本为初次上线测试版，难免存在误判等许多问题，遇到误判时请谅解，并可以将问题反馈给我们完善程序∧_∧*****
各字段值说明：
位置  为程序判断该段落文本在你论文中可能的位置，如果发现与你的论文中位置不符，请忽略其后的格式检查信息
字形  0表示未加粗，1表示加粗
行间距 N=数值/240，即为N倍行距
段前段后的数值单位均为磅
首行缩进0表示首行未缩进，1表示首行缩进2字符
warning信息表示可能存在的问题，不一定准确
**********并不华丽的分割线（然并卵）**********


''')
graphTitlePattern = re.compile('图(\s)*[0-9]+(\\.|-)[0-9]')#图标题的正则表达式
wrongGraphTitlePattern = re.compile('图(\s)*[0-9]')#错误图标题的正则表达式
excelTitlePattern = re.compile('表(\s)*[0-9]+(\\.|-)[0-9]')#图标题的正则表达式
wrongExcelTitlePattern = re.compile('表(\s)*[0-9]')#错误图标题的正则表达式
ObjectFlag=0
p_format={}.fromkeys(['fontCN','fontEN','fontSize','paraAlign','fontShape','paraSpace',
                         'paraIsIntent','paraFrontSpace','paraAfterSpace','paraGrade'])
for paragr in _iter(xml_tree,'p'):
#以<w:p>为最小单位迭代

    paraNum +=1
    ptext=get_ptext(paragr)
    if ObjectFlag == 1:
        if not graphTitlePattern.match(ptext):
            rp.write('     warning: 找不到对应图注 ----->'+ptext+'\n')
        ObjectFlag = 0
    if graphTitlePattern.match(ptext):
        if paraNum - 1 in locate.keys():
            if locate[paraNum - 1] != 'object':
                rp.write('    warning: 没有对应的图。--->' + ptext + '\n')
        else:
            rp.write('    warning: 没有对应的图。--->' + ptext + '\n')
        found = False
        for node in paragr.iter(tag=etree.Element):
            if _check_element_is(node, 'r'):
                for bookmarks in node.iter(tag=etree.Element):
                    if _check_element_is(bookmarks, 'bookmarkStart'):
                        if bookmarks.values()[1][:4] == '_Ref':
                            found = True
        if not found:
            rp.write('    此图注没被引用过' + ptext + '\n')
    if wrongGraphTitlePattern.match(ptext) and not graphTitlePattern.match(ptext):
        rp.write('    warning: 请改为符合规则的图注 ------>' + ptext + '\n')
        found = False
        for node in paragr.iter(tag=etree.Element):
            if _check_element_is(node, 'r'):
                for bookmarks in node.iter(tag=etree.Element):
                    if _check_element_is(bookmarks, 'bookmarkStart'):
                        if bookmarks.values()[1][:4] == '_Ref':
                            found = True
        if not found:
            rp.write('    此图注没被引用过' + ptext + '\n')
    if excelTitlePattern.match(ptext):
        found = False
        for node in paragr.iter(tag = etree.Element):
            if _check_element_is(node, 'r'):
                for bookmarks in node.iter(tag = etree.Element):
                    if _check_element_is(bookmarks, 'bookmarkStart'):
                        if bookmarks.values()[1][:4] == '_Ref':
                            found = True
        if not found:
            rp.write('    此图注没被引用过' + ptext + '\n')
    if wrongExcelTitlePattern.match(ptext) and not excelTitlePattern.match(ptext):
        rp.write('    warning: 请改为符合规则的图注------->'+ptext+'\n')
        found = False
        for node in paragr.iter(tag=etree.Element):
            if _check_element_is(node, 'r'):
                for bookmarks in node.iter(tag=etree.Element):
                    if _check_element_is(bookmarks, 'bookmarkStart'):
                        if bookmarks.values()[1][:4] == '_Ref':
                            found = True
        if not found:
            rp.write('    warning: 此图注没被引用过' + ptext + '\n' )
    if paraNum in locate.keys():
        location = locate[paraNum]
        if location == 'object':
            ObjectFlag = 1
    if  ptext == ' ' or ptext == '':
        empty_para += 1
        warnInfo=[]
        if empty_para==2:
            #print paraNum,' '
            record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'warning\',\'type\':\'warn\',\'correct\':\'    warning:不允许出现连续空行！\'},'
            Report += record
            rp.write('     warning:不允许出现连续空行 \n')
            #print '    warning:不允许出现连续空行！'
        continue
    empty_para =0
    get_format(paragr,p_format)
    #print paraNum,ptext
    #rp.write(str(paraNum)+' '+ptext+'\n')


    first_text = 0
    for run in _iter(paragr,'r'):
        flag = 0
        for w_t in _iter(run,'t'):
            if w_t.text != None and w_t.text != ' ':
                flag = 1

        if flag == 0:
                 continue
        for rPr in _iter(run,'rPr'):
            if first_text == 0:
                assign_fd(rPr,p_format)
                first_text = 1
            else:
                for detail in rPr.iter(tag=etree.Element):
                    if _check_element_is(detail,'rFonts'):
                        if has_key(detail,'eastAsia'):#有此属性
                            if p_format['fontCN'] != get_val(detail,'eastAsia').encode(Unicode_bt):
                                rp1.write(str(paraNum)+'_'+location+'_error_fontCN_'+rules_dct[location]['fontCN']+'\n')
                                break
                    elif _check_element_is(detail,'sz'):
                        if p_format['fontSize'] != get_val(detail,'val'):
                            rp1.write(str(paraNum)+'_'+location+'_error_fontSize_'+rules_dct[location]['fontSize']+'\n')
                            break
            
    


##        if warnInfo:
##            for each in warnInfo:
##                rp.write(each+'\n')
##                record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'warning\',\'type\':\'warn\',\'correct\':\''+each+'\'},'
##                

    #关键词这里比较特殊，要深入para内部分析run的rpr来看关键词内容的格式
    if location=='abstr5':
        if ':' not in ptext and '：' not in ptext :
            record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'warning\',\'type\':\'warn\',\'correct\':\'    warning: 关键词后面没有冒号！\'},'
            rp.write('    warning: 关键词后面没有冒号！------>'+ptext+'\n')
            Report += record
            
            
        for rpr_keyword in _iter(paragr,'rPr'):
            found=0
            for bold_sign in _iter(rpr_keyword,'b'):
                found=1
        if not found:
            record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'warning\',\'type\':\'warn\',\'correct\':\'    warning: 关键词内容没有加粗！\'},'
            Report += record
            rp.write('    warning: 关键词内容没有加粗！'+ptext+'\n')

    #图/表 标题需要检查是否使用了引用，以及图/表 标号是否和章节标号一致, 这部分没有把错误信息输入到checkout1.txt，modify.py也还没有写修改章号的方法
    if location in[ 'firstTitle','secondTitle','thirdTitle']:
        ptext.strip(' ')#删除开头的空格字符
        if ptext[0].isdigit():
            section_seq = int(ptext[0])
        else:
            section_seq=0
    elif location in ['objectTitle','tableTitle']:
        pat7 = re.compile('(图|表)(\s)*')
        if pat7.sub('',ptext)[0].isdigit():
            testtext = pat7.sub('',ptext)[0]
            if section_seq != int(pat7.sub('',ptext)[0]):
                record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'warning\',\'type\':\'warn\',\'correct\':\'    warning: 图/表 标号与章节标号不一致 或章节一级标题格式不正确导致程序未识别出来！\'},'
                Report += record
                rp.write('    waring:图表 标号和章节不一致------->'+ptext+'\n')
        found=0
        for refer in _iter(paragr,'fldChar'):
            found=1
        if not found:
            record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'warning\',\'type\':\'warn\',\'correct\':\'    warning: 图/表 标题未使用引用！\'},'
            Report += record
            #print '    warning: 图/表 标题未使用引用！'
            rp.write('    warning: 图/表 标题 未使用跟与图关联！-------->'+ptext+'\n')

    #在正文中引用了参考文献的时候，标号要使用上标格式。存在问题：可以会把一个数组的索引误判成 参考文献的标号，这部分没有把错误信息输入到checkout1.txt，modify.py也还没有写修改章号的方法
    if location =='reference':
        pat6 = re.compile('\\[[0-9]+\\]')#参考文献标号的正则表达式
        if pat6.search(ptext):
            used_superscript=0
            for run in _iter(paragr,'r'):
                rtext=get_ptext(run)
                if pat6.match(rtext):
                    for superscript in _iter(run,'vertAlign'):
                        if has_key(superscript,'val'):
                            used_superscript=1
            if not used_superscript:
                record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'warning\',\'type\':\'warn\',\'correct\':\'    warning: 正文中参考文献的引用未使用上标！\'},'
                Report += record
                rp.write('    warning: 正文中参考文献的引用未使用上标！------>'+ ptext+'\n')
                            
    
    if location in rules_dct.keys():
        #rp.write('    位置：'+rules_dct[location]['name']+'\n')
        #print '    位置：',rules_dct[location]['name']
        errorInfo=check_out(rules_dct[location],p_format,location,paraNum)
    else:
        #print '定位失败：规则接口中未定义该段落类别'
        #print location
        errorInfo=''
    if errorInfo :
        #print '    检查： False'
        for each in errorInfo:
            #print '    应为：',each
            record='{\'paraNum\':\''+str(paraNum)+'\',\'Level\':\'error\','+each+'},'
            Report += record
    else:
        pass
        #rp.write('    检查： 格式正确\n')
        #print '    检查： True'
for num in spaceNeeded:
    rp2.write('%d' %num)
    rp2.write('\n')
        

Report += ']'
#print Report



endTime=time.time()
print '   用时： %.2f ms' % (100*(endTime-startTime))


hyperlinks = []
bookmarks = []
#检查目录是否自动更新
for node in _iter(xml_tree, 'hyperlink'):
    temp=''
    for hl in _iter(node,'t'):
        temp += hl.text
    hyperlinks.append(node.values()[0])
    #print True,temp
for node in _iter(xml_tree, 'bookmarkStart'):
    bookmarks.append(node.values()[1])

catalog_ud= True
for i in hyperlinks:  
    if i not in bookmarks:
        catalog_ud =False
if catalog_ud:
    pass
    #print True,'目录正常更新'
else:
    pass
    #print False,'目录未更新'

#rp.write('\n\n\n论文格式检查完毕！\n')
#rp.write(Report)
rp.close()
rp1.close()
rp2.close()
###---zqd 新增 20160121-------------------------------------------------
##zipF = zipfile.ZipFile(Docx_Filename)
###创建临时目录
##tmp_dir = tempfile.mkdtemp()
###把原来的docx解压到临时目录
##zipF.extractall(tmp_dir)
###把新的xml写到里面
##with open(os.path.join(tmp_dir,'word/document.xml'),'w') as f:
##    xmlstr = etree.tostring (xml_tree, pretty_print=True,encoding="UTF-8")#此处不识别'gb2312'
##    f.write(xmlstr)
##
### Get a list of all the files in the original docx zipfile
##filenames = zipF.namelist()
### Now, create the new zip file and add all the filex into the archive
##zip_copy_filename = 'result.docx'
##with zipfile.ZipFile(zip_copy_filename, "w") as docx:
##    for filename in filenames:
##        docx.write(os.path.join(tmp_dir,filename),  filename)
##
### Clean up the temp dir
##shutil.rmtree(tmp_dir)
###-----------------------------------------------             
    
