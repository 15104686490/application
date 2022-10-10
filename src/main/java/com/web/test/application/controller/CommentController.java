package com.web.test.application.controller;

import com.web.test.application.model.CommentSingleton;
import com.web.test.application.other.AddCommentQuery;
import com.web.test.application.other.PageQuery;
import com.web.test.application.other.PageResult;
import com.web.test.application.other.ResultTest;
import com.web.test.application.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理评论相关请求
 */
@Slf4j
@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * 新增评论
     *
     * @param query
     * @return
     */
    @PostMapping(value = "/addComment")
    public ResultTest queryRules(@RequestBody AddCommentQuery query) {
        if (query == null) {
            return new ResultTest(null, 500, "请求为空");
        }
        if (query.getCommentValue() == null || query.getCommentValue().equals("")) {
            return new ResultTest(null, 500, "评论内容为空");
        }
        boolean flag = commentService.addComment(query);
        if (flag) {
            return new ResultTest(null, 200, "添加评论成功");
        } else {
            return new ResultTest(null, 500, "添加评论失败");
        }
    }

    /**
     * 分页查询评论
     * @param pageQuery
     * @return
     */
    @PostMapping(value = "/queryCommentPages")
    public PageResult queryRules(@RequestBody PageQuery pageQuery) {
        if (pageQuery == null) {
            return new PageResult(null, 500, 1, 0, 0, "查询异常，查询条件为空"
                    , 0);
        }

        if (pageQuery.getCurrentPage() <= 0) {
            pageQuery.setCurrentPage(1);
        }

        if (pageQuery.getPageCapacity() < 10) {
            pageQuery.setPageCapacity(10);
        }
       /* List<CommentSingleton> res = new ArrayList<>();*/
        try {
           PageResult res =  commentService.getCommentPage(pageQuery);
           return res;

        } catch (Exception e) {
            log.error("query comments error !!!! " + e.getMessage());
        }
        return new PageResult(null, 500, 1, 0, 10, "查询评论失败"
                , 0);
    }
}
