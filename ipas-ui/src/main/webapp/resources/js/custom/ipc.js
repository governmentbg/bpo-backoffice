$(function () {
    if ($(patentContainer).length > 0) {
        if ($("#ipcNumberAutocomplete").length > 0) {
            initIpcAutocomplete();
        }
    }
});

function initIpcAutocomplete() {
    $("#ipcNumberAutocomplete").autocomplete({
        source: function (request, response) {

            var url = $("#ipcNumberAutocomplete").data("url");
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {ipcNumber: request.term, sessionIdentifier: $('#session-object-identifier').val()},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

            commonAjaxCall(ajaxRequest, responseAction);
        },
        minLength: 3,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;

                }
                $('#ipc-add-button').show();
                $('#ipcNumberAutocomplete').val(ui.item.ipcSection + ui.item.ipcClass + ui.item.ipcSubclass + ui.item.ipcGroup + ui.item.ipcSubgroup);
                $('#new-ipcEdition').val(ui.item.ipcEdition);
                $('#new-ipcSection').val(ui.item.ipcSection);
                $('#new-ipcClass').val(ui.item.ipcClass);
                $('#new-ipcSubclass').val(ui.item.ipcSubclass);
                $('#new-ipcGroup').val(ui.item.ipcGroup);
                $('#new-ipcSubgroup').val(ui.item.ipcSubgroup);
                $('#new-ipcName').val(ui.item.ipcName);

                return false;
            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span><span>" + item.ipcSection + item.ipcClass + item.ipcSubclass + item.ipcGroup + item.ipcSubgroup + ' - ' + item.ipcName +
                "</span></span></a>").appendTo(ul);
    };
}

$(document).on("keyup", patentContainer + " #ipcNumberAutocomplete", function (e) {
    if ($('#ipc-add-button').css('display') != 'none') {
        $('#ipc-add-button').css('display', 'none');
        $('#new-ipcEdition').val('');
        $('#new-ipcSection').val('');
        $('#new-ipcClass').val('');
        $('#new-ipcSubclass').val('');
        $('#new-ipcGroup').val('');
        $('#new-ipcSubgroup').val('');
        $('#new-ipcName').val('');
    }

});

$(document).on("click", "[data-action='delete-specific-ipc']", function (e) {
    let panel = PatentPanel.IpcData;
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        if (null != url) {
            let callParams = {
                ipcEdition: $button.data('edition'),
                ipcSection: $button.data('section'),
                ipcClass: $button.data('class'),
                ipcSubclass: $button.data('subclass'),
                ipcGroup: $button.data('group'),
                ipcSubgroup: $button.data('subgroup')
            };

            var request = new CommonAjaxRequest(url, {requestData: callParams});
            var responseAction = new CommonAjaxResponseAction({
                updateContainerOnValid: patentContainer + " #panel-body-" + panel,
                executeOnValid: new FuncCall(handlePatentIpcInit, [panel])
            });
            commonAjaxCall(request, responseAction);
        }
    }
});


$(document).on("click", "[data-action='get-valid-ipcs']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    if (null != url) {
        let callParams = {
            ipcEdition: $button.data('edition'),
            ipcSection: $button.data('section'),
            ipcClass: $button.data('class'),
            ipcSubclass: $button.data('subclass'),
            ipcGroup: $button.data('group'),
            ipcSubgroup: $button.data('subgroup')
        };

        var request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#valid-ipcs-modal-content",
            executeOnValid: new FuncCall(commonOpenModalFormInit, ["#valid-ipcs-form-modal", false])
        });
        commonAjaxCall(request, responseAction);
    }
});

$(document).on("click", "[data-action='swap-ipc-position']", function (e) {
    let panel = PatentPanel.IpcData;
    let $button = $(this);
    let url = $button.data('url');
    if (null != url) {
        let callParams = {
            isHigherPosition: $button.data('higher-position'),
            ipcEdition: $button.data('edition'),
            ipcSection: $button.data('section'),
            ipcClass: $button.data('class'),
            ipcSubclass: $button.data('subclass'),
            ipcGroup: $button.data('group'),
            ipcSubgroup: $button.data('subgroup'),
            ipcQualification: $button.data('qualif')
        };

        var request = new CommonAjaxRequest(url, {requestData: callParams});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: patentContainer + " #panel-body-" + panel,
            executeOnValid: new FuncCall(handlePatentIpcInit, [panel])
        });
        commonAjaxCall(request, responseAction);
    }

});

$(document).on("click", "[data-action='add-patent-ipc']", function (e) {
    let panel = PatentPanel.IpcData;
    let $button = $(this);
    let url = $button.data('url');
    let data = null;
    if (null != url) {
        data = getFormValuesAsJson($(patentContainer + ' #new-ipc-hidden-fields'));
        let callParams = {
            data: JSON.stringify(data)
        };

        var request = new CommonAjaxRequest(url, {requestData: callParams});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: patentContainer + " #panel-body-" + panel,
            updateContainerOnInvalid: patentContainer + " #ipcs-errors-div",
            validationFailedExpression: "data.indexOf('<span class=\"none validation-indication\"></span>') != -1",
            executeOnValid: new FuncCall(handlePatentIpcInit, [panel])
        });
        commonAjaxCall(request, responseAction);
    }
});


$(document).on("click", "[data-action='ipc-copy-from-cpc-button']", function (e) {
    let panel = PatentPanel.IpcData;
    let $button = $(this);
    let url = $button.data('url');
    let data = null;
    if (null != url) {
        var request = new CommonAjaxRequest(url, {blockUI: true});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: patentContainer + " #panel-body-" + panel,
            updateContainerOnInvalid: patentContainer + " #ipcs-errors-div",
            validationFailedExpression: "data.indexOf('<span class=\"none validation-indication\"></span>') != -1",
            executeOnValid: new FuncCall(handlePatentIpcInit, [panel])
        });
        commonAjaxCall(request, responseAction);
    }
});

function handlePatentIpcInit(panel) {
    executeCommonInitialization({
        hiddenElementsWrapper: patentContainer + " #panel-body-" + panel,
        showHiddenElements: true
    });

    initIpcAutocomplete();

}