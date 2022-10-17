package com.web.test.application.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.CommentSingleton;
import com.web.test.application.other.AddCommentQuery;
import com.web.test.application.other.PageQuery;
import com.web.test.application.other.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentService {

    @Autowired
    BaseMapper baseMapper;

    public boolean addComment(AddCommentQuery query) {
        long time = System.currentTimeMillis();
        String commentId = time + "" + query.getUserid();
        long userIdLong = query.getUserid();
        String commentValue = query.getCommentValue();
        int state = 0;
        CommentSingleton commentSingleton = new CommentSingleton(commentId, state, commentValue, userIdLong);
        try {
            baseMapper.addcomment(commentSingleton);
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("add comment to db error !!!! " + e.getMessage());
        }
        return false;
    }


    public PageResult getCommentPage(PageQuery pageQuery) {
        int currentPage = pageQuery.getCurrentPage();
        int pageSize = pageQuery.getPageCapacity();
        List<CommentSingleton> list = new ArrayList<>();
        try {
            Page page = PageHelper.startPage(currentPage, pageSize);
            list = baseMapper.queryComments();
            PageInfo info = new PageInfo<>(page.getResult());
            int totals = (int) info.getTotal();
            int pages = info.getPages();
            int curPage = info.getPageNum();
            for (int i = 0; i < list.size(); i++) {
                int l = list.get(0).getCommentId().length();
                list.get(i).setUserId(Long.parseLong(list.get(i).getCommentId().substring(l - 4, l-1)));
            }
            /*log.error(info.getPageNum()+"");*/
            PageResult pageResult = new PageResult(list, 200, curPage, pages,
                    pageQuery.getPageCapacity(), "ok", totals);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("query comments error !!!! " + e.getMessage());
        }
        return null;
    }
}
