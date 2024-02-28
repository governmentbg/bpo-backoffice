$(document).ready(function () {
    let historyPanel = $('#panel-' + GeneralPanel.History);
    if (historyPanel.length > 0) {
        let url = historyPanel.data('url');

        let callParams = {};
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-" + GeneralPanel.History,
            executeOnValid: new FuncCall(executeCommonInitialization, [{
                initializeFormElementsWrapper: "#panel-" + GeneralPanel.History,
            }])
        });

        commonAjaxCall(request, responseAction);
    }
    $(document).on("click", "[data-action='object-history-details']", function (e) {
        let url = $( this ).data("url");
        let cn = $(this).data("changenbr");
        let callParams = {changeNumber : cn};
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#object-history-info-modal-wrapper",
            executeOnValid: new FuncCall(commonOpenModalFormInit, ["#object-history-info-modal", false])
        });

        commonAjaxCall(request, responseAction);

    });
    $(document).on("click", "#xml_copy_button", function (e) {
        navigator.clipboard.writeText(htmlDecode($("#xml_content").html()));
        BaseModal.openSuccessModal(messages["message.copied.to.clipboard"]);
    });

    function htmlDecode(input){
        var e = document.createElement('textarea');
        e.innerHTML = input;
        // handle case of empty input
        return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
    }

});