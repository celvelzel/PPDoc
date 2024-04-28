package com.majy.ppdocapi;

import cn.hutool.core.util.IdcardUtil;

public class IdCardValid
{
    public static void main(String[] args)
    {
        String idCard = "440304200210285711";
        System.out.println(IdcardUtil.isValidCard(idCard));
    }
}
