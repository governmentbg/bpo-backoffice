$(document).on("click", "[data-action='open-agent-power-of-attorney-modal']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        personNbr: $(this).data('person'),
        addressNbr: $(this).data('address'),
        personKind: $(this).data('kind'),
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#agent-power-of-attorney-modal-content",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#agent-power-of-attorney-form-modal", false])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='open-attorney-info-modal']", function (e) {
    let url = $(this).data('url');
    let infoData = {
        attorneyPowerTerm: $(this).data('powerterm'),
        reauthorizationRight: $(this).data('right'),
        authorizationCondition: $(this).data('condition'),
        priorReprsRevocation: $(this).data('revocation'),
    };

    let callParams = {
        powerOfAttorneyData: JSON.stringify(infoData)
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#agent-power-of-attorney-info-modal-content",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#agent-power-of-attorney-info-modal", false])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='edit-power-of-attorney-data']", function (e) {
    let url = $(this).data('url');

    let attorneyPowerTerm = $('#agent-data-attorney-power-term').val();
    let attorneyPowerTermIndefinite = $('#agent-data-attorney-power-term-indefinite').is(':checked');
    let reauthorizationRight = $('input[type=radio][name=\'agent-reauthorizationRight\']:checked').val();
    let authorizationCondition = $('#agent-data-authorization-condition').val();
    let personNbr = $('#agent-personNbr').val();
    let addressNbr = $('#agent-addressNbr').val();
    let personKind = $('#agent-kind').val();
    let priorReprsRevocation = $('#agent-data-prior-reprs-revocation').is(':checked');

    let formData = {
        attorneyPowerTerm: attorneyPowerTerm,
        attorneyPowerTermIndefinite: attorneyPowerTermIndefinite,
        reauthorizationRight: reauthorizationRight,
        priorReprsRevocation: priorReprsRevocation,
        authorizationCondition: authorizationCondition,
        personKind: personKind,
        personNbr: personNbr,
        addressNbr: addressNbr,
    }

    let callParams = {
        powerOfAttorneyInitialData: JSON.stringify(formData)
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#agent-power-of-attorney-modal-content",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            panelToInitialize: "#panel-" + GeneralPanel.Persons,
            showHiddenElements: true,
            hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons,
            modalToInitialize: "#agent-power-of-attorney-form-modal",
            modalStateExpression: "close",
        }])
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#agent-power-of-attorney-form-modal", false);
    }

    commonAjaxCall(request, responseAction);

});


$(document).on("click", "[data-action='edit-power-of-attorney-initial-data']", function (e) {
    let url = $(this).data('url');
    let attorneyPowerTerm = $('#agent-data-attorney-power-term').val();
    let attorneyPowerTermIndefinite = $('#agent-data-attorney-power-term-indefinite').is(':checked');
    let reauthorizationRight = $('input[type=radio][name=\'agent-reauthorizationRight\']:checked').val();
    let authorizationCondition = $('#agent-data-authorization-condition').val();
    let priorReprsRevocation = $('#agent-data-prior-reprs-revocation').is(':checked');

    let personKind = $('#agentForm-personKind').val();
    let onlyActive = $('#agentForm-onlyActive').val();

    let formData = {
        attorneyPowerTerm: attorneyPowerTerm,
        attorneyPowerTermIndefinite: attorneyPowerTermIndefinite,
        reauthorizationRight: reauthorizationRight,
        priorReprsRevocation: priorReprsRevocation,
        authorizationCondition: authorizationCondition,
        personKind: personKind,
        onlyActive: onlyActive
    }

    let callParams = {
        powerOfAttorneyInitialData: JSON.stringify(formData)
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#agent-modal-content",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: "#agent-modal-content",
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid() {
        commonOpenModalFormInit("#agent-form-modal", false);
        initImportAgentAutocompelte();
        $("#autocomplete-agentImport").focus();
    }

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#agent-form-modal", false);
    }

    commonAjaxCall(request, responseAction);

});




