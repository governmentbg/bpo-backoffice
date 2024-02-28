$(document).on("click", "[data-action='acp-violation-place-delete']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let area = "#violation-places-table-area";
        if (null != url) {
            let callParams = {
                id: $button.data('id'),
            };
            let request = new CommonAjaxRequest(url, {requestData: callParams});
            let responseAction = new CommonAjaxResponseAction({
                updateContainerOnValid: area,
                executeOnValid: new FuncCall(handleApcViolationPlacePanelInit, [])
            });
            commonAjaxCall(request, responseAction);
        }
    }

});


$(document).on("click", "[data-action='acp-violation-place-add']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let area = "#violation-places-table-area";
    if (null != url) {
        let callParams = {
            description: $('#acp-violation-place-description').val()
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: area,
            executeOnValid: new FuncCall(handleApcViolationPlacePanelInit, [])
        });
        commonAjaxCall(request, responseAction);
    }
});


function handleApcViolationPlacePanelInit() {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#violation-places-table-area"
    }, null);
}