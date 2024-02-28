package com.duosoft.ipas.webmodel.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 28.6.2019 г.
 * Time: 10:56
 */
@Data
@AllArgsConstructor
public class DepartmentWebModel {
    private boolean loaded;
    private OfficeDepartment department;
    private List<SectionWebModel> sections;
    private List<CUser> employees;

    public SectionWebModel addSection(boolean loaded, OfficeSection section, List<CUser> employees) {
        if (sections == null) {
            sections = new ArrayList<>();
        }
        SectionWebModel swm = new SectionWebModel(loaded, section, employees);
        sections.add(swm);
        return swm;
    }
}
