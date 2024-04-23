package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.service.DocumentService;
import com.majy.ppdocapi.utils.OCRUtils.DocOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.PaddleOcrUtils;
import com.majy.ppdocapi.utils.PdfToEditablePdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService
{
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    PdfToEditablePdfUtils pdfToEditablePdfUtils;

    @Override
    public PageBean page(Integer start, Integer pageSize)
    {
        //设置分页参数
        PageHelper.startPage(start, pageSize);
        //执行查询
        List<Document> documentList = documentMapper.list();
        Page<Document> pageHelper = (Page<Document>) documentList;
        //封装pageBean对象
        PageBean pageBean = new PageBean(pageHelper.getTotal(), pageHelper.getResult());
        return pageBean;
    }

    @Override
    public void delete(Integer id)
    {
        documentMapper.delete(id);
    }

    @Override
    public void add(Document document)
    {
        documentMapper.insert(document);
    }

    @Override
    public Map<String, String> handlePdfFile(MultipartFile file)
    {
        try
        {
            //若pdf没有可复制的文本
            if (! pdfToEditablePdfUtils.pdfCopyableChecker(file.getInputStream()))
            {
                // pdf识别，提取文字
                List jsons = PaddleOcrUtils.pdfToOcrText(file);
                // 根据识别文本提取信息
                return (Map<String, String>) DocOcrUtils.getStringStringMap(jsons);
            }
            else
            {
                //若pdf有可复制的文本，直接使用pdf中的文本
                String pdfText = PdfToEditablePdfUtils.getPdfText(file.getInputStream());
                return DocOcrUtils.getStringStringMap(pdfText);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getAllTypes()
    {
        return documentMapper.getAllTypes();
    }
}
