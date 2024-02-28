package bg.duosoft.ipas.integration.decisiondesktop.vars;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.05.2021
 * Time: 12:04
 */
public interface Variables {

    //GENERAL
    String GENERAL_DATE_TODAY = "general_date_today";

    //FILE
    String FILE_VAR_PREFIX = "file_";
    String FILE_APPLICATION_DATE = "file_application_date";
    String FILE_APPLICATION_NUMBER ="file_application_number";
    String FILE_REGISTRATION_NUMBER ="file_registration_number";
    String FILE_REGISTRATION_DATE ="file_registration_date";
    String FILE_EXPIRY_DATE ="file_expiry_date";
    String FILE_PRIORITY_DATE ="file_priority_date";
    String FILE_EXPERT_NAME = "file_expert_name";

    //OWNER
    String OWNER_NAME = "owner_name_first";
    String OWNER_ADDRESS = "owner_address_first";
    String OWNER_LIST = "owner_list";
    String OWNER_LIST_FORMATTED = "owner_list_formatted";
    String OWNER_NAME_LIST = "owner_name_list";

    //REPRESENTATIVE
    String REPRESENTATIVE_NAME = "representative_name_first";
    String REPRESENTATIVE_ADDRESS = "representative_address_first";
    String REPRESENTATIVE_LIST = "representative_list";

    //SERVICE PERSON
    String SERVICE_PERSON_NAME = "service_person_name";
    String SERVICE_PERSON_ADDRESS = "service_person_address";

    //MARK
    String MARK_NICE_LIST = "mark_nice_list";
    String MARK_NICE_LIST_COMPLEX = "mark_nice_list_complex";
    String MARK_NICE_CLASSES = "mark_nice_classes";
    String MARK_TYPE_NAME = "mark_type_name";
    String MARK_SIGN_NAME = "mark_sign_name";
    String MARK_SIGN_IMAGE = "mark_sign_image";

    //DESIGN
    String DESIGN_NAME = "design_name";
    String DESIGN_LIST = "design_list";
    String DESIGN_LIST_COMPLEX = "design_list_complex";
    String DESIGN_LIST_IDS = "design_list_ids";
    String DESIGN_LIST_PRODUCTS = "design_list_products";
    String DESIGN_LIST_LOCARNO= "design_list_locarno";
    String DESIGN_IMAGES = "design_images";
    String DESIGN_LIST_COUNT = "design_list_count";

    //USERDOC
    String USERDOC_PARENT_VAR_PREFIX = "parent_";
    String USERDOC_VAR_PREFIX = "userdoc_";
    String USERDOC_EXPERT_NAME = "userdoc_expert_name";
    String USERDOC_APPLICATION_NUMBER = "userdoc_application_number";
    String USERDOC_APPLICATION_DATE = "userdoc_application_date";
    String USERDOC_PAYEE_LIST = "userdoc_payee_list";
    String USERDOC_APPLICANT_LIST = "userdoc_applicant_list";
    String USERDOC_CREDITOR_LIST = "userdoc_creditor_list";
    String USERDOC_GRANTEE_LIST = "userdoc_grantee_list";
    String USERDOC_GRANTOR_LIST = "userdoc_grantor_list";
    String USERDOC_NEW_CA_LIST = "userdoc_new_ca_list";
    String USERDOC_NEW_REPRESENTATIVE_LIST = "userdoc_new_representative_list";
    String USERDOC_NEW_OWNER_LIST = "userdoc_new_owner_list";
    String USERDOC_OLD_CA_LIST = "userdoc_old_ca_list";
    String USERDOC_OLD_OWNER_LIST = "userdoc_old_owner_list";
    String USERDOC_OLD_REPRESENTATIVE_LIST = "userdoc_old_representative_list";
    String USERDOC_PAYER_LIST = "userdoc_payer_list";
    String USERDOC_PLEDGER_LIST = "userdoc_pledger_list";
    String USERDOC_REPRESENTATIVE_LIST = "userdoc_representative_list";

    String USERDOC_PAYEE_NAME_LIST = "userdoc_payee_name_list";
    String USERDOC_APPLICANT_NAME_LIST = "userdoc_applicant_name_list";
    String USERDOC_CREDITOR_NAME_LIST = "userdoc_creditor_name_list";
    String USERDOC_GRANTEE_NAME_LIST = "userdoc_grantee_name_list";
    String USERDOC_GRANTOR_NAME_LIST = "userdoc_grantor_name_list";
    String USERDOC_NEW_CA_NAME_LIST = "userdoc_new_ca_name_list";
    String USERDOC_NEW_REPRESENTATIVE_NAME_LIST = "userdoc_new_representative_name_list";
    String USERDOC_NEW_OWNER_NAME_LIST = "userdoc_new_owner_name_list";
    String USERDOC_OLD_CA_NAME_LIST = "userdoc_old_ca_name_list";
    String USERDOC_OLD_OWNER_NAME_LIST = "userdoc_old_owner_name_list";
    String USERDOC_OLD_REPRESENTATIVE_NAME_LIST = "userdoc_old_representative_name_list";
    String USERDOC_PAYER_NAME_LIST = "userdoc_payer_name_list";
    String USERDOC_PLEDGER_NAME_LIST = "userdoc_pledger_name_list";
    String USERDOC_REPRESENTATIVE_NAME_LIST = "userdoc_representative_name_list";

    String USERDOC_PAYEE_LIST_FORMATTED = "userdoc_payee_list_formatted";
    String USERDOC_APPLICANT_LIST_FORMATTED = "userdoc_applicant_list_formatted";
    String USERDOC_CREDITOR_LIST_FORMATTED = "userdoc_creditor_list_formatted";
    String USERDOC_GRANTEE_LIST_FORMATTED = "userdoc_grantee_list_formatted";
    String USERDOC_GRANTOR_LIST_FORMATTED = "userdoc_grantor_list_formatted";
    String USERDOC_NEW_CA_LIST_FORMATTED = "userdoc_new_ca_list_formatted";
    String USERDOC_NEW_REPRESENTATIVE_LIST_FORMATTED = "userdoc_new_representative_list_formatted";
    String USERDOC_NEW_OWNER_LIST_FORMATTED = "userdoc_new_owner_list_formatted";
    String USERDOC_OLD_CA_LIST_FORMATTED = "userdoc_old_ca_list_formatted";
    String USERDOC_OLD_OWNER_LIST_FORMATTED = "userdoc_old_owner_list_formatted";
    String USERDOC_OLD_REPRESENTATIVE_LIST_FORMATTED = "userdoc_old_representative_list_formatted";
    String USERDOC_PAYER_LIST_FORMATTED = "userdoc_payer_list_formatted";
    String USERDOC_PLEDGER_LIST_FORMATTED = "userdoc_pledger_list_formatted";
    String USERDOC_REPRESENTATIVE_LIST_FORMATTED = "userdoc_representative_list_formatted";

    String USERDOC_RECORDAL_DATE = "userdoc_recordal_date";

    /**
     * Array of EarlierMarkVariable
     */
    String USERDOC_EARLIER_MARKS = "userdoc_earlier_marks";

    String USERDOC_EFFECTIVE_DATE = "userdoc_effective_date";
    String USERDOC_LICENCE_EXPIRATION_DATE = "userdoc_licence_expiration_date";
    String USERDOC_APPEAL_DECISION_DATE ="userdoc_appeal_decision_date";
    String USERDOC_APPEAL_DECISION_NUMBER ="userdoc_appeal_decision_number";
    String USERDOC_BANKRUPTCY_COURT_NAME = "userdoc_bankruptcy_court_name";
    String USERDOC_BANKRUPTCY_CASE_NUMBER = "userdoc_bankruptcy_case_number";

    String USERDOC_APPROVED_NICE_LIST = "userdoc_approved_nice_list";
    String USERDOC_APPROVED_NICE_LIST_COMPLEX = "userdoc_approved_nice_list_complex";
    String USERDOC_SCOPE_DESIGNS_LIST = "userdoc_scope_designs_list";
    String USERDOC_SCOPE_DESIGNS_LIST_COMPLEX = "userdoc_scope_designs_list_complex";
    String USERDOC_SCOPE_DESIGNS_IMAGES = "userdoc_scope_designs_images";
    String USERDOC_SCOPE_NICE_CLASS_LIST = "userdoc_scope_nice_class_list";
    String USERDOC_SCOPE_NICE_CLASS_LIST_COMPLEX = "userdoc_scope_nice_class_list_complex";
    String USERDOC_SCOPE_NICE_CLASSES = "userdoc_scope_nice_classes";

    String USERDOC_LAST_ORDER_NUMBER = "userdoc_last_order_number";

}
