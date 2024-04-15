package com.majy.ppdocapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice
{
    private Integer invoiceId;
    private Integer documentId;
    private String invoiceUrl;
    private String invoiceCode;
    private String invoiceNumber;
    private String invoiceAmount;
    private String invoiceDate;
    private String purchaserName;
    private String sellerName;
    private String projectName;
    private String allInfo;
}
