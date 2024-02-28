$(document).on("change", '#acp-penalty-penaltyType, #acp-penalty-paymentStatus', function (e) {
    let $select = $(this);
    let url = $select.data('url');
    let data = getFormValuesAsJson($(markContainer + ' #panel-' + MarkPanel.AcpAdministrativePenaltyData));
    let callParams = {
        data: JSON.stringify(data)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#acp-penalty-fields-area",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: "#acp-penalty-fields-area",
            initializeFormElementsWrapper: "#acp-penalty-fields-area",
        }])
    });

    commonAjaxCall(request, responseAction);
});