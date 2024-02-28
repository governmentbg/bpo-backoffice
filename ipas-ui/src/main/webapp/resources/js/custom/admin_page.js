$(function () {
    if ($("#error-log-card-counter").length > 0) {
        const url = $("#error-log-card-counter").data('url');
        const request = new CommonAjaxRequest(url, {
            method: 'GET',
            requestData: {},
            useSessionId: false
        });
        const responseAction = new CommonAjaxResponseAction({
            executeOnValid: new FuncCall(function (unresolvedCount) {
                if (unresolvedCount === 0) {
                    return;
                }
                let message = unresolvedCount;
                if (unresolvedCount === 1) {
                    message += ' ' + messages["badge.new.single.w"]
                } else {
                    message += ' ' + messages["badge.new.multiple"]
                }
                $('#error-log-card-counter').text(message);
                $('#error-log-card-counter').removeClass('none');
            }, [])
        });
        commonAjaxCall(request, responseAction);
    }
});