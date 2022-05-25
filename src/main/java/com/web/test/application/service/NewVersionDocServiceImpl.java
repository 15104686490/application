package com.web.test.application.service;

import com.web.test.application.config.ConfigUtil;
import com.web.test.application.dao.ESAnalyzeDao;
import com.web.test.application.test.ConfigTest;
import com.web.test.application.test.PatternUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 支持07后的版本 word
 */
@Slf4j
@Service
public class NewVersionDocServiceImpl implements DocService {
    @Autowired
    ESAnalyzeDao esAnalyzeDao;

    /**
     * 获取docx中的正文内容，不包含tables
     * @param path
     * @return
     */
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

    @Override
    public List<String> getTableText(String path) {
        return null;
    }

    /**
     * 检查docx文件中的有效性
     * @param oldPath
     * @param newPath
     * @return
     */
    @Override
    public String checkRuluesOfText(String oldPath, String newPath) {
        log.error(oldPath);
        log.error(newPath);
        HashSet dictionaryConfigs = new HashSet();
        dictionaryConfigs.addAll(ConfigUtil.getStringConfigList("dictionary_words"));
        // String regx = ConfigTest.getConfig("regx");
        try {
            XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument
                    .openPackage(oldPath));
            XWPFDocument doc = docx.getDocument();
            List<XWPFParagraph> paragraph = doc.getParagraphs();// doc中段落
            for (XWPFParagraph xp : paragraph) {
                XWPFRun newRun = xp.createRun();
                String text = xp.getText().replaceAll(" ", "");
                text = text.replaceAll("《", "");
                text = text.replaceAll("》", "");
                text = text.replaceAll("（", "");
                text = text.replaceAll("）", "");
                List<String> analysisResults = esAnalyzeDao.getOriginalIKSmartAnalysisWords(text);
                for (String words : analysisResults) {
                    if (dictionaryConfigs.contains(words)) {
                        newRun.setBold(true);
                        newRun.setColor("FF0000");
                        newRun.setText(words);
                        newRun.addBreak();
                        log.error(words);
                    }
                }
            }
            List<XWPFTable> charts = doc.getTables();
            for (XWPFTable xwpfTable : charts) {
                List<XWPFTableRow> xwpfTableRowList = xwpfTable.getRows();
                for (XWPFTableRow xwpfTableRow : xwpfTableRowList) {
                    List<XWPFTableCell> xwpfTableCells = xwpfTableRow.getTableCells();
                    for (XWPFTableCell xwpfTableCell : xwpfTableCells) {
                        List<XWPFParagraph> cellParagraphs = xwpfTableCell.getParagraphs();
                        for (XWPFParagraph xp : cellParagraphs) {
                            XWPFRun newRun = xp.createRun();
                            String text = xp.getText().replaceAll(" ", "");
                            text = text.replaceAll("《", "");
                            text = text.replaceAll("》", "");
                            text = text.replaceAll("（", "");
                            text = text.replaceAll("）", "");
                            List<String> analysisResults = esAnalyzeDao.getOriginalIKSmartAnalysisWords(text);
                            // log.error(text);
                            for (String words : analysisResults) {
                                if (dictionaryConfigs.contains(words)) {
                                    newRun.setBold(true);
                                    newRun.setColor("FF0000");
                                    newRun.setText(words);
                                    newRun.addBreak();
                                    log.error(words);
                                }
                            }
                        }
                    }
                }
            }
            File tempFile = new File(newPath);
            FileOutputStream stream = new FileOutputStream(tempFile);
            doc.write(stream); //写入
            stream.close();
            doc.close();
            docx.close();
        } catch (Exception e) {
            log.error("检查docx文件中标准时出现异常");
            e.printStackTrace();
        }
        return null;
    }
}
