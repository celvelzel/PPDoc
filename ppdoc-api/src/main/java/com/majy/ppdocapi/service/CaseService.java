package com.majy.ppdocapi.service;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Case;

public interface CaseService
{
    PageBean page(Integer start, Integer pageSize);
    Result delete(Integer case_id);
    Result add(Case caseModel);
    Result update(Case caseModel);
    Result getById(Integer case_id);
}
