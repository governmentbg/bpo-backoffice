$(function () {
    if ($('#error-log-page-wrapper').length > 0) {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#error-log-page-wrapper",
        });
    }
});

$(document).on("click", "#error-log-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $tableWrapper = $('#error-log-table-wrapper');
    let url = $tableWrapper.data('url');

    let callParams = getFormValuesAsJson($('#error-log-filter-params'));
    callParams['sortColumn'] = checkEmptyField($(this).data('sort'));
    callParams['sortOrder'] = checkEmptyField($(this).data('order'));

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId:false});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#error-log-table-wrapper',
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "#error-log-table-wrapper [data-action='view-error-log']", function (e) {
    let url = $(this).data('url');
    let id = $(this).data('id');
    let callParams = getFormValuesAsJson($('#error-log-filter-params'));
    callParams['id'] = id;
    RequestUtils.get(url, callParams);
});

$(document).on("click", "[data-action='error-log-back']", function (e) {
    let url = $(this).data('url');
    let callParams = getFormValuesAsJson($('#error-log-filter-params'));
    RequestUtils.get(url, callParams);
});

$(document).on("click", "[data-action='open-resolve-error-modal']", function (e) {
    let url = $(this).data('url');
    let id = $(this).data('id');

    let callParams = {
        id: id
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#resolve-error-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#resolve-error-modal", true])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='resolve-error-submit']", function (e) {
    let url = $(this).data('url');
    let id = $(this).data('id');
    let comment = $('#error-log-resolveComment').val();
    let callParams = {
        id: id,
        comment: comment
    };
    RequestUtils.post(url, callParams);
});


$(document).on("click", "#search-error-log-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let resolved = $('#error-log-resolved-filter').val();
    let priority = $('#error-log-priority-filter').val();
    let about = $('#error-log-about-filter').val();
    let callParams = getFormValuesAsJson($('#error-log-filter-params'));
    callParams['resolved'] = resolved;
    callParams['priority'] = priority;
    callParams['about'] = about;

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId:false});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#error-log-table-wrapper',
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);

});