package com.majy.ppdocapi;

import com.majy.ppdocapi.mapper.DocumentMapper;
import com.majy.ppdocapi.pojo.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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
