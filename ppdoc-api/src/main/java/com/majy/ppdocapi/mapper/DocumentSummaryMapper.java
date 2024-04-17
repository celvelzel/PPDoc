package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Summary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface DocumentSummaryMapper
{
    @Options(useGeneratedKeys = true, keyProperty = "summary_id")
    @Insert("insert into document_summary(document_id,summary_type,summary_content) values(#{document_id},#{summary_type},#{summary_content})")
    void insert(Summary summary);
}
