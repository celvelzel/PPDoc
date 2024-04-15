package com.majy.ppdoc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document
{
    private Integer document_Id;
    private String document_url;
    private String document_name;
    private String document_type;
    private String all_info;
}
