package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Invoice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InvoiceMapper
{
    /**
     * 添加数据
     * @param invoice    发票对象
     */
    @Options(useGeneratedKeys = true, keyProperty = "invoice_id")
    @Insert("insert into invoice(file_name,seller_name,all_info,invoice_url,invoice_date,purchaser_name,project_name,document_id,invoice_code,invoice_number,invoice_amount) values(#{file_name},#{seller_name},#{all_info},#{invoice_url},#{invoice_date},#{purchaser_name},#{project_name},#{document_id},#{invoice_code},#{invoice_number},#{invoice_amount})")
    public void insert(Invoice invoice);

    /**
     * 根据发票id删除数据
     * @param invoice_id    发票id
     */
    @Delete("delete from invoice where invoice_id = #{invoice_id}")
    public Integer delete(Integer invoice_id);

    /**
     * 更新数据
     * @param invoice    发票对象
     */
    public Integer update(Invoice invoice);

    /**
     * 查询所有数据
     */
    @Select("select * from invoice")
    public List<Invoice> list();


    /**
     * 根据发票id查询数据
     * @param invoice_id    发票id
     * @return
     */
    @Select("select * from invoice where invoice_id = #{invoice_id}")
    public Invoice getByInvoiceId(Integer invoice_id);

    /**
     * 根据document_id查询发票数据
     * @param document_id    document_id
     * @return
     */
    @Select("select * from invoice where document_id = #{document_id}")
    public Invoice getInvoiceByDocumentId(Integer document_id);

    /**
     * 根据document_id删除发票数据
     * @param document_id    document_id
     */
    @Delete("delete from invoice where document_id = #{document_id}")
    public Integer deleteByDocumentId(Integer document_id);

    /**
     * 根据document_id更新发票数据
     * @param invoice    发票对象
     */
    @Update("update invoice set file_name= #{file_name},seller_name = #{seller_name},all_info = #{all_info},invoice_url = #{invoice_url},invoice_date = #{invoice_date},purchaser_name = #{purchaser_name},project_name = #{project_name},document_id = #{document_id},invoice_code = #{invoice_code},invoice_amount = #{invoice_amount} where document_id = #{document_id}")
    public Integer updateByDocumentId(Invoice invoice);

}
