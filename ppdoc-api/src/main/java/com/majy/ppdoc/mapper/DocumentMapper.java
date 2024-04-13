package com.majy.ppdoc.mapper;

import com.majy.ppdoc.pojo.Document;
import com.majy.ppdoc.pojo.IdCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DocumentMapper
{
    @Select("select * from document")
    public List<Document> list();
}
