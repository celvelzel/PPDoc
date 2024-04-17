package com.majy.ppdocapi.entity.po;

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
    private String id_card_url;
    private String file_name;
    private String name;
    private String nation;
    private String sex;
    private String birthday;
    private String address;
    private String card_number;
    private String all_info;
}
