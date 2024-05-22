package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itextpdf.text.DocumentException;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.License;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.LicenseMapper;
import com.majy.ppdocapi.service.LicenseService;
import com.majy.ppdocapi.utils.OCRUtils.DocOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.LicenseOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.PaddleOcrUtils;
import com.majy.ppdocapi.utils.OSSUtils;
import com.majy.ppdocapi.utils.PdfToEditablePdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
    @Autowired
    PdfToEditablePdfUtils pdfToEditablePdfUtils;

    @Value("${file.pdf.path}")
    private String pdfFolder;

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
        Document document = new Document(null, license.getLicense_url(), license.getLicense_ocr_url(), license.getFile_name(), "营业执照", license.getAll_info());
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
    public Result handlePdfFile(MultipartFile file)
    {
        try
        {
            InputStream inputStream = file.getInputStream();
            //若pdf没有可复制的文本,进行转换
            if (!pdfToEditablePdfUtils.pdfCopyableChecker(inputStream))
            {
                // pdf识别，提取文字
                List ocrResult = PaddleOcrUtils.pdfToOcrText(file);
                // 根据识别文本提取信息
                Map<String, String> dataMap = licenseOcrUtils.getStringStringMap(ocrResult);

                // 生成双层pdf
                // 上传的pdf文件存储路径
                String originPdfPath = pdfFolder + System.getProperty("file.separator") + "originPdf.pdf";
                // 最终生成的双层pdf文件存储路径
                String finalPdfPath = pdfFolder + System.getProperty("file.separator") + "FinalDpdf.pdf";
                // 合成的识别结果pdf文件存储路径
                String ocrPdfPath = pdfFolder + System.getProperty("file.separator") + "FinalOcrPdf.pdf";
                // 将上传的pdf文件保存到本地
                file.transferTo(new File(originPdfPath));
                // 调用工具类生成双层pdf
                pdfToEditablePdfUtils.pdf2Dpdf(new File(originPdfPath), ocrResult, finalPdfPath, ocrPdfPath);
                // 上传双层pdf文件到对象存储，并返回URL
                URL url = ossUtils.uploadFile(new File(finalPdfPath));
                URL ocrPdfUrl = ossUtils.uploadFile(new File(ocrPdfPath));

                // 关闭文件流
                inputStream.close();
                return Result.getSuccessResult(url, ocrPdfUrl, dataMap, "converted");
            } else
            {
                //若pdf有可复制的文本，直接使用pdf中的文本
                String pdfText = PdfToEditablePdfUtils.getPdfText(file.getInputStream());
                Map<String, String> dataMap = licenseOcrUtils.getStringStringMap(pdfText);
                URL fileUrl = ossUtils.uploadFile(file);

                // 关闭文件流
                inputStream.close();
                return Result.getSuccessResult(fileUrl, dataMap, "unconverted");
            }
        } catch (IOException | DocumentException e)
        {
            throw new RuntimeException(e);
        }
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
