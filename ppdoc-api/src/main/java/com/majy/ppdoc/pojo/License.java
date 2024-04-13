package com.majy.ppdoc.pojo;

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


    public License()
    {
    }

    public License(Integer licenseId, Integer documentId, String licenseUrl, String licenseCode, String licenseNumber, String licenseEnterpriseName, String licenseEnterpriseType, String licenseLegalRepresentative, String licenseBusinessScope, String licenseRegisteredCapital, String licenseEstablishDate, String licenseOperationPeriod, String licenseDomicile, String allInfo)
    {
        this.licenseId = licenseId;
        this.documentId = documentId;
        this.licenseUrl = licenseUrl;
        this.licenseCode = licenseCode;
        this.licenseNumber = licenseNumber;
        this.licenseEnterpriseName = licenseEnterpriseName;
        this.licenseEnterpriseType = licenseEnterpriseType;
        this.licenseLegalRepresentative = licenseLegalRepresentative;
        this.licenseBusinessScope = licenseBusinessScope;
        this.licenseRegisteredCapital = licenseRegisteredCapital;
        this.licenseEstablishDate = licenseEstablishDate;
        this.licenseOperationPeriod = licenseOperationPeriod;
        this.licenseDomicile = licenseDomicile;
        this.allInfo = allInfo;
    }

    /**
     * 获取
     * @return licenseId
     */
    public Integer getLicenseId()
    {
        return licenseId;
    }

    /**
     * 设置
     * @param licenseId
     */
    public void setLicenseId(Integer licenseId)
    {
        this.licenseId = licenseId;
    }

    /**
     * 获取
     * @return documentId
     */
    public Integer getDocumentId()
    {
        return documentId;
    }

    /**
     * 设置
     * @param documentId
     */
    public void setDocumentId(Integer documentId)
    {
        this.documentId = documentId;
    }

    /**
     * 获取
     * @return licenseUrl
     */
    public String getLicenseUrl()
    {
        return licenseUrl;
    }

    /**
     * 设置
     * @param licenseUrl
     */
    public void setLicenseUrl(String licenseUrl)
    {
        this.licenseUrl = licenseUrl;
    }

    /**
     * 获取
     * @return licenseCode
     */
    public String getLicenseCode()
    {
        return licenseCode;
    }

    /**
     * 设置
     * @param licenseCode
     */
    public void setLicenseCode(String licenseCode)
    {
        this.licenseCode = licenseCode;
    }

    /**
     * 获取
     * @return licenseNumber
     */
    public String getLicenseNumber()
    {
        return licenseNumber;
    }

    /**
     * 设置
     * @param licenseNumber
     */
    public void setLicenseNumber(String licenseNumber)
    {
        this.licenseNumber = licenseNumber;
    }

    /**
     * 获取
     * @return licenseEnterpriseName
     */
    public String getLicenseEnterpriseName()
    {
        return licenseEnterpriseName;
    }

    /**
     * 设置
     * @param licenseEnterpriseName
     */
    public void setLicenseEnterpriseName(String licenseEnterpriseName)
    {
        this.licenseEnterpriseName = licenseEnterpriseName;
    }

    /**
     * 获取
     * @return licenseEnterpriseType
     */
    public String getLicenseEnterpriseType()
    {
        return licenseEnterpriseType;
    }

    /**
     * 设置
     * @param licenseEnterpriseType
     */
    public void setLicenseEnterpriseType(String licenseEnterpriseType)
    {
        this.licenseEnterpriseType = licenseEnterpriseType;
    }

    /**
     * 获取
     * @return licenseLegalRepresentative
     */
    public String getLicenseLegalRepresentative()
    {
        return licenseLegalRepresentative;
    }

    /**
     * 设置
     * @param licenseLegalRepresentative
     */
    public void setLicenseLegalRepresentative(String licenseLegalRepresentative)
    {
        this.licenseLegalRepresentative = licenseLegalRepresentative;
    }

    /**
     * 获取
     * @return licenseBusinessScope
     */
    public String getLicenseBusinessScope()
    {
        return licenseBusinessScope;
    }

    /**
     * 设置
     * @param licenseBusinessScope
     */
    public void setLicenseBusinessScope(String licenseBusinessScope)
    {
        this.licenseBusinessScope = licenseBusinessScope;
    }

    /**
     * 获取
     * @return licenseRegisteredCapital
     */
    public String getLicenseRegisteredCapital()
    {
        return licenseRegisteredCapital;
    }

    /**
     * 设置
     * @param licenseRegisteredCapital
     */
    public void setLicenseRegisteredCapital(String licenseRegisteredCapital)
    {
        this.licenseRegisteredCapital = licenseRegisteredCapital;
    }

    /**
     * 获取
     * @return licenseEstablishDate
     */
    public String getLicenseEstablishDate()
    {
        return licenseEstablishDate;
    }

    /**
     * 设置
     * @param licenseEstablishDate
     */
    public void setLicenseEstablishDate(String licenseEstablishDate)
    {
        this.licenseEstablishDate = licenseEstablishDate;
    }

    /**
     * 获取
     * @return licenseOperationPeriod
     */
    public String getLicenseOperationPeriod()
    {
        return licenseOperationPeriod;
    }

    /**
     * 设置
     * @param licenseOperationPeriod
     */
    public void setLicenseOperationPeriod(String licenseOperationPeriod)
    {
        this.licenseOperationPeriod = licenseOperationPeriod;
    }

    /**
     * 获取
     * @return licenseDomicile
     */
    public String getLicenseDomicile()
    {
        return licenseDomicile;
    }

    /**
     * 设置
     * @param licenseDomicile
     */
    public void setLicenseDomicile(String licenseDomicile)
    {
        this.licenseDomicile = licenseDomicile;
    }

    /**
     * 获取
     * @return allInfo
     */
    public String getAllInfo()
    {
        return allInfo;
    }

    /**
     * 设置
     * @param allInfo
     */
    public void setAllInfo(String allInfo)
    {
        this.allInfo = allInfo;
    }

    public String toString()
    {
        return "License{licenseId = " + licenseId + ", documentId = " + documentId + ", licenseUrl = " + licenseUrl + ", licenseCode = " + licenseCode + ", licenseNumber = " + licenseNumber + ", licenseEnterpriseName = " + licenseEnterpriseName + ", licenseEnterpriseType = " + licenseEnterpriseType + ", licenseLegalRepresentative = " + licenseLegalRepresentative + ", licenseBusinessScope = " + licenseBusinessScope + ", licenseRegisteredCapital = " + licenseRegisteredCapital + ", licenseEstablishDate = " + licenseEstablishDate + ", licenseOperationPeriod = " + licenseOperationPeriod + ", licenseDomicile = " + licenseDomicile + ", allInfo = " + allInfo + "}";
    }
}
