package com.web.test.application.service;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.CollectRuleSingleton;
import com.web.test.application.model.RuleSingleton;
import com.web.test.application.other.PageQuery;
import com.web.test.application.other.PageResult;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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


    /**
     * 存储运行过程中的检测出有问题的标准
     */
    static ConcurrentMap<String, CollectRuleSingleton> COLLECT_RULES_MAP = new ConcurrentHashMap<>();


    static ConcurrentMap<String, CollectRuleSingleton> DATA_BASE_COLLECT_RULES_MAP = new ConcurrentHashMap<>();

    public List<String> queryFullNameList() {
        Set<String> hashSet = RULES_MAP.keySet();
        List<String> res = new ArrayList<>();
        // res.addAll(hashSet);
        RULES_MAP.forEach((a, b) -> {
            String temp = b.getFullName().replaceAll(" ", "");
            temp = temp.replaceAll("（", "");
            temp = temp.replaceAll("）", "");
            temp = temp.replaceAll("《", "");
            temp = temp.replaceAll("》", "");
            //res.add(b.getFullName().replaceAll(" ", ""));
            res.add(temp);
        });
        return res;
    }

    public List<String> queryFullNameListWithSymbol() {
        Set<String> hashSet = RULES_MAP.keySet();
        List<String> res = new ArrayList<>();
        // res.addAll(hashSet);
        RULES_MAP.forEach((a, b) -> {
            // String temp = b.getFullName().replaceAll(" ", "");
            String temp = "《"+b.getCnName()+"》"+"（"+b.getFullCode()+"）";
            //res.add(b.getFullName().replaceAll(" ", ""));
            res.add(temp);
        });
        return res;
    }


    public Set<String> queryCNNameSet() {
        Set<String> hashSet = new HashSet<>();
        RULES_MAP.forEach((s, r) -> {
            hashSet.add(r.getCnName().replaceAll(" ", ""));
        });
        return hashSet;
    }


    public Set<String> queryFullCodesSet() {
        Set<String> hashSet = new HashSet<>();
        RULES_MAP.forEach((s, r) -> {
            String temp = r.getFullCode().replaceAll(" ", "");
            temp.replaceAll("（", "");
            temp.replaceAll("）", "");
            temp.replaceAll("《", "");
            temp.replaceAll("》", "");
            // hashSet.add(r.getFullCode().replaceAll(" ", ""));
            hashSet.add(temp);
        });
        return hashSet;
    }


    public static ConcurrentMap getRulesMap() {
        return RULES_MAP;
    }

    public static void setRulesMap(ConcurrentMap<String, RuleSingleton> newMap) {
        RULES_MAP = newMap;
    }

    /*public void insertRules() {

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
            *//*for(String str : pdfText.split("\n")){
                System.out.println(str);
            }*//*
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
                    RuleSingleton ruleSingleton1 = new RuleSingleton(stubStrs.get(2).replaceAll(" ", ""),
                            stubStrs.get(1).replaceAll(" ", ""),
                            "《" + stubStrs.get(2).replaceAll(" ", "") + "》（" + stubStrs.get(1).replaceAll(" ", "") + "）");


                    RuleSingleton ruleSingleton2 = new RuleSingleton(stubStrs.get(2).replaceAll(" ", ""),
                            stubStrs.get(1).replaceAll(" ", ""),
                            "《" + stubStrs.get(2).replaceAll(" ", "") + "（" + stubStrs.get(1).replaceAll(" ", "") + "）》");
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
                    RuleSingleton ruleSingleton1 = new RuleSingleton(stubStrs.get(2).replaceAll(" ", ""),
                            stubStrs.get(1).replaceAll(" ", ""),
                            "《" + stubStrs.get(2).replaceAll(" ", "") + "》（" + stubStrs.get(1).replaceAll(" ", "") + "）");


                    RuleSingleton ruleSingleton2 = new RuleSingleton(stubStrs.get(2).replaceAll(" ", ""),
                            stubStrs.get(1).replaceAll(" ", ""),
                            "《" + stubStrs.get(2).replaceAll(" ", "") + "（" + stubStrs.get(1).replaceAll(" ", "") + "）》");
                    System.out.println(ruleSingleton1.toString());
                    System.out.println(ruleSingleton2.toString());
                    *//*System.out.println(stubStrs.get(0) + " || " + stubStrs.get(1) + "  ||  " + stubStrs.get(2)
                            + " || " + stubStrs.get(3) + "  size :" + stubStrs.size());*//*
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


    }*/

    public void showFullCode() {
        for (String str : queryFullCodesSet()) {
            log.error(str);
        }

        for (String str : queryFullNameList()) {
            log.error(str);
        }

        for (String str : queryCNNameSet()) {
            log.error(str);
        }
    }

    /*public void insertRulesV2() {
        try {
            HashSet set = new HashSet();
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
                    if (f1) {
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
                                str1 = str1 + ";" + str;
                            }

                        }
                    }
                    //System.out.println(str1);
                    String[] strArray = str1.split(";");
                    //System.out.println(strArray.length);
                    String fullName1 = "";

                    //RuleSingleton ruleSingleton = new RuleSingleton(strArray[2],strArray[3],fullName1);


                    RuleSingleton ruleSingleton1 = new RuleSingleton(strArray[2].replaceAll(" ", ""),
                            strArray[3].replaceAll(" ", ""),
                            "《" + strArray[2].replaceAll(" ", "") + "》（" + strArray[3].replaceAll(" ", "") + "）");


                    RuleSingleton ruleSingleton2 = new RuleSingleton(strArray[2].replaceAll(" ", ""),
                            strArray[3].replaceAll(" ", ""),
                            "《" + strArray[2].replaceAll(" ", "") + "（" + strArray[3].replaceAll(" ", "") + "）》");
                    //System.out.println(ruleSingleton1.toString());
                    //System.out.println(ruleSingleton2.toString());
                    if (set.add(ruleSingleton1.getFullCode())) {
                        //System.out.println(ruleSingleton1.toString());
                        arrayList.add(ruleSingleton1);
                        arrayList.add(ruleSingleton2);
                    } else {
                        System.out.println(ruleSingleton1);
                    }
                }

            }

            System.out.println(arrayList.size());

            for (RuleSingleton ruleSingleton : arrayList) {
                if (!queryFullNameList().contains(ruleSingleton.getFullName())) {
                    baseMapper.add(ruleSingleton);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public List getCommonRulePart() {
        List list = new ArrayList();
        HashSet<String> fArrayList = new HashSet<>();
        HashSet<String> eArrayList = new HashSet<>();
        HashSet<String> zhSet = new HashSet<>();
        for (String str : queryCNNameSet()) {
            fArrayList.add(str.substring(0, 4));
        }

        for (String str : queryCNNameSet()) {
            eArrayList.add(str.substring(str.length() - 4, str.length()));
        }

        for (String str : queryCNNameSet()) {
            zhSet.add(str.substring(0, 4) + "[^.]{0,35}" + str.substring(str.length() - 4, str.length()));
        }

        log.error("开头：");
        fArrayList.forEach(s -> {
            log.error(s);
        });
        list.add(fArrayList);
        log.error("结尾：");
        eArrayList.forEach(s -> {
            log.error(s);
        });
        list.add(eArrayList);
        list.add(zhSet);
        return list;
    }


    /**
     *
     */
    public void collectNewRules() {
        ConcurrentMap concurrentMap = new ConcurrentHashMap();
        ConcurrentMap concurrentMap1 = new ConcurrentHashMap();
        concurrentMap1.putAll(concurrentMap);
    }

    /**
     * 查询库中标准类型
     *
     * @return
     */
    public List queryRuleType() {
        List<String> typesList = new ArrayList<>();
        try {
            typesList = baseMapper.queryTypes();
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("查询标准类型列表出错   " + System.currentTimeMillis());
            log.error(e.getMessage());
        }

        return baseMapper.queryTypes();
    }


    public List queryAllRulesWithType() {
        List<RuleSingleton> rulesList = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>();

        for (RuleSingleton ruleSingleton : baseMapper.queryRulesListWithType()) {
            if (hashSet.add(ruleSingleton.getFullCode())) {
                rulesList.add(ruleSingleton);
            }
        }
        log.error("共查询到标准的数量为" + rulesList.size());
        //rulesList = baseMapper.queryRulesListWithType();
        return rulesList;
    }

    public static void insertCollectRule(CollectRuleSingleton collectRuleSingleton) {
        /*long timeStamp = System.currentTimeMillis();*/
        if (!DATA_BASE_COLLECT_RULES_MAP.containsKey(collectRuleSingleton.getRuleString())) {
            COLLECT_RULES_MAP.put(collectRuleSingleton.getRuleString(), collectRuleSingleton);
        } else {

        }
    }


    public static void setCollectRulesMap(ConcurrentMap<String, CollectRuleSingleton> newMap) {
        DATA_BASE_COLLECT_RULES_MAP = newMap;
    }

    public static ConcurrentMap<String, CollectRuleSingleton> getCollectRulesMap() {
        return COLLECT_RULES_MAP;
    }

    public static void clearCollectRulesMap() {
        COLLECT_RULES_MAP.clear();
    }


    public List<String> getRulesType() {
        List<String> arrayList = new ArrayList();
        Set<String> hashSet = new HashSet<>();
        RULES_MAP.forEach((s, r) -> {
            hashSet.add(r.getType().replaceAll(" ", ""));
        });
        arrayList.addAll(hashSet);
        return arrayList;
    }

    public PageResult queryRulePage(PageQuery pageQuery) {
        List<String> types = pageQuery.getTypes();
        List<RuleSingleton> ruleSingletonsList = queryAllRulesWithType();
        List<RuleSingleton> list = new ArrayList<>();
        String name = pageQuery.getQueryName();
        if (types.size() == 1 && types.get(0).equals("all")) {
            for (RuleSingleton ruleSingleton : ruleSingletonsList) {
                if (ruleSingleton.getFullName().contains(name)) {
                    String tempName = "《" + ruleSingleton.getCnName() + "》" + "（" + ruleSingleton.getFullCode() + "）";
                    ruleSingleton.setFullName(tempName);
                    list.add(ruleSingleton);
                }
            }
        } else if (types.size() >= 1) {
            for (RuleSingleton ruleSingleton : ruleSingletonsList) {
                if (ruleSingleton.getFullName().contains(name) && types.contains(ruleSingleton.getType())) {
                    String tempName = "《" + ruleSingleton.getCnName() + "》" + "（" + ruleSingleton.getFullCode() + "）";
                    ruleSingleton.setFullName(tempName);
                    list.add(ruleSingleton);
                }
            }
        }
        int ruleCount = list.size();
        log.error("总条数类似物：" + ruleCount);
        int pageCapacity = pageQuery.getPageCapacity();
        int totalPages = 0;
        int currentPage = pageQuery.getCurrentPage();
        int prePages = currentPage - 1;
        totalPages = ruleCount / pageCapacity;
        int mod = ruleCount % pageCapacity;
        if (mod > 0) {
            totalPages++;
        }
        if (ruleCount <= pageCapacity) {
            PageResult res = new PageResult(list, 200, 1, 1, pageCapacity, "", ruleCount);
            return res;
        }

        if (currentPage >= totalPages) {

            if (currentPage > totalPages) {
                prePages = totalPages - 1;
            }

            currentPage = totalPages;

            int startCount = prePages * pageCapacity;
            List<RuleSingleton> temp = new ArrayList();
            for (int i = startCount; i < ruleCount; i++) {
                temp.add(list.get(i));
            }
            PageResult res = new PageResult(temp, 200, currentPage, totalPages, pageCapacity, "", ruleCount);
            return res;
        }

        if (currentPage < totalPages) {
            int startCount = prePages * pageCapacity;
            List<RuleSingleton> temp = new ArrayList();
            for (int i = startCount; i < startCount + pageCapacity; i++) {
                temp.add(list.get(i));
            }
            PageResult res = new PageResult(temp, 200, currentPage, totalPages, pageCapacity, "", ruleCount);
            return res;
        }
        return null;
    }

    public void getDataFromExcel(String path) {
        //创建文件
        // File xlsFile = new File("C:\\Users\\lzy15\\Desktop\\规范查询\\标准名称添加汇总.xls");
        HashSet<String> set = new HashSet<>();
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, RuleSingleton> ruleTemp = new HashMap<>();
        RULES_MAP.forEach((k, v) -> {
            ruleTemp.put(v.getFullCode().replaceAll(" ", ""), v);
        });
        File xlsFile = new File(path);
        // 获得工作簿对象
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(xlsFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        // 获得所有工作表
        Sheet[] sheets = workbook.getSheets();
        // 遍历工作表
        if (sheets != null) {
            for (Sheet sheet : sheets) {
                // 获得行数
                int rows = sheet.getRows();
                // 获得列数
                int cols = sheet.getColumns();
                // 读取数据
                for (int row = 1; row < rows; row++) {
                    String name = "";
                    String code = "";
                    for (int col = 1; col < 3; col++) {
                        /*System.out.printf(sheet.getCell(col, row)
                                .getContents().replaceAll(" ","") +" ");*/
                        if (col == 2) {
                            code = sheet.getCell(col, row).getContents().replaceAll(" ", "");
                        } else {
                            name = sheet.getCell(col, row).getContents().replaceAll(" ", "");
                        }
                    }
                    if (set.add(code)) {
                        map.put(code, name);
                    }
                    //System.out.println();
                }
            }
        }
        List<RuleSingleton> list = new ArrayList<>();
        map.forEach((k, v) -> {
            String type = "待分类";
            if (ruleTemp.get(k) != null) {
                type = ruleTemp.get(k).getType();
            }
            RuleSingleton temp = new RuleSingleton(v, k, "《" + v + "》" + "（" + k + "）", type);
            RuleSingleton temp1 = new RuleSingleton(v, k, "《" + v + "（" + k + "）" + "》", type);
            list.add(temp);
            list.add(temp1);
        });
        list.forEach(l -> {
            baseMapper.add(l);
            System.out.println(l.toString());
        });
        workbook.close();
    }


}
