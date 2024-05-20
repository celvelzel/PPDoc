package com.majy.ppdocapi.service.impl;

import cn.hutool.core.util.CreditCodeUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.graph.Graph;
import com.majy.ppdocapi.entity.dto.*;
import com.majy.ppdocapi.entity.po.*;
import com.majy.ppdocapi.mapper.*;
import com.majy.ppdocapi.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Integer rowsAffected = caseMapper.deleteCaseById(case_id);
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
        // 初始化材料提交状态
        caseModel.setIs_defendant_submit(false);
        caseModel.setIs_plaintiff_submit(false);
        caseModel.setIs_related_submit(false);
        // 返回成功结果
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
        return Result.selectSuccess(caseMapper.getCaseById(case_id));
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
            else if (CreditCodeUtil.isCreditCode(caseObj.getPlaintiff_id())) // 信用代码有效则为企业
            {
                licenses.put("原告", licenseMapper.getByLicenseId(caseObj.getPlaintiff_license_id()));
            }
            else
            {
                log.info("原告id无效");
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
            else if (CreditCodeUtil.isCreditCode(caseObj.getDefendant_id())) // 信用代码有效则为企业
            {
                licenses.put("被告", licenseMapper.getByLicenseId(caseObj.getDefendant_license_id()));
            }
            else
            {
                log.info("被告id无效");
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


        CaseDetail caseDetail = new CaseDetail(caseObj, indictment, idcards, licenses, invoices, getPlaintiffRelatedCase(case_id), getDefendantRelatedCase(case_id));
        log.info("案件{}的详细信息：{}", case_id, caseDetail);
        GraphInfo originGraphInfo = new GraphInfo(caseDetail);
        return Result.success(originGraphInfo);
    }

    public List<Case> getPlaintiffRelatedCase(Integer case_id)
    {
        List<Case> Result = new ArrayList<>();
        Case caseObj = caseMapper.getCaseById(case_id);
        if (caseObj != null)
        {
            if (caseObj.getPlaintiff_id_card_id() != null)
            {
                caseMapper.getCaseByIDCardId(caseObj.getPlaintiff_id_card_id(), case_id).forEach(caseModel ->
                {
                    if (! Objects.equals(caseModel.getCase_id(), case_id))
                    {
                        Result.add(caseModel);
                        log.info("原告自然人相关案件,案件信息为{}", caseModel);
                    }
                });
            }
            if (caseObj.getPlaintiff_license_id() != null)
            {
                caseMapper.getCaseByLicenseId(caseObj.getPlaintiff_license_id(), case_id).forEach(caseModel ->
                {
                    if (! Objects.equals(caseModel.getCase_id(), case_id))
                    {
                        Result.add(caseModel);
                        log.info("原告企业相关案件,案件信息为{}", caseModel);
                    }
                });
            }
            return Result;
        }
        else
        {
            log.info("案件不存在");
            return null;
        }
    }

    public List<Case> getDefendantRelatedCase(Integer case_id)
    {
        List<Case> Result = new ArrayList<>();
        Case caseObj = caseMapper.getCaseById(case_id);
        if (caseObj != null)
        {
            if (caseObj.getDefendant_id_card_id() != null)
            {
                caseMapper.getCaseByIDCardId(caseObj.getDefendant_id_card_id(), caseObj.getCase_id()).forEach(caseModel ->
                {
                    if (! Objects.equals(caseModel.getCase_id(), case_id))
                    {
                        Result.add(caseModel);
                        log.info("被告自然人相关案件,案件信息为{}", caseModel);
                    }
                });
            }
            if (caseObj.getDefendant_license_id() != null)
            {
                caseMapper.getCaseByLicenseId(caseObj.getDefendant_license_id(), caseObj.getCase_id()).forEach(caseModel ->
                {
                    if (! Objects.equals(caseModel.getCase_id(), case_id))
                    {
                        Result.add(caseModel);
                        log.info("被告企业相关案件,案件信息为{}", caseModel);
                    }
                });
            }
            return Result;
        }
        else
        {
            log.info("案件不存在");
            return null;
        }
    }
}
