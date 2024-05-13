package com.majy.ppdocapi;

import cn.hutool.core.util.CreditCodeUtil;
import cn.hutool.core.util.IdcardUtil;

public class IsValid
{
    public static void main(String[] args)
    {
        String testCreditCode = "91310000MA1FPFRF44";
        System.out.println(CreditCodeUtil.isCreditCode(testCreditCode));
    }
}
