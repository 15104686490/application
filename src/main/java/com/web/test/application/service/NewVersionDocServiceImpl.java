package com.web.test.application.service;

import com.web.test.application.test.ConfigTest;
import com.web.test.application.test.PatternUtil;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持07后的版本 word
 */
@Service
public class NewVersionDocServiceImpl implements DocService {
    @Override
    public List<String> getText(String path) {
        List<String> result = new ArrayList();
        String regx = ConfigTest.getConfig("regx");
        try {
            /*XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument
                    .openPackage("C:\\Users\\dell\\IdeaProjects\\doc-test\\doc-test\\src\\main\\java\\test\\test2.docx"));
           */
            XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument
                    .openPackage(path));
            XWPFDocument doc = docx.getDocument();
            List<XWPFParagraph> paragraph = doc.getParagraphs();// doc中段落
            System.out.println(paragraph.size());
            doc.getCharts();
            for (XWPFParagraph xp : paragraph) {
                List<String> strs = PatternUtil.getPatternStringList(xp.getText(), regx);
                for (String str1 : strs) {
                    result.add(str1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
