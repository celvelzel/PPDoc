package com.majy;

import com.majy.ppdoc.mapper.DocumentMapper;
import com.majy.ppdoc.pojo.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class SpringbootMybatisQuickStartApplication
{
    @Autowired
    private DocumentMapper documentMapper;

    @Test
    public void testListDocunment(){
        List<Document> documentList = documentMapper.list();
        documentList.stream().forEach(
                System.out::println
        );
    }
}
