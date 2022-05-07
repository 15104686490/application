package com.web.test.application.controller;

import com.web.test.application.model.FileVO;
import com.web.test.application.service.FileService;
import com.web.test.application.service.NewVersionDocServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private NewVersionDocServiceImpl newVersionDocService;

    /**
     * 单个文件上传接口
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadByOne")
    public String uploadByOne(@RequestParam("file") MultipartFile file,
                              @RequestParam("userNumber") String userNumber) {
        try {
            InputStream is = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            byte[] bytes = os.toByteArray();
            // log.error(file.getName());
            // log.error(file.getOriginalFilename());
            // log.error(file.getSize() + "");
            String originalFilename = file.getOriginalFilename();
            String fileName = DigestUtils.md5Hex(userNumber + originalFilename);

            //检查上传文件是否已存在（待补充）


            //将上传文件信息存储到mysql中（待补充）

            FileVO vo = fileService.storeFile(bytes, fileName);
            String str = "";
            str.hashCode();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new Object());
            HashMap hashMap = new HashMap();

            return vo.toString();
        } catch (IOException e) {
            /*e.printStackTrace();*/
            log.error(e.getMessage());
            return "文件上传异常";
        }
    }

    /**
     * 单个文件检查接口
     *
     * @param fileName
     * @return
     */
    @PostMapping("/checkSingleFile")
    public String checkSingleFile(@RequestParam("fileName") String fileName,
                                  @RequestParam("userNumber") String userNumber) {
        return null;
    }


    /**
     * 获取文件用户的文件列表
     * @param userNumber
     * @return
     */
    @GetMapping("/getFilesList")
    public String getFilesList(@RequestParam("userNumber") String userNumber) {
        return null;
    }

}
