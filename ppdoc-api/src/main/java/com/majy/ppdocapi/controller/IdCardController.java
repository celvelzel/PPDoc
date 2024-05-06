package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.IdCard;
import com.majy.ppdocapi.service.IdCardService;
import com.majy.ppdocapi.entity.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/idcards")
public class IdCardController extends OcrController
{
    @Autowired
    private IdCardService idCardService;

    @PostMapping("/pdf/upload")
    public Result idCardPdfOcr(MultipartFile file) throws IOException
    {
        // 调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        Map<String, String> dataMap = idCardService.pdfFile2StringStringMap(file);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult, dataMap);
    }

    @PostMapping("/image/upload")
    public Result idCardImageOcr(MultipartFile file) throws IOException
    {
        //调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        Map<String, String> dataMap = idCardService.imageFile2StringStringMap(file);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult, dataMap);
    }

    @PostMapping
    public Result save(@RequestBody IdCard idCard)
    {
        log.info("新增身份证文档记录:{}",idCard);
        idCardService.add(idCard);
        return Result.createSuccess();
    }

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize)
    {
        log.info("进行分页查询，参数为:当前页数{} 每页条数{}", page, pageSize);

        PageBean pageBean = idCardService.page(page, pageSize);
        log.info("查询结果为:共有数据{}条", pageBean.getTotal());
        return Result.success(pageBean);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Integer id)
    {
        return idCardService.getById(id);
    }

    @PutMapping
    public Result update(@RequestBody IdCard idCard)
    {
        log.info("更新身份证文档记录:{}",idCard);
        return idCardService.update(idCard);
    }

    @DeleteMapping("/{documentId}")
    public Result delete(@PathVariable Integer documentId){
        log.info("根据文档id删除身份证文档:{}",documentId);
        //调用service，根据文档ID删除身份证记录
        idCardService.delete(documentId);
        return Result.deleteSuccess();
    }
}
