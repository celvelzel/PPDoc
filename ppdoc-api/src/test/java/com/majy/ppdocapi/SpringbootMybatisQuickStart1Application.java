package com.majy.ppdocapi;

import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.service.ModelService;
import com.majy.ppdocapi.utils.OCRUtils.IndictmentOcrUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMybatisQuickStart1Application
{
    @Autowired
    private ModelService modelService;
    @Autowired
    IndictmentOcrUtils indictmentOcrUtils;

    @Test
    public void test()
    {
        String trim = "民事起诉状原告：华夏银行股份有限公司上海分行，住所地：上海市浦东新区世纪大道100号，统一社会信用代码：91320000MA1K1L5X3负责人：李华，该分行行长委托诉讼代理人：张伟，男，该公司法务部员工，联系电话：021-12345678被告：王晓明，男，汉族，1988年6月15日出生，住上海市徐汇区天钥桥路123弄456号，联系电话：18939912158，公民身份号码：310106198806151234诉讼请求判令被告王晓明返还原告华夏银行股份有限公司上海分行借款本金人民币100万元整（Y1,000,000.00）；判令被告王晓明支付自借款逾期之日起至实际清偿之日止的利息及罚息，暂计至起诉之日为人民币10万元整（Y100,000.00）；判令原告对被告王晓明名下位于上海市徐汇区天钥桥路123弄456号的抵押房产享有优先受偿权;判令被告承担本案全部诉讼费用。事实与理由原告与被告于022年1月1日签订了编号为HX20220101的《个人借款合同》，约定原告向被告提供人民币100万元整的借款，用于个人消费，借款期限为24个月，自2022年1月1日至2024年1月1日，月利率为0.5%，逾期还款的罚息利率为贷款利率上浮50%。合同同时约定，被告以其名下位于上海市徐汇区天钥桥路123弄456号的房产为借款提供抵押担保，并办理了相应的抵押登记手续。原告已于合同签订当日向被告全额发放了贷款。然而，被告自2023年1月起未能按期偿还贷款本息，截至起诉之日已逾期超过3个月。原告多次催告被告1履行还款义务，被告均未予以回应。基于上述事实，原告依据《中华人民共和国合同法》第一百零七条关于违约责任的规定，以及《中华人民共和国担保法》第三十三条关于抵押权的规定，向贵院提起诉讼，请求依法判决。证据清单被告身份证;企业营业执照《个人借款合同》（编号：HX20220101）副本;贷款发放银行转账凭证;抵押房产的他项权利证明书;逾期还款记录及催收通知书;被告财产状况说明，包括房产证明、收入证明等；其他重要信息本案涉及的贷款及逾期利息计算方法和依据;被告的财产状况，特别是用于抵押的房产当前市值评估报告;原告为实现债权所支付的费用清单，包括但不限于诉讼费、律师费、公告费等。此致上海市黄浦区人民法院原告：华夏银行股份有限公司上海分行委托诉讼代理人：张伟2024年4月22日2";
        if (modelService == null)
        {
            throw new IllegalStateException("modelService is not initialized");
        }
        System.out.println("结果Map："+indictmentOcrUtils.invokeLLM(trim));
    }
}
