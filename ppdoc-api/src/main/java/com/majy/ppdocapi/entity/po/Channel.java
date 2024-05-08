package com.majy.ppdocapi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
public class Channel
{
    private Integer channelId;
    private String channelName;
    private String channelType;
    private String channelStatus;
    private String channelResponseTime;
    private LocalDateTime channelCreateTime;
    private String channelModelName;
    private String channelApiKey;
    private String channelSecretKey;
}
