package com.web.test.application.test;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.web.test.application.model.RuleSingleton;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordTableTest {
    public static void main(String[] args) {
        try {
            HashSet set =new HashSet();
            ArrayList arrayList = new ArrayList();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
