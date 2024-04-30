package com.majy.ppdocapi.service.impl;

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
//                if(!caseMapper.getCaseByIDCardId(caseObj.getDefendant_id_card_id(), case_id).isEmpty())
//                {
//
//                }
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
        log.info("案件{}的详细信息：{}", case_id, caseDetail);
        GraphInfo originGraphInfo = new GraphInfo(caseDetail);
        return Result.success(originGraphInfo);



        // 获取相关案件，进行联想，只进行一次迭代
//        List<Case> plaintiffRelatedCases = new ArrayList<Case>();
//        List<Case> defendantRelatedCases = new ArrayList<Case>();
//
//        if (IdcardUtil.isValidCard(caseObj.getPlaintiff_id()))
//        {
//            plaintiffRelatedCases = caseMapper.getCaseByIDCardId(caseObj.getPlaintiff_id_card_id(), caseObj.getCase_id());
//        }
//        else
//        {
//            plaintiffRelatedCases = caseMapper.getCaseByLicenseId(caseObj.getPlaintiff_license_id(), caseObj.getCase_id());
//        }
//        GraphInfo plaintiffGraph = new GraphInfo();
//        if (!plaintiffRelatedCases.isEmpty())
//        {
//            for (Case aCase : plaintiffRelatedCases)
//            {
//                if (! Objects.equals(aCase.getCase_id(), case_id))
//                {
//                    // 生成相关案件的图谱
//                    GraphInfo relatedGraphInfo = new GraphInfo((CaseDetail) getGraphByCaseId(aCase.getCase_id()).getData());
//                    // 将图谱的共同节点进行融合
//                    if(! relatedGraphInfo.getNodes().isEmpty())
//                    {
//                        plaintiffGraph = mergeGraph(originGraphInfo, relatedGraphInfo);
//                        log.info("原告相关案件图谱进行图谱融合");
//                    }
//                }
//            }
//        }
//
//        if (IdcardUtil.isValidCard(caseObj.getDefendant_id()))
//        {
//            defendantRelatedCases = caseMapper.getCaseByIDCardId(caseObj.getDefendant_id_card_id(), caseObj.getCase_id());
//        }
//        else
//        {
//            defendantRelatedCases = caseMapper.getCaseByLicenseId(caseObj.getDefendant_license_id(), caseObj.getCase_id());
//        }
//        GraphInfo defendantGraph = new GraphInfo();
//        if (!defendantRelatedCases.isEmpty())
//        {
//            for (Case aCase : defendantRelatedCases)
//            {
//                if (! Objects.equals(aCase.getCase_id(), case_id))
//                {
//                    // 生成相关案件的图谱
//                    GraphInfo relatedGraphInfo = new GraphInfo((CaseDetail) getGraphByCaseId(aCase.getCase_id()).getData());
//                    // 将图谱的共同节点进行融合
//                    if(! relatedGraphInfo.getNodes().isEmpty())
//                    {
//                        defendantGraph = mergeGraph(originGraphInfo, relatedGraphInfo);
//                        log.info("被告相关案件图谱进行图谱融合");
//                    }
//                }
//            }
//        }
//        if (! originGraphInfo.getNodes().isEmpty())
//        {
//            if (! defendantGraph.getNodes().isEmpty())
//            {
//                if (! plaintiffGraph.getNodes().isEmpty())
//                {
//                    log.info("被告相关案件和原告相关案件都不为空，进行图谱融合");
//                    return Result.success(mergeGraph(plaintiffGraph, defendantGraph));
//                }
//                return Result.success(defendantGraph);
//            }
//            else if (! plaintiffGraph.getNodes().isEmpty())
//            {
//                return Result.success(plaintiffGraph);
//            }
//            return Result.success(originGraphInfo);
//        }
//        return Result.selectFailure();
    }

    public GraphInfo mergeGraph(GraphInfo graph1, GraphInfo graph2)
    {
        GraphInfo mergedGraph = new GraphInfo();
        mergedGraph.setTypes(graph1.getTypes());
        mergedGraph.setCategories(graph1.getCategories());
        mergedGraph.setNodes(new ArrayList<>());
        mergedGraph.setLinks(new ArrayList<>());

        // 创建一个哈希表来存储第一个图中每个节点的索引
        Map<String, Integer> graph1NodeIndex = new HashMap<>();
        for (int i = 0; i < graph1.getNodes().size(); i++)
        {
            graph1NodeIndex.put(graph1.getNodes().get(i).getName(), i);
        }

        // 遍历第二个图的节点，找到相同的节点并合并连接
        for (Node node2 : graph2.getNodes())
        {
            Integer index1 = graph1NodeIndex.get(node2.getName());
            if (index1 != null)
            {
                // 如果找到了相同的节点，合并连接
                mergedGraph.getLinks().addAll(graph1.getLinks());
                mergedGraph.getLinks().addAll(graph2.getLinks());
                // 添加合并后的连接
                for (Link link : graph2.getLinks())
                {
                    if (link.getSource() == index1 || link.getTarget() == index1)
                    {
                        mergedGraph.getLinks().add(link);
                    }
                }
            }
            else
            {
                // 如果找不到相同的节点，直接添加第二个图的节点和连接
                mergedGraph.getNodes().add(node2);
                mergedGraph.getLinks().addAll(graph2.getLinks());
            }
        }

        return mergedGraph;
    }
}
