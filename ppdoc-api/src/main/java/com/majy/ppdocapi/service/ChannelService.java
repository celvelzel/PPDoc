package com.majy.ppdocapi.service;


import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Channel;

public interface ChannelService
{
    public PageBean page(Integer startPage, Integer pageSize);

    public Result add(Channel channel);

    public Result update(Channel channel);

    public Result deleteByChannelId(Integer channelId);

    public Result getByChannelId(Integer channelId);

    public Result testByChannelId(Integer channelId);

    public Result getByChannelName(String channelName);

    public Channel getChannelEnabled();
}
