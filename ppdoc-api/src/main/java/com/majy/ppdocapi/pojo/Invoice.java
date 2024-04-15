package com.majy.ppdocapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice
{
    private Integer invoice_id;
    private Integer document_id;
    private String invoice_url;
    private String invoice_code;
    private String invoice_number;
    private String invoice_amount;
    private String invoice_date;
    private String purchaser_name;
    private String seller_name;
    private String project_name;
    private String all_info;
}
