package com.web.test.application.controller;

import com.web.test.application.config.ConfigUtil;
import com.web.test.application.model.FileVO;
import com.web.test.application.service.FileService;
import com.web.test.application.service.NewVersionDocServiceImpl;
import com.web.test.application.test.ResultTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
// import org.apache.poi.ss.formula.functions.T;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
     * @param userID
     * @return
     */
    @PostMapping("/uploadByOne")
    public ResultTest uploadByOne(@RequestParam("file") MultipartFile file,
                                  @RequestParam("userID") String userID,
                                  @RequestParam("appName") String appName) {
        if (!ConfigUtil.getStringConfigList("whiteList").contains(appName)) {
            log.error(appName + "为未授权服务");
            return null;
        }
        try {
            InputStream is = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            byte[] bytes = os.toByteArray();
            // log.error(file.getName()); //字段名称
            log.error(file.getOriginalFilename()); //原始文件名称  包括后缀
            // log.error(file.getSize() + "");
            String originalFilename = file.getOriginalFilename();

            /*String fileName = DigestUtils.md5Hex(userID + originalFilename)
                    + originalFilename.substring(originalFilename.lastIndexOf(".")); // 生成磁盘保存的名称 userName 原始名称 后缀
            log.error(fileName);*/

            String tempFileName = DigestUtils.md5Hex(originalFilename) + System.currentTimeMillis()
                    + originalFilename.substring(originalFilename.lastIndexOf("."));


            //检查上传文件是否已存在（待补充）
            FileVO vo = fileService.storeFile(bytes, tempFileName);
            String str = "";

            str.hashCode();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new Object());
            HashMap hashMap = new HashMap();

            return new ResultTest(vo.toString(), 200, "上传成功"); // vo.toString();
        } catch (IOException e) {
            /*e.printStackTrace();*/
            log.error(e.getMessage());
            return new ResultTest(null, 500, "文件上传异常"); // "文件上传异常";
        }
    }


    /**
     * @param fileName
     * @param userID
     * @return
     */
    @PostMapping(value = "/downloadFile", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> download(@RequestParam String fileName,
                                           @RequestParam String userID) {
        /*通过fileName和userName查询MySQL file_info表判断文件是否存在*/
        boolean flag = false;
        if (true) {
            log.error(userID + "请求的文件" + fileName + "不存在");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {

        }
        return null;
    }

    /**
     * 单个文件检查接口
     *
     * @param fileName
     * @return
     */
    @Deprecated
    @PostMapping("/checkSingleFile")
    public String checkSingleFile(@RequestParam("fileName") String fileName,
                                  @RequestParam("userNumber") String userNumber) {
        return null;
    }


    /**
     * 获取文件用户的文件列表
     *
     * @param userNumber
     * @return
     */
    @Deprecated
    @GetMapping("/getFilesList")
    public String getFilesList(@RequestParam("userNumber") String userNumber) {

        return null;
    }

}
