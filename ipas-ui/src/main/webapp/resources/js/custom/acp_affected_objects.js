$(function () {
    if ($("#affected-objects-search-area").length > 0) {
        initAutocompleteByFillingNumber();
        initAutocompleteByRegistrationNumber();
        initTmviewAutocomplete();
    }
});

function initTmviewAutocomplete() {
    $("#acp-affected-object-tmview-autocomplete-reg-number").autocomplete({
        source: function (request, response) {
            var url = $("#acp-affected-object-tmview-autocomplete-reg-number").data("url");
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {
                    registrationNbr: request.term,
                    sessionIdentifier: $('#session-object-identifier').val()
                },
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

            commonAjaxCall(ajaxRequest, responseAction);
        },
        minLength: 1,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;

                }

                $('#acp-affected-object-tmview-autocomplete-reg-number').val(ui.item.RegistrationNumber);
                $('#affected-object-external-registration-number').val(ui.item.RegistrationNumber);
                $('#affected-object-external-name').val(ui.item.TradeMarkName);
                $('#affected-object-external-id').val(ui.item.ST13);

                executeCommonInitialization({
                    initializeFormElementsWrapper: "#affected-objects-search-area"
                }, null);
                return false;

            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        let itemText;
        if (item.TradeMarkName != null) {
            itemText = "<a><span><span>" + item.RegistrationNumber + ' ' + item.TradeMarkName + "</span></span></a>";
        } else {
            itemText = "<a><span><span>" + item.RegistrationNumber + "</span></span></a>";
        }

        return $("<li></li>")
            .data("item.autocomplete", item)
            .append(itemText).appendTo(ul);
    };
}

function initAutocompleteByRegistrationNumber() {
    $("#acp-affected-object-autocomplete-reg-number").autocomplete({
        source: function (request, response) {
            var url = $("#acp-affected-object-autocomplete-reg-number").data("url");
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {
                    registrationNbr: request.term,
                    fileType: $('#acp-affected-object-file-type').val(),
                    sessionIdentifier: $('#session-object-identifier').val()
                },
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

            commonAjaxCall(ajaxRequest, responseAction);
        },
        minLength: 1,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;

                }
                fillAffectedObjectInputFromAutocompletes(ui.item);
                return false;

            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        let itemText;
        if (item.title != null) {
            itemText = "<a><span><span>" + item.fileSeq + '/' + item.fileType + '/' + item.fileSeries + '/' + item.fileNbr + ' ' + item.title +
                "</span></span></a>";
        } else {
            itemText = "<a><span><span>" + item.fileSeq + '/' + item.fileType + '/' + item.fileSeries + '/' + item.fileNbr +
                "</span></span></a>";
        }

        return $("<li></li>")
            .data("item.autocomplete", item)
            .append(itemText).appendTo(ul);
    };

}


function initAutocompleteByFillingNumber() {
    $("#acp-affected-object-autocomplete-filing-number").autocomplete({
        source: function (request, response) {
            var url = $("#acp-affected-object-autocomplete-filing-number").data("url");
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {
                    fileNbr: request.term,
                    fileType: $('#acp-affected-object-file-type').val(),
                    sessionIdentifier: $('#session-object-identifier').val()
                },
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

            commonAjaxCall(ajaxRequest, responseAction);
        },
        minLength: 1,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;

                }
                fillAffectedObjectInputFromAutocompletes(ui.item);
                return false;

            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        let itemText;
        if (item.title != null) {
            itemText = "<a><span><span>" + item.fileSeq + '/' + item.fileType + '/' + item.fileSeries + '/' + item.fileNbr + ' ' + item.title +
                "</span></span></a>";
        } else {
            itemText = "<a><span><span>" + item.fileSeq + '/' + item.fileType + '/' + item.fileSeries + '/' + item.fileNbr +
                "</span></span></a>";
        }

        return $("<li></li>")
            .data("item.autocomplete", item)
            .append(itemText).appendTo(ul);
    };

}

function fillAffectedObjectInputFromAutocompletes(uiItem) {
    $('#acp-affected-object-autocomplete-filing-number').val(uiItem.fileSeq + '/' + uiItem.fileType + '/' + uiItem.fileSeries + '/' + uiItem.fileNbr);
    $('#affected-object-filling-number').val(uiItem.fileSeq + '/' + uiItem.fileType + '/' + uiItem.fileSeries + '/' + uiItem.fileNbr);

    if (uiItem.registrationNbr != null) {
        $('#acp-affected-object-autocomplete-reg-number').val(uiItem.registrationNbr);
        $('#affected-object-registration-number').val(uiItem.registrationNbr);
    }
    executeCommonInitialization({
        initializeFormElementsWrapper: "#affected-objects-search-area"
    }, null);
}


function clearAffectedObjectDataOnKeyUp(clearAction) {
    if ($('#affected-object-filling-number').val() !== '' || clearAction) {
        $('#affected-object-filling-number').val('');
        $('#affected-object-registration-number').val('');
        $('#acp-affected-object-autocomplete-filing-number').val('');
        $('#acp-affected-object-autocomplete-reg-number').val('');
    }
    if ($('#affected-object-external-registration-number').val() !== '' || clearAction) {
        $('#affected-object-external-registration-number').val('');
        $('#affected-object-external-name').val('');
        $('#affected-object-external-id').val('');
        $('#acp-affected-object-tmview-autocomplete-reg-number').val('');

    }
}

$(document).on("change", '#acp-affected-object-file-type', function (e) {
    clearAffectedObjectDataOnKeyUp();
    handleAutocompletesByFileType();
});

function handleAutocompletesByFileType() {
    let value = $("#acp-affected-object-file-type").val();
    if (value === "EXT_EM") {
        $(".affected-objects-local-search").hide();
        $(".affected-objects-tmview-search").show();
    } else {
        $(".affected-objects-local-search").show();
        $(".affected-objects-tmview-search").hide();
    }
}

$(document).on("keyup", "#acp-affected-object-autocomplete-filing-number", function (e) {
    clearAffectedObjectDataOnKeyUp();
});


$(document).on("keyup", "#acp-affected-object-autocomplete-reg-number", function (e) {
    clearAffectedObjectDataOnKeyUp();
});

$(document).on("keyup", "#acp-affected-object-tmview-autocomplete-reg-number", function (e) {
    clearAffectedObjectDataOnKeyUp();
});


$(document).on("click", "[data-action='acp-affected-object-add']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let area = "#affected-objects-table-area";
    if (null != url) {
        let callParams = {
            filingNumber: $('#affected-object-filling-number').val()
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: area,
            executeOnValid: new FuncCall(handleApcAffectedObjectsPanelInit, [])
        });
        commonAjaxCall(request, responseAction);
        clearAffectedObjectDataOnKeyUp(true);
    }
});

$(document).on("click", "[data-action='acp-affected-object-external-add']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let area = "#affected-objects-table-area";
    if (null != url) {
        let callParams = {
            registrationNumber: $('#affected-object-external-registration-number').val(),
            externalId: $('#affected-object-external-id').val(),
            name: $('#affected-object-external-name').val()
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: area,
            executeOnValid: new FuncCall(handleApcAffectedObjectsPanelInit, [])
        });
        commonAjaxCall(request, responseAction);
        clearAffectedObjectDataOnKeyUp(true);
    }
});


$(document).on("click", "[data-action='acp-affected-object-delete']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let area = "#affected-objects-table-area";
        if (null != url) {
            let callParams = {
                affectedObjectId: $button.data('id'),
            };
            let request = new CommonAjaxRequest(url, {requestData: callParams});
            let responseAction = new CommonAjaxResponseAction({
                updateContainerOnValid: area,
                executeOnValid: new FuncCall(handleApcAffectedObjectsPanelInit, [])
            });
            commonAjaxCall(request, responseAction);
        }
    }

});


function handleApcAffectedObjectsPanelInit() {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#affected-objects-search-area"
    }, null);
}