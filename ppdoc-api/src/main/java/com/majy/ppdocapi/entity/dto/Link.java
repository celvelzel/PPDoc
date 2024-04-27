package com.majy.ppdocapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link
{
    Integer source;
    Integer target;
    String name;
}
