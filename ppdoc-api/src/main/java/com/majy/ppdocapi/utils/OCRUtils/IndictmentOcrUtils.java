package com.majy.ppdocapi.utils.OCRUtils;

import com.majy.ppdocapi.service.ModelService;
import com.majy.ppdocapi.service.impl.ModelServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IndictmentOcrUtils extends PaddleOcrUtils
{
    @Autowired
    private ModelService modelService;

    //全局变量
    private static final String NO_INFO_FOUND = "未找到";

    public Map<String, String> getStringStringMap(List<List> jsons)
    {
        //调用父类的jsonToString方法，拼接OCR结果
        String trim = jsonToString(jsons);

        String allInfo = trim;

        Map<String, String> indictmentInfoMap = new HashMap<>();

        //调用LLM
        Map<String, String> invoiceLLMMap = invokeLLM(trim);
        indictmentInfoMap.putAll(invoiceLLMMap);

        return indictmentInfoMap;
    }

    /**
     * 调用智谱API，分析文本
     *
     * @param trim 拼接后的ocr识别的文字
     */
    public Map<String,String> invokeLLM(String trim)
    {
        String LLMResult =  modelService.extractInfo("zhipuai", trim, "案件类型,原告姓名,原告身份证（若原告为个人）/统一社会信用代码（若原告为企业））,原告类型（企业或个人）,原告地址,原告联系方式,被告姓名,被告身份证或统一认证代码,被告类型,被告地址,被告联系方式,诉讼请求,事实背景,法律依据,证据清单,法院名称,起诉状日期,其他重要信息");
        log.info("智谱LLM结果是：" + LLMResult);
        return new HashMap<>();
    }

    @Test
    public void test(){
        String trim = "民事起诉状（按揭类）原告：交通银行股份有限公司广西壮族自治区分行，住所地91450102898281063X.负责人：江洲，该分行行长。委托诉讼代理人：XXX，男，该公司员工。联系电话：0771-XXXxxXX委托诉讼代理人：XXX，男，该公司员工.联系电话：0771-XXXXX被告：XXX，男，XXX年XX月XX日出生，汉族，住XXX，公民身份号码XXXX，约定送达地址XXXX，联系电话XX。被告：XXX，女，XXX年XX月XX日出生，汉族，住XXX，公民身份号码XXX，约定送达地址XXXX，联系电话XXXX。诉讼请求一确认原告交通银行股份有限公司广西壮族自治区分行与被告XXX、XXX签订的编号为XXX的《个人房产抵押贷款合同》（下称“贷款合同”）贷款全部提前到期；二被告X、X向原告交通银行股份有限公司广西壮族自治区分行返还借款XXXX元；三、被告XXX、XX向原告交通银行股份有限公司广西壮族自治区分行支付利息（利息计算：1.计至X0X年XX月X日的利息为XXXX元、罚息XXX元、利息复利XXX元、罚息复利XXX元；2.以XXxX元为基数，自XXX年X月XX日至实际清偿之日止，按合同约定罚息利率计收罚息）：四、被告XXX、XXX如不能履行上述第二、第三项给付义务，原告交通银行股份有限公司广西壮族自治区分行有权对登记在被告XXX、XX名下的位于XXX房房产折价或者以拍卖、变卖该财产的价款优先受偿。五、被告XX、XXX承担本案诉讼费用及与本案有关的其他费用：其中包括但不限于案件受理费、财产保全费、评估费、公告费、鉴定费等。事实与理由原告与被告XXX、XXX、担保人XXX于XXX年XX月XX日签订了编号为XXX的《个人房产抵押贷款合同》，贷款合同约定：原告贷款XXXX元给被告XXX、XXX购买XXX房产；被告从发放贷款的次月起开始按月以XX还款法偿还贷款本息，月利率为XXX%：被告用所购买的房产为贷款提供抵押担保。上述贷款合同签订后，原告依约于XXX年XX月XX日向被告武海波发放了贷款XX元，双方办理了上述房产的抵押登记（编号为：XX）：原告已取得抵押物的他项权利证明，根据合同约定，担保人XX的保证责任解除。但是，被告并未依约按时向原告偿还贷款本息，截至XXXX年XX月XX日已逾期达XX期：为此，原告采取多种方式要求被告履行还款义务，但被告仍未按期清偿欠款。综上：被告的行为已构成违约，原告依据贷款合同第十三条“贷款提前到期”的“提前到期享件”约定宣布贷款全部提前到期，并要求被告立即偿还所有到期贷款本金、结清利息、承担原告实现本案债权所支付的各种费用（诉讼费、公告费、鉴定费、取证费、保全费以及律师费等）被告XX、XXX以XXX房产为贷款提供抵押担保，原告依法对抵押物享有优先受偿权。被告XXX向原告贷款发生在其与被告XXX婚姻存续期间，被告XXX对被告XXX的上述债务承担连带清偿责任。为此，原告向人民法院依法提起诉讼：恳请法院予以支持。此致南宁市兴宁区人民法院具状人：交通银行股份有限公司广西壮族自治区分行XXXX年XX月XX日";
        invokeLLM(trim);
    }
}
