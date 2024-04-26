package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Case;
import com.majy.ppdocapi.entity.po.IdCard;
import com.majy.ppdocapi.mapper.CaseMapper;
import com.majy.ppdocapi.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseServiceImpl implements CaseService
{
    @Autowired
    private CaseMapper caseMapper;

    @Override
    public PageBean page(Integer start, Integer pageSize)
    {
        //设置分页参数
        PageHelper.startPage(start, pageSize);
        //执行查询
        List<Case> caseList = caseMapper.getAllCases();
        Page<Case> pageHelper = (Page<Case>) caseList;
        //封装pageBean对象
        PageBean pageBean = new PageBean(pageHelper.getTotal(), pageHelper.getResult());
        return pageBean;
    }

    @Override
    public Result delete(Integer case_id)
    {
        // 尝试执行删除操作
        long rowsAffected = caseMapper.deleteCaseById(case_id);
        // 检查是否成功删除
        if (rowsAffected > 0) {
            return Result.deleteSuccess();
        } else {
            return Result.deleteFailure();
        }
    }

    @Override
    public Result add(Case caseModel)
    {
        caseMapper.insert(caseModel);
        return Result.createSuccess();
    }

    @Override
    public Result update(Case caseModel)
    {
        caseMapper.update(caseModel);
        return Result.updateSuccess();
    }

    @Override
    public Result getById(Integer case_id)
    {
        caseMapper.getCaseById(case_id);
        return Result.selectSuccess();
    }
}
