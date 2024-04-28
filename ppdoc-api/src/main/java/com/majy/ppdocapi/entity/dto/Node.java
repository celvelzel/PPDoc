package com.majy.ppdocapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node
{
    String name;
    Integer category;
    Integer index;
}
