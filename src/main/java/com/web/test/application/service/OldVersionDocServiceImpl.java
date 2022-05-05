package com.web.test.application.service;

import java.util.List;

//支持07后的版本
public class OldVersionDocServiceImpl implements DocService{
    @Override
    public List<String> getText(String path) {
        return null;
    }
}
