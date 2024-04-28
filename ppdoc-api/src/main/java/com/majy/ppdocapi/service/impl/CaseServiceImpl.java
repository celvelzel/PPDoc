package com.majy.ppdocapi.service.impl;

import cn.hutool.core.util.IdcardUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.CaseDetail;
import com.majy.ppdocapi.entity.dto.GraphInfo;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.*;
import com.majy.ppdocapi.mapper.*;
import com.majy.ppdocapi.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CaseServiceImpl implements CaseService
{
    @Autowired
    private CaseMapper caseMapper;

    @Autowired
    private IdCardMapper idCardMapper;

    @Autowired
    private IndictmentMapper indictmentMapper;

    @Autowired
    private LicenseMapper licenseMapper;

    @Autowired
    private InvoiceMapper invoiceMapper;

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
        if (rowsAffected > 0)
        {
            return Result.deleteSuccess();
        }
        else
        {
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

    @Override
    public Result getGraphByCaseId(Integer case_id)
    {
        Indictment indictment = new Indictment();
        Map<String, IdCard> idcards = new HashMap<String, IdCard>();
        Map<String, License> licenses = new HashMap<String, License>();
        List<Invoice> invoices = new ArrayList<Invoice>();

        Case caseObj = caseMapper.getCaseById(case_id);
        if (caseObj.getIndictment_id() != null)
        {
            indictment = indictmentMapper.getByIndictmentId(caseObj.getIndictment_id());
        }

        if (caseObj.getPlaintiff_id() != null)
        {
            if (IdcardUtil.isValidCard(caseObj.getPlaintiff_id())) // 身份证有效则为自然人
            {
                idcards.put("原告", idCardMapper.getByIdCardId(caseObj.getPlaintiff_id_card_id()));
            }
            else
            {
                licenses.put("原告", licenseMapper.getByLicenseId(caseObj.getPlaintiff_license_id()));
            }
        }
        else
        {
            log.info("原告id为空");
        }

        if (caseObj.getDefendant_id() != null)
        {
            if (IdcardUtil.isValidCard(caseObj.getDefendant_id())) // 身份证有效则为自然人
            {
                idcards.put("被告", idCardMapper.getByIdCardId(caseObj.getDefendant_id_card_id()));
            }
            else
            {
                licenses.put("被告", licenseMapper.getByLicenseId(caseObj.getDefendant_license_id()));
            }
        }
        else
        {
            log.info("被告id为空");
        }


        if (caseObj.getRelated_invoice_id() != null)
        {
            invoices.add(invoiceMapper.getByInvoiceId(caseObj.getRelated_invoice_id()));
        }
        CaseDetail caseDetail = new CaseDetail(caseObj, indictment, idcards, licenses, invoices);
        log.info("案件详细信息：{}", caseDetail);
        GraphInfo graphInfo = new GraphInfo(caseDetail);
        return Result.success(graphInfo);
    }
}
