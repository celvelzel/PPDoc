package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.IdCard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IdCardMapper
{
    /**
     * 添加身份证信息
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Select("insert into id_card(document_id, id_card_url, file_name,name, nation, sex, address, card_number, all_info) values(#{document_id}, #{id_card_url}, #{file_name}, #{name}, #{nation}, #{sex}, #{address}, #{card_number}, #{all_info})")
    public void insert(IdCard idCard);

    /**
     * 根据身份证id删除身份证信息
     */
    @Delete("delete from id_card where id = #{id}")
    public Integer delete(Integer id);

    /**
     * 根据document_id删除身份证信息
     */
    @Delete("delete from id_card where document_id = #{document_id}")
    public Integer deleteByDocumentId(Integer document_id);

    /**
     * 根据身份证id查询身份证信息
     *
     * @param id
     */
    @Select("select * from id_card where id = #{id}")
    public IdCard getByIdCardId(Integer id);

    /**
     * 根据document_id查询身份证信息
     *
     * @param document_id
     */
    @Select("select * from id_card where document_id = #{document_id}")
    public IdCard getByDocumentId(Integer document_id);

    /**
     * 查询所有身份证信息
     */
    @Select("select * from id_card")
    public List<IdCard> list();

    /**
     * 更新身份证信息
     */
    Integer update(IdCard idCard);
}
