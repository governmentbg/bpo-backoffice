package com.duosoft.ipas.controller.home.offidoc_notifications;

import bg.duosoft.ipas.core.model.search.OffidocNotificationSearchParam;
import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.service.offidoc.OffidocNotificationsService;
import bg.duosoft.ipas.util.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 13.05.2022
 * Time: 16:40
 */
@Slf4j
@Controller
@RequestMapping("/offidoc/notifications")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).OffidocNotifications.code())")
public class OffidocNotificationsController {

    private static final String SESSION_FILTER_NAME = "OFFIDOC_NOTIFICATIONS_SEARCH_FILTER";
    private static final String VIEW_FILTER_NAME = "offidocNotificationsSearchFilter";
    private static final String VIEW_NOTIFICATIONS_LIST = "offidocNotifications";
    private static final String VIEW_NOTIFICATIONS_COUNT = "offidocNotificationsCount";


    @Autowired
    private OffidocNotificationsService offidocNotificationsService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DOT), true, 10);
        binder.registerCustomEditor(Date.class, customDateEditor);
    }

    @GetMapping
    public String listOffidocNotifications(Model model, HttpServletRequest request){
        OffidocNotificationSearchParam searchParam = OffidocNotificationSearchParam.createDefaultSearchParam();
        request.getSession().setAttribute(SESSION_FILTER_NAME, searchParam);

        model.addAttribute(VIEW_FILTER_NAME, searchParam);
        model.addAttribute(VIEW_NOTIFICATIONS_LIST, offidocNotificationsService.findOffidocNotifications(searchParam));
        model.addAttribute(VIEW_NOTIFICATIONS_COUNT, offidocNotificationsService.countOffidocNotifications(searchParam));
        return "home/offidoc_notifications/offidoc_notifications";
    }

    @PostMapping("/filter")
    public String filterOffidocNotifications(Model model, HttpServletRequest request, @ModelAttribute OffidocNotificationSearchParam searchParam,
                                             @RequestParam(required = false) Integer pageSize,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false, defaultValue = "true") boolean isFiltering,
                                             @RequestParam(required = false) String sortColumn,
                                             @RequestParam(required = false) String sortOrder,
                                             @RequestParam(required = false) Integer tableTotal){

        OffidocNotificationSearchParam filter;
        if(isFiltering){
            filter = searchParam;
            OffidocNotificationSearchParam currentFilter = (OffidocNotificationSearchParam)request.getSession().getAttribute(SESSION_FILTER_NAME);
            if(currentFilter != null && currentFilter.getPageSize() != null){
                filter.setPageSize(currentFilter.getPageSize());
            } else {
                filter.setPageSize(Pageable.DEFAULT_PAGE_SIZE);
            }
            filter.setPage(Pageable.DEFAULT_PAGE);
            request.getSession().setAttribute(SESSION_FILTER_NAME, filter);
        } else {
            filter = (OffidocNotificationSearchParam)request.getSession().getAttribute(SESSION_FILTER_NAME);
            if(pageSize != null) {
                filter.setPageSize(pageSize);
            }
            if(page != null) {
                filter.setPage(page);
            }
            if(sortColumn != null){
                filter.setSortColumn(sortColumn);
            }
            if(sortOrder != null){
                filter.setSortOrder(sortOrder);
            }
        }

        model.addAttribute(VIEW_FILTER_NAME, filter);
        model.addAttribute(VIEW_NOTIFICATIONS_LIST, offidocNotificationsService.findOffidocNotifications(filter));
        if(isFiltering || tableTotal == null) {
            model.addAttribute(VIEW_NOTIFICATIONS_COUNT, offidocNotificationsService.countOffidocNotifications(filter));
        } else {
            model.addAttribute(VIEW_NOTIFICATIONS_COUNT, tableTotal);
        }
        return "home/offidoc_notifications/offidoc_notifications_table";
    }


}
