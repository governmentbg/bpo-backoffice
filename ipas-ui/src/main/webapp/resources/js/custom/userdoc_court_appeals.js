
$(document).on("click", "[data-action='delete-court-appeal']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let panel = $button.data('panel');
        let callParams = {
            courtAppealId: $button.data('appealid'),
        };
        updateCourtAppealsWithAjax(url,callParams);
    }
});

function updateCourtAppealsWithAjax(url, callParams) {
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    request.async = false;

    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#court-appeals-modal-div",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: userdocContainer + " #panel-body-"+UserdocPanel.CourtAppeals,
        executeOnValid: new FuncCall(handleCourtAppealsInit,[])
    });
    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#court-appeals-modal", false);
        initCourtsAutocomplete();
    }
    commonAjaxCall(request, responseAction);
}

function handleCourtAppealsInit() {
    executeCommonInitialization({
        inputsForEditWrapper:  userdocContainer + "#panel-body-"+UserdocPanel.CourtAppeals,
        showHiddenElements: true,
        hiddenElementsWrapper: userdocContainer + "#panel-body-"+UserdocPanel.CourtAppeals,
        initializeFormElementsWrapper: userdocContainer + "#panel-body-"+UserdocPanel.CourtAppeals,
        modalToInitialize: "#court-appeals-modal",
        modalStateExpression: "close",
    });
    initCourtsAutocomplete();
}

$(document).on("click", "[data-action='open-edit-court-appeal-modal']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        courtAppealId: $(this).data('appealid')
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    request.async = false;
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#court-appeals-modal-div",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#court-appeals-modal", false])
    });
    commonAjaxCall(request, responseAction);
    initCourtsAutocomplete();
});



$(document).on("click", "[data-action='edit-userdoc-court-appeals']", function (e) {
    UIBlocker.block();
    let $button = $(this);
    let url = $button.data('url');
    let data = null;
    let resultJson = {};



    resultJson['courtAppealId']=$('#userdoc-court-appeal-id').val();
    resultJson['courtCaseNbr']=$('#userdoc-court-case-nbr').val();
    resultJson['courtLink']=$('#userdoc-court-link').val();
    resultJson['courtCaseDate']=$('#userdoc-court-case-date').val();
    resultJson['courtId']=$("#userdoc-court-id option:selected").val();
    resultJson['judicialActNbr']=$('#userdoc-judicial-act-number').val();
    resultJson['judicialActDate']=$('#userdoc-judicial-act-date').val();
    resultJson['actTypeId']=$("#userdoc-act-type-id option:selected").val();
    resultJson['courtName']=$('#userdoc-court-appeal-name').val();

    data = resultJson;
    let callParams = {
        data: JSON.stringify(data)
    };
    updateCourtAppealsWithAjax(url,callParams);
    UIBlocker.unblock();
});