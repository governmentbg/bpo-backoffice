$(function () {
    if ($('#nice-class-edit-page-wrapper').length > 0) {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#nice-class-panel"
        });
    }
});

$(document).on("click", "#nice-class-table-wrapper [data-action='edit-nice-class']", function (e) {
    UIBlocker.block();
});

$(document).on("click", "[data-action='nice-class-save']", function (e) {
    let url = $(this).data('url');
    let id = $("#nice-class-readonly").val();
    let heading = $("#nice-class-heading").val();
    let alphaList = $("#nice-class-alpha-list").val();

    let callParams = {
        id: id,
        heading: heading,
        alphaList: alphaList
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#nice-class-data-panel",
        executeOnValid: new FuncCall(initEditNiceCLassPanel, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "#clear-nice-heading", function (e) {
    $("#nice-class-heading").val('');
    $("#nice-class-heading").css('height', '44px');
});

$(document).on("click", "#clear-nice-alpha-list", function (e) {
    $("#nice-class-alpha-list").val('');
    $("#nice-class-alpha-list").css('height', '44px');
});

function initEditNiceCLassPanel() {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#nice-class-panel"
    });

    initToastCloseBtn();
}