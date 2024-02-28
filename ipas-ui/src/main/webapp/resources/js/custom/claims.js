function fillAddWrapperFields(claimNbr, descrEn, descrBg) {
    $('#patent-claim-nbr').val(claimNbr);
    $('#patent-claim-en-description').val(descrEn);
    $('#patent-claim-bg-description').val(descrBg);
    executeCommonInitialization({
        initializeFormElementsWrapper: "#claims-add-wrapper"
    }, null);

}

$(document).on("click", "[data-action='edit-claims-list']", function (e) {
    let $button = $(this);
    let url = $button.data('url');

    let callParams = {
        claimNbr: $('#patent-claim-nbr').val(),
        descrEn: $('#patent-claim-en-description').val(),
        descrBg: $('#patent-claim-bg-description').val(),
        oldClaimNbr: $('#old-claimnbr').val(),

    };

    updateClaimsListWithAjax(url, callParams);
});

$(document).on("click", "[data-action='delete-specific-claim']", function (e) {

    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let callParams = {
            claimNbr: $button.data('claimnbr')
        };

        updateClaimsListWithAjax(url, callParams);
    }
});

function updateClaimsListWithAjax(url, callParams) {
    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#claim-modal-content",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: patentContainer + " #panel-body-" + PatentPanel.ClaimsData,
        executeOnValid: new FuncCall(handleClaimsInit, [])
    });


    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#patent-claim-form-modal", false);
    }

    commonAjaxCall(request, responseAction);
}

function handleClaimsInit() {
    executeCommonInitialization({
        inputsForEditWrapper: patentContainer + " #panel-body-" + PatentPanel.ClaimsData,
        showHiddenElements: true,
        hiddenElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.ClaimsData,
        initializeFormElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.ClaimsData,
        modalToInitialize: "#patent-claim-form-modal",
        modalStateExpression: "close",
    });
}

$(document).on("click", "[data-action='edit-specific-claim']", function (e) {
    let $button = $(this);
    fillAddWrapperFields($button.data('claimnbr'), $button.data('claim-descren'), $button.data('claim-descrbg'))
});

$(document).on("click", "[data-action='edit-claims-dialog-show']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        claimNbr: $(this).data('claimnbr'),
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#claim-modal-content",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#patent-claim-form-modal", false])
    });

    commonAjaxCall(request, responseAction);
});

