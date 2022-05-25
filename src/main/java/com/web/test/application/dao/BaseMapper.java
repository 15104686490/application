package com.web.test.application.dao;


// import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 */
// @Mapper
public interface BaseMapper {
    // @Select("")
    public List<Object> queryFilesList();
}
