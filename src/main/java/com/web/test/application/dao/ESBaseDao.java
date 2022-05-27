package com.web.test.application.dao;


import com.web.test.application.config.ConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;

import java.io.IOException;
import java.util.List;

/**
 *
 */
@Slf4j
public abstract class ESBaseDao {
    static RestHighLevelClient client;

    static {
        String host = ConfigUtil.getStringConfig("ESHost");
        int activePort = ConfigUtil.getIntegerConfig("ESActivePort");
        int standbyPort = ConfigUtil.getIntegerConfig("ESStandbyPort");
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost(host, activePort, "http"),
                new HttpHost(host, standbyPort, "http")));
    }

    /**
     * @return
     */
    public static RestHighLevelClient getSimpleESClient() {
        return client;
    }

    /**
     * @param request
     * @param requestOptions
     * @return
     */
    public static List<AnalyzeResponse.AnalyzeToken> getAnalyzeResult(AnalyzeRequest request, RequestOptions requestOptions) {
        try {
            AnalyzeResponse analyzeResponse = client.indices().analyze(request, requestOptions);
            return analyzeResponse.getTokens();
        } catch (IOException e) {
            log.error("从ES获取AnalyzeResponse异常");
            log.error(e.getMessage());
            return null;
            // e.printStackTrace();
        }
    }
}
