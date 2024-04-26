package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.License;
import com.majy.ppdocapi.service.LicenseService;
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
@RequestMapping("/api/licenses")
public class LicenseController extends OcrController
{
    @Autowired
    LicenseService licenseService;

    @PostMapping("/upload")
    public Result licenseOcr(MultipartFile file) throws IOException
    {
        //调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        Map<String, String> dataMap = licenseService.file2StringStringMap(file);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult,dataMap);
    }

    @PostMapping
    public Result save(@RequestBody License license)
    {
        log.info("新增营业执照文档记录:{}",license);
        licenseService.add(license);
        return Result.createSuccess();
    }

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize)
    {
        log.info("进行分页查询，参数为:当前页数{} 每页条数{}", page, pageSize);

        PageBean pageBean = licenseService.page(page, pageSize);
        log.info("查询结果为:共有数据{}条", pageBean.getTotal());
        return Result.success(pageBean);
    }

//    @GetMapping("/{license_id}")
//    public Result getByLicenseId(@PathVariable("license_id") Integer license_id)
//    {
//        return Result.success(licenseService.getByLicenseId(license_id));
//    }

    @DeleteMapping("/{documentId}")
    public Result delete(@PathVariable Integer documentId){
        log.info("根据文档id删除营业执照文档:{}",documentId);
        //调用service删除文档
        licenseService.delete(documentId);
        return Result.deleteSuccess();
    }
}
