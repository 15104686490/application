package com.web.test.application.model;

import lombok.Data;

@Data
public class FileVO {
    private Long id;
    private String oldName;
    private String name;
    private String suffix;
    private String url;
}
