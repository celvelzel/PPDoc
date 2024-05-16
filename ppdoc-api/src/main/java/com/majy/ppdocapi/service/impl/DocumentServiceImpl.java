package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itextpdf.text.DocumentException;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.service.DocumentService;
import com.majy.ppdocapi.utils.OCRUtils.DocOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.PaddleOcrUtils;
import com.majy.ppdocapi.utils.OSSUtils;
import com.majy.ppdocapi.utils.PdfToEditablePdfUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService
{
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    PdfToEditablePdfUtils pdfToEditablePdfUtils;
    @Autowired
    OSSUtils ossUtils;

    @Value("${file.pdf.path}")
    private String pdfFolder;
//    private String pdfFolder = "<LOCAL_PATH_REDACTED>";

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
    public Result delete(Integer id)
    {
        //删除对象存储中的对应文件
        if (documentMapper.getById(id).getDocument_url() != null)
        {
            ossUtils.deleteFile(documentMapper.getById(id).getDocument_url());
        }
        // 尝试执行删除操作
        Integer rowsAffected = documentMapper.delete(id);
        // 检查是否成功删除
        if (rowsAffected > 0)
        {
            return Result.deleteSuccess();
        } else
        {
            return Result.deleteFailure();
        }
    }

    @Override
    public Result add(Document document)
    {
        documentMapper.insert(document);
        return Result.createSuccess();
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
                Map<String, String> dataMap = DocOcrUtils.getStringStringMap(ocrResult);

                // 生成双层pdf
                // 上传的pdf文件存储路径
                String originPdfPath = pdfFolder + System.getProperty("file.separator") + "originPdf.pdf";
                // 最终生成的双层pdf文件存储路径
                String finalPdfPath = pdfFolder + System.getProperty("file.separator") + "FinalDpdf.pdf";
                // 将上传的pdf文件保存到本地
                file.transferTo(new File(originPdfPath));
                // 调用工具类生成双层pdf
                pdfToEditablePdfUtils.pdf2Dpdf(new File(originPdfPath), ocrResult, finalPdfPath);
                // 上传双层pdf文件到对象存储，并返回URL
                URL url = ossUtils.uploadFile(new File(finalPdfPath));

                // 关闭文件流
                inputStream.close();
                return Result.getSuccessResult(url, dataMap);
            } else
            {
                //若pdf有可复制的文本，直接使用pdf中的文本
                String pdfText = PdfToEditablePdfUtils.getPdfText(file.getInputStream());
                Map<String, String> dataMap = DocOcrUtils.getStringStringMap(pdfText);
                URL fileUrl = ossUtils.uploadFile(file);

                // 关闭文件流
                inputStream.close();
                return Result.getSuccessResult(fileUrl, dataMap);
            }
        } catch (IOException | DocumentException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result getAllTypes()
    {
        return Result.success(documentMapper.getAllTypes());
    }

    @Override
    public Result update(Document document)
    {
        Integer rowsAffected = documentMapper.update(document);
        if (rowsAffected == 0)
        {
            return Result.updateFailure();
        }
        return Result.updateSuccess();
    }

    @Override
    public Result getById(Integer documentId)
    {
        if (documentMapper.getById(documentId) != null)
        {
            return Result.selectSuccess(documentMapper.getById(documentId));
        }
        return Result.selectFailure();
    }
}
