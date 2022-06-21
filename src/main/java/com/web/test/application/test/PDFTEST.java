package com.web.test.application.test;

import com.web.test.application.model.RuleSingleton;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDFTEST {
    public static void main(String[] args) {

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
            /*for(String str : pdfText.split("\n")){
                System.out.println(str);
            }*/
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
                    RuleSingleton ruleSingleton1 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"》（"+stubStrs.get(1).replaceAll(" ","")+")");


                    RuleSingleton ruleSingleton2 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"（"+stubStrs.get(1).replaceAll(" ","")+")》");
                    System.out.println(ruleSingleton1.toString());
                    System.out.println(ruleSingleton2.toString());

                    // System.out.println(stubStrs.get(0) + " || " + stubStrs.get(1) + "  ||  " + stubStrs.get(2) + "  size :" + stubStrs.size());


                } else if (stubStrs.size() > 5) {
                    //System.out.println(stubStrs.get(0));

                    if (num.contains(Integer.valueOf(stubStrs.get(0)))) {
                        continue;
                    }
                    j++;
                    System.out.println(j);
                    RuleSingleton ruleSingleton1 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"》（"+stubStrs.get(1).replaceAll(" ","")+")");


                    RuleSingleton ruleSingleton2 = new RuleSingleton(stubStrs.get(2).replaceAll(" ",""),
                            stubStrs.get(1).replaceAll(" ",""),
                            "《"+stubStrs.get(2).replaceAll(" ","")+"（"+stubStrs.get(1).replaceAll(" ","")+")》");
                    System.out.println(ruleSingleton1.toString());
                    System.out.println(ruleSingleton2.toString());
                    /*System.out.println(stubStrs.get(0) + " || " + stubStrs.get(1) + "  ||  " + stubStrs.get(2)
                            + " || " + stubStrs.get(3) + "  size :" + stubStrs.size());*/

                } else {
                    // System.out.println(" 不包含 ：" + stubStrs.get(0));
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
