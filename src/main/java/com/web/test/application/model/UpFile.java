package com.web.test.application.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String suffix;
    private String path;
    private String oldName;
}
