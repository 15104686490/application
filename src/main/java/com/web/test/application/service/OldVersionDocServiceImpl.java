package com.web.test.application.service;

import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 支持07前的版本
 */
@Deprecated
@Slf4j
@Service
public class OldVersionDocServiceImpl implements DocService {
    @Override
    public List<String> getText(String path) {
        List<String> result = new ArrayList<>();
        try {
            InputStream is = new FileInputStream("");
            /*WordExtractor extractor = new WordExtractor(is);
            result.addAll(Arrays.asList(extractor.getParagraphText()));*/
            return null;
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("07前版本word处理处理异常");
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getTableText(String path) {
        return null;
    }

    @Override
    public String checkRuluesOfText(String path, String fileName) {
        return null;
    }

    public static void main(String[] args) {
        /*for(String str : new OldVersionDocServiceImpl().getText("C:\\\\Users\\\\dell\\\\IdeaProjects\\\\doc-test\\\\doc-test\\\\src\\\\main\\\\java\\\\test\\\\test.doc")){
            System.out.println("str");
        }*/


    }
}
