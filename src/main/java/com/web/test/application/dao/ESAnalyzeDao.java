package com.web.test.application.dao;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ES Analyze
 */
@Slf4j
@Component
public class ESAnalyzeDao extends ESBaseDao {
    /**
     * @param text
     * @return
     */
    public List<String> getOriginalIKSmartAnalysisWords(String text) {
        try {
            //此方法使用IK分词器中的ik_smart模式，按词典中的粗粒度进行分词
            ArrayList<String> result = new ArrayList<>();
            AnalyzeRequest request = AnalyzeRequest.buildCustomAnalyzer("ik_smart")
                    .build(text);
            List<AnalyzeResponse.AnalyzeToken> analyzeTokenList = getAnalyzeResult(request, RequestOptions.DEFAULT);
            for (AnalyzeResponse.AnalyzeToken analyzeToken : analyzeTokenList) {
                result.add(analyzeToken.getTerm().toString());
            }
            return result;
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("使用IK分词器Smart模式获取分词结果异常");
            log.error(e.getMessage());
            return null;
        }
    }
}
