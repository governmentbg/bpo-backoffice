package com.duosoft.ipas.webmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatedUserdocPanelGroundTypes {
    private boolean availableType;
    private Integer id;
    private String title;
    private String description;
}
