$(document).on("click", "[data-action='open-reports-modal']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let request = new CommonAjaxRequest(url,{});
    request.async = false;
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#reports-modal-div",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#reports-modal", false])
    });
    commonAjaxCall(request, responseAction);
    UIBlocker.unblock();
});

$(document).on("click", "[data-action='generate-report']", function (e) {
    let url = $(this).data('url');
    let fileName = $(".report-name-select option:selected").val();
    let callParams = {
        fileName: fileName,
        sessionIdentifier: $('#session-object-identifier').val()
    };
    RequestUtils.post(url, callParams);
    UIBlocker.unblock();
});