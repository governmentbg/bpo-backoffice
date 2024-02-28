package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.CExtLiabilityDetailExtended;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.payments.CNotLinkedPayment;
import bg.duosoft.ipas.core.service.payments.PaymentsService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.util.filter.LastPaymentsFilter;
import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.util.filter.NotLinkedPaymentsFilter;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: ggeorgiev
 * Date: 13.01.2021
 * Time: 12:12
 */
@Controller
@RequestMapping("/last-payments")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).LastPaymentsList.code())")
public class PaymentsHomeController {
    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/list")
    public String getLastPaymentsList(Model model,LastPaymentsFilter filter) {
        addLastPaymentsToModel(paymentsService, model, filter,userService);
        return "home/payments/last_payments";
    }

    @RequestMapping(value = "/mark-as-processed")
    public String markAsProcessed(Model model, @RequestParam String filingNumber,@RequestParam Integer id,LastPaymentsFilter filter) {
        CFileId fileId = BasicUtils.createCFileId(filingNumber);
        paymentsService.setLiabilityDetailAsProcessed(fileId.getFileSeq(),fileId.getFileType(),fileId.getFileSeries(),fileId.getFileNbr(),id);
        model.addAttribute("successMessage",messageSource.getMessage("payment.marked.as.processed", new String[]{filingNumber}, LocaleContextHolder.getLocale()));
        return updateMyObjectsTable(model,filter);
    }

    @RequestMapping(value = "/search")
    public String search(Model model, LastPaymentsFilter filter) {
        filter.setPage(Pageable.DEFAULT_PAGE);
        filter.setPageSize(Pageable.DEFAULT_PAGE_SIZE);
        addLastPaymentsToModel(paymentsService, model, filter,userService);

        return "home/payments/last_payments_table::table";
    }

    @RequestMapping(value = "/update-table")
    public String updateMyObjectsTable(Model model, LastPaymentsFilter filter) {
        addLastPaymentsToModel(paymentsService, model, filter,userService);
        return "home/payments/last_payments_table::table";
    }

    public static void addLastPaymentsToModel(PaymentsService paymentsService, Model model, LastPaymentsFilter filter, UserService userService) {

        List<Integer> responsibleUsers = userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId());

        List<CExtLiabilityDetailExtended> payments = paymentsService.getLastPayments(filter.getDateLastPaymentFrom(), filter.getDateLastPaymentTo(), responsibleUsers, PaymentsService.PAYMENT_NOT_PROCESSED, (filter.getPage() - 1) * filter.getPageSize(), filter.getPageSize(), filter.getSortColumn(), filter.getSortOrder());

        model.addAttribute("lastPaymentsList", payments);
        model.addAttribute("lastPaymentsCount", paymentsService.getLastPaymentsCount(filter.getDateLastPaymentFrom(), filter.getDateLastPaymentTo(), responsibleUsers, PaymentsService.PAYMENT_NOT_PROCESSED));
        model.addAttribute("lastPaymentsFilter", filter);
    }
}
