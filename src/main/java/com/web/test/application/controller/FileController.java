package com.web.test.application.controller;


import com.web.test.application.config.ConfigUtil;
import com.web.test.application.service.FileService;
import com.web.test.application.service.NewVersionDocServiceImpl;

import com.web.test.application.service.RulesService;
import com.web.test.application.task.QueryRuleTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private RulesService rulesService;

    private String FILE_PATH;

    /**
     * 单个文件上传 &处理 &下载结果 接口
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadByOne", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity uploadByOne(@RequestParam("file") MultipartFile file) {

        // @RequestParam("appName") String appName
        //接口权限白名单检查
        /*if (!ConfigUtil.getStringConfigList("whiteList").contains(appName)) {
            log.error(appName + "为未授权服务");
            return null;
        }*/
        if (!QueryRuleTask.RULES_FLAG) {
            log.error("服务未就绪");
            return null;
        }
        byte[] content;
        HttpHeaders headers = new HttpHeaders();
        String dealModel = "regx";
        /*从配置中心获取当前的处理模式*/
        dealModel = ConfigUtil.getStringConfig("deal_model");
        try {
            headers.setContentDispositionFormData("attachment",
                    new String(file.getName().getBytes(StandardCharsets.UTF_8), "iso-8859-1"));
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            FILE_PATH = ConfigUtil.getStringConfig("file_path");
            InputStream is = file.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            byte[] bytes = os.toByteArray();
            log.error(file.getOriginalFilename()); //原始文件名称  包括后缀
            String originalFilename = file.getOriginalFilename();
            String tempFileName = DigestUtils.md5Hex(originalFilename) + System.currentTimeMillis()
                    + originalFilename.substring(originalFilename.lastIndexOf("."));
            String deleteFileName = fileService.storeFile(bytes, tempFileName);
            String[] suffixs = originalFilename.split("\\.");
            String suffix = suffixs[suffixs.length - 1];
            /*目前支持对docx格式的处理，暂时没有支持老版本的doc格式*/
            //log.error(originalFilename);
            if (!suffix.equals("docx")) {
                log.error("上传文件格式异常，非docx格式");
                //return new ResultTest(null, 500, "上传文件格式异常，非docx格式");
                return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST); //400
            }
            String tempFilePath = FILE_PATH + originalFilename.split("\\.")[0] + System.currentTimeMillis() + ".docx";
            File tempFile = new File(tempFilePath);
            /*按模式标识采用不同的处理逻辑，模式标识存储在nacos配置中心中，可以实时切换
             * ik模式暂时弃用
             * */
            if (dealModel.equals("ik")) {
                //newVersionDocService.checkRuluesOfText(FILE_PATH + deleteFileName, tempFilePath); //利用分词方式
            } else if (dealModel.equals("regx")) {
                /*利用正则方式提取并比对*/
                newVersionDocService.checkRuluesOfTextRegx(FILE_PATH + deleteFileName, tempFilePath);
            }

            File downloadFile = new File(tempFilePath);
            content = FileUtils.readFileToByteArray(downloadFile);

            //清除临时文件
            File deleteFile = new File(FILE_PATH + deleteFileName);
            deleteFile.delete();
            tempFile.delete();

            return new ResponseEntity<>(content, headers, HttpStatus.OK); //200
        } catch (Exception e) {
            log.error("上传文件处理异常：" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR); //500
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
     * 获取rule公共部分以构建表达式
     *
     * @param userNumber
     * @return
     */
    @Deprecated
    @GetMapping("/getCommonRulePart")
    public List getFilesList(@RequestParam("userNumber") String userNumber) {
        return rulesService.getCommonRulePart();
    }

}
