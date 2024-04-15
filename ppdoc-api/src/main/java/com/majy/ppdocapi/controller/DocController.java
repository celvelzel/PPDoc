package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.pojo.Document;
import com.majy.ppdocapi.pojo.PageBean;
import com.majy.ppdocapi.pojo.Result;
import com.majy.ppdocapi.service.DocumentService;
import com.majy.ppdocapi.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/docs")
@Slf4j
public class DocController extends OcrController
{
    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public Result DocOcr(MultipartFile file) throws IOException
    {
        //调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        Map<String, String> dataMap = DocOcrUtils.getStringStringMap(jsons);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult, dataMap);
    }

    @PostMapping
    public Result add(@RequestBody Document document)
    {
        log.info("修改文档信息:{}",document);
        documentService.add(document);
        return Result.success();
    }

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize)
    {
        log.info("进行分页查询，参数为:当前页数{} 每页条数{}", page, pageSize);

        PageBean pageBean = documentService.page(page, pageSize);
        log.info("查询结果为:共有数据{}条", pageBean.getTotal());
        return Result.success(pageBean);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("根据id删除文档:{}",id);
        //调用service删除部门
        documentService.delete(id);
        return Result.success();
    }

}
