package com.web.test.application.service;

import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.RuleSingleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 规则操作service
 */
@Slf4j
@Service
public class RulesService {
    @Autowired
    BaseMapper baseMapper;

    /**
     * 存储定时扫表获取的规则
     */
    static ConcurrentMap<String, RuleSingleton> RULES_MAP = new ConcurrentHashMap<>();

    public List<String> queryFullNameList() {
        Set<String> hashSet = RULES_MAP.keySet();
        List<String> res = new ArrayList<>();
        // res.addAll(hashSet);
        RULES_MAP.forEach((a,b)->{
            res.add(b.getFullName().replaceAll(" ",""));
        });
        return res;
    }

    public Set<String> queryCNNameSet() {
        Set<String> hashSet = new HashSet<>();
        RULES_MAP.forEach((s, r) -> {
            hashSet.add(r.getCnName().replaceAll(" ",""));
        });
        return hashSet;
    }


    public Set<String> queryFullCodesSet() {
        Set<String> hashSet = new HashSet<>();
        RULES_MAP.forEach((s, r) -> {
            hashSet.add(r.getFullCode().replaceAll(" ",""));
        });
        return hashSet;
    }



    public static ConcurrentMap getRulesMap() {
        return RULES_MAP;
    }

    public void insertRules(){

        int[] array = {1, 2, 3, 11, 20, 34, 68, 79, 100, 107, 142, 147,
                170, 181, 236, 312, 345, 361, 383, 440, 475, 476, 477, 478, 479, 481,
                482, 483, 484, 485, 508, 509, 533, 538, 647, 650, 651, 652, 653, 654, 656,
                657, 658, 659, 664, 668, 674, 677, 680, 685, 688, 689, 693, 697, 708, 711,
                712, 713, 716, 717, 718, 719};
        ArrayList num = new ArrayList();
        ArrayList num1 = new ArrayList();
        int j = 0;

        for (int i : array) {
            num.add(i);
        }
        System.out.println(num.size());

        try {
            //  URL url = new URL("");
            File tempFile = new File("D:\\水利水电勘测设计现行技术标准名录（第十版+修订）.pdf");
            final PDDocument documentToBeParsed = PDDocument.load(tempFile);
            final PDFTextStripper stripper = new PDFTextStripper();
            final String pdfText = stripper.getText(documentToBeParsed);
            System.out.println("Parsed text size is " + pdfText.length() + " characters:");
            //System.out.println(pdfText);
            /*for(String str : pdfText.split("\n")){
                System.out.println(str);
            }*/
            List<String> strs = Arrays.asList(pdfText.split("\n"));
            for (int i = 1; i < strs.size(); i++) {

                List<String> stubStrs = Arrays.asList(strs.get(i).split(" "));

                if (stubStrs.size() < 6 && stubStrs.size() > 4) {
                    //System.out.println(stubStrs.get(0));

                    if (num.contains(Integer.valueOf(stubStrs.get(0)))) {
                        continue;
                    }
                    j++;
                    System.out.println(j);
                    RuleSingleton ruleSingleton1 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"》（"+stubStrs.get(1).replaceAll(" ","")+"）");


                    RuleSingleton ruleSingleton2 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"（"+stubStrs.get(1).replaceAll(" ","")+"）》");
                    System.out.println(ruleSingleton1.toString());
                    System.out.println(ruleSingleton2.toString());
                    baseMapper.add(ruleSingleton1);
                    baseMapper.add(ruleSingleton2);

                    // System.out.println(stubStrs.get(0) + " || " + stubStrs.get(1) + "  ||  " + stubStrs.get(2) + "  size :" + stubStrs.size());


                } else if (stubStrs.size() > 5) {
                    //System.out.println(stubStrs.get(0));

                    if (num.contains(Integer.valueOf(stubStrs.get(0)))) {
                        continue;
                    }
                    j++;
                    System.out.println(j);
                    RuleSingleton ruleSingleton1 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"》（"+stubStrs.get(1).replaceAll(" ","")+"）");


                    RuleSingleton ruleSingleton2 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"（"+stubStrs.get(1).replaceAll(" ","")+"）》");
                    System.out.println(ruleSingleton1.toString());
                    System.out.println(ruleSingleton2.toString());
                    /*System.out.println(stubStrs.get(0) + " || " + stubStrs.get(1) + "  ||  " + stubStrs.get(2)
                            + " || " + stubStrs.get(3) + "  size :" + stubStrs.size());*/
                    baseMapper.add(ruleSingleton1);
                    baseMapper.add(ruleSingleton2);

                } else {
                    // System.out.println(" 不包含 ：" + stubStrs.get(0));
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void showFullCode(){
        for(String str : queryFullCodesSet()){
            log.error(str);
        }

        for(String str : queryFullNameList()){
            log.error(str);
        }

        for(String str : queryCNNameSet()){
            log.error(str);
        }
    }

    public void insertRulesV2(){
        try {
        HashSet set =new HashSet();
        ArrayList<RuleSingleton> arrayList = new ArrayList();
        boolean f1 = true;
        XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument
                .openPackage("D:\\科研院检测专业规范名称.docx"));
        XWPFDocument doc = docx.getDocument();
        List<XWPFTable> charts = doc.getTables();
        for (XWPFTable xwpfTable : charts) {
            List<XWPFTableRow> xwpfTableRowList = xwpfTable.getRows();

            for (XWPFTableRow xwpfTableRow : xwpfTableRowList) {
                List<XWPFTableCell> xwpfTableCells = xwpfTableRow.getTableCells();
                if(f1) {
                    f1 = false;
                    continue;
                }
                String str1 = "";
                for (XWPFTableCell xwpfTableCell : xwpfTableCells) {
                    List<XWPFParagraph> cellParagraphs = xwpfTableCell.getParagraphs();

                    for (XWPFParagraph xp : cellParagraphs) {
                        int i = 1;
                        XWPFRun newRun = xp.createRun();
                        // int i = 1;
                        String text = xp.getText().replaceAll(" ", "");
                        //text = replaceSpecialSymbol(text, textSymbol, "。");
                        List<String> textList = Arrays.asList(text.split("。"));


                        for (String str : textList) {
                            //System.out.println(str);
                            str1 = str1 +";" +str ;
                        }

                    }
                }
                //System.out.println(str1);
                String [] strArray = str1.split(";");
                //System.out.println(strArray.length);
                String fullName1 = "";

                //RuleSingleton ruleSingleton = new RuleSingleton(strArray[2],strArray[3],fullName1);


                RuleSingleton ruleSingleton1 = new RuleSingleton(strArray[2].replaceAll(" ",""),
                        strArray[3].replaceAll(" ",""),
                        "《"+strArray[2].replaceAll(" ","")+"》（"+strArray[3].replaceAll(" ","")+"）");


                RuleSingleton ruleSingleton2 = new RuleSingleton(strArray[2].replaceAll(" ",""),
                        strArray[3].replaceAll(" ",""),
                        "《"+strArray[2].replaceAll(" ","")+"（"+strArray[3].replaceAll(" ","")+"）》");
                //System.out.println(ruleSingleton1.toString());
                //System.out.println(ruleSingleton2.toString());
                if(set.add(ruleSingleton1.getFullCode())){
                    //System.out.println(ruleSingleton1.toString());
                    arrayList.add(ruleSingleton1);
                    arrayList.add(ruleSingleton2);
                }else{
                    System.out.println(ruleSingleton1);
                }
            }

        }

        System.out.println(arrayList.size());

        for(RuleSingleton ruleSingleton : arrayList){
            if(!queryFullNameList().contains(ruleSingleton.getFullName())){
                baseMapper.add(ruleSingleton);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
