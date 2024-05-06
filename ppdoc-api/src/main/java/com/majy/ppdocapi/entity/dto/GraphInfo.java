package com.majy.ppdocapi.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Data
@AllArgsConstructor
public class GraphInfo
{
    private String types;
    private List<Category> categories;
    private List<Node> nodes;
    private List<Link> links;

    public GraphInfo()
    {
        this.types = "force";
        this.categories = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.links = new ArrayList<>();

        this.categories.add(new Category("案件"));
        this.categories.add(new Category("起诉状"));
        this.categories.add(new Category("自然人身份证"));
        this.categories.add(new Category("企业营业执照"));
        this.categories.add(new Category("发票"));
        this.categories.add(new Category("属性"));
    }

    public GraphInfo(CaseDetail caseDetail)
    {
        this.types = "force";
        this.categories = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.links = new ArrayList<>();

        this.categories.add(new Category("案件"));
        this.categories.add(new Category("起诉状"));
        this.categories.add(new Category("自然人身份证"));
        this.categories.add(new Category("企业营业执照"));
        this.categories.add(new Category("发票"));
        this.categories.add(new Category("属性"));

        this.nodes.add(new Node("案件" + caseDetail.getCaseobj().getCase_id().toString(), caseDetail.getCaseobj().getCase_id(), 0, this.nodes.size()));
        if (caseDetail.getIndictment() != null)
        {
            Integer indictmentId = caseDetail.getIndictment().getIndictment_id();
            this.nodes.add(new Node("起诉状" + caseDetail.getIndictment().getIndictment_id().toString(), indictmentId, 1, this.nodes.size()));
            this.links.add(new Link(0, 1, "起诉状"));
            Integer indictmentNode = this.nodes.size() - 1;
            this.nodes.add(new Node(caseDetail.getIndictment().getPlaintiff_name(), indictmentId, 5, this.nodes.size()));
            this.links.add(new Link(indictmentNode, this.nodes.size() - 1, "原告姓名"));
            if (caseDetail.getIndictment().getDefendant_contact() != null)
            {
                this.nodes.add(new Node(caseDetail.getIndictment().getPlaintiff_contact(), indictmentId, 5, this.nodes.size()));
                this.links.add(new Link(indictmentNode, this.nodes.size() - 1, "原告联系方式"));
            }
            this.nodes.add(new Node(caseDetail.getIndictment().getDefendant_name(), indictmentId, 5, this.nodes.size()));
            this.links.add(new Link(indictmentNode, this.nodes.size() - 1, "被告姓名"));
            if (caseDetail.getIndictment().getDefendant_contact() != null)
            {
                this.nodes.add(new Node(caseDetail.getIndictment().getDefendant_contact(), indictmentId, 5, this.nodes.size()));
                this.links.add(new Link(indictmentNode, this.nodes.size() - 1, "被告联系方式"));
            }
            this.nodes.add(new Node(caseDetail.getIndictment().getCase_type(), indictmentId, 5, this.nodes.size()));
            this.links.add(new Link(indictmentNode, this.nodes.size() - 1, "案件类型"));
            if (caseDetail.getIndictment().getLitigation_request() != null)
            {
                this.nodes.add(new Node(caseDetail.getIndictment().getCourt_name(), indictmentId, 5, this.nodes.size()));
                this.links.add(new Link(indictmentNode, this.nodes.size() - 1, "审理法院名称"));
            }
            if (caseDetail.getIndictment().getLitigation_request() != null)
            {
                this.nodes.add(new Node(caseDetail.getIndictment().getIndictment_date(), indictmentId, 5, this.nodes.size()));
                this.links.add(new Link(indictmentNode, this.nodes.size() - 1, "起诉状日期"));
            }
        }
        if (caseDetail.getIdCards() != null)
        {
            caseDetail.getIdCards().forEach((key, value) ->
            {
                Integer IdCardId = value.getId();
                // 添加自然人身份证节点 将key作为关系填入
                this.nodes.add(new Node(value.getName(), IdCardId, 2, this.nodes.size()));
                Integer idCardNode = this.nodes.size() - 1;
                this.links.add(new Link(0, this.nodes.size() - 1, key));
                // 添加属性
                this.nodes.add(new Node(value.getNation(), IdCardId, 5, this.nodes.size()));
                this.links.add(new Link(idCardNode, this.nodes.size() - 1, "民族"));
                if (value.getSex() != null)
                {
                    this.nodes.add(new Node(value.getSex(), IdCardId, 5, this.nodes.size()));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "性别"));
                }
                if (value.getBirthday() != null)
                {
                    this.nodes.add(new Node(value.getBirthday(), IdCardId, 5, this.nodes.size()));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "出生日期"));
                }
                if (value.getAddress() != null)
                {
                    this.nodes.add(new Node(value.getAddress(), IdCardId, 5, this.nodes.size()));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "住址"));
                }
                if (value.getCard_number() != null)
                {
                    this.nodes.add(new Node(value.getCard_number(), IdCardId, 5, this.nodes.size()));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "身份证号"));
                }
                //添加自然人相关案件
                if(key.equals("原告"))
                {
                    caseDetail.getPlaintiffRelatedCases().forEach(caseModel ->{
                        this.nodes.add(new Node("案件" + caseModel.getCase_id().toString(), caseModel.getCase_id(), 0, this.nodes.size()));
                        this.links.add(new Link(idCardNode, this.nodes.size() - 1, "相关案件"));
                    });
                }
                else if (key.equals("被告"))
                {
                    caseDetail.getDefendantRelatedCases().forEach(caseModel ->{
                        this.nodes.add(new Node("案件" + caseModel.getCase_id().toString(), caseModel.getCase_id(), 0, this.nodes.size()));
                        this.links.add(new Link(idCardNode, this.nodes.size() - 1, "相关案件"));
                    });
                }
            });
        }
        if (caseDetail.getLicenses() != null)
        {
            caseDetail.getLicenses().forEach((key, value) ->
            {
                Integer licenseId = value.getLicense_id();
                // 添加企业营业执照节点 将key作为关系填入
                this.nodes.add(new Node(value.getLicense_enterprise_name(), licenseId, 3, this.nodes.size()));
                Integer licenseNode = this.nodes.size() - 1;
                this.links.add(new Link(0, this.nodes.size() - 1, key));
                // 添加属性
                this.nodes.add(new Node(value.getLicense_code(), licenseId, 5, this.nodes.size()));
                this.links.add(new Link(licenseNode, this.nodes.size() - 1, "统一社会信用代码"));
                if (value.getLicense_number() != null)
                {
                    this.nodes.add(new Node(value.getLicense_number(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "证照编号"));
                }
                if (value.getLicense_enterprise_type() != null)
                {
                    this.nodes.add(new Node(value.getLicense_enterprise_type(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "类型"));
                }
                if (value.getLicense_enterprise_name() != null)
                {
                    this.nodes.add(new Node(value.getLicense_legal_representative(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "企业名称"));
                }
                if (value.getLicense_legal_representative() != null)
                {
                    this.nodes.add(new Node(value.getLicense_legal_representative(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "法定代表人"));
                }
                if (value.getLicense_business_scope() != null)
                {
                    this.nodes.add(new Node(value.getLicense_business_scope(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "经营范围"));
                }
                if (value.getLicense_registered_capital() != null)
                {
                    this.nodes.add(new Node(value.getLicense_registered_capital(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "注册资本"));
                }
                if (value.getLicense_establish_date() != null)
                {
                    this.nodes.add(new Node(value.getLicense_establish_date(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "成立日期"));
                }
                if (value.getLicense_operation_period() != null)
                {
                    this.nodes.add(new Node(value.getLicense_operation_period(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "营业期限"));
                }
                if (value.getLicense_domicile() != null)
                {
                    this.nodes.add(new Node(value.getLicense_domicile(), licenseId, 5, this.nodes.size()));
                    this.links.add(new Link(licenseNode, this.nodes.size() - 1, "住所"));
                }
                //添加企业相关案件
                if(key.equals("原告"))
                {
                    caseDetail.getPlaintiffRelatedCases().forEach(caseModel ->{
                        this.nodes.add(new Node("案件" + caseModel.getCase_id().toString(), caseModel.getCase_id(), 0, this.nodes.size()));
                        this.links.add(new Link(licenseNode, this.nodes.size() - 1, "相关案件"));
                    });
                }
                else if (key.equals("被告"))
                {
                    caseDetail.getDefendantRelatedCases().forEach(caseModel ->{
                        this.nodes.add(new Node("案件" + caseModel.getCase_id().toString(), caseModel.getCase_id(), 0, this.nodes.size()));
                        this.links.add(new Link(licenseNode, this.nodes.size() - 1, "相关案件"));
                    });
                }
            });
        }
        if (caseDetail.getInvoices() != null)
        {
            caseDetail.getInvoices().forEach(invoice ->
            {
                Integer invoiceId = invoice.getInvoice_id();
                // 添加发票节点
                this.nodes.add(new Node("发票" + invoice.getInvoice_id(), invoiceId, 4, this.nodes.size()));
                Integer invoiceNode = this.nodes.size() - 1;
                this.links.add(new Link(0, this.nodes.size() - 1, "案件材料"));
                // 添加属性
                if (invoice.getInvoice_date() != null)
                {
                    this.nodes.add(new Node(invoice.getInvoice_date(), invoiceId, 5, this.nodes.size()));
                    this.links.add(new Link(invoiceNode, this.nodes.size() - 1, "开票日期"));
                }
                if (invoice.getInvoice_number() != null)
                {
                    this.nodes.add(new Node(invoice.getInvoice_number(), invoiceId, 5, this.nodes.size()));
                    this.links.add(new Link(invoiceNode, this.nodes.size() - 1, "发票号码"));
                }
                if (invoice.getInvoice_amount() != null)
                {
                    this.nodes.add(new Node(invoice.getInvoice_amount(), invoiceId, 5, this.nodes.size()));
                    this.links.add(new Link(invoiceNode, this.nodes.size() - 1, "金额"));
                }
                if (invoice.getInvoice_code() != null)
                {
                    this.nodes.add(new Node(invoice.getInvoice_code(), invoiceId, 5, this.nodes.size()));
                    this.links.add(new Link(invoiceNode, this.nodes.size() - 1, "发票代码"));
                }
                if(invoice.getPurchaser_name() != null)
                {
                    this.nodes.add(new Node(invoice.getPurchaser_name(), invoiceId, 5, this.nodes.size()));
                    this.links.add(new Link(invoiceNode, this.nodes.size() - 1, "购买方"));
                }
                if(invoice.getSeller_name() != null)
                {
                    this.nodes.add(new Node(invoice.getSeller_name(), invoiceId, 5, this.nodes.size()));
                    this.links.add(new Link(invoiceNode, this.nodes.size() - 1, "销售方"));
                }
                if(invoice.getProject_name() != null)
                {
                    this.nodes.add(new Node(invoice.getProject_name(), invoiceId, 5, this.nodes.size()));
                    this.links.add(new Link(invoiceNode, this.nodes.size() - 1, "项目名称"));
                }
            });
        }
    }

    public void addNode(Node node)
    {
        this.nodes.add(node);
    }

    public void addLink(Link link)
    {
        this.links.add(link);
    }

    public Integer getNodeIndex(Integer id, Integer category)
    {
        for (int i = 0; i < this.nodes.size(); i++)
        {
            if (this.nodes.get(i).getMyId().equals(id) && this.nodes.get(i).getCategory().equals(category))
            {
                return i;
            }
        }
        return null;
    }
}
