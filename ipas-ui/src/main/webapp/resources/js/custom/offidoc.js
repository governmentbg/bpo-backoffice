let offidocContainer = '#' + PanelContainer.Offidoc;
let decisionsContainer = '#' + OffidocPanel.Decisions;

$(function () {
    if ($("#open-generated-file-panel").length > 0) {
        $(offidocContainer + ' #panel-offidoc-generate-files-data').find('a[data-action="edit"]').click()
    }
    if($(offidocContainer).length >0 ){
        loadDecisionsData();
    }
    initPublishedDecisionPanel();
});


function initPublishedDecisionPanel() {
    uploadDecisionForPublishing();
}

$(document).on("click", "[data-action='delete-decision-for-publishing']", function (e) {
    let $button = $(this);
    let url = $button.data('url');

    let request = new CommonAjaxRequest(url, {requestData: null});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: ".published-decision-wrapper",
        executeOnValid: new FuncCall(initPublishedDecisionPanel, [false])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='add-decision-for-publishing']", function (e) {
    e.preventDefault();
    $("#decision-for-publishing-upload").trigger('click');
});


function uploadDecisionForPublishing() {
    UIBlocker.block();
    $("#decision-for-publishing-upload").fileupload({
        url: $(this).data('url'),
        complete: function (response, e) {
            $(".published-decision-wrapper").empty();
            $(".published-decision-wrapper").append(response.responseText);
            initPublishedDecisionPanel();
        },
        fail: function (data, e) {
            alert('errors occurred');
        }
    }).bind('fileuploadsubmit', function (e, data) {
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val()
        };
    });
    UIBlocker.unblock();
}


$(document).on("click", "[data-action='generate-offidoc-file']", function (e) {
    let $button = $(this);

    let url = $button.data('url');
    let template = $button.data('template');
    let message = $button.data('message');

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, message)
    } else {
        let callParams = {
            templateName: template,
            sessionIdentifier: $('#session-object-identifier').val()
        };
        RequestUtils.post(url, callParams);
    }
});

function loadDecisionsData() {
    let decisionsPanel = $('#panel-offidoc-decisions-data');
    if (decisionsPanel.length > 0) {
        let url = decisionsPanel.data('url');

        let callParams = {};
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: decisionsContainer,
            executeOnValid: new FuncCall(executeCommonInitialization, [{
                panelToInitialize: decisionsContainer,
                initializeFormElementsWrapper: decisionsContainer,
            }])
        });

        commonAjaxCall(request, responseAction);
    }
}

$(document).on("click", "#createDecision", function(){
    let templateId = $("#templateId").val();
    if(templateId != ""){
        $("#templateName").val($("#templateId option:selected").text());

        let callParams = $("#createDecision").closest("form").serialize();
        let url = $("#createDecision").closest("form").attr("action");
        let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: decisionsContainer,
            executeOnValid: new FuncCall(afterPanelDataModification, [])
        });

        commonAjaxCall(request, responseAction);

    } else {
        Confirmation.openInfoModal($("#templateEmptyWarn").text());
    }
});

$(document).on("click", "[data-action='edit-decision']", function(e){
    e.preventDefault();
    let url = $(this).data('url');
    window.open(url);
    $(this).closest(".action-menu").addClass("none");
});

$(document).on("click", "[data-action='delete-decision']", function (e) {
    e.preventDefault();
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data('url');
        let context = $(this).data('context');

        let callParams = {
            contextId: context
        };
        let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: decisionsContainer,
            executeOnValid: new FuncCall(afterPanelDataModification, [])
        });

        commonAjaxCall(request, responseAction);
    }
});

function afterPanelDataModification(data){
    executeCommonInitialization({
        panelToInitialize: decisionsContainer,
        initializeFormElementsWrapper: decisionsContainer,
        showHiddenElements: true
    }, data);
    $('#panel-offidoc-decisions-data a[data-action="edit"]').click();
}

$(document).on("click", "[data-action='export-decision-file']", function (e) {
    e.preventDefault();
    let url = $(this).data('url');
    let context = $(this).data('context');
    let templateName = $(this).data('name');

    let callParams = {
        contextId: context,
        templateName: templateName
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data != 'ok'",
        executeOnValid: new FuncCall((data) => {
            Confirmation.openInfoModal($("#successfulExport").text());
        }, []),
        executeOnInvalid: new FuncCall((data) => {
            Confirmation.openInfoModal($("#exportFailed").text());
        }, []),
        executeOnError: new FuncCall((data) => {
            Confirmation.openInfoModal($("#exportFailed").text());
        }, [])
    });

    commonAjaxCall(request, responseAction);
});