package com.web.test.application.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.web.test.application.config.ConfigUtil;
import com.web.test.application.config.NacosUtil;
import com.web.test.application.dao.ESAnalyzeDao;

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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支持07后的版本 word docx后缀
 */
@Slf4j
@Service
public class NewVersionDocServiceImpl implements DocService {
    @Autowired
    ESAnalyzeDao esAnalyzeDao;

    @Autowired
    AnalysisServiceImpl analysisService;

    @Autowired
    RulesService rulesService;

    // boolean flag = true;

    /**
     * 获取docx中的正文内容，不包含tables
     *
     * @param path
     * @return
     */
    @Deprecated
    @Override
    public List<String> getText(String path) {
        List<String> result = new ArrayList();
        String regx = NacosUtil.getConfig("regx");
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
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    @Override
    public String checkRuluesOfText(String oldPath, String newPath) {
        // log.error(oldPath);
        // log.error(newPath);
        HashSet dictionaryConfigs = new HashSet();
        dictionaryConfigs.addAll(ConfigUtil.getStringConfigList("dictionary_words"));
        List<String> symbols = ConfigUtil.getStringConfigList("special_symbols");
        boolean isBold = Boolean.valueOf(ConfigUtil.getStringConfig("text_bold_flag"));
        String newTextColor = ConfigUtil.getStringConfig("new_text_color");
        try {
            XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument
                    .openPackage(oldPath));
            XWPFDocument doc = docx.getDocument();
            List<XWPFParagraph> paragraph = doc.getParagraphs();// doc中段落
            for (XWPFParagraph xp : paragraph) {
                XWPFRun newRun = xp.createRun();
                int i = 1;
                String text = replaceSpecialSymbol(xp.getText(), symbols, "");
                List<String> analysisResults = analysisService.getIkSmartAnalysisWords(text);
                int j = 0;
                for (String words : analysisResults) {
                    if (dictionaryConfigs.contains(words)) {
                        newRun.setBold(isBold);
                        newRun.setColor(newTextColor);
                        if ((j + 1) < analysisResults.size() && dictionaryConfigs
                                .contains(words + analysisResults.get(j + 1))) {
                            newRun.setText(i + ". " + words + analysisResults.get(j + 1));
                        } else if ((j - 1) > 0 && dictionaryConfigs.contains(analysisResults.get(j - 1) + words)) {
                            continue;
                        } else {
                            newRun.setText(i + ". " + words);
                        }

                        newRun.addBreak();
                        // log.error(words);
                        i++;
                    }
                    j++;
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
                            int i = 1;
                            String text = replaceSpecialSymbol(xp.getText(), symbols, "");
                            List<String> analysisResults = analysisService.getIkSmartAnalysisWords(text);
                            // log.error(text);
                            int j = 0;
                            for (String words : analysisResults) {
                                if (dictionaryConfigs.contains(words)) {
                                    newRun.setBold(isBold);
                                    newRun.setColor(newTextColor);
                                    // newRun.setText(i + ". " + words);
                                    if ((j + 1) < analysisResults.size() && dictionaryConfigs
                                            .contains(words + analysisResults.get(j + 1))) {
                                        newRun.setText(i + ". " + words + analysisResults.get(j + 1));
                                    } else if ((j - 1) > 0 && dictionaryConfigs.contains(analysisResults.get(j - 1) + words)) {
                                        continue;
                                    } else {
                                        newRun.setText(i + ". " + words);
                                    }

                                    newRun.addBreak();
                                    // log.error(words);
                                    i++;
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

    /**
     * 利用正则方式提取规则
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    @Override
    public String checkRuluesOfTextRegx(String oldPath, String newPath) {
        /*if (flag) {
            //rulesService.showFullCode();
            // rulesService.insertRulesV2();
            flag = false;
        }*/
        XWPFParagraph first;
        HashSet<String> langConfigs = new HashSet<>();
        HashSet<String> shortConfigs = new HashSet<>();
        HashSet<String> codeConfigs = new HashSet<>();
        List<String> textSymbol = new ArrayList<>();
        HashSet<Pattern> langConfigsPattern = new HashSet<>();
        HashSet<Pattern> shortConfigsPattern = new HashSet<>();
        HashSet<Pattern> codeConfigsPattern = new HashSet<>();
        langConfigs.addAll(ConfigUtil.getStringConfigList("lang_rules_configs"));
        shortConfigs.addAll(ConfigUtil.getStringConfigList("p_set"));
        codeConfigs.addAll(ConfigUtil.getStringConfigList("code_configs"));

        textSymbol.add("，");
        textSymbol.add("；");
        textSymbol.add("、");


        for (String str : langConfigs) {
            Pattern pattern = Pattern.compile(str);
            langConfigsPattern.add(pattern);
        }
        for (String str : shortConfigs) {
            Pattern pattern = Pattern.compile(str);
            shortConfigsPattern.add(pattern);
        }
        for (String str : codeConfigs) {
            Pattern pattern = Pattern.compile(str);
            codeConfigsPattern.add(pattern);
        }

        boolean isBold = Boolean.valueOf(ConfigUtil.getStringConfig("text_bold_flag"));
        String newTextColor = ConfigUtil.getStringConfig("new_text_color");
        try {
            XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument
                    .openPackage(oldPath));
            XWPFDocument doc = docx.getDocument();
            List<XWPFParagraph> paragraph = doc.getParagraphs();// doc中段落
            HashSet<String> totalResult = new HashSet<>();
            ArrayList<String> resultList = new ArrayList<>();
            for (XWPFParagraph xp : paragraph) {

                XWPFRun newRun = xp.createRun();
                int i = 1;

                String text = xp.getText().replaceAll(" ", "");
                text = replaceSpecialSymbol(text, textSymbol, "。");
                List<String> textList = Arrays.asList(text.split("。"));

                for (String str : textList) {
                    LinkedHashSet<String> paragraphResult = new LinkedHashSet();
                    boolean f = false;
                    for (Pattern p : langConfigsPattern) {
                        Matcher matcher = p.matcher(str);

                        while (matcher.find()) {
                            String temp = matcher.group();

                            //此处暂时省略对照先验证提取
                            if (!rulesService.queryFullNameList().contains(temp)) {
                                i++;
                                paragraphResult.add(temp);
                                if (totalResult.add(temp)) {
                                    resultList.add(temp);
                                }
                            }
                            f = true;
                            // break;
                        }
                    }
                    if (!f) {
                        for (Pattern p : codeConfigsPattern) {
                            boolean codeFlag = false;
                            Matcher matcher = p.matcher(str);

                            while (matcher.find()) {
                                String temp = matcher.group();
                                //此处暂时省略对照先验证提取
                                if (!rulesService.queryFullCodesSet().contains(temp)) {

                                    i++;
                                    paragraphResult.add(temp);
                                    if (totalResult.add(temp)) {
                                        resultList.add(temp);
                                    }

                                }
                                f = true;
                                codeFlag = true;
                                break;
                            }
                            if (codeFlag) {
                                break;
                            }
                        }
                    }
                    newRun.setBold(isBold);
                    newRun.setColor(newTextColor);
                    int j = 1;
                    for (String pr : paragraphResult) {

                        newRun.setText(j + ". " + pr);
                        newRun.addBreak();
                        j++;
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
                            int i = 1;

                            XWPFRun newRun = xp.createRun();
                            // int i = 1;
                            String text = xp.getText().replaceAll(" ", "");
                            text = replaceSpecialSymbol(text, textSymbol, "。");
                            List<String> textList = Arrays.asList(text.split("。"));
                            for (String str : textList) {
                                boolean f = false;
                                TreeSet<String> paragraphResult = new TreeSet();
                                for (Pattern p : langConfigsPattern) {
                                    Matcher matcher = p.matcher(str);

                                    while (matcher.find()) {
                                        String temp = matcher.group();

                                        if (!rulesService.queryFullNameList().contains(temp)) {
                                            i++;
                                            paragraphResult.add(temp);
                                            if (totalResult.add(temp)) {
                                                resultList.add(temp);
                                            }
                                        }
                                        f = true;
                                        // break;
                                    }
                                }
                                if (!f) {
                                    for (Pattern p : codeConfigsPattern) {
                                        boolean codeFlag = false;
                                        Matcher matcher = p.matcher(str);

                                        while (matcher.find()) {
                                            String temp = matcher.group();
                                            //此处暂时省略对照先验证提取
                                            if (!rulesService.queryFullCodesSet().contains(temp)) {
                                                i++;
                                                paragraphResult.add(temp);
                                                if (totalResult.add(temp)) {
                                                    resultList.add(temp);
                                                }

                                            }
                                            f = true;
                                            codeFlag = true;
                                            break;
                                        }
                                        if (codeFlag) {
                                            break;
                                        }
                                    }
                                }

                                if (!f) {
                                    boolean breakFlag = false;
                                    for (Pattern p : shortConfigsPattern) {
                                        Matcher matcher = p.matcher(str);
                                        while (matcher.find()) {
                                            String temp = matcher.group();
                                            // log.error(temp);
                                            if (!rulesService.queryCNNameSet().contains(temp)) {
                                                i++;
                                                paragraphResult.add(temp);
                                                if (totalResult.add(temp)) {
                                                    resultList.add(temp);
                                                }
                                            } else {
                                                f = true;
                                                breakFlag = true;
                                                break;
                                            }
                                        }
                                        if (breakFlag) {
                                            break;
                                        }
                                    }
                                }

                                newRun.setBold(isBold);
                                newRun.setColor(newTextColor);
                                int j = 1;
                                for (String pr : paragraphResult) {
                                    newRun.setText(j + ". " + pr);
                                    newRun.addBreak();
                                    j++;
                                }
                            }

                        }
                    }
                }
            }

            if (paragraph.size() >= 1 && resultList != null && !resultList.isEmpty()) {
                XWPFParagraph xp = paragraph.get(0);
                XWPFRun newRun = xp.createRun();
                newRun.setBold(isBold);
                newRun.setColor(newTextColor);
                int k = 1;
                for (int i = 0; i < 10; i++) {
                    newRun.addBreak();
                }
                newRun.setText("本文中下列引用规范可能存在问题，请进行核对：");
                newRun.addBreak();
                for (String s : resultList) {
                    newRun.setText(k + ". " + s);
                    newRun.addBreak();
                    k++;
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

    /**
     * 替换文本中的内容
     *
     * @param text
     * @param symbols
     * @param replaceString
     * @return
     */
    private String replaceSpecialSymbol(String text, List<String> symbols, String replaceString) {
        if (text == null || text.length() == 0) {
            return text;
        }
        if (symbols.size() == 0) {
            return text;
        }
        for (String symbol : symbols) {
            text = text.replaceAll(symbol, replaceString);
        }
        return text;
    }
}
