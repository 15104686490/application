package com.web.test.application.service;

import java.util.List;

/**
 *
 */
public interface DocService {
    /**
     * @param path
     * @return
     */
    //获取文章中的引用
    public List<String> getText(String path);

    /**
     * @param path
     * @return
     */
    public List<String> getTableText(String path);

    /**
     * @param path
     * @param fileName
     * @return
     */
    public String checkRuluesOfText(String path, String fileName);

    public String checkRuluesOfTextRegx(String path, String fileName);
}
