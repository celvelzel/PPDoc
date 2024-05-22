package com.majy.ppdocapi.entity.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class License
{
    private Integer license_id;
    private Integer document_id;
    private String file_name;
    private String license_url;
    private String license_ocr_url;
    private String license_code;
    private String license_number;
    private String license_enterprise_name;
    private String license_enterprise_type;
    private String license_legal_representative;
    private String license_business_scope;
    private String license_registered_capital;
    private String license_establish_date;
    private String license_operation_period;
    private String license_domicile;
    private String all_info;
}
