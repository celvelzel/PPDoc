package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itextpdf.text.DocumentException;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.Indictment;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.IndictmentMapper;
import com.majy.ppdocapi.service.IndictmentService;
import com.majy.ppdocapi.utils.OCRUtils.DocOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.IndictmentOcrUtils;
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
public class IndictmentServiceImpl implements IndictmentService
{
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private IndictmentMapper indictmentMapper;
    @Autowired
    private IndictmentOcrUtils indictmentOcrUtils;
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
        //执行查询，获取分页结果
        Page<Indictment> indictmentPage = (Page<Indictment>)indictmentMapper.list();
        //封装PageBean对象
        return new PageBean(indictmentPage.getTotal(), indictmentPage.getResult());
    }

    @Override
    public Result delete(Integer documentId)
    {
        if (documentMapper.getById(documentId).getDocument_url() != null)
        {
            ossUtils.deleteFile(documentMapper.getById(documentId).getDocument_url());
        }
        Integer rowsAffected =  indictmentMapper.deleteByDocumentId(documentId);
        documentMapper.delete(documentId);;
        if (rowsAffected == 0)
        {
            return Result.deleteFailure();
        }
        return Result.deleteSuccess();
    }

    @Override
    public Result add(Indictment indictment)
    {
        Document newDocument = new Document(indictment.getDocument_id(), indictment.getIndictment_url(),indictment.getIndictment_ocr_url(), indictment.getFile_name(),"起诉状",indictment.getAll_info());
        documentMapper.insert(newDocument);
        indictment.setDocument_id(newDocument.getDocument_id());
        indictmentMapper.insert(indictment);
        return Result.createSuccess();
    }

    @Override
    public Map<String, String> file2StringStringMap(MultipartFile file)
    {
        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        return indictmentOcrUtils.getStringStringMap(jsons);
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
                Map<String, String> dataMap = indictmentOcrUtils.getStringStringMap(ocrResult);

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
                Map<String, String> dataMap = indictmentOcrUtils.getStringStringMap(pdfText);
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
    public Result getByIndictmentId(Integer indictmentId)
    {
        return Result.selectSuccess(indictmentMapper.getByIndictmentId(indictmentId));
    }

    @Override
    public Result update(Indictment indictment)
    {
        Integer rowsAffected = indictmentMapper.update(indictment);
        if(rowsAffected == 0)
        {
            return Result.updateFailure();
        }
        return Result.updateSuccess();
    }
}
