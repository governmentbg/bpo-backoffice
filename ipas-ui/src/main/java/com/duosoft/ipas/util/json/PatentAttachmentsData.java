package com.duosoft.ipas.util.json;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatentAttachmentsData {

    private Integer drawingsCnt;
    private Integer drawingPubl;
    private Integer descriptionPagesCnt;
    private Integer inventionsGroupCnt;
    private Integer claimsCnt;

}
