package com.web.test.application.controller;


import com.web.test.application.config.ConfigUtil;
import com.web.test.application.service.FileService;
import com.web.test.application.service.NewVersionDocServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

    // @Value("${file.path}")
    private String FILE_PATH;

    /**
     * 单个文件上传接口
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadByOne", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> uploadByOne(@RequestParam("file") MultipartFile file) {
        // @RequestParam("appName") String appName
        //接口权限白名单检查
        /*if (!ConfigUtil.getStringConfigList("whiteList").contains(appName)) {
            log.error(appName + "为未授权服务");
            return null;
        }*/
        try {
            FILE_PATH = ConfigUtil.getStringConfig("file_path");
            InputStream is = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            byte[] bytes = os.toByteArray();
            log.error(file.getOriginalFilename()); //原始文件名称  包括后缀
            String originalFilename = file.getOriginalFilename();
            String tempFileName = DigestUtils.md5Hex(originalFilename) + System.currentTimeMillis()
                    + originalFilename.substring(originalFilename.lastIndexOf("."));
            //log.error("tempFileName   " + tempFileName);
            String deleteFileName = fileService.storeFile(bytes, tempFileName);
            String suffix = originalFilename.split("\\.")[1];
            //log.error(originalFilename);
            if (!suffix.equals("docx")) {
                log.error("上传文件格式异常，非docx格式");
                return null;
            }
            String tempFilePath = FILE_PATH + originalFilename.split("\\.")[0] + System.currentTimeMillis() + ".docx";
            File tempFile = new File(tempFilePath);
            newVersionDocService.checkRuluesOfText(FILE_PATH + deleteFileName, tempFilePath);
            File downloadFile = new File(tempFilePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment",
                    new String(file.getName().getBytes(StandardCharsets.UTF_8), "iso-8859-1"));
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            byte[] content = FileUtils.readFileToByteArray(downloadFile);

            //清除临时文件
            //log.error(deleteFileName);
            File deleteFile = new File(FILE_PATH + deleteFileName);
            deleteFile.delete();
            tempFile.delete();
            //downloadFile.delete();
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("上传文件处理异常：" + e.getStackTrace());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param fileName
     * @param userID
     * @return 测试下载文件
     */
    @GetMapping(value = "/downloadFile", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> download(@RequestParam String fileName,
                                           @RequestParam String userID) {
        /*通过fileName和userName查询MySQL file_info表判断文件是否存在*/
        try {
            boolean flag = false;
            if (flag) {
                log.error(userID + "请求的文件" + fileName + "不存在");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                File file = new File("D://11.jpg");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment",
                        new String(file.getName().getBytes(StandardCharsets.UTF_8), "iso-8859-1"));
                headers.add("Access-Control-Expose-Headers", "Content-Disposition");
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                byte[] content = FileUtils.readFileToByteArray(file);
                return new ResponseEntity<>(content, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // return null;
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
