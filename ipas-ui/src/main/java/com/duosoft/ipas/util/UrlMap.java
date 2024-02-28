package com.duosoft.ipas.util;

import bg.duosoft.ipas.enums.UserdocGroup;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.util.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UrlMap {

    private final static String MARK_URL = "/mark/detail/";

    public final static Url HOME_URL = new Url("/home", "Начало");

    public final static Url MARK_SEARCH = new Url("/mark/search", "search.mark.title", "search.mark.title.short", "#icon-trademark", SecurityRole.MarkViewOwn, SecurityRole.MarkViewAll);
    public final static Url INTERNATIONAL_MARK_SEARCH = new Url("/international_mark/search", "search.international_mark.title", "search.international_mark.title.short", "#icon-Int-Reg", SecurityRole.MarkViewOwn, SecurityRole.MarkViewAll);
    public final static Url GEOGRAPHICAL_INDICATIONS_SEARCH = new Url("/geographical_indications/search", "search.geographical_indications.title", "search.geographical_indications.title.short", "#icon-geo", SecurityRole.MarkViewOwn, SecurityRole.MarkViewAll);
    public final static Url ACP_SEARCH = new Url("/acp/search", "search.acp.title", "search.acp.title.short", "#icon-Mallet", SecurityRole.AcpViewOwn, SecurityRole.AcpViewAll);
    public final static Url PATENT_SEARCH = new Url("/patent/search", "search.patent.title", "search.patent.title.short", "#icon-Patent-2020", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url PATENT_LIKE_SEARCH = new Url("/patent-like/search", "search.patent-like.title", "search.patent-like.title.short", "#icon-Search-1", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url EU_PATENT_SEARCH = new Url("/eupatent/search", "search.eupatent.title", "search.eupatent.title.short", "#icon-EUpatent", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url UTILITY_MODELS_SEARCH = new Url("/utility_model/search", "search.utility_model.title", "search.utility_model.title.short", "#icon-Model-2020", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url PLANTS_AND_BREEDS_SEARCH = new Url("/plants_and_breeds/search", "search.plants_and_breeds.title", "search.plants_and_breeds.title.short", "#icon-clover-panda", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url SPC_SEARCH = new Url("/spc/search", "search.spc.title", "search.spc.title.short", "#icon-pills", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url DESING_SEARCH = new Url("/design/search", "search.design.title", "search.design.title.short", "#icon-design", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url INTERNATIONAL_DESING_SEARCH = new Url("/international-design/search", "search.international-design.title", "search.international-design.title.short", "#icon-Int-Des", SecurityRole.PatentViewOwn, SecurityRole.PatentViewAll);
    public final static Url USERDOC_SEARCH = new Url("/userdoc/search", "search.userdoc.title", "search.userdoc.title.short", "#icon-pencil", SecurityRole.UserdocViewOwn, SecurityRole.UserdocViewAll);
    public final static Url DISPUTE_SEARCH = new Url("/recordals/dispute", "Жалби, искания и спорове", "Спорове", "#icon-Dispute", SecurityRole.DisputesView);
//    public final static Url COURT_DECISIONS_SEARCH = new Url("ipas.properties.courtURL", true, "Административно-наказателна дейност", "АНД", "#icon-Mallet", SecurityRole.CourtDecisionsView);
    public final static Url BPO_JOURNAL = new Url("ipas.properties.journalURL", true, "Електронен бюлетин", "Електронен бюлетин", "#icon-bulletin", null);
    public final static Url STRUCTURE_LIST = new Url("/structure/list", "Организационна структура", "Структура", "", SecurityRole.StructureView);
    public final static Url STRUCTURE_GROUP_LIST = new Url("/structure/group/list", "Права", "Права", "", SecurityRole.StructureGroupView);
    public final static Url USERS_AND_ROLES = new Url("/users/sync", "Потребители и права", "Потребители и права", "#icon-user-ver-2", SecurityRole.UsersAndRolesView);
    public final static Url ADMIN_MODULE = new Url("/admin/home", "Административен модул", "Административен модул", "#icon-user-ver-2", SecurityRole.AdminModule);
    public final static Url EXPIRING_MARK_NOTIFICATIONS_URL = new Url("/expiring-marks/notifications", "Уведомления за изтичащи марки", "Уведомления за изтичащи марки", "#icon-trademark", SecurityRole.ExpiringMarksNotifications);
    public final static Url PATENT_PAID_FEES_CERTIFICATE_URL = new Url("/patent-certificates/paid-fees", "Удостоверения за платени такси", "Удостоверения за платени такси", "#icon-Patent-2020", SecurityRole.CertificatesForPaidPatentFees);
    public final static Url PAYMENTS_MODULE =  new Url("/payments-module", "Плащания", "Плащания", "#icon-payment1", SecurityRole.PaymentsModule);

    public final static Url JASPER_SOFT_URL = new Url("", "", "", "#icon-JasperSoft", null);
    public final static Url OFFIDOC_NOTIFICATIONS_URL = new Url("/offidoc/notifications", "е-Нотификации", "е-Нотификации", "#icon-Notification", SecurityRole.OffidocNotifications);
    public final static Url DD_TEMPLATE_MANAGER_URL = new Url("", "", "", "#icon-law", null);

    //Home lists
    public final static Url RECEPTIONS = new Url("/reception/list", "Новопостъпили заявки за обекти на ИС", "Новопостъпили заявки за обекти на ИС", "", SecurityRole.IpObjectsViewReceptionList);
    public final static Url LAST_ACTIONS = new Url("/last-action/list", "Последно извършени действия", "Последно извършени действия", "", SecurityRole.IpObjectsViewLastActionsList);
    public final static Url NEWLY_ALLOCATED_USERDOCS = new Url("/newly-allocated-userdocs/list", "Новоразпределени входящи документи", "Новоразпределени входящи документи", "", SecurityRole.NewlyAllocatedUserdocs);
    public final static Url MY_OBJECTS = new Url("/my-objects/list", "Моите обекти на ИС", "Моите обекти на ИС", "", SecurityRole.IpObjectsMyObjectsList);
    public final static Url IMARK_MY_OBJECTS = new Url("/imark-my-objects/list", "MP с посочване на България", "MP с посочване на България", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_MAIN = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.MAIN.code(), "Моите производства", "Моите производства", "", SecurityRole.IpObjectsMyUserdocsList);
    public final static Url MY_USERDOCS_CORRESP = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.CORRESP.code(), "Моята кореспонденция", "Моята кореспонденция", "", SecurityRole.IpObjectsMyUserdocsList);
    public final static Url MY_USERDOCS_ZMR = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.ZMR.code(), "Заявки за МР", "Заявки за МР", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_MAIN_ZMR = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.MAIN_ZMR.code(), "Заявки за вписвания по ЗМР", "Заявки за вписвания по ЗМР", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_CORRESP_ZMR = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.CORRESP_ZMR.code(), "Кореспонденция по ЗМР", "Кореспонденция по ЗМР", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_MAIN_MR = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.MAIN_MR.code(), "Производства по МР", "Производства по МР", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_CORRESP_MR = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.CORRESP_MR.code(), "Кореспонденция по МР", "Кореспонденция по МР", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_CORRESP_MR_WIPO = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.CORRESP_MR_WIPO.code(), "Кореспонденция от СОИС по МР", "Кореспонденция от СОИС по МР", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_CORRESP_ZMR_WIPO = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.CORRESP_ZMR_WIPO.code(), "Кореспонденция от СОИС по ЗМР", "Кореспонденция от СОИС по ЗМР", "", SecurityRole.HomePanelsZmAndZmr);
    public final static Url MY_USERDOCS_CORRESP_EXACT = new Url("/my-grouped-userdocs/list?userdocGroupName=" + UserdocGroup.CORRESP_EXACT.code(), "Обща кореспонденция", "Обща кореспонденция", "", SecurityRole.HomePanelExactCorresp);
    public final static Url USERDOC_CORRESPONDENCE_TERMS = new Url("/userdoc-correspondence-terms/list", "Срокове по кореспонденция", "Срокове по кореспонденция", "", SecurityRole.IpObjectsUserdocExpiredTermsList);
    public final static Url EXPIRED_TERM = new Url("/expired-term/list", "Изтекли срокове по обекти на ИС", "Изтекли срокове по обекти на ИС", "", SecurityRole.IpObjectsViewExpiredTermList);
    public final static Url EXPIRED_TERM_ZMR = new Url("/expired-term-zmr/list", "Изтекли срокове по ЗМР", "Изтекли срокове по ЗМР", "", SecurityRole.IpObjectsViewExpiredTermZmrList);
    public final static Url WAITING_IN_TERM = new Url("/waiting-term/list", "Изчакване на срокове по обекти на ИС", "Изчакване на срокове по обекти на ИС", "", SecurityRole.IpObjectsViewWaitingTermList);
    public final static Url WAITING_IN_TERM_ZMR = new Url("/waiting-term-zmr/list", "Изчакване на срокове по ЗМР", "Изчакване на срокове по ЗМР", "", SecurityRole.IpObjectsViewWaitingTermZmrList);
    public final static Url INTERNATIONAL_MARKS = new Url("/international-marks/list", "Международни регистрации", "Международни регистрации", "", SecurityRole.IpObjectsViewInternationalMarksList);
    public final static Url INTERNATIONAL_MARKS_ENOTIFS = new Url("/enotif/list", "Постъпили международни регистрации", "Постъпили международни регистрации", "", SecurityRole.IpObjectsViewInternationalMarkReceptionsList);
    public final static Url USERDOC_RECEPTIONS = new Url("/reception-userdoc/list", "Новопостъпили входящи документи", "Новопостъпили входящи документи", "", SecurityRole.UserdocViewReceptionList);
    public final static Url USERDOC_ZM_ZMR_RECEPTIONS = new Url("/reception-userdoc-zm-zmr/list", "Новопостъпили входящи документи - МР и ЗМР", "Новопостъпили входящи документи - МР и ЗМР", "", SecurityRole.UserdocInternationalMarkReceptionsListZmAndZmr);
    public final static Url LAST_PAYMENTS = new Url("/last-payments/list", "Последни плащания", "Последни плащания", "", SecurityRole.LastPaymentsList);
    public final static Url NOT_LINKED_PAYMENTS = new Url("/not-linked-payments/list", "Необвързани плащания", "Необвързани плащания", "", SecurityRole.NotLinkedPaymentsList);

    //Admin panel
    public final static Url MISSING_EPO_PATENTS = new Url("/admin/missing-epo-patents", "Добавяне на европейски патенти", "Добавяне на европейски патенти", "", null);
    public final static Url MISSING_USERDOCS_IN_IPAS = new Url("/admin/missing-userdocs", "Добавяне на липсващи входящи документи в IPAS", "Добавяне на липсващи входящи документи в IPAS", "", null);
    public final static Url MISSING_USERDOCS_IN_ABDOCS = new Url("/admin/missing-userdocs-in-abdocs", "Добавяне на липсващи документи в деловодната система", "Добавяне на липсващи документи в деловодната система", "", null);
    public final static Url MISSING_INTL_MARKS_IN_ABDOCS = new Url("/admin/missing-international-marks-in-abdocs", "Добавяне на липсващи международни регистрации в деловодната система", "Добавяне на липсващи международни регистрации в деловодната система", "", null);
    public final static Url MISSING_INTL_ZMR_IN_IPAS = new Url("/admin/mark-old-international-registration", "Добавяне на липсващи заявки за международни регистрации в IPAS", "Добавяне на липсващи заявки за международни регистрации в IPAS", "", null);
    public final static Url PERSON_ADMINISTRATION = new Url("/admin/person/split", "Администриране на лица", "Администриране на лица", "", null);
    public final static Url PERSON_ADMINISTRATION_SPLIT = new Url("/admin/person/split", "Разделяне на лица с повече от един адрес", "Разделяне на лица с повече от един адрес", "", null);
    public final static Url PERSON_ADMINISTRATION_REMOVE_NOT_USED = new Url("/admin/person/remove-not-used", "Изтриване на неизползваеми лица", "Изтриване на неизползваеми лица", "", null);
    public final static Url PERSON_ADMINISTRATION_DUPLICATE = new Url("/admin/person/duplicate", "Изтриване на дублирани лица", "Изтриване на дублирани лица", "", null);
    public final static Url PERSON_ADMINISTRATION_MERGE = new Url("/admin/person/merge", "Обединяване на лица", "Обединяване на лица", "", null);

    public final static Url ADMIN_ERROR_LOG = new Url("/admin/error-log/list", "Грешки, изискващи проверка от администратор", "Грешки, изискващи проверка от администратор", "", null);
    public final static Url ADMIN_USERS_AND_ROLES = new Url("/users/sync", "Администриране на потребители и права", "Администриране на потребители и права", "#icon-user-ver-2", SecurityRole.UsersAndRolesView);
    public final static Url USERDOC_ADMINISTRATION = new Url("/admin/userdoc-types/list", "Администриране на вторични действия", "Администриране на вторични действия", "", null);
    public final static Url OFFIDOC_ADMINISTRATION = new Url("/admin/offidoc/list", "Администриране на изходящи документи", "Администриране на изходящи документи", null);
    public final static Url NICE_CLASSES_ADMINISTRATION = new Url("/admin/nice-class/list", "Администриране на стоки и услуги", "Администриране на стоки и услуги", null);
    public final static Url ADMIN_CHANGE_LOG = new Url("/admin/userdoc-reg-number-change-log/list", "Променени регистрационни номера на вторични действия", "Променени регистрационни номера на вторични действия", "", null);

    public final static Url ADMIN_AUTOMATIC_ACTIONS_LOG = new Url("/admin/automatic-actions-log/list", "Лог на изпълнения на автоматични действия", "Лог на изпълнения на автоматични действия", "", null);

    //Payments module
    public final static Url NOT_LINKED_PAYMENTS_PORTAL = new Url("ipas.properties.notLinkedPaymentsURL", true, "Неразпределени плащания", "Неразпределени плащания", "", SecurityRole.PaymentsModule);
    public final static Url UNCLEAR_OBJECT_PAYMENTS_PORTAL = new Url("ipas.properties.unclearObjectPaymentsURL", true, "Плащания с неясен обект", "Плащания с неясен обект", "", SecurityRole.PaymentsExtendedRights);
    public final static Url WRONG_PAYMENTS_PORTAL = new Url("ipas.properties.wrongLiabilitiesPaymentsURL", true, "Невърнати погрешни плащания", "Невърнати погрешни плащания", "", SecurityRole.PaymentsModule);
    public final static Url WAITING_PAYMENTS_PORTAL = new Url("ipas.properties.waitingLiabilitiesPaymentsURL", true, "Чакащи задължения", "Чакащи задължения", "", SecurityRole.PaymentsModule);
    public final static Url PAID_PAYMENTS_PORTAL = new Url("ipas.properties.paidLiabilitiesPaymentsURL", true, "Извършени плащания", "Извършени плащания", "", SecurityRole.PaymentsModule);
    public final static Url IMPORT_FILE_PAYMENTS_PORTAL = new Url("ipas.properties.importFilePaymentsURL", true, "Импорт на файл от счетоводството", "Импорт на файл от счетоводството", "", SecurityRole.PaymentsAdministration);
    public final static Url NO_OBJECT_PAYMENTS_PORTAL = new Url("ipas.properties.noObjectPaymentsURL", true, "Плащания без посочен обект", "Плащания без посочен обект", "", SecurityRole.PaymentsAdministration);
    public final static Url CONFIG_PAYMENTS_PORTAL = new Url("ipas.properties.configLiabilitiesPaymentsURL", true, "Кодове на задължения", "Кодове на задължения", "", SecurityRole.PaymentsAdministration);
    public final static Url FEE_VALIDATION_PORTAL = new Url("ipas.properties.feeValidationURL", true, "Валидиране на eFiling конфигурацията", "Валидиране на eFiling конфигурацията", "", SecurityRole.PaymentsAdministration);

    public final static List<Url> SERVICE_URLS = new ArrayList();
    public final static List<Url> ADMIN_SERVICE_URLS = new ArrayList();
    public final static List<Url> EXTERNAL_SERVICE_URLS = new ArrayList();
    public final static List<Url> HOME_LISTS_URLS = new ArrayList();
    public final static List<Url> ADMIN_PANEL_URLS = new ArrayList();
    public final static List<Url> ADMIN_PERSON_URLS = new ArrayList();
    public final static List<Url> PAYMENTS_MODULE_URLS = new ArrayList<>();

    static {
        // first row
        SERVICE_URLS.add(MARK_SEARCH);
        SERVICE_URLS.add(INTERNATIONAL_MARK_SEARCH);
        SERVICE_URLS.add(GEOGRAPHICAL_INDICATIONS_SEARCH);
        SERVICE_URLS.add(ACP_SEARCH);
        // second row
        SERVICE_URLS.add(PATENT_LIKE_SEARCH);
        SERVICE_URLS.add(DESING_SEARCH);
        SERVICE_URLS.add(INTERNATIONAL_DESING_SEARCH);
        // third row
        SERVICE_URLS.add(PATENT_SEARCH);
        SERVICE_URLS.add(UTILITY_MODELS_SEARCH);
        SERVICE_URLS.add(EU_PATENT_SEARCH);
        // forth row
        SERVICE_URLS.add(SPC_SEARCH);
        SERVICE_URLS.add(PLANTS_AND_BREEDS_SEARCH);
        SERVICE_URLS.add(USERDOC_SEARCH);

        ADMIN_SERVICE_URLS.add(EXPIRING_MARK_NOTIFICATIONS_URL);
        ADMIN_SERVICE_URLS.add(PATENT_PAID_FEES_CERTIFICATE_URL);
        ADMIN_SERVICE_URLS.add(DISPUTE_SEARCH);
//        ADMIN_SERVICE_URLS.add(COURT_DECISIONS_SEARCH);
        ADMIN_SERVICE_URLS.add(ADMIN_MODULE);
        ADMIN_SERVICE_URLS.add(BPO_JOURNAL);
        ADMIN_SERVICE_URLS.add(PAYMENTS_MODULE);
        ADMIN_SERVICE_URLS.add(OFFIDOC_NOTIFICATIONS_URL);

        EXTERNAL_SERVICE_URLS.add(JASPER_SOFT_URL);
        EXTERNAL_SERVICE_URLS.add(OFFIDOC_NOTIFICATIONS_URL);

        HOME_LISTS_URLS.add(RECEPTIONS);
        HOME_LISTS_URLS.add(USERDOC_RECEPTIONS);
        HOME_LISTS_URLS.add(USERDOC_ZM_ZMR_RECEPTIONS);
        HOME_LISTS_URLS.add(LAST_ACTIONS);
        HOME_LISTS_URLS.add(NEWLY_ALLOCATED_USERDOCS);
        HOME_LISTS_URLS.add(MY_OBJECTS);
        HOME_LISTS_URLS.add(IMARK_MY_OBJECTS);
        HOME_LISTS_URLS.add(MY_USERDOCS_MAIN);
        HOME_LISTS_URLS.add(MY_USERDOCS_CORRESP);
        HOME_LISTS_URLS.add(MY_USERDOCS_ZMR);
        HOME_LISTS_URLS.add(MY_USERDOCS_MAIN_ZMR);
        HOME_LISTS_URLS.add(MY_USERDOCS_CORRESP_ZMR);
        HOME_LISTS_URLS.add(MY_USERDOCS_MAIN_MR);
        HOME_LISTS_URLS.add(MY_USERDOCS_CORRESP_MR);
        HOME_LISTS_URLS.add(MY_USERDOCS_CORRESP_MR_WIPO);
        HOME_LISTS_URLS.add(MY_USERDOCS_CORRESP_ZMR_WIPO);
        HOME_LISTS_URLS.add(MY_USERDOCS_CORRESP_EXACT);
        HOME_LISTS_URLS.add(USERDOC_CORRESPONDENCE_TERMS);
        HOME_LISTS_URLS.add(EXPIRED_TERM);
        HOME_LISTS_URLS.add(EXPIRED_TERM_ZMR);
        HOME_LISTS_URLS.add(WAITING_IN_TERM);
        HOME_LISTS_URLS.add(WAITING_IN_TERM_ZMR);
        HOME_LISTS_URLS.add(INTERNATIONAL_MARKS_ENOTIFS);
        HOME_LISTS_URLS.add(LAST_PAYMENTS);
        HOME_LISTS_URLS.add(NOT_LINKED_PAYMENTS);

        ADMIN_PANEL_URLS.add(ADMIN_USERS_AND_ROLES);
        ADMIN_PANEL_URLS.add(PERSON_ADMINISTRATION);
        ADMIN_PANEL_URLS.add(ADMIN_ERROR_LOG);
        ADMIN_PANEL_URLS.add(USERDOC_ADMINISTRATION);
        ADMIN_PANEL_URLS.add(OFFIDOC_ADMINISTRATION);
        ADMIN_PANEL_URLS.add(NICE_CLASSES_ADMINISTRATION);
        ADMIN_PANEL_URLS.add(MISSING_EPO_PATENTS);
        ADMIN_PANEL_URLS.add(MISSING_USERDOCS_IN_IPAS);
        ADMIN_PANEL_URLS.add(MISSING_USERDOCS_IN_ABDOCS);
        ADMIN_PANEL_URLS.add(MISSING_INTL_MARKS_IN_ABDOCS);
        ADMIN_PANEL_URLS.add(MISSING_INTL_ZMR_IN_IPAS);
        ADMIN_PANEL_URLS.add(ADMIN_CHANGE_LOG);
        ADMIN_PANEL_URLS.add(ADMIN_AUTOMATIC_ACTIONS_LOG);

        ADMIN_PERSON_URLS.add(PERSON_ADMINISTRATION_SPLIT);
        ADMIN_PERSON_URLS.add(PERSON_ADMINISTRATION_REMOVE_NOT_USED);
        ADMIN_PERSON_URLS.add(PERSON_ADMINISTRATION_DUPLICATE);
        ADMIN_PERSON_URLS.add(PERSON_ADMINISTRATION_MERGE);

        PAYMENTS_MODULE_URLS.add(NOT_LINKED_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(UNCLEAR_OBJECT_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(WRONG_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(WAITING_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(PAID_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(IMPORT_FILE_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(NO_OBJECT_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(CONFIG_PAYMENTS_PORTAL);
        PAYMENTS_MODULE_URLS.add(FEE_VALIDATION_PORTAL);

    }

    public static List<Url> getServiceUrlsFiltered() {
        return SERVICE_URLS.stream().filter(r -> r.getRoles() == null || SecurityUtils.hasAnyRights(r.getRoles())).collect(Collectors.toList());
    }

    public static List<Url> getAdminServiceUrlsFiltered() {
        return ADMIN_SERVICE_URLS.stream().filter(r -> r.getRoles() == null || SecurityUtils.hasAnyRights(r.getRoles())).collect(Collectors.toList());
    }

    public static List<Url> getHomeUrlsFiltered() {
        return HOME_LISTS_URLS.stream().filter(r -> r.getRoles() == null || SecurityUtils.hasAnyRights(r.getRoles())).collect(Collectors.toList());
    }

    public static List<Url> getPaymentsModuleUrlsFiltered() {
        return PAYMENTS_MODULE_URLS.stream().filter(r -> r.getRoles() == null || SecurityUtils.hasAnyRights(r.getRoles())).collect(Collectors.toList());
    }

}
