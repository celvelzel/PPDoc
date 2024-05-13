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
            case "百度文心大模型":
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
            case "百度文心大模型":
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

    @Override
    public Result getByChannelName(String channelName)
    {
        return Result.selectSuccess(channelMapper.getByChannelName(channelName));
    }

    @Override
    public Channel getChannelEnabled()
    {
        return channelMapper.getChannelEnabled();
    }

    /**
     * 启用指定的渠道
     *
     * @param channelId 需要启用的渠道ID
     * @return 返回操作结果，如果操作成功，则返回更新成功的结果
     */
    @Override
    public Result enableChannel(Integer channelId)
    {
        // 获取当前已启用的渠道，并将其状态更新为“未启用”
        Channel enabledChannel =  channelMapper.getChannelEnabled();
        enabledChannel.setChannelStatus("未启用");
        channelMapper.update(enabledChannel);

        // 根据指定的渠道ID，获取该渠道并将其状态更新为“已启用”
        Channel disabledChannel = channelMapper.selectByChannelId(channelId);
        disabledChannel.setChannelStatus("已启用");
        channelMapper.update(disabledChannel);

        // 返回操作成功的结果
        return Result.updateSuccess();
    }


    /**
     * 禁用指定的渠道。
     *
     * @param channelId 渠道ID，用于标识需要被禁用的渠道。
     * @return 返回一个结果对象，表示禁用操作是否成功。
     */
    @Override
    public Result disableChannel(Integer channelId)
    {
        // 根据渠道ID查询渠道信息
        Channel channel = channelMapper.selectByChannelId(channelId);
        // 设置渠道状态为"未启用"
        channel.setChannelStatus("未启用");
        // 更新渠道状态
        channelMapper.update(channel);
        // 返回禁用成功的结果
        return Result.updateSuccess();
    }

}
