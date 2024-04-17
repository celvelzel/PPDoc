package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.Invoice;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.InvoiceMapper;
import com.majy.ppdocapi.service.InvoiceService;
import com.majy.ppdocapi.utils.InvoiceOcrUtils;
import com.majy.ppdocapi.utils.PaddleOcrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceServiceImpl implements InvoiceService
{
    @Autowired
    InvoiceMapper invoiceMapper;
    @Autowired
    DocumentMapper documentMapper;

    @Override
    public PageBean page(Integer start, Integer pageSize)
    {
        //设置分页参数
        PageHelper.startPage(start, pageSize);
        //执行查询
        List<Invoice> invoiceList = invoiceMapper.list();
        Page<Invoice> pageHelper = (Page<Invoice>) invoiceList;
        //封装pageBean对象
        PageBean pageBean = new PageBean(pageHelper.getTotal(), pageHelper.getResult());
        return pageBean;
    }

    @Override
    public void delete(Integer documentId)
    {
        invoiceMapper.deleteByDocumentId(documentId);
        documentMapper.delete(documentId);
    }

    @Override
    public void add(Invoice invoice)
    {
        Document document = new Document(null, invoice.getInvoice_url(), invoice.getFile_name(), "发票", invoice.getAll_info());
        documentMapper.insert(document);
        invoice.setDocument_id(document.getDocument_id());
        invoiceMapper.insert(invoice);
    }

    @Override
    public Map<String, String> file2StringStringMap(MultipartFile file)
    {
        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        return InvoiceOcrUtils.getStringStringMap(jsons);
    }
}
