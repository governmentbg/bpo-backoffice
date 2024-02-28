package com.duosoft.ipas.controller.ipobjects.userdoc;


import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.util.UserDocSearchResult;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.search.UserDocSearchService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonRoleService;
import bg.duosoft.ipas.core.model.search.CUserdocSearchParam;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.util.json.ActionTypeData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/userdoc/search")
public class UserDocSearchController {
    private static final String URL = "userdoc";

    @Autowired
    private UserDocSearchService userDocSearchService;

    @Autowired
    private UserdocPersonRoleService userdocPersonRoleService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private FileTypeGroupService fileTypeGroupService;

    @Autowired
    private ActionTypeService actionTypeService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private YAMLConfig yamlConfig;

    @ModelAttribute
    public void addAttributes(ModelAndView mav) {
        mav.addObject("prop", yamlConfig.getSearch().getUserdoc());
        mav.addObject("personRoles", userdocPersonRoleService.findAll());
        mav.addObject("fileTypeGroups", fileTypeGroupService.getUserdocFileTypesGroupNamesMap());
        mav.addObject("statuses", statusService.getAllByProcessTypesOrder(userdocTypesService.getAllProcTypes()));
        mav.addObject("userdocTypes", userdocTypesService.selectUserdocTypesByUserdocName(""));
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public final ModelAndView getForm(ModelAndView mav, HttpServletRequest request) throws JsonProcessingException {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();

        Page<UserDocSearchResult> page = null;

        CUserdocSearchParam sessionObject = HttpSessionUtils.getSessionAttribute(URL, request);
        if (!Objects.isNull(sessionObject)){
            searchParam = sessionObject;

            page = userDocSearchService.search(searchParam);
        }

        ObjectMapper objMapper = new ObjectMapper();
        String json = objMapper.writeValueAsString(searchParam);

        //mav.addObject("prop", getSearchProperties());
        mav.addObject("searchParam", searchParam);
        mav.addObject("page", page);
        mav.addObject("json", json);

        mav.setViewName("ipobjects/userdoc/search/index");
        return mav;
    }

    @RequestMapping(value = {""}, method = RequestMethod.POST)
    public final ModelAndView getList(ModelAndView mav, HttpServletRequest request, @RequestParam(required = false) String data) throws JsonProcessingException {

        CUserdocSearchParam searchParam = JsonUtil.readJson(data, CUserdocSearchParam.class);

        if (searchParam.getPage() == null) {
            searchParam.page(0);
        }
        if (searchParam.getPageSize() == null) {
            searchParam.pageSize(10);
        }


        HttpSessionUtils.setSessionAttribute(URL, searchParam, request);

        Page<UserDocSearchResult> page = userDocSearchService.search(searchParam);

        ObjectMapper objMapper = new ObjectMapper();
        String json = objMapper.writeValueAsString(searchParam);

        mav.addObject("searchParam", searchParam);

        mav.addObject("page", page);
        mav.addObject("json", json);
        mav.setViewName("ipobjects/userdoc/search/list::result");
        return mav;
    }

    @RequestMapping(value = {"sort"}, method = RequestMethod.POST)
    public final ModelAndView getSortList(ModelAndView mav, HttpServletRequest request, @RequestParam(required = false) String data) throws JsonProcessingException {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();

        CUserdocSearchParam sessionObject = HttpSessionUtils.getSessionAttribute(URL, request);
        if (!Objects.isNull(sessionObject)){
            searchParam = sessionObject;
        }

        CUserdocSearchParam reqSearchParam = JsonUtil.readJson(data, CUserdocSearchParam.class);

        searchParam.page(0)
                .sortColumn(reqSearchParam.getSortColumn())
                .sortOrder(reqSearchParam.getSortOrder());

        if (Objects.isNull(searchParam.getPageSize())){
            searchParam.pageSize(10);
        }

        HttpSessionUtils.setSessionAttribute(URL, searchParam, request);

        Page<UserDocSearchResult> page = userDocSearchService.search(searchParam);

        ObjectMapper objMapper = new ObjectMapper();
        String json = objMapper.writeValueAsString(searchParam);

        mav.addObject("searchParam", searchParam);

        mav.addObject("page", page);
        mav.addObject("json", json);
        mav.setViewName("ipobjects/userdoc/search/list::result");
        return mav;
    }

    @GetMapping(value = "/userdoc-types", produces = "application/json")
    @ResponseBody
    public Map<String, String> autocompleteuserdoctypes(@RequestParam String userdocName, HttpServletRequest request) {
        Map<String, String> map = userdocTypesService.selectUserdocTypesByUserdocName(userdocName);

        return map;
    }

    @GetMapping(value = "/action-types", produces = "application/json")
    @ResponseBody
    public List<ActionTypeData> autocompleteAction(@RequestParam(required = false) String actionName) {
        if (actionName == null || actionName.isEmpty())
            actionName = "%";

        List<ActionTypeData> autocompleteList;
        List<CActionType> foundActionTypes = actionTypeService.findActionTypesByProcTypesAndActionName(
                userdocTypesService.getAllProcTypes(),
                actionName);

        if (!Objects.isNull(foundActionTypes) && foundActionTypes.size() != 0) {
            Set<ActionTypeData> autocompleteSet = new HashSet<>();
            foundActionTypes.stream()
                    .forEach(f -> autocompleteSet.add(new ActionTypeData(f)));

            autocompleteList = autocompleteSet.stream()
                    .sorted(Comparator.comparing(ActionTypeData::getActionName))
                    .collect(Collectors.toList());
        } else {
            autocompleteList = new ArrayList<>();
        }

        return autocompleteList;
    }

    @GetMapping(value = "/action-type", produces = "application/json")
    @ResponseBody
    public ActionTypeData loadAction(@RequestParam String actionType) {

        CActionType foundActionType = actionTypeService.findById(actionType);

        if (Objects.isNull(foundActionType)) {
            return new ActionTypeData();
        }

        return new ActionTypeData(foundActionType);
    }



    @RequestMapping(value = {"clear"})
    public final RedirectView clearSession(HttpServletRequest request) {

        HttpSessionUtils.removeSessionAttribute(request, URL);

        String url = request.getRequestURL().toString().replace("/clear", "");
        return new RedirectView(url);
    }
}