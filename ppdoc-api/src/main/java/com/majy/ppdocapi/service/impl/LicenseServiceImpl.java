package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.License;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.LicenseMapper;
import com.majy.ppdocapi.service.LicenseService;
import com.majy.ppdocapi.utils.OCRUtils.LicenseOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.PaddleOcrUtils;
import com.majy.ppdocapi.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class LicenseServiceImpl implements LicenseService
{
    @Autowired
    LicenseMapper licenseMapper;
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    LicenseOcrUtils licenseOcrUtils;
    @Autowired
    OSSUtils ossUtils;

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
    public Result deleteByDocumentId(Integer documentId)
    {
        if (documentMapper.getById(documentId).getDocument_url() != null)
        {
            ossUtils.deleteFile(documentMapper.getById(documentId).getDocument_url());
        }
        Integer rowsAffected = licenseMapper.deleteByDocumentId(documentId);
        documentMapper.delete(documentId);
        if (rowsAffected == 0)
        {
            return Result.deleteFailure();
        }
        return Result.deleteSuccess();
    }

    @Override
    public Result add(License license)
    {
        Document document = new Document(null, license.getLicense_url(), license.getFile_name(), "营业执照", license.getAll_info());
        documentMapper.insert(document);
        license.setDocument_id(document.getDocument_id());
        licenseMapper.insert(license);
        return Result.createSuccess();
    }

    @Override
    public Map<String, String> file2StringStringMap(MultipartFile file)
    {
        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        return licenseOcrUtils.getStringStringMap(jsons);
    }

    @Override
    public Result getByLicenseId(Integer licenseId)
    {
        return Result.selectSuccess(licenseMapper.getByLicenseId(licenseId));
    }

    @Override
    public Result update(License license)
    {
        Integer rowsAffected = licenseMapper.update(license);
        if (rowsAffected == 0)
        {
            return Result.updateFailure();
        }
        return Result.updateSuccess();
    }
}
