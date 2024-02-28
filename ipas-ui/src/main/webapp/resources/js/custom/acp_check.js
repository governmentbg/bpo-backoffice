$(document).on("click", "[data-action='delete-acp-check-reason']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let area = "#check-reasons-area";
        if (null != url) {
            let callParams = {
                id: $button.data('id'),
            };
            let request = new CommonAjaxRequest(url, {requestData: callParams});
            let responseAction = new CommonAjaxResponseAction({
                updateContainerOnValid: area,
                executeOnValid: new FuncCall(handleApcCheckPanelInit, [])
            });
            commonAjaxCall(request, responseAction);
        }
    }

});




$(document).on("click", "[data-action='add-acp-check-reason']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let area = "#check-reasons-area";
    if (null != url) {
        let callParams = {
            id: $('#acp-check-reasonId').val()
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: area,
            executeOnValid: new FuncCall(handleApcCheckPanelInit, [])
        });
        commonAjaxCall(request, responseAction);
    }
});




function handleApcCheckPanelInit() {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#check-reasons-area"
    }, null);
}