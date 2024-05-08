package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Channel;
import com.majy.ppdocapi.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/channels")
public class ChannelController
{
    @Autowired
    private ChannelService channelService;

    @GetMapping
    public Result<PageBean> page(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize)
    {
        log.info("进行分页查询渠道，参数为:当前页数{} 每页条数{}", page, pageSize);

        PageBean pageBean = channelService.page(page, pageSize);
        log.info("查询结果为:共有数据{}条", pageBean.getTotal());
        return Result.success(pageBean);
    }

    @PostMapping
    public Result add(@RequestBody Channel channel)
    {
        log.info("进行新增渠道，参数为:{}", channel);
        return channelService.add(channel);
    }

    @PutMapping
    public Result update(@RequestBody Channel channel)
    {
        log.info("进行修改渠道，参数为:{}", channel);
        return channelService.update(channel);
    }

    @DeleteMapping("/{channelId}")
    public Result delete(@PathVariable Integer channelId)
    {
        log.info("进行删除渠道，参数为:{}", channelId);
        return channelService.deleteByChannelId(channelId);
    }

    @GetMapping("/{channelId}")
    public Result get(@PathVariable Integer channelId)
    {
        log.info("进行查询渠道，参数为:{}", channelId);
        return channelService.getByChannelId(channelId);
    }
}
