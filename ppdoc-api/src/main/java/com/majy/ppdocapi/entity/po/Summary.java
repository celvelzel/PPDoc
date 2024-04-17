package com.majy.ppdocapi.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Summary
{
    private Integer summary_id;
    private Integer document_id;
    private String summary_type;
    private String summary_content;
}
