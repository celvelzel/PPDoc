package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Document;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface DocumentMapper
{
    /**
     * 查询总记录数
     */
    @Select("Select count(*) from document")
    public Long count();

    /**
     * 分页查询
     *
     * @param start    起始位置
     * @param pageSize 每页显示条数
     * @return 文档对象集合
     */
    @Select("select * from document limit #{start},#{pageSize}")
    public List<Document> page(Integer start, Integer pageSize);

    /**
     * 添加数据
     *
     * @param document 文档对象
     */
    @Options(useGeneratedKeys = true, keyProperty = "document_id")
    @Insert("insert into document(document_url,document_ocr_url,document_name,document_type,all_info) " +
            "values(#{document_url},#{document_ocr_url},#{document_name},#{document_type},#{all_info})")
    public void insert(Document document);

    /**
     * 根据id删除数据
     *
     * @param document_id 文档id
     */
    @Delete("delete from document where document_id = #{document_id}")
    public Integer delete(Integer document_id);

    /**
     * 更新数据
     *
     * @param document 文档对象
     */
    public Integer update(Document document);

    /**
     * 查询所有数据
     *
     * @return 文档对象集合
     */
    @Select("select * from document")
    public List<Document> list();

    @Select("select * from document where document_id = #{document_id}")
    public Document getById(Integer document_id);

    @Select("select * from document where document_name = #{document_name}")
    public Document getByName(String document_name);

    @Select("select * from document where document_type = #{document_type}")
    public List<Document> getByType(String document_type);

    @Select("select distinct document_type from document")
    public List<String> getAllTypes();
}
