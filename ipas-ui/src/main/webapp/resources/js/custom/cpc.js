$(function () {
    if($(patentContainer).length > 0) {
        if ($("#cpcNumberAutocomplete").length > 0) {
            initCpcAutocomplete();
        }
    }
});

function initCpcAutocomplete() {
    $("#cpcNumberAutocomplete").autocomplete({
        source: function (request, response) {

            var url = $("#cpcNumberAutocomplete").data("url");
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {cpcNumber: request.term, sessionIdentifier: $('#session-object-identifier').val()},
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
                $('#cpc-add-button').show();
                $('#cpcNumberAutocomplete').val(ui.item.cpcSection + ui.item.cpcClass + ui.item.cpcSubclass + ui.item.cpcGroup + ui.item.cpcSubgroup);
                $('#new-cpcEdition').val(ui.item.cpcEdition);
                $('#new-cpcSection').val(ui.item.cpcSection);
                $('#new-cpcClass').val(ui.item.cpcClass);
                $('#new-cpcSubclass').val(ui.item.cpcSubclass);
                $('#new-cpcGroup').val(ui.item.cpcGroup);
                $('#new-cpcSubgroup').val(ui.item.cpcSubgroup);
                $('#new-cpcName').val(ui.item.cpcName);

                return false;
            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span><span>" + item.cpcSection + item.cpcClass + item.cpcSubclass + item.cpcGroup + item.cpcSubgroup + ' - ' + item.cpcName +
                "</span></span></a>").appendTo(ul);
    };
}

$(document).on("keyup", patentContainer + " #cpcNumberAutocomplete", function (e) {
    if ($('#cpc-add-button').css('display') != 'none') {
        $('#cpc-add-button').css('display', 'none');
        $('#new-cpcEdition').val('');
        $('#new-cpcSection').val('');
        $('#new-cpcClass').val('');
        $('#new-cpcSubclass').val('');
        $('#new-cpcGroup').val('');
        $('#new-cpcSubgroup').val('');
        $('#new-cpcName').val('');
    }

});

$(document).on("click", "[data-action='delete-specific-cpc']", function (e) {
    let panel = PatentPanel.CpcData;
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        if (null != url) {
            let callParams = {
                cpcEdition: $button.data('edition'),
                cpcSection: $button.data('section'),
                cpcClass: $button.data('class'),
                cpcSubclass: $button.data('subclass'),
                cpcGroup: $button.data('group'),
                cpcSubgroup: $button.data('subgroup')
            };

            var request = new CommonAjaxRequest(url, {requestData: callParams});
            var responseAction = new CommonAjaxResponseAction({
                updateContainerOnValid: patentContainer + " #panel-body-" + panel,
                executeOnValid: new FuncCall(handlePatentCpcInit, [panel])
            });
            commonAjaxCall(request, responseAction);
        }
    }
});



$(document).on("click", "[data-action='get-valid-cpcs']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    if (null != url) {
        let callParams = {
            cpcEdition: $button.data('edition'),
            cpcSection: $button.data('section'),
            cpcClass: $button.data('class'),
            cpcSubclass: $button.data('subclass'),
            cpcGroup: $button.data('group'),
            cpcSubgroup: $button.data('subgroup')
        };

        var request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#valid-cpcs-modal-content",
            executeOnValid: new FuncCall(commonOpenModalFormInit, ["#valid-cpcs-form-modal", false])
        });
        commonAjaxCall(request, responseAction);
    }
});

$(document).on("click", "[data-action='swap-cpc-position']", function (e) {
    let panel = PatentPanel.CpcData;
    let $button = $(this);
    let url = $button.data('url');
    if (null != url) {
        let callParams = {
            isHigherPosition: $button.data('higher-position'),
            cpcEdition: $button.data('edition'),
            cpcSection: $button.data('section'),
            cpcClass: $button.data('class'),
            cpcSubclass: $button.data('subclass'),
            cpcGroup: $button.data('group'),
            cpcSubgroup: $button.data('subgroup'),
            cpcQualification: $button.data('qualif')
        };

        var request = new CommonAjaxRequest(url, {requestData: callParams});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: patentContainer + " #panel-body-" + panel,
            executeOnValid: new FuncCall(handlePatentCpcInit, [panel])
        });
        commonAjaxCall(request, responseAction);
    }

});

$(document).on("click", "[data-action='add-patent-cpc']", function (e) {
    let panel = PatentPanel.CpcData;
    let $button = $(this);
    let url = $button.data('url');
    let data = null;
    if (null != url) {
        data = getFormValuesAsJson($(patentContainer + ' #new-cpc-hidden-fields'));
        let callParams = {
            data: JSON.stringify(data)
        };

        var request = new CommonAjaxRequest(url, {requestData: callParams});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: patentContainer + " #panel-body-" + panel,
            updateContainerOnInvalid: patentContainer + " #cpcs-errors-div",
            validationFailedExpression: "data.indexOf('<span class=\"none validation-indication\"></span>') != -1",
            executeOnValid: new FuncCall(handlePatentCpcInit, [panel])
        });
        commonAjaxCall(request, responseAction);
    }
});

function handlePatentCpcInit(panel) {
    executeCommonInitialization({
        hiddenElementsWrapper: patentContainer + " #panel-body-" + panel,
        showHiddenElements: true
    });

    initCpcAutocomplete();

}