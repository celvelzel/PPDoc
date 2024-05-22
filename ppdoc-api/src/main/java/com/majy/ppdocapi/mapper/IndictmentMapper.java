package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Indictment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IndictmentMapper
{
    /*
    * 添加起诉状
     */
    @Options(useGeneratedKeys = true, keyProperty = "indictment_id")
    @Insert("INSERT INTO indictment (document_id, indictment_url, indictment_ocr_url, file_name, case_type, plaintiff_name, plaintiff_id, plaintiff_type, plaintiff_address, plaintiff_contact, defendant_name, defendant_id, defendant_type, defendant_address, defendant_contact, litigation_request, facts_background, legal_basis, evidence_list, court_name, indictment_date, all_info) " +
            "VALUES (#{document_id}, #{indictment_url}, #{indictment_ocr_url}, #{file_name}, #{case_type}, #{plaintiff_name}, #{plaintiff_id}, #{plaintiff_type}, #{plaintiff_address}, #{plaintiff_contact}, #{defendant_name}, #{defendant_id}, #{defendant_type}, #{defendant_address}, #{defendant_contact}, #{litigation_request}, #{facts_background}, #{legal_basis}, #{evidence_list}, #{court_name}, #{indictment_date}, #{all_info})")
    public void insert(Indictment indictment);

    /*
    * 删除起诉状
     */
    @Delete("delete from indictment where indictment_id = #{indictment_id}")
    public Integer delete(Integer indictment_id);

    /*
    * 根据文档ID删除起诉状
     */
    @Delete("delete from indictment where document_id = #{document_id}")
    public Integer deleteByDocumentId(Integer document_id);

    /*
    * 更新起诉状
     */
    public Integer update(Indictment indictment);

    /*
    * 根据ID查询起诉状
     */
    @Select("select * from indictment where indictment_id = #{indictment_id}")
    public Indictment getByIndictmentId(Integer indictment_id);

    /*
    * 根据文档ID查询起诉状
     */
    @Select("select * from indictment where document_id = #{document_id}")
    public Indictment getByDocumentId(Integer document_id);

    /*
    * 查询所有起诉状
     */
    @Select("select * from indictment")
    public List<Indictment> list();
}
