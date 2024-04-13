package com.majy.ppdoc.pojo;

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


    public Invoice()
    {
    }

    public Invoice(Integer invoiceId, Integer documentId, String invoiceUrl, String invoiceCode, String invoiceNumber, String invoiceAmount, String invoiceDate, String purchaserName, String sellerName, String projectName, String allInfo)
    {
        this.invoiceId = invoiceId;
        this.documentId = documentId;
        this.invoiceUrl = invoiceUrl;
        this.invoiceCode = invoiceCode;
        this.invoiceNumber = invoiceNumber;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
        this.purchaserName = purchaserName;
        this.sellerName = sellerName;
        this.projectName = projectName;
        this.allInfo = allInfo;
    }

    /**
     * 获取
     * @return invoiceId
     */
    public Integer getInvoiceId()
    {
        return invoiceId;
    }

    /**
     * 设置
     * @param invoiceId
     */
    public void setInvoiceId(Integer invoiceId)
    {
        this.invoiceId = invoiceId;
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
     * @return invoiceUrl
     */
    public String getInvoiceUrl()
    {
        return invoiceUrl;
    }

    /**
     * 设置
     * @param invoiceUrl
     */
    public void setInvoiceUrl(String invoiceUrl)
    {
        this.invoiceUrl = invoiceUrl;
    }

    /**
     * 获取
     * @return invoiceCode
     */
    public String getInvoiceCode()
    {
        return invoiceCode;
    }

    /**
     * 设置
     * @param invoiceCode
     */
    public void setInvoiceCode(String invoiceCode)
    {
        this.invoiceCode = invoiceCode;
    }

    /**
     * 获取
     * @return invoiceNumber
     */
    public String getInvoiceNumber()
    {
        return invoiceNumber;
    }

    /**
     * 设置
     * @param invoiceNumber
     */
    public void setInvoiceNumber(String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * 获取
     * @return invoiceAmount
     */
    public String getInvoiceAmount()
    {
        return invoiceAmount;
    }

    /**
     * 设置
     * @param invoiceAmount
     */
    public void setInvoiceAmount(String invoiceAmount)
    {
        this.invoiceAmount = invoiceAmount;
    }

    /**
     * 获取
     * @return invoiceDate
     */
    public String getInvoiceDate()
    {
        return invoiceDate;
    }

    /**
     * 设置
     * @param invoiceDate
     */
    public void setInvoiceDate(String invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    /**
     * 获取
     * @return purchaserName
     */
    public String getPurchaserName()
    {
        return purchaserName;
    }

    /**
     * 设置
     * @param purchaserName
     */
    public void setPurchaserName(String purchaserName)
    {
        this.purchaserName = purchaserName;
    }

    /**
     * 获取
     * @return sellerName
     */
    public String getSellerName()
    {
        return sellerName;
    }

    /**
     * 设置
     * @param sellerName
     */
    public void setSellerName(String sellerName)
    {
        this.sellerName = sellerName;
    }

    /**
     * 获取
     * @return projectName
     */
    public String getProjectName()
    {
        return projectName;
    }

    /**
     * 设置
     * @param projectName
     */
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
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
        return "Invoice{invoiceId = " + invoiceId + ", documentId = " + documentId + ", invoiceUrl = " + invoiceUrl + ", invoiceCode = " + invoiceCode + ", invoiceNumber = " + invoiceNumber + ", invoiceAmount = " + invoiceAmount + ", invoiceDate = " + invoiceDate + ", purchaserName = " + purchaserName + ", sellerName = " + sellerName + ", projectName = " + projectName + ", allInfo = " + allInfo + "}";
    }
}
