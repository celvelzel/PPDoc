package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.License;
import com.majy.ppdocapi.service.LicenseService;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LicenseMapper
{
    @Select("select * from license")
    public List<License> list();

    /**
     * 添加营业执照数据
     *
     * @param license
     */
    @Options(useGeneratedKeys = true, keyProperty = "license_id")
    @Insert("insert into license(document_id,license_url,file_name,license_code,license_number,license_enterprise_name,license_enterprise_type,license_legal_representative,license_business_scope,license_registered_capital,license_establish_date,license_operation_period,license_domicile,all_info) " +
            "values(#{document_id},#{license_url},#{file_name},#{license_code},#{license_number},#{license_enterprise_name},#{license_enterprise_type},#{license_legal_representative},#{license_business_scope},#{license_registered_capital},#{license_establish_date},#{license_operation_period},#{license_domicile},#{all_info})")
    public void insert(License license);

    /**
     * 根据license_id删除营业执照数据
     * @param license_id
     */
    @Delete("delete from license where license_id = #{license_id}")
    public void delete(Integer license_id);

    /**
     * 根据license_id查询营业执照数据
     */
    @Select("select * from license where license_id = #{license_id}")
    public License getById(Integer license_id);

    /**
     * 根据document_id查询营业执照数据
     */
    @Select("select * from license where document_id = #{document_id}")
    public License getByDocumentId(Integer document_id);

    /**
     * 根据license_id更新营业执照数据
     *
     * @param license
     */
    @Update("update license set file_name= #{file_name}, document_id = #{document_id},license_url = #{license_url},license_code = #{license_code},license_number = #{license_number},license_enterprise_name = #{license_enterprise_name},license_enterprise_type = #{license_enterprise_type},license_legal_representative = #{license_legal_representative},license_establish_date = #{license_establish_date},license_operation_period = #{license_operation_period},license_domicile = #{license_domicile},all_info = #{all_info} where license_id = #{license_id}")
    public void update(License license);


    /**
     * 根据document_id删除营业执照数据
     *
     * @param document_id
     */
    @Delete("delete from license where document_id = #{document_id}")
    public void deleteByDocumentId(Integer document_id);
}
