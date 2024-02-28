package com.duosoft.ipas.controller.admin.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.*;
import com.duosoft.ipas.webmodel.structure.DepartmentWebModel;
import com.duosoft.ipas.webmodel.structure.DivisionWebModel;
import com.duosoft.ipas.webmodel.structure.SectionWebModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: ggeorgiev
 * Date: 30.7.2019 г.
 * Time: 12:10
 */
@Controller
@RequestMapping(value = "/structure")
public class StructureListController extends StructureControllerBase {
    private static final String NAV_ATTRIBUTE = "navAttribute"; //pri redakciq na nqkoi element ot dyrvoto (division, department, section, user), pri vry6tane nazad, otvarq dyrvoto do posledno redaktiraniq element. Za celta. Pri natiskane na edit na nqkoj ot node-ovete, se zapisva v sesiqta i koi e otvoreniq node...

    //starting point of structure list!
    @GetMapping("/list")
    public String listDivision(Model model, HttpSession session) {
        if (session.getAttribute(NAV_ATTRIBUTE) != null) {
            String nav = (String) session.getAttribute(NAV_ATTRIBUTE);
            session.removeAttribute(NAV_ATTRIBUTE);
            return generateNavList(model, nav);
        } else {
            return generateHomeList(model);
        }


    }

    //generira originalnata stranica - sydryjashta samo spisyka s divisions!
    private String generateHomeList(Model model) {
        List<OfficeDivision> divisions = officeDivisionService.getDivisionsByPartOfName(null, false);
        List<DivisionWebModel> divisionWebModelList = divisions.stream().map(d -> new DivisionWebModel(false, d, null, null)).collect(Collectors.toList());
        model.addAttribute("divisions", divisionWebModelList);
        model.addAttribute("collapse", false);
        return "structure/list/list";
    }


    public String generateNavList(Model model, String nav) {
        OfficeStructureId sid;
        if (nav.startsWith("u")) {//it's user
            User u = userService.getUser(Integer.parseInt(nav.replace("u-", "")));
            sid = new OfficeStructureId(u.getOfficeDivisionCode(), u.getOfficeDepartmentCode(), u.getOfficeSectionCode());
        } else {//it's structure node...
            String[] parts = nav.split("-");
            sid = new OfficeStructureId(parts[0], parts.length > 1 ? parts[1] : null, parts.length > 2 ? parts[2] : null);
        }

        List<OfficeDivision> divisions = officeDivisionService.getDivisionsByPartOfName(null, false);
        List<DivisionWebModel> divisionWebModelList = new ArrayList<>();
        for (OfficeDivision d : divisions) {
            if (d.getOfficeStructureId().getFullDivisionCode().equals(sid.getFullDivisionCode())) {
                List<OfficeDepartment> deps = officeDepartmentService.getDepartmentsOfDivision(sid.getOfficeDivisionCode(), false);
                List<CUser> emps = simpleUserService.getUsers(sid.getOfficeDivisionCode(), null, null, true, false);
                DivisionWebModel divWm = new DivisionWebModel(true, d, null, emps);
                divisionWebModelList.add(divWm);
                for (OfficeDepartment dep : deps) {
                    if (dep.getOfficeStructureId().getFullDepartmentCode().equals(sid.getFullDepartmentCode())) {
                        List<CUser> depEmps = simpleUserService.getUsers(sid.getOfficeDivisionCode(), sid.getOfficeDepartmentCode(), null, true, false);
                        List<OfficeSection> sections = officeSectionService.getSectionsOfDepartment(sid.getOfficeDivisionCode(), sid.getOfficeDepartmentCode(), false);
                        DepartmentWebModel depWm = divWm.addDepartment(true, dep, depEmps);
                        for (OfficeSection s : sections) {
                            if (s.getOfficeStructureId().getFullSectionCode().equals(sid.getFullSectionCode())) {
                                List<CUser> sectEmps = simpleUserService.getUsers(sid.getOfficeDivisionCode(), sid.getOfficeDepartmentCode(), sid.getOfficeSectionCode(), true, false);
                                depWm.addSection(true, s, sectEmps);
                            } else {
                                depWm.addSection(false, s, null);
                            }
                        }

                    } else {
                        divWm.addDepartment(false, dep, null);
                    }

                }
            } else {
                divisionWebModelList.add(new DivisionWebModel(false, d, null, null));
            }
        }
        model.addAttribute("nav", nav);
        model.addAttribute("divisions", divisionWebModelList);
        model.addAttribute("collapse", false);

        return "structure/list/list";
    }

    //search structure nodes + users for the given string
    @RequestMapping("/find")
    public String find(@RequestParam("partOfName") String partOfName, Model model) {

        List<OfficeDivision> divisions = officeDivisionService.getDivisionsByPartOfName(partOfName, false);
        List<OfficeDepartment> departments = officeDepartmentService.getDepartmentsByPartOfName(partOfName, false);
        List<OfficeSection> sections = officeSectionService.getSectionsByPartOfName(partOfName, false);
        List<User> users = userService.getUsersByPartOfName(partOfName);

        //groups divisions, departments, sections by code!
        Map<OfficeStructureId, OfficeDivision> divisionMap = divisions
                .stream()
                .collect(Collectors.toMap(OfficeDivision::getOfficeStructureId, Function.identity()));
        Map<OfficeStructureId, OfficeDepartment> departmentMap = departments
                .stream()
                .collect(Collectors.toMap(OfficeDepartment::getOfficeStructureId, Function.identity()));
        Map<OfficeStructureId, OfficeSection> sectionMap = sections
                .stream()
                .collect(Collectors.toMap(OfficeSection::getOfficeStructureId, Function.identity()));

        //razpredelq user-ite po section/division/department
        Map<OfficeStructureId, List<User>> usersMap = users.stream().collect(Collectors.groupingBy(u -> new OfficeStructureId(u.getOfficeDivisionCode(), u.getOfficeDepartmentCode(), u.getOfficeSectionCode())));


        //dopylva lipsvashtite sections, divisions, departments, kato vzema vsichki keys i proverqva po vseki edin ot tqh dali ima section/department/division!
        Stream.of(usersMap.keySet(), sectionMap.keySet(), departmentMap.keySet(), divisionMap.keySet()).flatMap(Set::stream).distinct().forEach(k -> {
            if (k.isSection()) {//trqbva da se potyrsi dali imame department, division, section s toq code!
                addDivisionIfNecessary(divisionMap, k);
                addDepartmentIfNecessary(departmentMap, k);
                addSectionIfNecessary(sectionMap, k);
            } else if (k.isDepartment()) {
                addDivisionIfNecessary(divisionMap, k);
                addDepartmentIfNecessary(departmentMap, k);
            } else if (k.isDivision()) {
                addDivisionIfNecessary(divisionMap, k);
            } else {
                throw new RuntimeException("Unknown key...." + k);
            }
        });


        //generira webmodela
        //vyrti po vsichki divisions, sled tova po vsichki departments kym toq division, sled tova po vseki edin section v dadeniq department....
        List<DivisionWebModel> res = new ArrayList<>();
        for (Map.Entry<OfficeStructureId, OfficeDivision> divisionEntry : divisionMap.entrySet()) {
            List<DepartmentWebModel> depsWM = new ArrayList<>();
            departmentMap.entrySet().stream().filter(e -> divisionEntry.getKey().equals(e.getKey())).forEach(departmentEntry -> {
                List<SectionWebModel> sectionsWM = new ArrayList<>();
                sectionMap.entrySet().stream().filter(e -> departmentEntry.getKey().equals(e.getKey())).forEach(sectionEntry -> {
                    List<User> u = usersMap.get(sectionEntry.getKey());
                    sectionsWM.add(new SectionWebModel(!CollectionUtils.isEmpty(u), sectionEntry.getValue(), u == null ? null : u.stream().map(r -> (CUser)r).collect(Collectors.toList())));
                });
                List<User> u = usersMap.get(departmentEntry.getKey());
                depsWM.add(new DepartmentWebModel(!CollectionUtils.isEmpty(u) || !CollectionUtils.isEmpty(sectionsWM), departmentEntry.getValue(), sectionsWM, u == null ? null : u.stream().map(r -> (CUser)r).collect(Collectors.toList())));
            });
            List<User> u = usersMap.get(divisionEntry.getKey());
            res.add(new DivisionWebModel(!CollectionUtils.isEmpty(u) || !CollectionUtils.isEmpty(depsWM), divisionEntry.getValue(), depsWM, u == null ? null : u.stream().map(r -> (CUser)r).collect(Collectors.toList())));
        }

        model.addAttribute("divisions", res);
        model.addAttribute("collapse", true);

        return "structure/list/list";

    }


    //list section node
    @RequestMapping("/list/section-node")
    public String listSection(@RequestParam("divisionCode") String divisionCode, @RequestParam("departmentCode") String departmentCode, @RequestParam("sectionCode") String sectionCode/*, @RequestParam("index") String index*/, Model model) {
        List<CUser> users = simpleUserService.getUsers(divisionCode, departmentCode, sectionCode, true, false);
        OfficeSection section = officeSectionService.getSection(divisionCode, departmentCode, sectionCode);

        SectionWebModel sectionModel = new SectionWebModel(true, section, users);
        model.addAttribute("section", sectionModel);
        return "structure/list/list_section :: list-section";
    }

    //list department node
    @RequestMapping("/list/department-node")
    public String listDepartment(@RequestParam("divisionCode") String divisionCode, @RequestParam("departmentCode") String departmentCode/*, @RequestParam("index") String index*/, Model model) {
        OfficeDepartment department = officeDepartmentService.getDepartment(divisionCode, departmentCode);

        List<OfficeSection> sections = officeSectionService.getSectionsOfDepartment(divisionCode, departmentCode, false);
        List<CUser> users = simpleUserService.getUsers(divisionCode, departmentCode, null, true, false);

        DepartmentWebModel departmentModel = new DepartmentWebModel(true, department, null, users);
        sections.forEach(s -> departmentModel.addSection(false, s, null));
        model.addAttribute("department", departmentModel);

        return "structure/list/list_department :: list-department";
    }

    //list section node
    @RequestMapping("/list/division-node")
    public String listDivision(@RequestParam("divisionCode") String divisionCode, Model model) {
        List<CUser> users = simpleUserService.getUsers(divisionCode, null, null, true, false);
        OfficeDivision division = officeDivisionService.getDivision(divisionCode);
        DivisionWebModel divisionModel = new DivisionWebModel(true, division, null, users);
        List<OfficeDepartment> departments = officeDepartmentService.getDepartmentsOfDivision(divisionCode, false);
        departments.forEach(d -> divisionModel.addDepartment(false, d, null));

        model.addAttribute("division", divisionModel);
        return "structure/list/list_division :: list-division";
    }


    //updates the nav element (currently opened node for edit/view/)
    @RequestMapping("/list/updateNav")
    @ResponseBody
    public String updateNavElement(@RequestParam("nav") String nav, HttpSession session) {
        session.setAttribute(NAV_ATTRIBUTE, nav);
        return "done...";
    }

    private void addDivisionIfNecessary(Map<OfficeStructureId, OfficeDivision> map, OfficeStructureId newKey) {
        map.computeIfAbsent(newKey, (k) -> officeDivisionService.getDivision(k.getOfficeDivisionCode()));
    }

    private void addDepartmentIfNecessary(Map<OfficeStructureId, OfficeDepartment> map, OfficeStructureId newKey) {
        map.computeIfAbsent(newKey, (k) -> officeDepartmentService.getDepartment(k.getOfficeDivisionCode(), k.getOfficeDepartmentCode()));
    }

    private void addSectionIfNecessary(Map<OfficeStructureId, OfficeSection> map, OfficeStructureId newKey) {
        map.computeIfAbsent(newKey, (k) -> officeSectionService.getSection(k.getOfficeDivisionCode(), k.getOfficeDepartmentCode(), k.getOfficeSectionCode()));
    }
}
