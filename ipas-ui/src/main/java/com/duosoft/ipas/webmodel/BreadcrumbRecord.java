package com.duosoft.ipas.webmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BreadcrumbRecord {
    private String text;
    private String link;
    private String ipObjectFilingNumber;
}
