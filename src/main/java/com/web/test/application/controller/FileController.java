package com.web.test.application.controller;

import com.web.test.application.model.FileVO;
import com.web.test.application.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 文件处理 controller
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * 单个文件上传接口
     * @param file
     * @return
     */
    @PostMapping("/uploadByOne")
    public String uploadByOne(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            byte[] bytes = os.toByteArray();
            log.error(file.getName());
            log.error(file.getOriginalFilename());
            log.error(file.getSize() + "");

            FileVO vo = fileService.storeFile(bytes, file.getOriginalFilename());
            String str = "";
            str.hashCode();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new Object());
            HashMap hashMap = new HashMap();

            return vo.toString();
        } catch (IOException e) {
            /*e.printStackTrace();*/
            log.error(e.getMessage());
            return "error!!!";
        }
    }
}
