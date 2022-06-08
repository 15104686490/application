package com.web.test.application.service;

import com.web.test.application.model.FileVO;

/**
 *
 */
public interface FileService {
    /**
     * @param id
     * @return
     */
    FileVO getById(Integer id);

    /**
     * @param content
     * @param name
     * @return
     */
    String storeFile(byte[] content, String name);

    /**
     * @param content
     * @param path
     * @param fileName
     */
    void storeFileWithFileName(byte[] content, String path, String fileName);
}
