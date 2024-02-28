$(document).on("click", "[data-action='edit-citations-list']", function (e) {
    let $button = $(this);
    let url = $button.data('url');

    let callParams = {
        refNumber: $('#patent-citation-ref-number').val(),
        refDescription: $('#patent-citation-ref-description').val(),
        refClaims: $('#patent-citation-ref-claims').val(),
        oldRefNumber: $('#old-refNumber').val(),

    };

    console.log("callParams", callParams);

    updateCitationsListWithAjax(url, callParams);
});


$(document).on("click", "[data-action='edit-patent-citation']", function (e) {
    console.log("edit citation");

    let $button = $(this);
    let url = $button.data('url');

    let callParams = {
        refNumber: $('#patent-citation-ref-number').val(),
        refDescription: $('#patent-citation-ref-description').val(),
        refClaims: $('#patent-citation-ref-claims').val(),
        oldRefNumber: $('#old-refNumber').val(),
    };

    updateCitationsListWithAjax(url, callParams);
});

$(document).on("click", "[data-action='delete-patent-citation']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let callParams = {
            refNumber: $button.data('ref-number')
        };

        updateCitationsListWithAjax(url, callParams);
    }
});

function updateCitationsListWithAjax(url, callParams) {
    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#citation-modal-content",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: patentContainer + " #panel-body-" + PatentPanel.CitationsData,
        executeOnValid: new FuncCall(handleCitationInit, [])
    });


    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#patent-citation-form-modal", false);
    }

    commonAjaxCall(request, responseAction);
}

function handleCitationInit() {
    executeCommonInitialization({
        inputsForEditWrapper: patentContainer + " #panel-body-" + PatentPanel.CitationsData,
        showHiddenElements: true,
        hiddenElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.CitationsData,
        initializeFormElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.CitationsData,
        modalToInitialize: "#patent-citation-form-modal",
        modalStateExpression: "close",
    });
}

$(document).on("click", "[data-action='open-citation-modal']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        refNumber: $(this).data('ref-number'),
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#citation-modal-content",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#patent-citation-form-modal", false])
    });

    commonAjaxCall(request, responseAction);
});
