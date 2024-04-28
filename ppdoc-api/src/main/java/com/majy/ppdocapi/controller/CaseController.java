package com.majy.ppdocapi.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Case;
import com.majy.ppdocapi.service.CaseService;
import com.majy.ppdocapi.service.IdCardService;
import com.majy.ppdocapi.service.IndictmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/cases")
@Slf4j
public class CaseController extends OcrController
{
    @Autowired
    private CaseService caseService;


    @PostMapping
    public Result add(@RequestBody Case caseModel) {
        log.info("新增案件记录:{}", caseModel);
        return caseService.add(caseModel);
    }

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("进行分页查询，参数为:当前页数{} 每页条数{}", page, pageSize);

        PageBean pageBean = caseService.page(page, pageSize);
        log.info("查询结果为:共有数据{}条", pageBean.getTotal());
        return Result.success(pageBean);
    }

    @GetMapping("/{case_id}")
    public Result getByCaseId(@PathVariable("case_id") Integer case_id)
    {
        return Result.success(caseService.getById(case_id));
    }

    @GetMapping("/graph/{case_id}")
    public Result getGraphByCaseId(@PathVariable("case_id") Integer case_id)
    {
        return caseService.getGraphByCaseId(case_id);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("根据案件id删除案件:{}", id);
        // 调用service删除案件
        return caseService.delete(id);
    }
}
