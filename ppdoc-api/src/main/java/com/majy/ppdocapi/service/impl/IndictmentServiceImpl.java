package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.Indictment;
import com.majy.ppdocapi.entity.po.Invoice;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.IndictmentMapper;
import com.majy.ppdocapi.service.IndictmentService;
import com.majy.ppdocapi.utils.OCRUtils.IndictmentOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.PaddleOcrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndictmentServiceImpl implements IndictmentService
{
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private IndictmentMapper indictmentMapper;
    @Autowired
    private IndictmentOcrUtils indictmentOcrUtils;

    @Override
    public PageBean page(Integer start, Integer pageSize)
    {
        //设置分页参数
        PageHelper.startPage(start, pageSize);
        //执行查询，获取分页结果
        Page<Indictment> indictmentPage = (Page<Indictment>)indictmentMapper.list();
        //封装PageBean对象
        return new PageBean(indictmentPage.getTotal(), indictmentPage.getResult());
    }

    @Override
    public void delete(Integer documentId)
    {
        indictmentMapper.deleteByDocumentId(documentId);
        documentMapper.delete(documentId);;
    }

    @Override
    public void add(Indictment indictment)
    {
        Document newDocument = new Document(indictment.getDocument_id(), indictment.getIndictment_url(),indictment.getFile_name(),"起诉状",indictment.getAll_info());
        documentMapper.insert(newDocument);
        indictment.setDocument_id(newDocument.getDocument_id());
        indictmentMapper.insert(indictment);
    }

    @Override
    public Map<String, String> file2StringStringMap(MultipartFile file)
    {
        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        return indictmentOcrUtils.getStringStringMap(jsons);
    }
}
