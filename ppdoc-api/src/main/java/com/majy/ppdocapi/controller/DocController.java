package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
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

        //调用Service中的方法，获取提取到的信息
        Map<String, String> dataMap = documentService.file2StringStringMap(file);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult, dataMap);
    }

    @PostMapping
    public Result save(@RequestBody Document document)
    {
        log.info("新增文档记录:{}",document);
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
        //调用service删除文档
        documentService.delete(id);
        return Result.success();
    }
}
