package com.web.test.application.service;

import com.web.test.application.config.ConfigUtil;
import com.web.test.application.model.FileVO;
import com.web.test.application.model.UpFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * file service
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    // @Value("${file.path}")
    private String FILE_PATH;

    /*@Value("${access.path}")
    private String Access_BASE_URL;*/

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");


    @Override
    public FileVO getById(Integer id) {
        return null;
    }

    /**
     * @param content
     * @param originFileName
     * @return
     */
    @Override
    public String storeFile(byte[] content, String originFileName) {
        FILE_PATH = ConfigUtil.getStringConfig("file_path");
        //临时文件路径配置
        String tempFilePathConfig = ConfigUtil.getStringConfig("tempFilePath");
        // 获取文件后缀 生成目录路径
        // 配置文件里的file.path + yyyyMMdd 格式组成文件夹路径
        String folder = LocalDateTime.now().format(dateTimeFormatter);
        String suffix = originFileName.substring(originFileName.lastIndexOf(".")),
                filePath = FILE_PATH;
        // 保存文件并返回文件名
        String fileName = this.storeFile(content, filePath, suffix, originFileName);
        Object o = new Object();
        o.hashCode();
        Integer integer = Integer.valueOf(1);
        RuntimeException runtimeException;
        UpFile file = new UpFile();
        file.setName(fileName);
        file.setSuffix(suffix);
        file.setPath(folder);
        file.setOldName(originFileName);
        // log.error(FILE_PATH);

        return fileName;
    }


    /**
     * @param content
     * @param path
     * @param fileName
     */
    @Override
    public void storeFileWithFileName(byte[] content, String path, String fileName) {
        // 目录不存在则创建
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (FileOutputStream os = new FileOutputStream(path + fileName);
             ByteArrayInputStream is = new ByteArrayInputStream(content)) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            log.error("存储文件到本地时发生异常：{}", e);
        }
    }


    /**
     * @param content
     * @param path
     * @param suffix
     * @param name
     * @return
     */
    private String storeFile(byte[] content, String path, String suffix, String name) {
        // String fileName = generateFileName(suffix);
        String fileName = name;
        storeFileWithFileName(content, path, fileName);
        return fileName;
    }

    private String generateFileName(String suffix) {
        return generateFileName() + suffix;
    }

    private String generateFileName() {
        return System.currentTimeMillis() + "";
    }
}
