package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.pojo.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface DocumentMapper
{
    @Select("select * from document")
    public List<Document> list();
}
