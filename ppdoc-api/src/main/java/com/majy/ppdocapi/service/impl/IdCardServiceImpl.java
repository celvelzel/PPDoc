package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.IdCard;
import com.majy.ppdocapi.entity.po.Invoice;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.IdCardMapper;
import com.majy.ppdocapi.mapper.InvoiceMapper;
import com.majy.ppdocapi.service.IdCardService;
import com.majy.ppdocapi.utils.IdCardOcrUtils;
import com.majy.ppdocapi.utils.PaddleOcrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class IdCardServiceImpl implements IdCardService
{
    @Autowired
    IdCardMapper idCardMapper;
    @Autowired
    DocumentMapper documentMapper;

    @Override
    public PageBean page(Integer start, Integer pageSize)
    {
        //设置分页参数
        PageHelper.startPage(start, pageSize);
        //执行查询
        List<IdCard> idCardList = idCardMapper.list();
        Page<IdCard> pageHelper = (Page<IdCard>) idCardList;
        //封装pageBean对象
        PageBean pageBean = new PageBean(pageHelper.getTotal(), pageHelper.getResult());
        return pageBean;
    }

    @Override
    public void delete(Integer documentId)
    {
        idCardMapper.deleteByDocumentId(documentId);
        documentMapper.delete(documentId);
    }

    @Override
    public void add(IdCard idCard)
    {
        Document document = new Document(null, idCard.getId_card_url(), idCard.getFile_name(), "身份证", idCard.getAll_info());
        documentMapper.insert(document);
        idCard.setDocument_id(document.getDocument_id());
        idCardMapper.insert(idCard);
    }

    @Override
    public Map<String, String> pdfFile2StringStringMap(MultipartFile file)
    {
        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        return IdCardOcrUtils.getStringStringMap(jsons);
    }

    @Override
    public Map<String, String> imageFile2StringStringMap(MultipartFile file) throws IOException
    {
        //使用PaddleOcrUtils工具对上传的文件进行OCR处理，并获取识别结果。
        List<MultipartFile> files = new java.util.ArrayList<>();
        files.add(file);
        List<List> jsons = PaddleOcrUtils.getOcrText(files);

        // 对识别结果进行信息提取，转换为Map类型，并返回给前端。
        return IdCardOcrUtils.getStringStringMap(jsons);
    }
}
