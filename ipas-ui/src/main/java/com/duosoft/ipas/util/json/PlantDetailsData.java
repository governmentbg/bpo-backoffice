package com.duosoft.ipas.util.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlantDetailsData {
    private String title;
    private String englishTitle;
    private String mainAbstract;
    private String proposedDenomination;
    private String proposedDenominationEng;
    private String apprDenomination;
    private String apprDenominationEng;
    private String publDenomination;
    private String publDenominationEng;
    private String rejDenomination;
    private String rejDenominationEng;
    private String features;
    private String stability;
    private String testing;
    private Long id;
    private Integer attachmentType;
}
