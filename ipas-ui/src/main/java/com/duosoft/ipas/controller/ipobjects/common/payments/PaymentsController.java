package com.duosoft.ipas.controller.ipobjects.common.payments;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.payments.CLiabilityDetal;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.integration.payments.service.PaymentsIntegrationService;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.file.CFileSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    private PaymentsIntegrationService paymentsIntegrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProcessService processService;

    @PostMapping("/load-panel")
    public String loadPaymentsPanel(HttpServletRequest request, Model model, @RequestParam String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        String referenceNumber = HttpSessionUtils.getFilingNumberFromSessionObject(fullSessionIdentifier);
        model.addAttribute("referenceNumber", referenceNumber);
        addEditEnabledToModel(request, model, sessionIdentifier);
        addLiabilityDetailsToModel(model, referenceNumber);
        addNotLinkedPaymentsStatusToModel(request,sessionIdentifier, model);
        return "ipobjects/common/payments/payments_panel :: payments";
    }

    private void addLiabilityDetailsToModel(Model model, String referenceNumber) {
        try {
            List<CLiabilityDetal> liabilityDetails = paymentsIntegrationService.getAllLiabilityDetails(referenceNumber);
            model.addAttribute("liabilityDetails", liabilityDetails);
            model.addAttribute("hasError", false);
        } catch (Exception e) {
            model.addAttribute("hasError", true);
            log.error(e.getMessage(), e);
        }
    }

    protected void addEditEnabledToModel(HttpServletRequest request, Model model, String sessionIdentifier) {
        String ipObjectPrefix = SessionObjectUtils.getIpObjectPrefixByFullSessionIdentifier(request, sessionIdentifier);
        CFile sessionFile = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
        model.addAttribute("editEnabled", SecurityUtils.hasIpObjectPaymentsRights(sessionFile.getFileId(),processService));
    }

    private void addNotLinkedPaymentsStatusToModel(HttpServletRequest request, String sessionIdentifier, Model model) {
        String objectFilingNumber = getMainObjectFilingNumber(request, sessionIdentifier);
        model.addAttribute("objectFilingNumber", objectFilingNumber);
        model.addAttribute("containsNotLinkedPayments", paymentsIntegrationService.containsNotLinkedPayments(objectFilingNumber));
    }

    protected String getMainObjectFilingNumber(HttpServletRequest request, String sessionIdentifier) {
        CFile sessionFile = CFileSessionUtils.getSessionFile(request, sessionIdentifier);
        return sessionFile.getFileId().createFilingNumber();
    }

}
