package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.payments.CNotLinkedPayment;
import bg.duosoft.ipas.core.service.payments.PaymentsService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.filter.NotLinkedPaymentsFilter;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import liquibase.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 13.01.2021
 * Time: 12:12
 */
@Controller
@RequestMapping("/not-linked-payments")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).NotLinkedPaymentsList.code())")
public class NotLinkedPaymentsHomeController {
    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/list")
    public String getNotLinkedPaymentsList(Model model, NotLinkedPaymentsFilter filter) {
        addNotLinkedPaymentsToModel(paymentsService, model, filter, userService, 200);
        return "home/payments/not_linked_payments";
    }


    @RequestMapping(value = "/search")
    public String search(Model model, NotLinkedPaymentsFilter filter) {
        addNotLinkedPaymentsToModel(paymentsService, model, filter, userService, 200);
        return "home/payments/not_linked_payments_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateNotLinkedPaymentsTable(Model model, NotLinkedPaymentsFilter filter) {
        addNotLinkedPaymentsToModel(paymentsService, model, filter, userService, 200);
        return "home/payments/not_linked_payments_table::table";
    }


    private static void constructNotLinkedPaymentsDependingOnRoles(List<CNotLinkedPayment> notLinkedPayments) {
        if (!CollectionUtils.isEmpty(notLinkedPayments)) {
            if (!SecurityUtils.hasRights(SecurityRole.MarkViewOwn)) {
                notLinkedPayments.removeIf(r -> BasicUtils.isMarkLikeFileType(r.getFileId().getFileType()));
            }
            if (!SecurityUtils.hasRights(SecurityRole.PatentViewOwn)) {
                notLinkedPayments.removeIf(r -> BasicUtils.isPatentLikeFileType(r.getFileId().getFileType()));
            }
        }
    }


    public static void addNotLinkedPaymentsToModel(PaymentsService paymentsService, Model model, NotLinkedPaymentsFilter filter, UserService userService, Integer maxCount) {
        List<Integer> responsibleUsers = userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId());
        List<CNotLinkedPayment> notLinkedPayments;

        if (SecurityUtils.hasRights(SecurityRole.PaymentsExtendedRights)) {
            notLinkedPayments = paymentsService.getAllNotLinkedPayments(filter.getDateFrom(), filter.getDateTo());
            constructNotLinkedPaymentsDependingOnRoles(notLinkedPayments);
        } else {
            notLinkedPayments = paymentsService.getNotLinkedPaymentsPerResponsibleUsers(filter.getDateFrom(), filter.getDateTo(), responsibleUsers);
        }

        model.addAttribute("notLinkedPaymentsCount", notLinkedPayments.size());
        model.addAttribute("notLinkedPaymentsList", maxCount == null ? notLinkedPayments : notLinkedPayments.stream().limit(maxCount).collect(Collectors.toList()));
        model.addAttribute("notLinkedPaymentsFilter", filter);
    }
}
