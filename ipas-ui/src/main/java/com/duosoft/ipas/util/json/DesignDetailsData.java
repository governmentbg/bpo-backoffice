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
public class DesignDetailsData {
    private String designMainTitle;
    private String designMainTitleEn;
    private List<SingleDesignData> singleDesigns;
}
