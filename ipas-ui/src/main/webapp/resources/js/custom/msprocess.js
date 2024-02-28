$(document).on("click", "a[data-action='save'][data-panel='msprocess-main-data']", function (e) {
    if (!$(document).find("span.edited:not(.none)").length == 0) {
        $("#submitPanel").show()
    }
});


$(document).on("click", "[data-action='save-msprocess']", function (e) {
    e.preventDefault();
    let panelContainer = $(this).data('container');
    let editModePanels = $('#' + panelContainer).find('.panel[data-mode="edit"]');
    if (editModePanels.length > 0) {
        let titles = [];
        editModePanels.each(function (index, element) {
            let title = $(element).find(".panel-title").text();
            if (title !== undefined && title !== '') {
                titles.push(title)
            }
        });
        let joinTitles = titles.join(', ');
        Confirmation.openInfoModal(messages['open.panels.error.message'] + joinTitles);
    } else {
        UIBlocker.block();

        let data = {
            processType: $('#msprocess-processType').val(),
            processNumber: $('#msprocess-processNumber').val(),
            manualProcDescription: $('#msprocess-manualProcDescription').val()
        }

        let callParams = {
            editedPanels: getEditedPanelIds(panelContainer),
            data: JSON.stringify(data),
            sessionIdentifier: $('#session-object-identifier').val()
        };
        let url = $(this).data('url');
        RequestUtils.post(url, callParams);
    }
});


$(document).on("click", "[data-action='delete-msprocess']", function (e) {
    e.preventDefault();
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, messages["msprocess.delete.confirm"])
    } else {
        UIBlocker.block();
        let validationURL = $(this).data('validation-url');
        let url = $(this).data('url');

        let sessionIdentifier = $('#session-object-identifier').val();
        if (sessionIdentifier === undefined) {
            sessionIdentifier = null;
        }

        let callParams = {
            processIdString: $(this).data("process"),
            sessionIdentifier: sessionIdentifier,
        };

        let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            executeOnValid: new FuncCall(handleExecuteOnValid, []),
        });
        commonAjaxCall(request, responseAction);

        function handleExecuteOnValid(hasActions) {
            if (hasActions) {
                BaseModal.openInfoModal(messages["msprocess.delete.actions.msg"]);
                UIBlocker.unblock();
            } else {
                RequestUtils.post(url, callParams);
            }
        }
    }
});

$(document).on("click", "[data-action='create-manual-sub-process']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, messages["msprocess.create.confirm"])
    } else {
        let url = $button.data('url');
        let msProcessType = $('#msprocess-select').find("option:selected").val();

        let sessionIdentifier = $('#session-object-identifier').val();
        if (sessionIdentifier === undefined) {
            sessionIdentifier = null;
        }

        let callParams = {
            processIdString: $(this).data("process"),
            msProcessType: msProcessType,
            sessionIdentifier: sessionIdentifier,
        };
        RequestUtils.post(url, callParams);
    }
});