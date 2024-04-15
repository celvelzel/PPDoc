package com.majy.ppdocapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCard
{
    private Integer id;
    private Integer document_id;
    private String name;
    private String nation;
    private String sex;
    private String address;
    private String card_number;
    private String all_info;
}
