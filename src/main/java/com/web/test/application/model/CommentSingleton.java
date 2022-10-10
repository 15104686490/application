package com.web.test.application.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
@ToString
public class CommentSingleton {

    public CommentSingleton(String commentId, int state, String commentTxt, long userId) {
        this.commentId = commentId;
        this.state = state;
        this.commentTxt = commentTxt;
        this.userId = userId;
    }

    /**
     * 评论id
     */
    String commentId;

    /**
     * 当前评论状态，0：未处理，1：已处理
     */
    int state;

    /**
     * 评论内容
     */
    String commentTxt;

    /**
     * 评论用户id
     */
    long userId;


    /**
     * 评论创建时间
     */
    String createdTime;


}
