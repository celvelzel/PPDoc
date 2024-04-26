package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Case;
import com.majy.ppdocapi.entity.po.IdCard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CaseMapper
{
    /**
     * 查询所有案件信息
     */
    @Select("select * from t_case")
    public List<Case> getAllCases();

    /**
     * 添加数据
     */
    @Options(useGeneratedKeys = true, keyProperty = "case_id")
    @Insert("INSERT INTO t_case (indictment_id, case_type, plaintiff_name, plaintiff_id, plaintiff_type, defendant_name, defendant_id, defendant_type, plaintiff_id_card_id, defendant_id_card_id, plaintiff_license_id, defendant_license_id, related_invoice_id) " +
            "VALUES (#{indictment_id}, #{case_type}, #{plaintiff_name}, #{plaintiff_id}, #{plaintiff_type}, #{defendant_name}, #{defendant_id}, #{defendant_type}, #{plaintiff_id_card_id}, #{defendant_id_card_id}, #{plaintiff_license_id}, #{defendant_license_id}, #{related_invoice_id})")
    void insert(Case caseModel);

    /**
     * 根据案件ID删除数据
     */
    @Delete("DELETE FROM t_case WHERE case_id = #{case_id}")
    long deleteCaseById(Integer case_id);

    /**
     * 根据案件ID更新数据
     */
    @Update("UPDATE t_case SET indictment_id = #{indictment_id}, case_type = #{case_type}, plaintiff_name = #{plaintiff_name}, plaintiff_id = #{plaintiff_id}, plaintiff_type = #{plaintiff_type}, defendant_name = #{defendant_name}, defendant_id = #{defendant_id}, defendant_type = #{defendant_type}, plaintiff_id_card_id = #{plaintiff_id_card_id}, defendant_id_card_id = #{defendant_id_card_id}, plaintiff_license_id = #{plaintiff_license_id}, defendant_license_id = #{defendant_license_id}, related_invoice_id = #{related_invoice_id} " +
            "WHERE case_id = #{case_id}")
    void update(Case caseModel);

    /**
     * 根据案件ID查找数据
     */
    @Select("SELECT * FROM t_case WHERE case_id = #{case_id}")
    Case getCaseById(Integer case_id);
}
