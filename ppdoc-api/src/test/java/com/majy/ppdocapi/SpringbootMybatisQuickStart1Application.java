package com.majy.ppdocapi;

import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.entity.po.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SpringbootMybatisQuickStart1Application
{
    @Autowired
    private DocumentMapper documentMapper;

    @Test
    public void testListDocument(){
        List<Document> documentList = documentMapper.list();
        if (documentList != null) {
            for (Document document : documentList) {
                System.out.println(document);
            }
        }
        else{
            System.out.println("没有数据");
        }
    }
}
