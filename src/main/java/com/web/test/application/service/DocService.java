package com.web.test.application.service;

import java.util.List;

public interface DocService {
    //获取文章中的引用
    public List<String> getText(String path);
}
