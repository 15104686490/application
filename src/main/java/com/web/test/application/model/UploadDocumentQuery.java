package com.web.test.application.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.model
 * @Project：application
 * @name：uploadDocumentQuery
 * @Date：2023/1/5 15:50
 * @Filename：uploadDocumentQuery
 */
@Getter
@Setter
public class UploadDocumentQuery {

    String code;

    String cnName;

    MultipartFile file;

}
