package com.majy.ppdocapi.entity.dto;

import com.majy.ppdocapi.entity.po.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDetail
{
    private Case caseobj;
    private Indictment indictment;
    private Map<String, IdCard> idCards;
    private Map<String, License> licenses;
    private List<Invoice> invoices;
}
