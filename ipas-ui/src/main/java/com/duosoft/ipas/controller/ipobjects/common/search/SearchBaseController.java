package com.duosoft.ipas.controller.ipobjects.common.search;


import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CLogo;
import bg.duosoft.ipas.core.model.mark.CViennaClassSimple;
import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.service.jasper.RestClientService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.*;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.service.publication.PublicationSearchService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.persistence.model.nonentity.PublicationSection;
import bg.duosoft.ipas.rest.model.search.RSearchParam;
import bg.duosoft.ipas.util.jasper.ReportParam;
import bg.duosoft.ipas.util.jasper.ReportStatus;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.config.YAMLConfig;
import com.duosoft.ipas.controller.rest.mapper.RSearchParamMapper;
import com.duosoft.ipas.util.json.ActionTypeData;
import com.duosoft.ipas.util.json.DesignLocarnoData;
import com.duosoft.ipas.util.json.PatentCpcData;
import com.duosoft.ipas.util.json.PatentIpcData;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class SearchBaseController<T extends Serializable> {
    private final static String CONTENT_DISPOSITION = "Content-Disposition";
    private final static String HEADER_FILENAME_VALUE = "attachment;filename=%s-%s.%s";

    protected YAMLConfig properties;

    @Value("${jasper.rest.report.url.search.ipos}")
    private String jasperReportUrl;

    @Autowired
    private ClassIpcService classIpcService;

    @Autowired
    private ClassCpcService classCpcService;

    @Autowired
    private LocarnoClassesService locarnoClassesService;

    @Autowired
    protected IpoSearchService service;

    @Autowired
    private CountryService countryService;

    @Autowired
    private PublicationSearchService publicationSearchService;

    @Autowired
    private ActionTypeService actionTypeService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private SignTypeService signTypeService;

    @Autowired
    private MarkService markService;

    @Autowired
    private ViennaClassSimpleService viennaClassSimpleService;

    @Autowired
    private RestClientService jasperRestClientService;

    @Autowired
    private FileTypeService fileTypeService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private SimpleUserService simpleUserService;
    @Autowired
    private RSearchParamMapper rSearchParamMapper;
    @Autowired
    private ConfigParamService configParamService;

    public SearchBaseController(YAMLConfig properties) {
        super();
        this.properties = properties;
    }

    protected abstract YAMLConfig.Search.SearchBase getSearchProperties();

    protected String getRequestForValidationType() {
        return "";
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public final ModelAndView getForm(ModelAndView mav, HttpServletRequest request) throws JsonProcessingException {
        CSearchParam searchParam = new CSearchParam();
        searchParam.setFileTypes(getSearchProperties().getFileTypes());

        searchParam.setRequestForValidationType(getRequestForValidationType());

        List<String> bulletinsByProcessTypes = null;
        List<PublicationSection> publicationSectionsByProcessTypes = null;
        Page<CSearchResult> page = null;

        CSearchParam sessionObject = HttpSessionUtils.getSessionAttribute(getSearchProperties().getMsgPrefix(), request);
        if (!Objects.isNull(sessionObject)) {
            searchParam = sessionObject;

            if (Objects.isNull(searchParam.getSelectedFileTypes()) || searchParam.getSelectedFileTypes().isEmpty()) {
                searchParam.setFileTypes(getSearchProperties().getFileTypes());
            } else {
                searchParam.setFileTypes(searchParam.getSelectedFileTypes());
            }

            searchParam.setRequestForValidationType(getRequestForValidationType());

            bulletinsByProcessTypes = publicationSearchService
                    .getBulletinsByFileTypes(getSearchProperties().getFileTypes(), sessionObject.getPublicationYear());

            publicationSectionsByProcessTypes = publicationSearchService
                    .getPublicationSectionsByFileTypes(getSearchProperties().getFileTypes(), sessionObject.getPublicationYear(), sessionObject.getPublicationBulletin());

            page = service.search(searchParam);
        } else {

            bulletinsByProcessTypes = publicationSearchService
                    .getBulletinsByFileTypes(getSearchProperties().getFileTypes(), searchParam.getPublicationYear());

            publicationSectionsByProcessTypes = publicationSearchService
                    .getPublicationSectionsByFileTypes(getSearchProperties().getFileTypes(), searchParam.getPublicationBulletin());

        }

        ObjectMapper objMapper = new ObjectMapper();
        String json = objMapper.writeValueAsString(searchParam);

        mav.addObject("prop", getSearchProperties());
        mav.addObject("fileTypes", fileTypeService.findAllByFileTypInOrderByFileTypeName(getSearchProperties().getFileTypes()));
        mav.addObject("searchParam", searchParam);
        mav.addObject("nationalities", countryService.getCountryMap());
        mav.addObject("statuses", statusService.getAllByFileTypesOrder(getSearchProperties().getFileTypes()));
        mav.addObject("publicationYears", publicationSearchService.getYearsByFileTypes(getSearchProperties().getFileTypes()));
        mav.addObject("publicationBulletins", bulletinsByProcessTypes);
        mav.addObject("publicationSects", publicationSectionsByProcessTypes);
        mav.addObject("signTypesMap", signTypeService.getSignTypesMap());
        mav.addObject("applTypes", applicationTypeService.getApplicationTypesMapOrderByApplTypeName(getSearchProperties().getFileTypes()));
        mav.addObject("applSubTypes", applicationTypeService.getSubTypesByFileTypesOrderByApplTypeName(getSearchProperties().getFileTypes()));
        mav.addObject("page", page);
        mav.addObject("json", json);

        mav.setViewName("ipobjects/common/search/index");
        return mav;
    }

    @RequestMapping(value = {""}, method = RequestMethod.POST)
    public final ModelAndView getList(ModelAndView mav,
                                      HttpServletRequest request,
                                      @RequestParam(required = false) String data) throws JsonProcessingException {

        CSearchParam searchParam = JsonUtil.readJson(data, CSearchParam.class);

        if (Objects.isNull(searchParam.getSelectedFileTypes()) || searchParam.getSelectedFileTypes().isEmpty()) {
            searchParam.setFileTypes(getSearchProperties().getFileTypes());
        } else {
            searchParam.setFileTypes(searchParam.getSelectedFileTypes());
        }

        searchParam.setRequestForValidationType(getRequestForValidationType());

        if (searchParam.getPage() == null) {
            searchParam.page(0);
        }
        if (searchParam.getPageSize() == null) {
            searchParam.pageSize(10);
        }


        HttpSessionUtils.setSessionAttribute(getSearchProperties().getMsgPrefix(), searchParam, request);

        Page<CSearchResult> page = service.search(searchParam);

        ObjectMapper objMapper = new ObjectMapper();
        String json = objMapper.writeValueAsString(searchParam);

        mav.addObject("searchParam", searchParam);
        mav.addObject("prop", getSearchProperties());

        mav.addObject("page", page);
        mav.addObject("json", json);
        mav.setViewName("ipobjects/common/search/list::result");
        return mav;
    }

    @RequestMapping(value = {"sort"}, method = RequestMethod.POST)
    public final ModelAndView getSortList(ModelAndView mav,
                                          HttpServletRequest request,
                                          @RequestParam(required = false) String data) throws JsonProcessingException {
        CSearchParam searchParam = new CSearchParam();
        searchParam.setFileTypes(getSearchProperties().getFileTypes());
        searchParam.setRequestForValidationType(getRequestForValidationType());

        CSearchParam sessionObject = HttpSessionUtils.getSessionAttribute(getSearchProperties().getMsgPrefix(), request);
        if (!Objects.isNull(sessionObject)) {
            searchParam = sessionObject;

            if (Objects.isNull(searchParam.getSelectedFileTypes()) || searchParam.getSelectedFileTypes().isEmpty()) {
                searchParam.setFileTypes(getSearchProperties().getFileTypes());
            }

            searchParam.setRequestForValidationType(getRequestForValidationType());
        }

        CSearchParam reqSearchParam = JsonUtil.readJson(data, CSearchParam.class);
        reqSearchParam.setFileTypes(getSearchProperties().getFileTypes());

        searchParam.page(0)
                .sortColumn(reqSearchParam.getSortColumn())
                .sortOrder(reqSearchParam.getSortOrder());

        if (searchParam.getPageSize() == null) {
            searchParam.pageSize(10);
        }

        HttpSessionUtils.setSessionAttribute(getSearchProperties().getMsgPrefix(), searchParam, request);

        Page<CSearchResult> page = service.search(searchParam);

        ObjectMapper objMapper = new ObjectMapper();
        String json = objMapper.writeValueAsString(searchParam);

        mav.addObject("searchParam", searchParam);
        mav.addObject("prop", getSearchProperties());

        mav.addObject("page", page);
        mav.addObject("json", json);
        mav.setViewName("ipobjects/common/search/list::result");
        return mav;
    }

    @RequestMapping(value = {"clear"})
    public final RedirectView clearSession(HttpServletRequest request) {

        HttpSessionUtils.removeSessionAttribute(request, getSearchProperties().getMsgPrefix());

        String url = request.getRequestURL().toString().replace("/clear", "");
        return new RedirectView(url);
    }

    @RequestMapping(value = {"appl-sub-typ"}, method = RequestMethod.POST)
    public final ModelAndView getApplSubTyp(HttpServletRequest request, ModelAndView mav, @RequestParam String applTyp) {
        List<CApplicationSubType> options;

        if (Objects.isNull(applTyp) || applTyp.isBlank()) {
            options = applicationTypeService
                    .getSubTypesByFileTypesOrderByApplTypeName(getSearchProperties().getFileTypes());
        } else {
            options = applicationTypeService
                    .getSubTypesByFileTypesApplTypOrderByApplTypeName(getSearchProperties().getFileTypes(), applTyp);
        }

        mav.addObject("applSubTypes", options);
        mav.addObject("selectedApplSubTyp", "");

        mav.setViewName("ipobjects/common/search/fragments::applSubType");

        return mav;
    }

    @RequestMapping(value = {"bulletin"}, method = RequestMethod.POST)
    public final ModelAndView getBulletins(ModelAndView mav, @RequestParam(required = false) String year) {

        List<String> options = publicationSearchService
                .getBulletinsByFileTypes(getSearchProperties().getFileTypes(), year);

        mav.addObject("options", options);
        mav.addObject("ipoObjectType", getSearchProperties().getMsgPrefix());

        mav.setViewName("ipobjects/common/search/fragments::publication-bulletin");

        return mav;
    }

    @RequestMapping(value = {"section"}, method = RequestMethod.POST)
    public final ModelAndView getSections(ModelAndView mav,
                                          @RequestParam(required = false) String year,
                                          @RequestParam(required = false) String bulletin) {

        List<PublicationSection> options = publicationSearchService
                .getPublicationSectionsByFileTypes(getSearchProperties().getFileTypes(), year, bulletin);
        mav.addObject("options", options);
        mav.addObject("ipoObjectType", getSearchProperties().getMsgPrefix());

        mav.setViewName("ipobjects/common/search/fragments::publication-sect");

        return mav;
    }

    @GetMapping(value = "/action-types", produces = "application/json")
    @ResponseBody
    public List<ActionTypeData> autocompleteAction(@RequestParam(required = false) String actionName) {
        if (actionName == null || actionName.isEmpty())
            actionName = "%";

        List<ActionTypeData> autocompleteList;
        List<CActionType> foundActionTypes = actionTypeService
                .findActionTypesByFileTypesAndActionName(getSearchProperties().getFileTypes(), actionName);

        if (!Objects.isNull(foundActionTypes) && foundActionTypes.size() != 0) {
            Set<ActionTypeData> autocompleteSet = new HashSet<>();
            foundActionTypes.stream()
                    .forEach(f -> autocompleteSet.add(new ActionTypeData(f)));
            autocompleteList = new ArrayList<>(autocompleteSet);
            autocompleteList.stream()
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


    @GetMapping(value = "/user", produces = "application/json")
    @ResponseBody
    public List<CUser> loadResponsibleUserByUserName(@RequestParam(required = false) String userName) {
        if (userName == null || userName.isEmpty())
            userName = "%";

        List<CUser> foundUsers = simpleUserService.findByUsernameLike(userName, false);

        if (Objects.isNull(foundUsers)) {
            return new ArrayList<>();
        }

        return foundUsers;
    }


    @GetMapping(value = "/user-id", produces = "application/json")
    @ResponseBody
    public CUser loadResponsibleUserById(@RequestParam(required = false) Integer userId) {
        if (userId == null)
            return new CUser();

        CUser foundUser = simpleUserService.findSimpleUserById(userId);

        if (Objects.isNull(foundUser)) {
            return new CUser();
        }

        return foundUser;
    }


    @RequestMapping(value = {"/img/{fileSeq}/{fileTyp}/{fileSer}/{fileNbr}"}, method = RequestMethod.GET)
    public final void loadImg(HttpServletResponse response,
                              @PathVariable("fileSeq") String fileSeq,
                              @PathVariable("fileTyp") String fileTyp,
                              @PathVariable("fileSer") Integer fileSer,
                              @PathVariable("fileNbr") Integer fileNbr) {
        CFileId cFileId = new CFileId();
        cFileId.setFileSeq(fileSeq);
        cFileId.setFileType(fileTyp);
        cFileId.setFileSeries(fileSer);
        cFileId.setFileNbr(fileNbr);

        CLogo cLogo = markService.selectMarkLogo(cFileId);
        if (Objects.nonNull(cLogo) && cLogo.isLoaded())
            writeData(response, cLogo.getLogoData());
    }

    protected void writeData(HttpServletResponse response, byte[] data) {
        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            response.flushBuffer();
            outputStream.close();
        } catch (IOException e) {
            //TODO
            log.error("Error writing image to response!", e);
        }
    }

    @GetMapping(value = "/ipc", produces = "application/json")
    @ResponseBody
    public List<PatentIpcData> autocompleteIpcs(@RequestParam String ipcNumber, @RequestParam(required = false) Boolean withLikeCriteria, HttpServletRequest request) {
        if (ipcNumber.contains(" - ")) {
            ipcNumber = ipcNumber.replace(" - ", "");
        }
        List<PatentIpcData> autocompleteList = new ArrayList<>();
        List<CIpcClass> foundIpcs = new ArrayList<>();
        if (Objects.nonNull(withLikeCriteria) && withLikeCriteria) {
            foundIpcs = classIpcService.findAllIpcClassesByIpcNumber(ipcNumber, properties.getMaxAutoCompletResults());
        } else {
            CIpcClass ipcClassByIpcNumber = classIpcService.findIpcClassByIpcNumber(ipcNumber);
            if (Objects.nonNull(ipcClassByIpcNumber)) {
                foundIpcs.add(ipcClassByIpcNumber);
            }
        }

        if (!CollectionUtils.isEmpty(foundIpcs)) {
            CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.IPC_LATEST_VERSION);
            foundIpcs.stream().forEach(f -> autocompleteList.add(new PatentIpcData(f, cConfigParam.getValue())));
        } else {
            autocompleteList.add(new PatentIpcData(ipcNumber.toUpperCase()));
        }
        return autocompleteList;
    }


    @GetMapping(value = "/cpc", produces = "application/json")
    @ResponseBody
    public List<PatentCpcData> autocompleteCpcs(@RequestParam String cpcNumber, @RequestParam(required = false) Boolean withLikeCriteria, HttpServletRequest request) {
        if (cpcNumber.contains(" - ")) {
            cpcNumber = cpcNumber.replace(" - ", "");
        }
        List<PatentCpcData> autocompleteList = new ArrayList<>();
        List<CCpcClass> foundCpcs = new ArrayList<>();
        if (Objects.nonNull(withLikeCriteria) && withLikeCriteria) {
            foundCpcs = classCpcService.findAllCpcClassesByCpcNumber(cpcNumber, properties.getMaxAutoCompletResults());
        } else {
            CCpcClass cpcClassByCpcNumber = classCpcService.findCpcClassByCpcNumber(cpcNumber);
            if (Objects.nonNull(cpcClassByCpcNumber)) {
                foundCpcs.add(cpcClassByCpcNumber);
            }

        }
        if (!CollectionUtils.isEmpty(foundCpcs)) {
            CConfigParam cConfigParam = configParamService.selectExtConfigByCode(configParamService.CPC_LATEST_VERSION);
            foundCpcs.stream().forEach(f -> autocompleteList.add(new PatentCpcData(f, cConfigParam.getValue())));
        } else {
            autocompleteList.add(new PatentCpcData(cpcNumber.toUpperCase()));
        }

        return autocompleteList;
    }

    @GetMapping(value = "/vienna-classes", produces = "application/json")
    @ResponseBody
    public List<CViennaClassSimple> getViennaClasses(Model model, @RequestParam(required = false, name = "vienna-code") String viennaCode) {
        String code = viennaCode == null ? "" : viennaCode;
        List<CViennaClassSimple> cViennaClasses = viennaClassSimpleService.findAllByViennaCode(code, properties.getMaxAutoCompletResults());

        return cViennaClasses;
    }

    @GetMapping(value = "/locarno", produces = "application/json")
    @ResponseBody
    public List<DesignLocarnoData> autocompleteLocarnoClasses(@RequestParam String locarnoClass, HttpServletRequest request) {
        List<DesignLocarnoData> autocompleteList = new ArrayList<>();

        List<CLocarnoClasses> foundLocarnoClasses = locarnoClassesService.findByLocarnoClassCode(locarnoClass, properties.getMaxAutoCompletResults());

        foundLocarnoClasses.stream().forEach(f -> autocompleteList.add(new DesignLocarnoData(f)));
        return autocompleteList;
    }

    @RequestMapping(path = "/create-report", method = RequestMethod.GET)
    @ResponseBody
    public ReportParam createReport(HttpServletRequest request) throws IOException {

        CSearchParam sessionObject = HttpSessionUtils.getSessionAttribute(getSearchProperties().getMsgPrefix(), request);
        RequestBody requestBody = new RequestBody();

        requestBody.setUsername(SecurityUtils.getLoggedUsername());
        if (sessionObject != null) {
            RSearchParam rSearchParam = rSearchParamMapper.toRest(sessionObject);

            rSearchParam.setPageSize(0);

            if (CollectionUtils.isEmpty(rSearchParam.getSelectedFileTypes())) {
                rSearchParam.setFileTypes(getSearchProperties().getFileTypes());
            } else {
                rSearchParam.setFileTypes(rSearchParam.getSelectedFileTypes());
            }

            requestBody.setData(rSearchParam);
        }

        ReportParam<RequestBody> reportParam = new ReportParam<>();

        reportParam.setLeadingZero(getSearchProperties().getLeadingZero());
        reportParam.setRequestForValidationType(getSearchProperties().getRequestForValidation());
        reportParam.setFileTypes(getSearchProperties().getFileTypes().stream().collect(Collectors.joining(",")));
        reportParam.setRequestBody(requestBody);
        reportParam.setExportFileFormat(request.getParameter("export-file-format"));
        reportParam.setReportUrl(jasperReportUrl);

        reportParam = jasperRestClientService.runReport(reportParam);

        return reportParam;
    }

    @RequestMapping(path = "/check-report", method = RequestMethod.GET)
    @ResponseBody
    public ReportStatus checkReport(
            HttpServletRequest request) {
        ReportParam reportParam = ReportParam.getReportParam(request);

        ReportStatus reportStatus = jasperRestClientService.checkReport(reportParam);

        return reportStatus;
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ReportParam reportParam = ReportParam.getReportParam(request);

        byte[] reportByteArr = jasperRestClientService.getReport(reportParam);

        response.setHeader(CONTENT_DISPOSITION,
                String.format(HEADER_FILENAME_VALUE,
                        getSearchProperties().getExportFileName(),
                        new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(new Date()),
                        reportParam.getExportFileFormat()));

        response.getOutputStream().write(reportByteArr);
    }

    @Getter
    @Setter
    private static class RequestBody {
        private String username;

        private RSearchParam data;
    }
}
