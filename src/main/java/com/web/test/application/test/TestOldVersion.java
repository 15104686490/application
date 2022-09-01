package com.web.test.application.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.poi.hwpf.extractor.WordExtractor;

public class TestOldVersion {
    public static void main(String[] args) {
        try {
            InputStream is = new FileInputStream(new File("C:\\\\Users\\\\dell\\\\IdeaProjects\\\\doc-test\\\\doc-test\\\\src\\\\main\\\\java\\\\test\\\\test2.doc"));
            WordExtractor wordExtractor = new WordExtractor(is);

            for (String str : wordExtractor.getParagraphText()) {
                System.out.println(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
