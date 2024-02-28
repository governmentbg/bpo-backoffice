$(document).ready(function () {
    let $processPanel = $('#panel-' + GeneralPanel.Process);
    if ($processPanel.length > 0) {
        let url = $processPanel.data('url');
        let process = $processPanel.data('process');
        let request = new CommonAjaxRequest(url, {
            requestData: {
                processIdString: process
            }
        });
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-" + GeneralPanel.Process,
            executeOnValid: new FuncCall(executeOnValid, [])
        });

        function executeOnValid() {
            Panel.init($("#panel-" + GeneralPanel.Process));
        }

        commonAjaxCall(request, responseAction);
    }
});

$(document).on("click", "[data-action='process-tree'] li[data-load='false']", function (e) {
    let $target = $(e.target);
    let isClickedOnLoadTreeIcon = $target.hasClass("has-children");
    if (isClickedOnLoadTreeIcon) {
        let $treeNode = $(this);
        let processEventId = $treeNode.data('process');
        let processEventType = $treeNode.data('type');

        let isViewMode = true;
        let menuElement = $treeNode.find("i[data-process='" + processEventId + "']");
        if (menuElement && menuElement.length > 0) {
            isViewMode = menuElement.hasClass("none")
        }

        let closestUL = $treeNode.closest('ul[data-action=\'process-tree\']');
        let url = closestUL.data('url');
        let mainProcessId = closestUL.data('process');
        UIBlocker.block();
        $.ajax({
            url: url,
            type: 'POST',
            datatype: 'html',
            data: {
                mainProcessId: mainProcessId,
                processEventId: processEventId,
                processEventType: processEventType,
                isViewMode: isViewMode,
                sessionIdentifier: $('#session-object-identifier').val(),
            },
            success: function (data) {
                $treeNode.replaceWith(data);
                let selectedLi = $("ul[data-action='process-tree'] li[data-process='" + processEventId + "']");
                let jsSelectedLi = selectedLi.get(0);
                activateTree(jsSelectedLi);
                selectedLi.click();
                UIBlocker.unblock();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                UIBlocker.unblock();
                openErrorModal(xhr);
            }
        })

    }

});

$(document).on("click", "[data-action='toggle-menu'] ", function (e) {
    let menuId = $(this).data('id');
    let $menu = $('#' + menuId);
    let isClosed = $menu.hasClass('none');
    $('.action-menu').addClass('none');
    if (isClosed) {
        $menu.removeClass('none');
    } else {
        $menu.addClass('none');
    }
});


$(document).on("click", "[data-action='insert-next-action'] ", function (e) {
    let sessionIdentifier = $('#session-object-identifier').val()
    let selectedOption = $('#next-process-action-select').find("option:selected");
    let processActionType = selectedOption.data('type');
    let actionTyp = selectedOption.val();
    let process = $(this).data('process');
    let insertActionUrl = $(this).data('url');
    let openModalUrl = $(this).data('modal-url');
    let shouldOpenModalUrl = $(this).data('modal-check-url');

    let callParams = {
        process: process,
        processActionType: processActionType,
        actionTyp: actionTyp
    };

    let request = new CommonAjaxRequest(shouldOpenModalUrl, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(handleShouldOpenModal, [])
    });

    UIBlocker.block();
    commonAjaxCall(request, responseAction);

    function handleShouldOpenModal(result) {
        if (result === true) {
            openInsertNextActionModal.call(this);
        } else {
            RequestUtils.post(insertActionUrl, {
                actionTyp: actionTyp,
                process: process,
                sessionIdentifier: sessionIdentifier
            });
        }
    }

    function openInsertNextActionModal() {
        let callParams = {
            actionTyp: actionTyp,
            process: process,
        };
        let request = new CommonAjaxRequest(openModalUrl, {requestData: callParams, blockUI: true});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#process-action-modal-wrapper",
            executeOnValid: new FuncCall(commonOpenModalFormInit, ["#process-action-form-modal", true])
        });

        commonAjaxCall(request, responseAction);
    }

});


$(document).on("click", "[data-action='save-process-action']", function (e) {
    UIBlocker.block();
    let sessionIdentifier = $('#session-object-identifier').val()
    let process = $(this).data('process');
    let actionTyp = $(this).data('action-type');
    let url = $(this).data('url');
    let validationURL = $(this).data('validation-url');
    let dataJSON = {};
    dataJSON ['notes1'] = checkEmptyField($('#process-action-notes1').val());
    dataJSON ['notes2'] = checkEmptyField($('#process-action-notes2').val());
    dataJSON ['notes3'] = checkEmptyField($('#process-action-notes3').val());
    dataJSON ['notes4'] = checkEmptyField($('#process-action-notes4').val());
    dataJSON ['notes5'] = checkEmptyField($('#process-action-notes5').val());
    dataJSON ['manualDueDate'] = checkEmptyField($('#process-action-manualDueDate').val());
    dataJSON ['actionDate'] = checkEmptyField($('#process-action-actionDate').val());
    dataJSON ['specialFinalStatus'] = checkEmptyField($('#process-action-statusCode').find("option:selected").val());
    let selectedOffidocs = [];
    $('#process-action-form-modal input[data-type="office-document-template-checkbox"]:checked').each(function () {
        selectedOffidocs.push($(this).val());
    });
    dataJSON ['offidocTemplates'] = selectedOffidocs;
    dataJSON ['recordalDate'] = checkEmptyField($('#process-action-recordalDate').val());
    dataJSON ['invalidationDate'] = checkEmptyField($('#process-action-invalidationDate').val());
    dataJSON ['executionConfirmationText'] = checkEmptyField($('#process-action-executionConfirmationText').val());
    let transferCorrespondenceAddressValue = $('input[type=radio][name=\'process-action-transferCorrespondenceAddress\']:checked').val();
    if (checkEmptyField(transferCorrespondenceAddressValue) != null) {
        dataJSON ['transferCorrespondenceAddress'] = transferCorrespondenceAddressValue === 'true';
    }
    
    let callParams = {
        actionTyp: actionTyp,
        process: process,
        data: JSON.stringify(dataJSON),
        sessionIdentifier: sessionIdentifier
    };
    let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#process-action-modal-wrapper",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        executeOnValid: new FuncCall(RequestUtils.post, [url, callParams]),
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#process-action-form-modal", true);
        UIBlocker.unblock();
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("change", "input[type=radio][name='process-action-radio']", function (e) {
    let url = $(this).data('url');
    let type = $(this).data('type');
    let process = $(this).data('process');
    let request = new CommonAjaxRequest(url, {requestData: {type: type, process: process}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#next-action-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#next-action-wrapper",
            inputsForEditWrapper: "#next-action-wrapper"
        }])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='process-edit']", function (e) {
    let url = $(this).data('url');
    let process = $(this).data('process');
    let panel = $(this).data('panel');
    let isReception = $(this).data('reception');

    let callParams = {
        process: process,
        isReception: isReception,
        isEdit: true
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#error-modal-wrapper",
        executeOnInvalid: new FuncCall(commonOpenModalFormInit, ["#process-error-modal", true]),
        updateContainerOnValid: "#panel-body-" + panel,
        executeOnValid: new FuncCall(handleProcessEditMode, [panel, true, "#panel-body-" + panel, "#panel-body-" + panel])
    });
    commonAjaxCall(request, responseAction);
});

function handleProcessEditMode(panel, showHidden, showHiddenWrapper, editModeElements) {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#panel-" + panel,
        panelToInitialize: "#panel-" + panel,
        showHiddenElements: showHidden,
        hiddenElementsWrapper: showHiddenWrapper,
        inputsForEditWrapper: editModeElements
    });
    initUserAutocomplete($("#autocomplete-process-responsibleUser"));
    $('#panel-' + panel + " a[data-action='process-cancel']").removeClass('none');
    $('#panel-' + panel + " a[data-action='process-edit']").addClass('none');

    Object.values(PanelContainer).forEach(value => {
        let panelContainer = $('#' + value);
        if (panelContainer.length > 0) {
            $("#" + value + " a[data-action='cancel']").click();
            $("#" + value + " a[data-action='edit']").addClass('none');
        }
    });
}

$(document).on("click", "[data-action='process-cancel']", function (e) {
    let url = $(this).data('url');
    let process = $(this).data('process');
    let panel = $(this).data('panel');
    let isReception = $(this).data('reception');

    let callParams = {
        process: process,
        isReception: isReception,
        isEdit: false
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#panel-body-" + panel,
        executeOnValid: new FuncCall(handleProcessPanelViewMode, [panel, true, "#panel-body-" + panel, "#panel-body-" + panel])
    });
    commonAjaxCall(request, responseAction);
});

function handleProcessPanelViewMode(panel) {
    executeCommonInitialization({
        initializeFormElementsWrapper: " #panel-" + panel,
        panelToInitialize: "#panel-" + panel,
    });
    initUserAutocomplete($("#autocomplete-process-responsibleUser"));
    $('#panel-' + panel + " a[data-action='process-cancel']").addClass('none');
    $('#panel-' + panel + " a[data-action='process-edit']").removeClass('none');

    Object.values(PanelContainer).forEach(value => {
        let panelContainer = $('#' + value);
        if (panelContainer.length > 0) {
            $("#" + value + " a[data-action='edit']").removeClass('none');
        }
    });
}

$(document).on("click", "[data-action='change-responsible-user']", function (e) {
    UIBlocker.block();
    let sessionIdentifier = $('#session-object-identifier').val();
    let wrapperSelector = '#process-responsible-user-wrapper';
    let url = $(this).data('url');
    let validationURL = $(this).data('validation-url');
    let process = $(this).data('process');
    let userId = $("#autocomplete-process-responsibleUser").data('id');
    let userName = $("#autocomplete-process-responsibleUser").val();

    let callParams = {
        sessionIdentifier: sessionIdentifier,
        process: process,
        userId: userId,
        userName: userName
    };
    let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: wrapperSelector,
        executeOnInvalid: new FuncCall(handleResponsibleUserInitialization, [wrapperSelector]),
        executeOnValid: new FuncCall(RequestUtils.post, [url, callParams]),
    });
    commonAjaxCall(request, responseAction);
});

function handleResponsibleUserInitialization(wrapperSelector) {
    executeCommonInitialization({
        initializeFormElementsWrapper: wrapperSelector,
        inputsForEditWrapper: wrapperSelector
    });
    initUserAutocomplete($("#autocomplete-process-responsibleUser"));
    UIBlocker.unblock();
}

$(document).on("click", "[data-action='change-expiration-date']", function (e) {
    UIBlocker.block();
    let sessionIdentifier = $('#session-object-identifier').val();
    let wrapperSelector = '#process-manual-due-date-wrapper';
    let url = $(this).data('url');
    let validationURL = $(this).data('validation-url');
    let process = $(this).data('process');
    let manualDueDate = $("#input-process-dueDate").val();
    let callParams = {
        sessionIdentifier: sessionIdentifier,
        process: process,
        manualDueDate: manualDueDate
    };

    let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: wrapperSelector,
        executeOnInvalid: new FuncCall(handleManualDueDateInitialization, [wrapperSelector]),
        executeOnValid: new FuncCall(RequestUtils.post, [url, callParams]),
    });
    commonAjaxCall(request, responseAction);
});

function handleManualDueDateInitialization(wrapperSelector) {
    executeCommonInitialization({
        initializeFormElementsWrapper: wrapperSelector,
        inputsForEditWrapper: wrapperSelector
    });
    UIBlocker.unblock();
}

$(document).on("click", "[data-action='action-edit-modal']", function (e) {
    let url = $(this).closest('span').data('url');
    let closestActionMenuId = $(this).closest("div.action-menu").attr('id');
    let processAction = $("i[data-id='" + closestActionMenuId + "']").data('process')
    let callParams = {
        actionIdString: processAction,
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#process-edit-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#edit-action-modal", true])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='action-modal-edit-submit']", function (e) {
    UIBlocker.block();
    let wrapperSelector = '#process-edit-modal-wrapper';
    let url = $(this).data('url');
    let validationURL = $(this).data('validation-url');
    let actionIdString = $(this).data('action-id');

    let data = getFormValuesAsJson($('#process-edit-modal-wrapper'));
    Object.keys(data).forEach(value => {
        data[value + "Exist"] = true;
    });

    let callParams = {
        actionIdString: actionIdString,
        data: JSON.stringify(data)
    };
    setSessionIdentifier(callParams);

    let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: wrapperSelector,
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        executeOnValid: new FuncCall(RequestUtils.post, [url, callParams]),
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#edit-action-modal", false);
        UIBlocker.unblock();
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='abdocs-offidoc-redirect']", function (e) {
    let $button = $(this);
    let offidoc = $button.data('offidoc');
    let process = $button.data('process');
    let url = $button.data('url');
    let callParams = {
        offidoc: offidoc,
        process: process
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(openURL, []),
    });
    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='abdocs-userdoc-redirect']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let process = $button.data('process');
    let document = $button.data('document');
    let callParams = {
        process: process,
        document: document
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(openURL, []),
    });
    commonAjaxCall(request, responseAction);
});

function openURL(data) {
    window.open(data, '_blank');
}

$(document).on("click", "[data-action='action-delete-modal']", function (e) {
    let url = $(this).closest('span').data('url');
    let closestActionMenuId = $(this).closest("div.action-menu").attr('id');
    let processAction = $("i[data-id='" + closestActionMenuId + "']").data('process');
    let callParams = {
        actionIdString: processAction,
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#process-delete-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#delete-action-modal", true])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='action-modal-delete-submit']", function (e) {
    UIBlocker.block();
    let wrapperSelector = '#process-delete-modal-wrapper';
    let url = $(this).data('url');
    let validationURL = $(this).data('validation-url');
    let actionIdString = $(this).data('action-id');

    let deleteReason = $('#delete-action-deleteReason').val();
    let callParams = {
        actionIdString: actionIdString,
        deleteReason: deleteReason
    };
    setSessionIdentifier(callParams);

    let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: wrapperSelector,
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        executeOnValid: new FuncCall(RequestUtils.post, [url, callParams]),
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#delete-action-modal", true);
        UIBlocker.unblock();
    }

    commonAjaxCall(request, responseAction);
});

$(document).mouseup(function (e) {
    if ($('[data-action=\'toggle-menu\']').is(e.target)) {
        return;
    }

    if (!$('.action-menu').is(e.target) && $('.action-menu').has(e.target).length === 0) {
        $('.action-menu').addClass('none')
    }
});


$(document).on("click", "[data-action='abdocs-object-redirect']", function (e) {
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

$(document).on("click", "[data-action='open-register-userdoc-modal']", function (e) {
    let url = $(this).data('url');
    let callParams = {};
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#process-register-userdoc-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#register-document-modal", true])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "#submit-register-process-document", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let validationURL = $(this).data('validation-url');
    let formData = getFormValuesAsJson($('#register-document-modal'));
    let callParams = {
        data: JSON.stringify(formData),
    };

    let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#process-register-userdoc-modal-wrapper",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        executeOnValid: new FuncCall(RequestUtils.post, [url, callParams]),
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#register-document-modal", true);
        UIBlocker.unblock();
    }

    commonAjaxCall(request, responseAction);
});