package com.web.test.application.other;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddCommentQuery {

    /**
     * 用户id
     */
    int userid;


    /**
     * 评论内容
     */
    String commentValue;
}
