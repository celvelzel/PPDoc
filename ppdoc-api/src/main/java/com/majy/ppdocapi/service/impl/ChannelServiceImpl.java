package com.majy.ppdocapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Channel;
import com.majy.ppdocapi.mapper.ChannelMapper;
import com.majy.ppdocapi.service.ChannelService;
import com.majy.ppdocapi.utils.ModelUtils.BaiDuUTtils;
import com.majy.ppdocapi.utils.ModelUtils.KimiUtils;
import com.majy.ppdocapi.utils.ModelUtils.ZhiPuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService
{
    @Autowired
    private ChannelMapper channelMapper;


    @Override
    public PageBean page(Integer startPage, Integer pageSize)
    {
        //设置分页参数
        PageHelper.startPage(startPage, pageSize);
        //执行查询
        List<Channel> channelList = channelMapper.list();
        Page<Channel> pageHelper = (Page<Channel>) channelList;
        //封装PageBean对象
        PageBean pageBean = new PageBean(pageHelper.getTotal(), pageHelper.getResult());
        return pageBean;
    }

    @Override
    public Result add(Channel channel)
    {
        switch (channel.getChannelType())
        {
            case "智谱ChatGLM":
                channel.setChannelResponseTime(String.valueOf(ZhiPuUtils.getResponseTime(channel.getChannelApiKey(), channel.getChannelModelName())));
                break;
            case "Moonshot AI":
                channel.setChannelResponseTime(String.valueOf(KimiUtils.getResponseTime(channel.getChannelApiKey(), channel.getChannelModelName())));
                break;
            case"百度文心大模型":
                channel.setChannelResponseTime(String.valueOf(BaiDuUTtils.getResponseTime(channel.getChannelApiKey(), channel.getChannelSecretKey(), channel.getChannelModelName())));
                break;
            default:
                return Result.fail("不支持的渠道类型");
        }
        channel.setChannelStatus("未启用");
        channel.setChannelCreateTime(LocalDateTime.now());
        channelMapper.insert(channel);
        return Result.createSuccess();
    }

    @Override
    public Result update(Channel channel)
    {
        // 尝试执行更新操作
        Integer rowsAffected = channelMapper.update(channel);
        // 检查是否成功更新
        if (rowsAffected > 0)
        {
            return Result.updateSuccess();
        }
        else
        {
            return Result.updateFailure();
        }
    }

    @Override
    public Result deleteByChannelId(Integer channelId)
    {
        // 尝试执行删除操作
        Integer rowsAffected = channelMapper.deleteByChannelId(channelId);
        // 检查是否成功删除
        if (rowsAffected > 0)
        {
            return Result.deleteSuccess();
        }
        else
        {
            return Result.deleteFailure();
        }
    }

    @Override
    public Result getByChannelId(Integer channelId)
    {
        return Result.selectSuccess(channelMapper.selectByChannelId(channelId));
    }

    @Override
    public Result testByChannelId(Integer channelId)
    {
        Channel channel = channelMapper.selectByChannelId(channelId);
        switch (channel.getChannelType())
        {
            case "智谱ChatGLM":
                channel.setChannelResponseTime(String.valueOf(ZhiPuUtils.getResponseTime(channel.getChannelApiKey(), channel.getChannelModelName())));
                break;
            case "Moonshot AI":
                channel.setChannelResponseTime(String.valueOf(KimiUtils.getResponseTime(channel.getChannelApiKey(), channel.getChannelModelName())));
                break;
            case"百度文心大模型":
                channel.setChannelResponseTime(String.valueOf(BaiDuUTtils.getResponseTime(channel.getChannelApiKey(), channel.getChannelSecretKey(), channel.getChannelModelName())));
                break;
            default:
                return Result.fail("不支持的渠道类型");
        }
        // 尝试执行更新操作
        Integer rowsAffected = channelMapper.update(channel);
        // 检查是否成功更新
        if (rowsAffected > 0)
        {
            return Result.updateSuccess(channel.getChannelResponseTime());
        }
        else
        {
            return Result.updateFailure();
        }
    }
}
