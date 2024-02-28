package com.duosoft.ipas.controller.rest.search;

import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.search.RSearchResponse;
import bg.duosoft.ipas.rest.model.search.RSearchParam;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.mapper.RSearchParamMapper;
import com.duosoft.ipas.controller.rest.mapper.RSearchResultElementMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

/**
 * User: Georgi
 * Date: 17.9.2020 г.
 * Time: 22:21
 */
@Slf4j
@RestController
@Api(tags = {"SEARCH"})
@RequestMapping(BASE_REST_URL + "/search")
public class SearchController extends BaseRestController {
    @Autowired
    private RSearchParamMapper rSearchParamMapper;
    @Autowired
    private RSearchResultElementMapper rSearchResultElementMapper;
    @Autowired
    private IpoSearchService ipoSearchService;
    @ApiOperation(value = "Search IP Object")
    @PostMapping(value = "/ipobjects", produces = "application/json")
    @PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).IpObjectsSearchForeignObjectsData.code())")
    public RestApiResponse<RSearchResponse> searchIpObject(@RequestBody RestApiRequest<RSearchParam> searchParam) {
        Page<CSearchResult> sr = ipoSearchService.search(rSearchParamMapper.toCore(searchParam.getData()));
        RSearchResponse res = new RSearchResponse(rSearchResultElementMapper.toRestList(sr.getContent()), sr.getTotalPages(), sr.getTotalElements());
        return generateResponse(res);
    }
}
