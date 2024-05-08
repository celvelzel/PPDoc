package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.entity.po.Channel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChannelMapper
{
    List<Channel> list();

    Channel selectByChannelId(Integer ChannelId);

    void insert(Channel channel);

    Integer update(Channel channel);

    Integer deleteByChannelId(Integer channelId);
}
