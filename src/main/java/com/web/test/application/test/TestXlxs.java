package com.web.test.application.test;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.test
 * @Project：application
 * @name：TestXlxs
 * @Date：2023/1/31 8:30
 * @Filename：TestXlxs
 */
public class TestXlxs {
    public static void main(String[] args) {
        try {
            //创建工作簿对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream("C://chart1.xlsx"));
            //获取工作簿下sheet的个数
            int sheetNum = xssfWorkbook.getNumberOfSheets();
            System.out.println("该excel文件中总共有："+sheetNum+"个sheet");
            //遍历工作簿中的所有数据
            for(int i = 0;i<sheetNum;i++) {
                //读取第i个工作表
                System.out.println("读取第"+(i+1)+"个sheet");
                XSSFSheet sheet = xssfWorkbook.getSheetAt(i);
                //获取最后一行的num，即总行数。此处从0开始
                int maxRow = sheet.getLastRowNum();
                for (int row = 0; row <= maxRow; row++) {
                    //获取最后单元格num，即总单元格数 ***注意：此处从1开始计数***
                    int maxRol = sheet.getRow(row).getLastCellNum();
                    System.out.println("--------第" + row + "行的数据如下--------");
                    for (int rol = 0; rol < maxRol; rol++){
                        System.out.print(sheet.getRow(row).getCell(rol) + "  ");
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
