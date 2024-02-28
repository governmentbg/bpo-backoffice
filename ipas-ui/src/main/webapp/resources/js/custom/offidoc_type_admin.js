$(function () {
    if ($('#offidoc-type-page-wrapper').length > 0) {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#offidoc-type-page-wrapper"
        });
    }
});

$("#offidoc-page-wrapper").on('keydown',function(e) {
    if(e.which == 13) {
        $("[data-action='submit-offidoc-type-search']").click();
    }
});

$(document).on("click", "[data-action='clear-offidoc-type-search']", function (e) {
    UIBlocker.block();
});

$(document).on("click", "[data-action='submit-offidoc-type-search']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        offidocName: $('#offidocName-filter').val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#offidoc-table-wrapper',
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "#offidoc-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $wrapper = $('#offidoc-table-wrapper');
    let url = $wrapper.data('url');
    let callParams = getFormValuesAsJson($('#offidoc-filter-params'));
    callParams['sortColumn'] = checkEmptyField($(this).data('sort'));
    callParams['sortOrder'] = checkEmptyField($(this).data('order'));

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateWithAjaxAndUnblock(request, '#offidoc-table-wrapper');
});

$(document).on("click", "#offidoc-table-wrapper [data-action='edit-offidoc'], [data-action='offidoc-back']", function (e) {
    let url = $(this).data('url');
    let callParams = getFormValuesAsJson($('#offidoc-filter-params'));
    RequestUtils.get(url, callParams);
});

$(document).on("click", "[data-action='main-offidoc-panel-save']", function (e) {
    let url = $(this).data('url');
    let dataJSON = getFormValuesAsJson($("#offidoc-type-main-panel"));
    dataJSON['offidocType'] = $("#offidoc").data('offidoc-type');
    let callParams = {
        data: JSON.stringify(dataJSON)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI:true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#offidoc-main-data-panel",
        executeOnValid: new FuncCall(initMainOffidocPanel, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action=add-offidoc-template]", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let template = $("#offidoc-template").val();

        if (template == "" || template == null || template === undefined) {
            return;
        }
        let url = $button.data("url");
        addDeleteChangeOffidocTemplate(url, template);
    }
});

$(document).on("click", "[data-action='delete-offidoc-template'], [data-action='change-default-template']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data("url");
        let id = $(this).data("id");
        addDeleteChangeOffidocTemplate(url, id);
    }
});

function addDeleteChangeOffidocTemplate(url, template) {
    let callParams = {
        offidocType: $("#offidoc").data('offidoc-type'),
        template: template
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#offidoc-template-data-panel"
    });
    commonAjaxCall(request, responseAction);
}

$(document).on("click", "[data-action='open-offidoc-template-modal']", function (e) {
    let url = $(this).data('url');
    let templateName = $(this).data('id');
    let offidocType = $("#offidoc").data('offidoc-type');

    let callParams = {
        templateName: templateName,
        offidocType: offidocType
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#offidoc-template-edit-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#offidoc-template-edit-modal", false])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='edit-offidoc-template']", function (e) {
    let url = $(this).data('url');
    let template = $(this).data('template');
    let offidocType = $(this).data('offidoc-type');
    let nameConfig = $("input[type='radio'][name='template-nameConfig-radio']:checked").val();

    let callParams = {
        template: template,
        offidocType: offidocType,
        nameConfig: nameConfig
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#offidoc-template-data-panel",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#offidoc-templates-panel"
        }])
    });

    commonAjaxCall(request, responseAction);
});

function initMainOffidocPanel() {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#offidoc-type-main-panel"
    });

    initToastCloseBtn();
}