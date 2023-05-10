package com.web.test.application.test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

public class ExcelTest {
    public static void main(String[] args) {
        //创建文件
        File xlsFile = new File("C:\\Users\\lzy15\\Desktop\\规范查询\\标准名称添加汇总.xls");
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
                for (int row = 0; row < rows; row++) {
                    for (int col = 1; col < 3; col++) {
                        System.out.printf(sheet.getCell(col, row)
                                .getContents().replaceAll(" ","") +" ");
                    }
                    System.out.println();
                }
            }
        }
        workbook.close();
    }
}
