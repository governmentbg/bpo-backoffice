$(document).on("click","a[data-action='show-terms-text']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        niceClassCode: $(this).data("id")
    };

    let request = new CommonAjaxRequest(url, {method: "GET", requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#international-nice-terms-text-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#international-nice-class-terms-text-modal", false])
    });

    commonAjaxCall(request, responseAction);
});