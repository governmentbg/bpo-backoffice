package com.duosoft.ipas.webmodel.structure;

import bg.duosoft.ipas.core.model.userdoc.court_appeal.CUserdocCourtAppeal;
import bg.duosoft.ipas.core.service.nomenclature.CourtsService;
import bg.duosoft.ipas.core.service.nomenclature.JudicialActTypeService;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserdocCourtAppealWebModel {
    private Integer courtAppealId;
    private String courtCaseNbr;
    private String courtLink;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date courtCaseDate;
    private String judicialActNbr;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date judicialActDate;
    private String courtName;
    private Integer actTypeId;


    public static void initWebObject(CUserdocCourtAppeal cAppeal, UserdocCourtAppealWebModel webAppealObject) {
        webAppealObject.setCourtAppealId(cAppeal.getCourtAppealId());
        webAppealObject.setCourtCaseNbr(cAppeal.getCourtCaseNbr());
        webAppealObject.setCourtLink(cAppeal.getCourtLink());
        webAppealObject.setCourtCaseDate(cAppeal.getCourtCaseDate());
        webAppealObject.setJudicialActNbr(cAppeal.getJudicialActNbr());
        webAppealObject.setJudicialActDate(cAppeal.getJudicialActDate());
        if (Objects.nonNull(cAppeal.getCourt())) {
            webAppealObject.setCourtName(cAppeal.getCourt().getName() == null ? "" : cAppeal.getCourt().getName());
        }
        if (Objects.nonNull(cAppeal.getJudicialActType())) {
            webAppealObject.setActTypeId(cAppeal.getJudicialActType().getId());
        }
    }

    public static void initCObject(CUserdocCourtAppeal cAppeal, UserdocCourtAppealWebModel webAppealObject, CourtsService courtsService, JudicialActTypeService judicialActTypeService) {
        cAppeal.setCourtAppealId(webAppealObject.getCourtAppealId());
        cAppeal.setCourtLink(StringUtils.isEmpty(webAppealObject.getCourtLink()) ? null : webAppealObject.getCourtLink());
        cAppeal.setCourtCaseNbr(Objects.nonNull(webAppealObject.getCourtCaseNbr()) && !webAppealObject.getCourtCaseNbr().isEmpty() ? webAppealObject.getCourtCaseNbr() : null);
        cAppeal.setCourtCaseDate(webAppealObject.getCourtCaseDate());
        cAppeal.setJudicialActNbr(Objects.nonNull(webAppealObject.getJudicialActNbr()) && !webAppealObject.getJudicialActNbr().isEmpty() ? webAppealObject.getJudicialActNbr() : null);
        cAppeal.setJudicialActDate(webAppealObject.getJudicialActDate());
        if (Objects.nonNull(webAppealObject.getCourtName())) {
            cAppeal.setCourt(courtsService.selectByName(webAppealObject.getCourtName()).get(0));
        }
        if (Objects.nonNull(webAppealObject.getActTypeId())) {
            cAppeal.setJudicialActType(judicialActTypeService.findById(webAppealObject.getActTypeId()));
        }
    }
}
