package com.duosoft.ipas.util.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SingleDesignData {
    private String filingNumber;
    private String designSingleTitle;
    private String designSingleTitleEn;
    private String designSingleStatusCode;
    private String designSingleApplSubType;
    private List<SingleDesignDrawingsData> singleDesignDrawings;
}
