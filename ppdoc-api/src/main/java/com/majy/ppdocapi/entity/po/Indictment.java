package com.majy.ppdocapi.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indictment {
    private Integer indictment_id;
    private Integer document_id;
    private String indictment_url;
    private String file_name;
    private String case_type;
    private String plaintiff_name;
    private String plaintiff_id;
    private String plaintiff_type;
    private String plaintiff_address;
    private String plaintiff_contact;
    private String defendant_name;
    private String defendant_id;
    private String defendant_type;
    private String defendant_address;
    private String defendant_contact;
    private String litigation_request;
    private String facts_background;
    private String legal_basis;
    private String evidence_list;
    private String court_name;
    private String indictment_date;
    private String all_info;
}

