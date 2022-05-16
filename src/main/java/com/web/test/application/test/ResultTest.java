package com.web.test.application.test;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ResultTest<T> {

    T data;

    int code;

    String meg;

    /*public static ResultTest buildSuccessResult(T value,int code, String st){
        return new ResultTest(r);
    }*/

    public static void main(String[] args) {
        // new ResultTest<>().setData(new ArrayList<>());
    }
}
