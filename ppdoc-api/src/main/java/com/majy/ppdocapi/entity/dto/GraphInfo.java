package com.majy.ppdocapi.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphInfo
{
    String types = "force";
    List<Category> categories;
    List<Node> nodes;
    List<Link> links;

    public GraphInfo(CaseDetail caseDetail)
    {
        this.categories.add(new Category("案件"));
        this.categories.add(new Category("起诉状"));
        this.categories.add(new Category("自然人身份证"));
        this.categories.add(new Category("企业营业执照"));
        this.categories.add(new Category("发票"));
        this.categories.add(new Category("属性"));


        this.nodes.add(new Node("案件" + caseDetail.getCaseobj().getCase_id().toString(), 0));
        if (caseDetail.getIndictment() != null)
        {
            this.nodes.add(new Node("起诉状" + caseDetail.getIndictment().getIndictment_id().toString(), 1));
            this.links.add(new Link(0, 1, "起诉状"));
        }
        if (caseDetail.getIdCards() != null)
        {
            caseDetail.getIdCards().forEach((key, value) ->
            {
                this.nodes.add(new Node(value.getName(), 2));
                Integer idCardNode = this.nodes.size() - 1;
                this.links.add(new Link(0, this.nodes.size() - 1, key));
                this.nodes.add(new Node(value.getName(), 5));
                this.links.add(new Link(idCardNode, this.nodes.size() - 1, "姓名"));
                this.nodes.add(new Node(value.getNation(), 5));
                this.links.add(new Link(idCardNode, this.nodes.size() - 1, "民族"));
                if (value.getSex() != null)
                {
                    this.nodes.add(new Node(value.getSex(), 5));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "性别"));
                }
                if (value.getBirthday() != null)
                {
                    this.nodes.add(new Node(value.getBirthday(), 5));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "出生日期"));
                }
                if (value.getAddress() != null)
                {
                    this.nodes.add(new Node(value.getAddress(), 5));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "住址"));
                }
                if (value.getCard_number() != null)
                {
                    this.nodes.add(new Node(value.getCard_number(), 5));
                    this.links.add(new Link(idCardNode, this.nodes.size() - 1, "身份证号"));
                }
            });
        }
    }


}


