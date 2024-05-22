package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itextpdf.text.DocumentException;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.IdCard;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.IdCardMapper;
import com.majy.ppdocapi.service.IdCardService;
import com.majy.ppdocapi.utils.OCRUtils.IdCardOcrUtils;
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
public class IdCardServiceImpl implements IdCardService
{
    @Autowired
    IdCardMapper idCardMapper;
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    OSSUtils ossUtils;
    @Autowired
    PdfToEditablePdfUtils pdfToEditablePdfUtils;

    @Value("${file.pdf.path}")
    private String pdfFolder;

    @Override
    public Result getById(Integer id)
    {
        if (idCardMapper.getByIdCardId(id) != null)
        {
            return Result.selectSuccess(idCardMapper.getByIdCardId(id));
        }
        return Result.selectFailure();
    }

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
    public Result delete(Integer documentId)
    {
        if (documentMapper.getById(documentId).getDocument_url() != null)
        {
            ossUtils.deleteFile(documentMapper.getById(documentId).getDocument_url());
        }
        Integer rowsAffected = idCardMapper.deleteByDocumentId(documentId);
        documentMapper.delete(documentId);
        if (rowsAffected == 0)
        {
            return Result.deleteFailure();
        }
        return Result.deleteSuccess();
    }

    @Override
    public Result add(IdCard idCard)
    {
        Document document = new Document(null, idCard.getId_card_url(), idCard.getFile_name(), "身份证", idCard.getAll_info());
        documentMapper.insert(document);
        idCard.setDocument_id(document.getDocument_id());
        idCardMapper.insert(idCard);
        return Result.createSuccess();
    }

    @Override
    public Result handlePdfFile(MultipartFile file)
    {
        try
        {
            InputStream inputStream = file.getInputStream();
            //对身份证不做是否可复制的判断，直接进行OCR和双层pdf转换

            // pdf识别，提取文字
            List ocrResult = PaddleOcrUtils.pdfToOcrText(file);
            // 根据识别文本提取信息
            Map<String, String> dataMap = IdCardOcrUtils.getStringStringMap(ocrResult);

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
        } catch (IOException | DocumentException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Result handleImageFile(MultipartFile file)
    {
        try
        {
            InputStream inputStream = file.getInputStream();

            // 图片识别，提取文字
            List<MultipartFile> files = new java.util.ArrayList<>();
            files.add(file);
            List<List> ocrResult = PaddleOcrUtils.imageList2OcrText(files);
            // 根据识别文本提取信息
            Map<String, String> dataMap = IdCardOcrUtils.getStringStringMap(ocrResult);

            // 生成双层pdf
            // 上传的图片文件存储路径
            String originPdfPath = pdfFolder + System.getProperty("file.separator") + file.getOriginalFilename();
            // 最终生成的双层pdf文件存储路径
            String finalPdfPath = pdfFolder + System.getProperty("file.separator") + "FinalDpdf.pdf";
            // 将上传的图片文件保存到本地
            file.transferTo(new File(originPdfPath));
            // 调用工具类生成双层pdf
            pdfToEditablePdfUtils.image2Dpdf(new File(originPdfPath), ocrResult, finalPdfPath);
            // 上传双层pdf文件到对象存储，并返回URL
            URL url = ossUtils.uploadFile(new File(finalPdfPath));

            // 关闭文件流
            inputStream.close();
            return Result.getSuccessResult(url, dataMap, "converted");
        } catch (IOException | DocumentException e)
        {
            throw new RuntimeException(e);
        }
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
        List<List> jsons = PaddleOcrUtils.imageList2OcrText(files);

        // 对识别结果进行信息提取，转换为Map类型，并返回给前端。
        return IdCardOcrUtils.getStringStringMap(jsons);
    }

    @Override
    public Result update(IdCard idCard)
    {
        Integer rowsAffected = idCardMapper.update(idCard);
        if (rowsAffected == 0)
        {
            return Result.updateFailure();
        }
        return Result.updateSuccess();
    }
}
