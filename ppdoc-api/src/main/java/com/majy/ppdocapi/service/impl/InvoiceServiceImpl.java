package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itextpdf.text.DocumentException;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.Invoice;
import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.mapper.InvoiceMapper;
import com.majy.ppdocapi.service.InvoiceService;
import com.majy.ppdocapi.utils.OCRUtils.DocOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.InvoiceOcrUtils;
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
public class InvoiceServiceImpl implements InvoiceService
{
    @Autowired
    InvoiceMapper invoiceMapper;
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    InvoiceOcrUtils invoiceOcrUtils;
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
        List<Invoice> invoiceList = invoiceMapper.list();
        Page<Invoice> pageHelper = (Page<Invoice>) invoiceList;
        //封装pageBean对象
        PageBean pageBean = new PageBean(pageHelper.getTotal(), pageHelper.getResult());
        return pageBean; //返回PageBean对象
    }

    @Override
    public Result deleteByDocumentId(Integer documentId)
    {
        if (null != documentMapper.getById(documentId).getDocument_url())
        {
            ossUtils.deleteFile(documentMapper.getById(documentId).getDocument_url());
        }
        Integer rowsAffected = invoiceMapper.deleteByDocumentId(documentId);
        documentMapper.delete(documentId);
        if (rowsAffected == 0)
        {
            return Result.deleteFailure();
        }
        return Result.deleteSuccess();
    }

    @Override
    public Result add(Invoice invoice)
    {
        Document document = new Document(null, invoice.getInvoice_url(), invoice.getFile_name(), "发票", invoice.getAll_info());
        documentMapper.insert(document);
        invoice.setDocument_id(document.getDocument_id());
        invoiceMapper.insert(invoice);
        return Result.createSuccess();
    }

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
                Map<String, String> dataMap = invoiceOcrUtils.getStringStringMap(ocrResult);

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
                Map<String, String> dataMap = invoiceOcrUtils.getStringStringMap(pdfText);
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
    public Map<String, String> file2StringStringMap(MultipartFile file)
    {
        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        return invoiceOcrUtils.getStringStringMap(jsons);
    }

    @Override
    public Result getByInvoiceId(Integer invoiceId)
    {
        return Result.selectSuccess(invoiceMapper.getByInvoiceId(invoiceId));
    }

    @Override
    public Result update(Invoice invoice)
    {
        Integer rowsAffected = invoiceMapper.update(invoice);
        if (rowsAffected == 0)
        {
            return Result.updateFailure();
        }
        return Result.updateSuccess();
    }
}
