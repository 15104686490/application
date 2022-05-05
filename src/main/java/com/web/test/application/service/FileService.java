package com.web.test.application.service;

import com.web.test.application.model.FileVO;

public interface FileService {
    FileVO getById(Integer id);

    FileVO storeFile(byte[] content, String name);

    void storeFileWithFileName(byte[] content, String path, String fileName);
}
