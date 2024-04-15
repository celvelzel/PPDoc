package com.majy.ppdocapi.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocOcrUtils extends PaddleOcrUtils
{
    public static Map<String, String> getStringStringMap(List<List> jsons){
        //调用父类的jsonToString方法，拼接OCR结果
        String trim = jsonToString(jsons);
        String allInfo = trim;
        Map<String, String> docInfoMap = new HashMap<>();
        docInfoMap.put("allInfo", allInfo);
        return docInfoMap;
    }

}
