package com.duosoft.ipas.controller.home;

import bg.duosoft.ipas.core.model.mark.CEnotif;
import bg.duosoft.ipas.core.service.*;
import bg.duosoft.ipas.core.service.enotif.EnotifService;
import bg.duosoft.ipas.core.service.payments.PaymentsService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.core.service.reception.ReceptionUserdocRequestService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.*;
import bg.duosoft.ipas.util.filter.*;
import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.*;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

import static bg.duosoft.ipas.util.filter.LastPaymentsFilter.DEFAULT_LAST_PAYMENT_FROM;
import static bg.duosoft.ipas.util.filter.NotLinkedPaymentsFilter.DEFAULT_NOT_LINKED_PAYMENT_FROM;
import static com.duosoft.ipas.controller.home.NotLinkedPaymentsHomeController.addNotLinkedPaymentsToModel;

@Controller
public class HomeController {
    private static final int HOME_PAGE_PANELS_PAGE_SIZE = 5;

    @Autowired
    private UserService userService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private ReceptionUserdocRequestService receptionUserdocRequestService;

    @Autowired
    private WaitingTermService waitingTermService;

    @Autowired
    private MyObjectsService myObjectsService;

    @Autowired
    private MyGroupedUserdocsService myGroupedUserdocsService;

    @Autowired
    private ExpiredTermService expiredTermService;

    @Autowired
    private LastActionService lastActionService;

    @Autowired
    private UserdocWaitingTermsService userdocWaitingTermsService;

    @Autowired
    private EnotifService enotifService;

    @Autowired
    private UserdocCorrespondenceTermsService userdocCorrespondenceTermsService;

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private NewlyAllocatedUserdocService newlyAllocatedUserdocService;

    @GetMapping(value = {"", "/", "/home/"})
    public String redirectToHomePage(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHome(Model model) {
        return "home/home_page";
    }

    @PostMapping("/load-receptions-panel")
    public String fillReceptionsPanel(Model model) {
        List<ReceptionRequestSimpleResult> receptions = receptionRequestService.getFirstReceptionsWithoutStatus();
        Integer receptionCount = receptionRequestService.getReceptionsWithoutStatusCount();
        model.addAttribute("receptions", receptions);
        model.addAttribute("total", receptionCount);
        return "home/reception/reception_list_home_panel :: panel";
    }


    @PostMapping("/load-userdoc-receptions-zm-zmr-panel")
    public String fillUserdocZmAndZmrReceptionsPanel(Model model) {
        return loadUserdocReceptions(model, UserdocGroup.getAllWithoutMainAndCorrespGroup(), "home/reception/userdoc/last_added_userdocs_zm_zmr_home_panel :: panel");
    }


    @PostMapping("/load-userdoc-receptions-panel")
    public String fillUserdocReceptionsPanel(Model model) {
        return loadUserdocReceptions(model, UserdocGroup.getMainAndCorrespGroup(), "home/reception/userdoc/last_added_userdocs_home_panel :: panel");
    }


    private String loadUserdocReceptions(Model model, List<String> userdocGroups, String htmlPage) {
        ReceptionUserdocListFilter filter = new ReceptionUserdocListFilter(
                Sortable.DESC_ORDER,
                ReceptionUserdocSorterUtils.RECEPTION_USERDOC_CREATE_DATE,
                Pageable.DEFAULT_PAGE,
                HOME_PAGE_PANELS_PAGE_SIZE, null, null, null, null, null, null, null, userdocGroups);

        List<UserdocSimpleResult> userdocReceptions = receptionUserdocRequestService.selectUserdocReceptions(filter);
        Integer total = receptionUserdocRequestService.selectUserdocReceptionsCount(filter);
        model.addAttribute("userdocReceptions", userdocReceptions);
        model.addAttribute("total", total);
        return htmlPage;
    }


    @PostMapping("/load-my-objects-panel")
    public String fillMyObjectsPanel(Model model) {
        MyObjectsHomePanelFilter filter = new MyObjectsHomePanelFilter(MyObjectsFilter.DESC_ORDER, MyObjectsSorterUtils.STATUS_DATE,
                SecurityUtils.getLoggedUserId(), HOME_PAGE_PANELS_PAGE_SIZE, null);
        List<IPObjectHomePanelResult> myObjectsList = myObjectsService.getMyObjectsForHomePanelList(filter);
        Integer myObjectsCount = myObjectsService.getMyObjectsHomePanelCount(filter);
        Integer newlyAllocatedObjectsCount = myObjectsService.getNewlyAllocatedObjectsCount(filter);
        model.addAttribute("myObjectsList", myObjectsList);
        model.addAttribute("total", myObjectsCount);
        model.addAttribute("newlyAllocatedObjectsCount", newlyAllocatedObjectsCount);
        model.addAttribute("myObjectsParentUrl", "my-objects");
        return "home/my_objects/my_objects_home_panel :: panel";
    }


    @PostMapping("/load-imark-my-objects-panel")
    public String fillImarkMyObjectsPanel(Model model) {
        List<String> fileTypeList = new ArrayList<>();
        fileTypeList.add(FileType.INTERNATIONAL_MARK_I.code());
        fileTypeList.add(FileType.INTERNATIONAL_MARK_R.code());
        fileTypeList.add(FileType.INTERNATIONAL_MARK_B.code());
        MyObjectsHomePanelFilter filter = new MyObjectsHomePanelFilter(MyObjectsFilter.DESC_ORDER, MyObjectsSorterUtils.STATUS_DATE,
                SecurityUtils.getLoggedUserId(), HOME_PAGE_PANELS_PAGE_SIZE, fileTypeList);
        List<IPObjectHomePanelResult> myObjectsList = myObjectsService.getMyObjectsForHomePanelList(filter);
        Integer myObjectsCount = myObjectsService.getMyObjectsHomePanelCount(filter);
        Integer newlyAllocatedObjectsCount = myObjectsService.getNewlyAllocatedObjectsCount(filter);
        model.addAttribute("myObjectsList", myObjectsList);
        model.addAttribute("total", myObjectsCount);
        model.addAttribute("newlyAllocatedObjectsCount", newlyAllocatedObjectsCount);
        model.addAttribute("myObjectsParentUrl", "imark-my-objects");
        return "home/my_objects/imark_my_objects_home_panel :: panel";
    }


    @PostMapping("/load-userdoc-correspondence-terms-panel")
    public String fillUserdocCorrespondenceTerms(Model model) {
        MyUserdocsFilter filter = new MyUserdocsFilter(MyUserdocsFilter.DEFAULT_PAGE, HOME_PAGE_PANELS_PAGE_SIZE,
                MyUserdocsFilter.DESC_ORDER, UserdocCorrespondenceTermsSortedUtils.EXPIRATION_DATE, SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData) ? null : SecurityUtils.getLoggedUserId(), null, null, null, null, null, null, null, null, null, true, true, null, null);
        List<UserdocSimpleResult> userdocCorrespondenceTermsList = userdocCorrespondenceTermsService.getUserdocCorrespondenceTermsList(filter);
        Integer userdocCorrespondenceTermsCount = userdocCorrespondenceTermsService.getUserdocCorrespondenceTermsCount(filter);
        model.addAttribute("userdocCorrespondenceTermsList", userdocCorrespondenceTermsList);
        model.addAttribute("total", userdocCorrespondenceTermsCount);
        return "home/userdoc_correspondence_terms/userdoc_correspondence_terms_home_panel :: panel";
    }

    @PostMapping("/load-userdoc-waiting-terms-panel")
    public String fillUserdocWaitingTerms(Model model) {
        MyUserdocsFilter filter = new MyUserdocsFilter(MyUserdocsFilter.DEFAULT_PAGE, HOME_PAGE_PANELS_PAGE_SIZE,
                MyUserdocsFilter.DESC_ORDER, MyUserdocsSorterUtils.EXPIRATION_DATE, SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData) ? null : SecurityUtils.getLoggedUserId(), null, null, null, null, null, null, null, null, null, null, null, null, null);
        List<UserdocSimpleResult> userdocWaitingTermsList = userdocWaitingTermsService.getUserdocWaitingTermsList(filter);
        Integer userdocWaitingTermsCount = userdocWaitingTermsService.getUserdocWaitingTermsCount(filter);
        model.addAttribute("userdocWaitingTermsList", userdocWaitingTermsList);
        model.addAttribute("total", userdocWaitingTermsCount);
        return "home/userdoc_waiting_terms/userdoc_waiting_terms_home_panel :: panel";
    }


    @PostMapping("/load-userdocs-main-group-panel")
    public String fillMyUserdocsWithMainGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.MAIN.code());
    }

    @PostMapping("/load-userdocs-corresp-group-panel")
    public String fillMyUserdocsWithCorrespGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.CORRESP.code());
    }

    @PostMapping("/load-userdocs-zmr-group-panel")
    public String fillMyUserdocsWithZmrGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.ZMR.code());
    }

    @PostMapping("/load-userdocs-main-zmr-group-panel")
    public String fillMyUserdocsWithMainZmrGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.MAIN_ZMR.code());
    }

    @PostMapping("/load-userdocs-corresp-zmr-group-panel")
    public String fillMyUserdocsWithCorrespZmrGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.CORRESP_ZMR.code());
    }


    @PostMapping("/load-userdocs-main-mr-group-panel")
    public String fillMyUserdocsWithMainMrGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.MAIN_MR.code());
    }


    @PostMapping("/load-userdocs-corresp-mr-group-panel")
    public String fillMyUserdocsWithCorrespMrGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.CORRESP_MR.code());
    }


    @PostMapping("/load-userdocs-corresp-mr-wipo-group-panel")
    public String fillMyUserdocsWithCorrespMrWipoGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.CORRESP_MR_WIPO.code());
    }

    @PostMapping("/load-userdocs-corresp-exact-group-panel")
    public String fillMyUserdocsWithCorrespExactGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.CORRESP_EXACT.code());
    }

    @PostMapping("/load-userdocs-corresp-zmr-wipo-group-panel")
    public String fillMyUserdocsWithCorrespZmrWipoGroupPanel(Model model) {
        return loadHomePanelsRelatedToUserdocGroupName(model, UserdocGroup.CORRESP_ZMR_WIPO.code());
    }


    private String loadHomePanelsRelatedToUserdocGroupName(Model model, String userdocGroupName) {
        MyUserdocsFilter filter = new MyUserdocsFilter(MyUserdocsFilter.DEFAULT_PAGE, HOME_PAGE_PANELS_PAGE_SIZE,
                MyUserdocsFilter.DESC_ORDER, MyUserdocsSorterUtils.FILING_DATE, SecurityUtils.getLoggedUserId(), null, null, userdocGroupName, null, null, null, null, null, true, false, null, false, null);
        List<UserdocSimpleResult> userdocsWithGroup = myGroupedUserdocsService.getMyUserdocsList(filter);
        Integer userdocsWithGroupCount = myGroupedUserdocsService.getMyUserdocsCount(filter);
        Integer newlyAllocatedUserdocsCount = myGroupedUserdocsService.getNewlyAllocatedUserdocsCount(filter);
        model.addAttribute("myUserdocsList", userdocsWithGroup);
        model.addAttribute("total", userdocsWithGroupCount);
        model.addAttribute("newlyAllocatedUserdocsCount", newlyAllocatedUserdocsCount);
        model.addAttribute("userdocGroupName", userdocGroupName);
        return "home/my_grouped_userdocs/my_grouped_userdocs_home_panel :: panel";
    }


    @PostMapping("/load-expired-terms-panel")
    public String fillExpiredTermsPanel(Model model) {
        return loadExpiredTermsPanels(model, ExpiredTermsPanelConfig.ALL.code(), "home/expired_term/expired_term_home_panel :: panel");
    }

    @PostMapping("/load-expired-terms-zmr-panel")
    public String fillExpiredTermsZmrPanel(Model model) {
        return loadExpiredTermsPanels(model, ExpiredTermsPanelConfig.ZMR.code(), "home/expired_term/expired_term_zmr_home_panel :: panel");
    }

    private String loadExpiredTermsPanels(Model model, String panelType, String htmlPage) {
        ExpiredTermFilter filter = new ExpiredTermFilter(ExpiredTermFilter.DEFAULT_PAGE, HOME_PAGE_PANELS_PAGE_SIZE,
                SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData) ? null : SecurityUtils.getLoggedUserId(), null, ExpiredTermFilter.DESC_ORDER, ExpiredTermSortedUtils.ACTION_DATE, null, null, panelType,null);
        List<IPObjectSimpleResult> expiredTermList = expiredTermService.getExpiredTermsList(filter);
        Integer expiredTermCount = expiredTermService.getExpiredTersmCount(filter);
        model.addAttribute("expiredTermList", expiredTermList);
        model.addAttribute("total", expiredTermCount);
        return htmlPage;
    }


    @PostMapping("/load-waiting-terms-panel")
    public String fillWaitingTermsPanel(Model model) {
        return loadWaitingTermsPanels(model, WaitingTermsPanelConfig.ALL.code(), "home/waitingterm/waiting_term_home_panel :: panel");
    }

    @PostMapping("/load-waiting-terms-zmr-panel")
    public String fillWaitingTermsPanelZmr(Model model) {
        return loadWaitingTermsPanels(model, WaitingTermsPanelConfig.ZMR.code(), "home/waitingterm/waiting_term_zmr_home_panel :: panel");
    }

    private String loadWaitingTermsPanels(Model model, String panelType, String htmlPage) {

        WaitingTermFilter filter = new WaitingTermFilter(
                WaitingTermFilter.ASC_ORDER,
                WaitingTermSorterUtils.EXPIRATION_DATE,
                WaitingTermFilter.DEFAULT_PAGE, HOME_PAGE_PANELS_PAGE_SIZE,
                SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData) ? null : SecurityUtils.getLoggedUserId(), null, null, panelType, null);

        List<IPObjectSimpleResult> waitingTermList = waitingTermService.getWaitingTermList(filter);
        Integer waitingTermCount = waitingTermService.getWaitingTermCount(filter);
        model.addAttribute("waitingTermList", waitingTermList);
        model.addAttribute("total", waitingTermCount);
        return htmlPage;
    }

    @PostMapping("/load-international-marks-panel")
    public String fillInternationalMarksPanel(Model model) {
        List<CEnotif> allWithTopOrderByGaznoDesc = enotifService.findAllWithTopOrderByGaznoDesc();
        model.addAttribute("enotifs", allWithTopOrderByGaznoDesc);
        model.addAttribute("total", enotifService.getEnotifsCount());
        return "home/international_marks/international_marks_home_panel :: panel";
    }

    @PostMapping("/load-last-actions-panel")
    public String fillLastActionPanel(Model model) {
        LastActionFilter filter = new LastActionFilter(
                WaitingTermFilter.DEFAULT_PAGE,
                HOME_PAGE_PANELS_PAGE_SIZE,
                ActionTypeKind.BOTH.code(),
                null,
                SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData) ? null : SecurityUtils.getLoggedUserId(), null, null, null, null, null, null, null, null, null, null);

        List<LastActionsResult> lastActionList = lastActionService.getLastActionList(filter);
        Integer lastActionCount = lastActionService.getLastActionCount(filter);
        model.addAttribute("lastActionList", lastActionList);
        model.addAttribute("total", lastActionCount);
        return "home/lastaction/last_action_home_panel :: panel";
    }


    @PostMapping("/load-newly-allocated-userdocs-panel")
    public String fillNewlyAllocatedUserdocsPanel(Model model) {
        NewlyAllocatedUserdocFilter newlyAllocatedUserdocFilter = new NewlyAllocatedUserdocFilter(Sortable.DESC_ORDER,
                NewlyAllocatedUserdocSorterUtils.USERDOC_DATE_CHANGED, NewlyAllocatedUserdocFilter.DEFAULT_PAGE, HOME_PAGE_PANELS_PAGE_SIZE, null, null
                , null, null, null, null, SecurityUtils.hasRights(SecurityRole.IpObjectsSearchForeignObjectsData) ? null : SecurityUtils.getLoggedUserId(), null, true);

        List<NewlyAllocatedUserdocSimpleResult> newlyAllocatedUserdocs = newlyAllocatedUserdocService.selectNewlyAllocatedUserdocs(newlyAllocatedUserdocFilter);
        Integer newlyAllocatedUserdocsCount = newlyAllocatedUserdocService.selectNewlyAllocatedUserdocsCount(newlyAllocatedUserdocFilter);
        model.addAttribute("newlyAllocatedUserdocs", newlyAllocatedUserdocs);
        model.addAttribute("total", newlyAllocatedUserdocsCount);
        return "home/newly_allocated_userdocs/newly_allocated_userdocs_home_panel :: panel";
    }

    @PostMapping("/load-last-payments-panel")
    public String loadLastPaymentsPannel(Model model) {
        LastPaymentsFilter filter = new LastPaymentsFilter(DEFAULT_LAST_PAYMENT_FROM, null, Pageable.DEFAULT_PAGE, HOME_PAGE_PANELS_PAGE_SIZE);
        PaymentsHomeController.addLastPaymentsToModel(paymentsService, model, filter, userService);

        return "home/payments/last_payments_home_panel :: panel";
    }


    @PostMapping("/load-not-linked-payments-panel")
    public String loadNotLinkedPaymentLiabilities(Model model) {
        NotLinkedPaymentsFilter filter = new NotLinkedPaymentsFilter(DEFAULT_NOT_LINKED_PAYMENT_FROM, null);

        addNotLinkedPaymentsToModel(paymentsService, model, filter, userService, HOME_PAGE_PANELS_PAGE_SIZE);

        return "home/payments/not_linked_payments_home_panel :: panel";
    }

}
