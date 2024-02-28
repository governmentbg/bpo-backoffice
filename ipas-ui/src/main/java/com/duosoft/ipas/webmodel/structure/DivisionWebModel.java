package com.duosoft.ipas.webmodel.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 28.6.2019 г.
 * Time: 10:55
 */
@Data
@AllArgsConstructor
public class DivisionWebModel {
    private boolean loaded;
    private OfficeDivision division;
    private List<DepartmentWebModel> departments;
    private List<CUser> employees;

    public DepartmentWebModel addDepartment(boolean loaded, OfficeDepartment department, List<CUser> employees) {
        if (departments == null) {
            departments = new ArrayList<>();
        }
        DepartmentWebModel dwm = new DepartmentWebModel(loaded, department, null, employees);
        departments.add(dwm);
        return dwm;
    }
}
