function initSearchPersonModalElements(personAutocomplete, cityAutocomplete) {
    // initPersonAutocomplete(personAutocomplete, false);
    // initSettlementAutocomplete(cityAutocomplete, false);
}

$(document).on("click", "[data-action='person-search-modal']", function (e) {
    let url = $(this).data('url');

    let callParams = {
        personKind: $(this).data('kind'),
        tempParentPersonNbr: $(this).data('tempparentpersonnbr')
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#person-search-modal-wrapper",
        executeOnValid: new FuncCall(handleOpenPersonSearchModal, ["#person-search-modal", false, "#person-searchModal-personName", "#person-searchModal-city"])
    });

    function handleOpenPersonSearchModal(modal, initEdit, personAutocomplete, cityAutocomplete) {
        commonOpenModalFormInit(modal, initEdit);
        initSearchPersonModalElements(personAutocomplete, cityAutocomplete);
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='person-search-submit']", function (e) {
    let blockUI = true;
    if (e.originalEvent === undefined) {
        blockUI = false;
    }

    let invalidContainerSelector = '#person-search-modal-body';
    let validContainerSelector = '#person-search-modal-table-wrapper';
    let url = $(this).data('url');
    let data = getFormValuesAsJson($('#person-search-content-data'));

    let $indCompany = $('#person-searchModal-indCompany');
    if ($indCompany.length > 0) {
        data['indCompany'] = checkEmptyField($indCompany.val());
    }

    let callParams = {
        personKind: $('#person-searchModal-personKind').val(),
        tempParentPersonNbr: $('#person-searchModal-tempParentPersonNbr').val(),
        representativeType: $('#person-searchModal-representativeType').val(),
        data: JSON.stringify(data)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: blockUI});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: invalidContainerSelector,
        executeOnInvalid: new FuncCall(handleInvalidPersonSearch, [invalidContainerSelector]),
        updateContainerOnValid: validContainerSelector,
        executeOnValid: new FuncCall(handleValidPersonSearch, [validContainerSelector]),
    });

    function handleInvalidPersonSearch(invalidContainerSelector) {
        executeCommonInitialization({
            initializeFormElementsWrapper: invalidContainerSelector,
            inputsForEditWrapper: invalidContainerSelector
        });
        initSearchPersonModalElements("#person-searchModal-personName", "#person-searchModal-city");
        $('#search-person-loader').hide();
    }

    function handleValidPersonSearch(validContainerSelector) {
        executeCommonInitialization({
            initializeFormElementsWrapper: validContainerSelector,
            inputsForEditWrapper: validContainerSelector
        });
        $('#search-person-loader').hide();
    }
    commonAjaxCall(request, responseAction);

    $("#person-search-modal-body .validation-error").remove();
});

$(document).on("click", "[data-action='person-info-modal']", function (e) {
    const url = $(this).data('url');
    const personNbr = $(this).data('person');
    const addressNbr = $(this).data('address');
    let fromDatabase = $(this).data('fromdatabase');

    const callParams = {
        personNbr: personNbr,
        addressNbr: addressNbr,
        fromDatabase: fromDatabase === undefined ? false : fromDatabase
    };

    const request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    const responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#person-info-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#person-info-modal", false])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='import-person']", function (e) {
    let url = $(this).data('url');

    let personNbr = $(this).data('person');
    let addressNbr = $(this).data('address');
    let personKind = $(this).data('kind');
    let tempParentPersonNbr = $(this).data('tempparentpersonnbr');
    let representativeType = $(this).data('representativetype');

    let callParams = {
        personNbr: personNbr,
        addressNbr: addressNbr,
        personKind: personKind,
        tempParentPersonNbr: tempParentPersonNbr,
        representativeType: representativeType
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});

    let responseAction;
    let about = $('#person-modal-content').data('about');
    if (about === 'reception') {

        let updateContainerOnValid;
        if (personKind == 1) { //Applicant
            updateContainerOnValid = "#reception-applicant-wrapper";
        } else {
            updateContainerOnValid = "#reception-agents-wrapper";
        }

        responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: updateContainerOnValid,
            executeOnValid: new FuncCall(commonCloseModalFormInit, ["#" + $(this).closest('div.modal').attr('id'), false]),
        });
    } else {
        responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
            executeOnValid: new FuncCall(commonCloseModalFormInit, ["#" + $(this).closest('div.modal').attr('id'), false]),
        });
    }
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='person-search-clear']", function (e) {
    $('#person-search-modal-body input[type="text"]').val('');
    $('#person-search-modal-body label').removeClass('active');
    $("#person-search-modal-body .validation-error").remove();
    $('#person-search-modal-table-wrapper').empty();
});

$(document).on("change paste keyup",
    " #person-searchModal-personName," +
    " #person-searchModal-street," +
    " #person-searchModal-city," +
    " #person-searchModal-zipCode," +
    " #person-searchModal-email," +
    " #person-searchModal-telephone", function (e) {

    setTimeout(() => {
        let personName = $('#person-searchModal-personName').val();
        if (personName !== undefined && personName !== null && personName !== '') {
            $('[data-action=\'person-search-submit\']').click();
            $('#search-person-loader').show();
            $('#search-person-result-table').addClass('block-element')
        } else {
            $('#person-search-modal-table-wrapper').empty();
        }
    },250);


    });

$(document).on("change", 'input[name="person-searchModal-personNameSearchType"]', function (e) {
    setTimeout(() => {
        let personName = $('#person-searchModal-personName').val();
        if (personName !== undefined && personName !== null && personName !== '') {
            $('[data-action=\'person-search-submit\']').click();
            $('#search-person-loader').show();
            $('#search-person-result-table').addClass('block-element')
        }
    },250);
});