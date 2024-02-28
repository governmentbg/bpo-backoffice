const unspecifiedStorageId = '2';


$(document).on("click", "[data-action='edit-acp-taken-item-dialog-show']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        id: $(this).data('id'),
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    request.async = false;
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#acp-taken-item-modal-content",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#acp-item-form-modal", false])
    });
    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='acp-taken-item-delete']", function (e) {

    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let callParams = {
            id: $button.data('id')
        };
        updateAcpTakenItemAjax(url, callParams);
    }
});

$(document).on("change", '#acp-taken-item-storage', function (e) {
    if ($(this).val() !== unspecifiedStorageId) {
        $('#acp-taken-item-storage-description').val('');
        $('#acp-taken-item-storage-description-div').hide();
    } else {
        $('#acp-taken-item-storage-description-div').show();
    }
    executeCommonInitialization({
        initializeFormElementsWrapper: "#acp-taken-item-storage-description-div"
    }, null);
});

$(document).on("click", "[data-action='edit-acp-taken-item']", function (e) {
    let $button = $(this);
    let url = $button.data('url');

    let callParams = {
        id: $('#acp-taken-item-id').val(),
        type: $('#acp-taken-item-type').val(),
        typeDescription: $('#acp-taken-item-type-description').val(),
        storage: $('#acp-taken-item-storage').val(),
        storageDescription: $('#acp-taken-item-storage-description').val(),
        count: $('#acp-taken-item-count').val(),
        forDestruction: $('#acp-taken-item-forDestruction').is(":checked"),
        returned: $('#acp-taken-item-returned').is(":checked"),
        inStock: $('#acp-taken-item-in-stock').is(":checked")
    };
    updateAcpTakenItemAjax(url, callParams);
});


function updateAcpTakenItemAjax(url, callParams) {
    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#acp-taken-item-modal-content",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: markContainer + " #panel-body-" + MarkPanel.AcpTakenItemsData,
        executeOnValid: new FuncCall(handleAcpTakenItemInit, [])
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#acp-item-form-modal", false);
    }

    commonAjaxCall(request, responseAction);
}

function handleAcpTakenItemInit() {
    executeCommonInitialization({
        inputsForEditWrapper: markContainer + " #panel-body-" + MarkPanel.AcpTakenItemsData,
        showHiddenElements: true,
        hiddenElementsWrapper: markContainer + " #panel-body-" + MarkPanel.AcpTakenItemsData,
        initializeFormElementsWrapper: markContainer + " #panel-body-" + MarkPanel.AcpTakenItemsData,
        modalToInitialize: "#acp-item-form-modal",
        modalStateExpression: "close",
    });
}

