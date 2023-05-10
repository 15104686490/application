package com.web.test.application.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.test
 * @Project：application
 * @name：TxtTest
 * @Date：2022/11/22 16:06
 * @Filename：TxtTest
 */
public class TxtTest {
    public static void main(String[] args) {
        isLegalMagicSquare("D:\\新增标准1125加空格.txt");
    }




    public static void isLegalMagicSquare(String fileName) {
        try {
            int [] temp = {1,1};
            int abc = temp[1];
            List<Integer> list = new ArrayList<>();
            int sdf = list.get(1);
            list.remove(1);
            File myFile = new File(fileName);
            if (myFile.isFile() && myFile.exists()) {
                InputStreamReader Reader = new InputStreamReader(new FileInputStream(myFile), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(Reader);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt.split(" ")[0]);
                    /*if(!lineTxt.isEmpty() && lineTxt.length()>3) {
                        System.out.println(lineTxt);
                    }*/
                }
                Reader.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

}
