package com.majy.ppdoc.pojo;


public class Document
{
    private Integer documentId;
    private String documentUrl;
    private String documentName;
    private String documentType;
    private String all_info;


    public Document()
    {
    }

    public Document(Integer documentId, String documentUrl, String documentName, String documentType, String all_info)
    {
        this.documentId = documentId;
        this.documentUrl = documentUrl;
        this.documentName = documentName;
        this.documentType = documentType;
        this.all_info = all_info;
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
     * @return documentUrl
     */
    public String getDocumentUrl()
    {
        return documentUrl;
    }

    /**
     * 设置
     * @param documentUrl
     */
    public void setDocumentUrl(String documentUrl)
    {
        this.documentUrl = documentUrl;
    }

    /**
     * 获取
     * @return documentName
     */
    public String getDocumentName()
    {
        return documentName;
    }

    /**
     * 设置
     * @param documentName
     */
    public void setDocumentName(String documentName)
    {
        this.documentName = documentName;
    }

    /**
     * 获取
     * @return documentType
     */
    public String getDocumentType()
    {
        return documentType;
    }

    /**
     * 设置
     * @param documentType
     */
    public void setDocumentType(String documentType)
    {
        this.documentType = documentType;
    }

    /**
     * 获取
     * @return all_info
     */
    public String getAll_info()
    {
        return all_info;
    }

    /**
     * 设置
     * @param all_info
     */
    public void setAll_info(String all_info)
    {
        this.all_info = all_info;
    }

    public String toString()
    {
        return "Document{documentId = " + documentId + ", documentUrl = " + documentUrl + ", documentName = " + documentName + ", documentType = " + documentType + ", all_info = " + all_info + "}";
    }
}
