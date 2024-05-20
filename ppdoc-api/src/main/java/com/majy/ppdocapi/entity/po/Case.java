package com.majy.ppdocapi.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case
{
    private Integer case_id;
    private Integer indictment_id;
    private String case_type;
    private String plaintiff_name;
    private String plaintiff_id;
    private String plaintiff_type;
    private String defendant_name;
    private String defendant_id;
    private String defendant_type;
    private Integer plaintiff_id_card_id;
    private Integer defendant_id_card_id;
    private Integer plaintiff_license_id;
    private Integer defendant_license_id;
    private Integer related_invoice_id;
    private Boolean is_plaintiff_submit;
    private Boolean is_defendant_submit;
    private Boolean is_related_submit;
}
