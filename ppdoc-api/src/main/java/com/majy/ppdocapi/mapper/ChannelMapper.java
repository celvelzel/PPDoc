package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChannelMapper
{
    @Select("select * from channel")
    List<Channel> list();

    Channel selectByChannelId();

    void insert(Channel channel);

    void update(Channel channel);

    void deleteByChannelId(Integer channelId);
}
