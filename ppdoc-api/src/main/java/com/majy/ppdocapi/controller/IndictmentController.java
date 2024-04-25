package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Indictment;
import com.majy.ppdocapi.entity.po.Invoice;
import com.majy.ppdocapi.service.IndictmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/indictments")
public class IndictmentController extends OcrController
{
    @Autowired
    private IndictmentService indictmentService;

    @PostMapping("/upload")
    public Result invoiceOcr(MultipartFile file) throws IOException
    {
        // 调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        //调用Service中的方法，获取提取到的信息
        Map<String,String> dataMap = indictmentService.file2StringStringMap(file);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult,dataMap);
    }

    @PostMapping
    public Result save(@RequestBody Indictment indictment)
    {
        log.info("新增起诉状文档记录:{}",indictment);
        indictmentService.add(indictment);
        return Result.createSuccess();
    }

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize)
    {
        log.info("进行分页查询，参数为:当前页数{} 每页条数{}", page, pageSize);

        PageBean pageBean = indictmentService.page(page, pageSize);
        log.info("查询结果为:共有数据{}条", pageBean.getTotal());
        return Result.success(pageBean);
    }

    @GetMapping("/{indictment_id}")
    public Result getByIndictmentId(@PathVariable("indictment_id") Integer indictment_id)
    {
        return Result.success(indictmentService.getByIndictmentId(indictment_id));
    }

    @DeleteMapping("/{documentId}")
    public Result delete(@PathVariable Integer documentId){
        log.info("根据文档id删除起诉状文档:{}",documentId);
        //调用service，根据文档ID删除发票
        indictmentService.delete(documentId);
        return Result.deleteSuccess();
    }
}
