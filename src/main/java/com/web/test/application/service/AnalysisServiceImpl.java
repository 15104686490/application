package com.web.test.application.service;

import com.web.test.application.dao.ESAnalyzeDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    ESAnalyzeDao esAnalyzeDao;

    @Override
    public List<String> getIkSmartAnalysisWords(String text) {
        return esAnalyzeDao.getOriginalIKSmartAnalysisWords(text);
    }
}
