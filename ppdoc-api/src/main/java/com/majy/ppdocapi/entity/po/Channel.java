package com.majy.ppdocapi.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel
{
    private Integer channelId;
    private String channelName;
    private String channelType;
    private String channelState;
    private String channelResponseTime;
    private Date channelCraterTime;
    private String channelModelName;
    private String channelApiKey;
    private String channelSecretKey;
}
