package com.web.test.application.service;

import com.web.test.application.config.ConfigUtil;
import com.web.test.application.config.NacosUtil;
import com.web.test.application.dao.ESAnalyzeDao;

import com.web.test.application.model.CheckResultSingleton;
import com.web.test.application.model.CollectRuleSingleton;
import com.web.test.application.test.PatternUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.util.ZipSecureFile;
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

   /* @Autowired
    AnalysisServiceImpl analysisService;*/

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
                // List<String> analysisResults = analysisService.getIkSmartAnalysisWords(text);
                List<String> analysisResults = new ArrayList<>();
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
                            //List<String> analysisResults = analysisService.getIkSmartAnalysisWords(text);
                            List<String> analysisResults = new ArrayList<>();

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
        ZipSecureFile.setMinInflateRatio(-1D);
        XWPFParagraph first;
        HashSet<String> langConfigs = new HashSet<>();
        HashSet<String> shortConfigs = new HashSet<>();
        HashSet<String> codeConfigs = new HashSet<>();
        List<String> textSymbol = new ArrayList<>();
        List<String> Symbols = new ArrayList<>();
        HashSet<Pattern> langConfigsPattern = new HashSet<>();
        HashSet<Pattern> shortConfigsPattern = new HashSet<>();
        HashSet<Pattern> codeConfigsPattern = new HashSet<>();
        boolean endPrintOfParagraph = false;
        endPrintOfParagraph = ConfigUtil.getBooleanConfig("paragraph_end_print_flag");
        langConfigs.addAll(ConfigUtil.getStringConfigList("lang_rules_configs"));
        shortConfigs.addAll(ConfigUtil.getStringConfigList("p_set"));
        codeConfigs.addAll(ConfigUtil.getStringConfigList("code_configs"));

        textSymbol.add("，");
        textSymbol.add("；");
        // textSymbol.add("、");


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

            ArrayList<CheckResultSingleton> resultSingletonList = new ArrayList<>();

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
                    /*完整规范名称*/
                    for (Pattern p : langConfigsPattern) {
                        Matcher matcher = p.matcher(str);
                        while (matcher.find()) {
                            String temp = matcher.group();

                            String onlyStringTemp = temp;
                            // String onlyCode = "";
                            onlyStringTemp = onlyStringTemp.replaceAll("（", "");
                            onlyStringTemp = onlyStringTemp.replaceAll("）", "");
                            onlyStringTemp = onlyStringTemp.replaceAll("《", "");
                            onlyStringTemp = onlyStringTemp.replaceAll("》", "");
                            /*处理完整的标准*/
                            if (!rulesService.queryFullNameList().contains(onlyStringTemp)) {
                                i++;
                                paragraphResult.add(temp);
                                boolean flag = totalResult.add(temp);
                                if (flag) {
                                    resultList.add(temp);
                                }
                                RulesService.insertCollectRule(new CollectRuleSingleton(temp, System.currentTimeMillis() + ""));
                                /*库中没有此标准需判断标准有问题的原因，若没有原因则将此标准进行收集
                                 * 分别取出标准的编码和文字部分进行判断
                                 * */
                                boolean codeFlag = false;
                                boolean cnFlag = false;
                                String onlyString = temp;
                                String onlyCode = "";
                                onlyString = onlyString.replaceAll("（", "");
                                onlyString = onlyString.replaceAll("）", "");
                                onlyString = onlyString.replaceAll("《", "");
                                onlyString = onlyString.replaceAll("》", "");
                                /*for (Pattern pcode : codeConfigsPattern) {
                                    Matcher matcherCode = p.matcher(str);
                                    while (matcherCode.find()) {
                                        String tempCode = matcher.group();
                                        if (!rulesService.queryFullCodesSet().contains(temp)) {
                                            codeFlag = false;
                                        } else {
                                            codeFlag = true;
                                            break;
                                        }
                                    }
                                    if (codeFlag) {
                                        break;
                                    }
                                }*/
                                String adviceCode = "";
                                for (String fullCode : rulesService.queryFullCodesSet()) {
                                    //String startOfCode = getStartOfCode(fullCode);
                                    if (onlyString.contains(fullCode)) {
                                        codeFlag = true;
                                        adviceCode = fullCode;
                                        break;
                                    } else {

                                    }
                                }
                                if (!codeFlag) {
                                    for (String fullCode : rulesService.queryFullCodesSet()) {
                                        String startOfFullCode = getStartOfCode(fullCode);
                                        /*log.error(startOfFullCode);*/
                                        if (adviceCode.equals("") && (!startOfFullCode.equals("*"))
                                                && onlyString.contains(startOfFullCode)) {
                                            adviceCode = fullCode;
                                        }
                                    }
                                }
                                for (String onlyCn : rulesService.queryCNNameSet()) {
                                    if (onlyString.contains(onlyCn)) {
                                        cnFlag = true;
                                        break;
                                    } else {

                                    }
                                }
                                String advice = "";
                                if ((!codeFlag) || (!cnFlag)) {
                                    for (String tempFullRule : rulesService.queryFullNameListWithSymbol()) {
                                        if ((!adviceCode.equals("")) && tempFullRule.contains(adviceCode)) {
                                            advice = tempFullRule;
                                            break;
                                        }
                                    }
                                }

                                String reason = "";
                                if ((codeFlag) && (!cnFlag)) {
                                    reason = "标准存在问题，请重点检查标准的名称部分";
                                } else if ((!codeFlag) && (cnFlag)) {
                                    reason = "标准存在问题，请重点检查标准的编码部分";
                                } else {
                                    reason = "标准存在问题请检查";
                                }
                                if (!advice.equals("")) {
                                    reason = reason + "，可参考标准：" + advice;
                                }
                                CheckResultSingleton singleton = new CheckResultSingleton(temp, reason);

                                if (flag) {
                                    resultSingletonList.add(singleton);
                                }

                            } else {

                            }
                            f = true;
                            // break;
                        }
                    }
                    /*编号*/
                    if (!f) {

                        for (Pattern p : codeConfigsPattern) {
                            boolean codeFlag = false;
                            Matcher matcher = p.matcher(str);

                            while (matcher.find()) {
                                String temp = matcher.group();
                                String onlyCode = temp;
                                onlyCode = onlyCode.replaceAll("（", "");
                                onlyCode = onlyCode.replaceAll("）", "");
                                onlyCode = onlyCode.replaceAll("《", "");
                                onlyCode = onlyCode.replaceAll("》", "");

                                //此处暂时省略对照先验证提取
                                if (!rulesService.queryFullCodesSet().contains(onlyCode)) {

                                    String adviceCode = "";
                                    for (String fullCode : rulesService.queryFullCodesSet()) {
                                        String startOfFullCode = getStartOfCode(fullCode);
                                        String startOfOnlyCode = getStartOfCode(onlyCode);
                                        // log.error(startOfOnlyCode);
                                        if (adviceCode.equals("") && (!startOfFullCode.equals("*"))
                                                && onlyCode.equals(startOfFullCode)) {
                                            adviceCode = fullCode;
                                        }
                                    }


                                    String advice = "";
                                    if (!adviceCode.equals("")) {
                                        for (String tempFullRule : rulesService.queryFullNameListWithSymbol()) {
                                            if ((!adviceCode.equals("")) && tempFullRule.contains(adviceCode)) {
                                                advice = tempFullRule;
                                                break;
                                            }
                                        }
                                    }
                                    i++;
                                    paragraphResult.add(temp);
                                    RulesService.insertCollectRule(new CollectRuleSingleton(temp, System.currentTimeMillis() + ""));
                                    if (totalResult.add(temp)) {
                                        String reason = "引用标准编码存在问题请检查";
                                        if (!advice.equals("")) {
                                            reason = reason + "，可参考标准：" + advice;
                                        }
                                        // boolean errorOfCode = false;
                                        CheckResultSingleton singleton = new CheckResultSingleton(temp, reason);
                                        resultSingletonList.add(singleton);
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
                    /*控制段尾输出*/
                    //endPrintOfParagraph = ConfigUtil.getBooleanConfig("paragraph_end_print_flag");
                    if (endPrintOfParagraph) {
                        for (String pr : paragraphResult) {

                            newRun.setText(j + ". " + pr);
                            newRun.addBreak();
                            j++;
                        }
                    } else {
                        /*log.info("");*/
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
                                    /*log.error();*/
                                    while (matcher.find()) {
                                        String temp = matcher.group();

                                        //此处暂时省略对照先验证提取
                                        if (!rulesService.queryFullNameList().contains(temp)) {
                                            i++;
                                            paragraphResult.add(temp);
                                            boolean flag = totalResult.add(temp);
                                            if (flag) {
                                                resultList.add(temp);
                                            }
                                            RulesService.insertCollectRule(new CollectRuleSingleton(temp, System.currentTimeMillis() + ""));
                                            /*库中没有此标准需判断标准有问题的原因，若没有原因则将此标准进行收集
                                             * 分别取出标准的编码和文字部分进行判断
                                             *
                                             * */
                                            boolean codeFlag = false;
                                            boolean cnFlag = false;
                                            String onlyString = temp;
                                            String onlyCode = "";
                                            onlyString.replaceAll("（", "");
                                            onlyString.replaceAll("）", "");
                                            onlyString.replaceAll("《", "");
                                            onlyString.replaceAll("》", "");

                                /*for (Pattern pcode : codeConfigsPattern) {
                                    Matcher matcherCode = p.matcher(str);
                                    while (matcherCode.find()) {
                                        String tempCode = matcher.group();
                                        if (!rulesService.queryFullCodesSet().contains(temp)) {
                                            codeFlag = false;
                                        } else {
                                            codeFlag = true;
                                            break;
                                        }
                                    }
                                    if (codeFlag) {
                                        break;
                                    }
                                }*/
                                            String adviceCode = "";
                                            for (String fullCode : rulesService.queryFullCodesSet()) {
                                                //String startOfCode = getStartOfCode(fullCode);
                                                if (onlyString.contains(fullCode)) {
                                                    codeFlag = true;
                                                    adviceCode = fullCode;
                                                    break;
                                                } else {

                                                }
                                            }

                                            if (!codeFlag) {
                                                for (String fullCode : rulesService.queryFullCodesSet()) {
                                                    String startOfFullCode = getStartOfCode(fullCode);
                                                    /*log.error(startOfFullCode);*/
                                                    if (adviceCode.equals("") && (!startOfFullCode.equals("*"))
                                                            && onlyString.contains(startOfFullCode)) {
                                                        adviceCode = fullCode;
                                                    }
                                                }
                                            }

                                            for (String onlyCn : rulesService.queryCNNameSet()) {
                                                if (onlyString.contains(onlyCn)) {
                                                    cnFlag = true;
                                                    break;
                                                } else {

                                                }
                                            }
                                            String advice = "";
                                            if ((!codeFlag) || (!cnFlag)) {
                                                for (String tempFullRule : rulesService.queryFullNameListWithSymbol()) {
                                                    if ((!adviceCode.equals("")) && tempFullRule.contains(adviceCode)) {
                                                        advice = tempFullRule;
                                                        break;
                                                    }
                                                }
                                            }

                                            String reason = "";
                                            if ((codeFlag) && (!cnFlag)) {
                                                reason = "标准存在问题，请重点检查标准的名称部分";
                                            } else if ((!codeFlag) && (cnFlag)) {
                                                reason = "标准存在问题，请重点检查标准的编码部分";
                                            } else {
                                                reason = "标准存在问题请检查";
                                            }
                                            if (!advice.equals("")) {
                                                reason = reason + "，可参考标准：" + advice;
                                            }
                                            CheckResultSingleton singleton = new CheckResultSingleton(temp, reason);

                                            if (flag) {
                                                resultSingletonList.add(singleton);
                                            }

                                        } else {

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

                                            String onlyCode = temp;
                                            onlyCode = onlyCode.replaceAll("（", "");
                                            onlyCode = onlyCode.replaceAll("）", "");
                                            onlyCode = onlyCode.replaceAll("《", "");
                                            onlyCode = onlyCode.replaceAll("》", "");

                                            //此处暂时省略对照先验证提取
                                            if (!rulesService.queryFullCodesSet().contains(onlyCode)) {


                                                String adviceCode = "";
                                                for (String fullCode : rulesService.queryFullCodesSet()) {
                                                    String startOfFullCode = getStartOfCode(fullCode);
                                                    String startOfOnlyCode = getStartOfCode(onlyCode);
                                                    // log.error(startOfOnlyCode);
                                                    if (adviceCode.equals("") && (!startOfFullCode.equals("*"))
                                                            && onlyCode.contains(startOfFullCode)) {
                                                        adviceCode = fullCode;
                                                    }
                                                }

                                                String advice = "";
                                                if (!adviceCode.equals("")) {
                                                    for (String tempFullRule : rulesService.queryFullNameListWithSymbol()) {
                                                        if ((!adviceCode.equals("")) && tempFullRule.contains(adviceCode)) {
                                                            advice = tempFullRule;
                                                            break;
                                                        }
                                                    }
                                                }

                                                i++;
                                                paragraphResult.add(temp);
                                                RulesService.insertCollectRule(new CollectRuleSingleton(temp, System.currentTimeMillis() + ""));

                                                String reason = "引用标准编码存在问题请检查";

                                                if (!advice.equals("")) {
                                                    reason = reason + "，可参考标准：" + advice;
                                                }

                                                //boolean errorOfCode = false;

                                                if (totalResult.add(temp)) {
                                                    resultList.add(temp);
                                                    CheckResultSingleton singleton = new CheckResultSingleton(temp, reason);
                                                    resultSingletonList.add(singleton);
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
                                                RulesService.insertCollectRule(new CollectRuleSingleton(temp, System.currentTimeMillis() + ""));
                                                if (totalResult.add(temp)) {
                                                    resultList.add(temp);
                                                    String reason = "引用标准名称存在问题请检查";
                                                    CheckResultSingleton singleton = new CheckResultSingleton(temp, reason);
                                                    resultSingletonList.add(singleton);
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
                                /*控制段尾的输出*/
                                if (endPrintOfParagraph) {
                                    for (String pr : paragraphResult) {
                                        newRun.setText(j + ". " + pr);
                                        newRun.addBreak();
                                        j++;
                                    }
                                } else {
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
                newRun.setText("本文中下列引用标准规范存在问题，请进行核对：");
                newRun.addBreak();
                /*仅文本结果输出*/
                /*for (String s : resultList) {
                    newRun.setText(k + ". " + s);
                    newRun.addBreak();
                    k++;
                }*/

                for (CheckResultSingleton singleton : resultSingletonList) {
                    String singletonOut = k + "." + singleton.getTxtValue() + "：" + singleton.getReason();
                    newRun.setText(singletonOut);
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

    /*public String getRulesTime(String code) {
        String res = "";*//*－ — -*//*
        if ((!code.contains("-")) && (!code.contains("-"))) {

        } else if (false) {

        }
        return res;
    }*/

    /**
     * 获取code中有连接符号的前半部分
     *
     * @param code
     * @return
     */
    public String getStartOfCode(String code) {
        /*－ — -*/
        String res = "";
        try {
            if (code.contains("－")) {
                res = getStartOfCode(code, "－");
            } else if (code.contains("—")) {
                res = getStartOfCode(code, "—");
            } else if (code.contains("-")) {
                res = getStartOfCode(code, "-");
            } else {
                res = "*";
            }
        } catch (Exception e) {
            res = "*";
            e.printStackTrace();
        }
        return res;
    }

    public String getStartOfCode(String code, String split) {
        String res = "";
        if (code.contains(split)) {
            int lastIndex = code.lastIndexOf(split);
            res = code.substring(0, lastIndex);
        } else {
            res = "*";
        }
        return res;
    }
}
