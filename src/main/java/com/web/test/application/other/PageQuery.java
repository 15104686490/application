package com.web.test.application.other;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class PageQuery {

    //int code;
    int currentPage;

    /*int totalPages;*/

    int pageCapacity;

    List<String> types;

    String queryName;
}
