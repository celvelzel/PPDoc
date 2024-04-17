package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.Invoice;
import com.majy.ppdocapi.entity.po.License;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.InvoiceMapper;
import com.majy.ppdocapi.mapper.LicenseMapper;
import com.majy.ppdocapi.service.LicenseService;
import com.majy.ppdocapi.utils.LicenseOcrUtils;
import com.majy.ppdocapi.utils.PaddleOcrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class LicenseServiceImpl implements LicenseService
{
    @Autowired
    LicenseMapper licenseMapper;
    @Autowired
    DocumentMapper documentMapper;

    @Override
    public PageBean page(Integer start, Integer pageSize)
    {
        //设置分页参数
        PageHelper.startPage(start, pageSize);
        //执行查询
        List<License> licenseList = licenseMapper.list();
        Page<License> pageHelper = (Page<License>) licenseList;
        //封装pageBean对象
        PageBean pageBean = new PageBean(pageHelper.getTotal(), pageHelper.getResult());
        return pageBean;
    }

    @Override
    public void delete(Integer documentiId)
    {
        licenseMapper.deleteByDocumentId(documentiId);
        documentMapper.delete(documentiId);
    }

    @Override
    public void add(License license)
    {
        Document document = new Document(null, license.getLicense_url(), license.getFile_name(), "营业执照", license.getAll_info());
        documentMapper.insert(document);
        license.setDocument_id(document.getDocument_id());
        licenseMapper.insert(license);
    }

    @Override
    public Map<String, String> file2StringStringMap(MultipartFile file)
    {
        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        return LicenseOcrUtils.getStringStringMap(jsons);
    }
}
