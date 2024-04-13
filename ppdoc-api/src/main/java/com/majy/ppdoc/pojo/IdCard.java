package com.majy.ppdoc.pojo;

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


    public IdCard()
    {
    }

    public IdCard(Integer id, Integer documentId, String name, String nation, String sex, String address, String cardNumber, String allInfo)
    {
        this.id = id;
        this.documentId = documentId;
        this.name = name;
        this.nation = nation;
        this.sex = sex;
        this.address = address;
        this.cardNumber = cardNumber;
        this.allInfo = allInfo;
    }

    /**
     * 获取
     * @return id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Integer id)
    {
        this.id = id;
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
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * 获取
     * @return nation
     */
    public String getNation()
    {
        return nation;
    }

    /**
     * 设置
     * @param nation
     */
    public void setNation(String nation)
    {
        this.nation = nation;
    }

    /**
     * 获取
     * @return sex
     */
    public String getSex()
    {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    /**
     * 获取
     * @return address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * 设置
     * @param address
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * 获取
     * @return cardNumber
     */
    public String getCardNumber()
    {
        return cardNumber;
    }

    /**
     * 设置
     * @param cardNumber
     */
    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
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
        return "IdCard{id = " + id + ", documentId = " + documentId + ", name = " + name + ", nation = " + nation + ", sex = " + sex + ", address = " + address + ", cardNumber = " + cardNumber + ", allInfo = " + allInfo + "}";
    }
}
