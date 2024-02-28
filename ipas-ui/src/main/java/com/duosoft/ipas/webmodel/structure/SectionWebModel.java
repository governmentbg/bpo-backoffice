package com.duosoft.ipas.webmodel.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 28.6.2019 г.
 * Time: 10:56
 */
@Data
@AllArgsConstructor
public class SectionWebModel {
    private boolean loaded;
    private OfficeSection section;
    private List<CUser> employees;
}
