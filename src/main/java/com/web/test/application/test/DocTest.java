package com.web.test.application.test;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DocTest {

    /**
     * 测试对word文档的处理
     * @param args
     */
    public static void main(String[] args) {
        try {

            // 2007及以后的版本
            XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument
                    .openPackage("C:\\Users\\dell\\IdeaProjects\\doc-test\\doc-test\\src\\main\\java\\test\\test2.docx"));
            String str = docx.getText();
            XWPFDocument doc = docx.getDocument();
            List<XWPFParagraph> paragraph = doc.getParagraphs();// doc中段落
            System.out.println(paragraph.size());
            doc.getCharts();
            for (XWPFParagraph xp : paragraph) {
                // System.out.println(xp.getText());
                List<String> strs = PatternUtil.getPatternStringList(xp.getText());
                if (strs.size() > 0) {
                    // xp.setBorderBottom(Borders.WAVELINE);
                    XWPFRun run = xp.createRun();
                    // xwpfRun.setUnderline(UnderlinePatterns.WAVY_HEAVY);
                    // xwpfRun.setUnderlineColor("FF0000");
                    run.setBold(true); //加粗
                    run.setColor("FF0000");
                    CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();

                } else {
                    continue;
                }
                for (String str1 : strs) {
                    System.out.println(str1);
                }
                // xp.getCTP().addNewR().addNewCommentReference().setId(1);
            }


            /*
            word文档中表格中文本的处理逻辑(tables)
            问题：
            1.表格中引用规范的形式与正文中不同，需要使用不同的规则
            */
            List<XWPFTable> charts = doc.getTables();
            System.out.println("size of tables is " + charts.size());
            for (XWPFTable xwpfTable : charts) {
                List<XWPFTableRow> xwpfTableRowList = xwpfTable.getRows();
                for (XWPFTableRow xwpfTableRow : xwpfTableRowList) {
                    List<XWPFTableCell> xwpfTableCells = xwpfTableRow.getTableCells();
                    for (XWPFTableCell xwpfTableCell : xwpfTableCells) {
                        System.out.println(xwpfTableCell.getText());
                        //System.out.println(PatternUtil.getPatternStringList(xwpfTableCell.getText()));
                    }
                }
            }
            File file = new File("C:\\Users\\dell\\IdeaProjects\\doc-test\\doc-test\\src\\main\\java\\test\\" +
                    System.currentTimeMillis() + ".docx");
            FileOutputStream stream = new FileOutputStream(file);
            doc.write(stream);
            stream.close();
            System.out.println("job is done !!!");

           /* XWPFComment[] xwpfComment = doc.getComments();
            for (XWPFComment x : xwpfComment) {
                System.out.println(x.getId());
                System.out.println(x.getDate());
                System.out.println(x.getText());
            }*/


            //System.out.println("start");
            //System.out.println(str);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
