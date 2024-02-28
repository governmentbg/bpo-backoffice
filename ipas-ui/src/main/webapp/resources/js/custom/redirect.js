$(document).on("click", "[data-action='public-register-object-redirect']", function (e) {
    let $button = $(this);
    let filingNumber = $button.data('number');
    let url = $button.data('url');
    let callParams = {
        filingNumber: filingNumber
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(openURL, []),
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='journal-redirect']", function () {
    let $button = $(this);
    let processEvent = $button.data('process');
    let url = $button.data('url');
    let callParams = {
        processEventId: processEvent
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(openURL, [])
    });

    commonAjaxCall(request, responseAction);
});