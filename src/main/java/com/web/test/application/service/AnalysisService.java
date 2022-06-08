package com.web.test.application.service;

import java.util.List;

/**
 *
 */
public interface AnalysisService {
    /**
     * @param text
     * @return
     */
    public List<String> getIkSmartAnalysisWords(String text);
}
