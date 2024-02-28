package com.duosoft.ipas.controller.payments;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments-module")
@PreAuthorize("hasAuthority(T(bg.duosoft.ipas.enums.security.SecurityRole).PaymentsModule.code())")
public class PaymentsModuleController {

    @GetMapping
    public String viewPaymentsModule() {
        return "payments/payments_module";
    }
}
