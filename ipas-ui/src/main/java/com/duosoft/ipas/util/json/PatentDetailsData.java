package com.duosoft.ipas.util.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatentDetailsData {

    private String title;
    private String englishTitle;
    private String mainAbstract;
    private String englishAbstract;
    private String description;
    private Integer drawingsCnt;
    private Integer drawingPubl;
    private Integer descriptionPagesCnt;
    private Integer inventionsGroupCnt;
    private Integer claimsCnt;
    private Integer attachmentType;

}
