package com.majy.ppdoc.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class License
{
    private Integer licenseId;
    private Integer documentId;
    private String licenseUrl;
    private String licenseCode;
    private String licenseNumber;
    private String licenseEnterpriseName;
    private String licenseEnterpriseType;
    private String licenseLegalRepresentative;
    private String licenseBusinessScope;
    private String licenseRegisteredCapital;
    private String licenseEstablishDate;
    private String licenseOperationPeriod;
    private String licenseDomicile;
    private String allInfo;
}
