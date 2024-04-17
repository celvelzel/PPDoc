package com.majy.ppdocapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果分装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean
{
    private Long total;
    private List rows;
}
