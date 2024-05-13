package com.majy.ppdocapi;

import com.majy.ppdocapi.mapper.ChannelMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMybatisQuickStart1Application
{
    @Autowired
    ChannelMapper channelMapper;

    @Test
    public void test()
    {
        System.out.println(channelMapper.getChannelEnabled());
    }

}
