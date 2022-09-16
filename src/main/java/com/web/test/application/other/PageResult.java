package com.web.test.application.other;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PageResult<T> {
    T data;

    int code;

    int currentPage;

    int totalPages;

    int pageCapacity;

    String msg;

}
