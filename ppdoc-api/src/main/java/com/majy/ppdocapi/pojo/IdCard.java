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
    private Integer documentId;
    private String name;
    private String nation;
    private String sex;
    private String address;
    private String cardNumber;
    private String allInfo;
}
