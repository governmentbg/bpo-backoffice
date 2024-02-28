$(document).ready(function () {
    let paymentsPanel = $('#panel-' + GeneralPanel.Payments);
    if (paymentsPanel.length > 0) {
        let url = paymentsPanel.data('url');

        let callParams = {};
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-" + GeneralPanel.Payments,
            executeOnValid: new FuncCall(executeCommonInitialization, [{
                initializeFormElementsWrapper: "#panel-" + GeneralPanel.Payments,
            }])
        });

        commonAjaxCall(request, responseAction);
    }
});