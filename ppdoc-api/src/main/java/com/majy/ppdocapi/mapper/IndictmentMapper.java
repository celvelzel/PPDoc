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
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO indictment (document_id, indictment_url, file_name, case_type, plaintiff_name, plaintiff_number, plaintiff_type, plaintiff_address, plaintiff_contact, defendant_name, defendant_number, defendant_type, defendant_address, defendant_contact, litigation_request, facts_background, legal_basis, evidence_list, court_name, indictment_date, all_info) " +
            "VALUES (#{document_id}, #{indictment_url}, #{file_name}, #{case_type}, #{plaintiff_name}, #{plaintiff_number}, #{plaintiff_type}, #{plaintiff_address}, #{plaintiff_contact}, #{defendant_name}, #{defendant_number}, #{defendant_type}, #{defendant_address}, #{defendant_contact}, #{litigation_request}, #{facts_background}, #{legal_basis}, #{evidence_list}, #{court_name}, #{indictment_date}, #{all_info})")
    public void insert(Indictment indictment);

    /*
    * 删除起诉状
     */
    @Delete("delete from indictment where indictment_id = #{indictment_id}")
    public void delete(Integer indictment_id);

    /*
    * 根据文档ID删除起诉状
     */
    @Delete("delete from indictment where document_id = #{document_id}")
    public void deleteByDocumentId(Integer document_id);

    /*
    * 更新起诉状
     */
    @Update("UPDATE indictment SET document_id = #{document_id}, file_name = #{file_name}, case_type = #{case_type}, plaintiff_name = #{plaintiff_name}, plaintiff_number = #{plaintiff_number}, plaintiff_type = #{plaintiff_type}, plaintiff_address = #{plaintiff_address}, plaintiff_contact = #{plaintiff_contact}, defendant_name = #{defendant_name}, defendant_number = #{defendant_number}, defendant_type = #{defendant_type}, defendant_address = #{defendant_address}, defendant_contact = #{defendant_contact}, litigation_request = #{litigation_request}, facts_background = #{facts_background}, legal_basis = #{legal_basis}, evidence_list = #{evidence_list}, court_name = #{court_name}, indictment_date = #{indictment_date}, all_info = #{all_info} " +
            "WHERE indictment_id = #{indictment_id}")
    public void update(Indictment indictment);

    /*
    * 根据ID查询起诉状
     */
    @Select("select * from indictment where indictment_id = #{indictment_id}")
    public Indictment getById(Integer indictment_id);

    /*
    * 根据文档ID查询起诉状
     */
    @Select("select * from indictment where document_id = #{document_id}")
    public Indictment getByDocumentId(Integer documentId);

    /*
    * 查询所有起诉状
     */
    @Select("select * from indictment")
    public List<Indictment> list();
}
