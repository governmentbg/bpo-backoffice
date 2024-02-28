$(function () {
    if ($("#autocomplete-agentImport").length > 0) {
        initImportAgentAutocompelte();
    }
});

$(document).on("click", "[data-action='open-person-form-modal']", function (e) {
    let url = $(this).data('url');
    let isFromSearchForm = $(this).data('is-search');

    let callParams = {
        personKind: $(this).data('kind'),
        personNbr: $(this).data('person'),
        addressNbr: $(this).data('address'),
        tempParentPersonNbr: $(this).data('tempparentpersonnbr'),
        representativeType: $(this).data('representativetype')
    };

    let loadPersonFromSearch = $(this).data('load-person-from-search');
    if (loadPersonFromSearch === true) {
        callParams['loadPersonFromSearch'] = true;
    }

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#error-modal-wrapper",
        executeOnInvalid: new FuncCall(commonOpenModalFormInit, ["#process-error-modal", true]),
        updateContainerOnValid: "#person-modal-content",
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid() {
        commonOpenModalFormInit("#person-form-modal", false);
        initSettlementAutocomplete("#person-cityName", true, false, true, true);

        if (isFromSearchForm === true) {
            let searchFormValues = getFormValuesAsJson($('#person-search-modal-body'));
            $('#person-personName').val(searchFormValues.personName);
            $('#person-addressStreet').val(searchFormValues.street);
            $('#person-zipCode').val(searchFormValues.zipCode);
            $('#person-email').val(searchFormValues.email);
            $('#person-telephone').val(searchFormValues.telephone);
            checkForActiveLabels($('#person-form-modal'));
        }

    }

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='delete-person']", function (e) {
    personConfirmationAction($(this));
});

$(document).on("click", "[data-action='change-person-ca']", function (e) {
    personConfirmationAction($(this));
});

$(document).on("click", "[data-action='change-main-owner']", function (e) {
    personConfirmationAction($(this));
});

/*
 * Common for delete-person and change-person-ca
 */
function personConfirmationAction(button) {
    if (Confirmation.exist(button)) {
        Confirmation.openConfirmCloneBtnModal(button, button.data('message'))
    } else {
        let url = button.data('url');
        let callParams = {
            personKind: button.data('kind'),
            personNbr: button.data('person'),
            addressNbr: button.data('address')
        };
        var request = new CommonAjaxRequest(url, {requestData: callParams});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
            executeOnValid: new FuncCall(executeCommonInitialization, [{
                panelToInitialize: "#panel-" + GeneralPanel.Persons,
                showHiddenElements: true,
                hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
            }])
        });

        commonAjaxCall(request, responseAction);
    }
}

$(document).on("click", "[data-action='person-change-position']", function (e) {
    let button = $(this);
    let url = button.data('url');
    let callParams = {
        personKind: button.data('kind'),
        personNbr: button.data('person'),
        addressNbr: button.data('address'),
        direction: button.data('direction')
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            panelToInitialize: "#panel-" + GeneralPanel.Persons,
            showHiddenElements: true,
            hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
        }])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='save-person']", function (e) {
    let url = $(this).data('url');
    let personFormData = getFormValuesAsJson($('#person-form-modal'));
    let callParams = {
        data: JSON.stringify(personFormData)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});

    let responseAction;
    let about = $('#person-modal-content').data('about');
    if (about === 'reception') {
        let updateContainerOnValid;
        if (personFormData['personKind'] == 1) { //Applicant
            updateContainerOnValid = "#reception-applicant-wrapper";
        } else {
            updateContainerOnValid = "#reception-agents-wrapper";
        }

        responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: updateContainerOnValid,
            updateContainerOnInvalid: "#person-modal-content",
            validationFailedExpression: "data.indexOf('person-validation') != -1",
            executeOnValid: new FuncCall(handleReceptionExecuteOnValid, []),
            executeOnInvalid: new FuncCall(handleExecuteOnInvalid, [])
        });
    } else {
        responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
            updateContainerOnInvalid: "#person-modal-content",
            validationFailedExpression: "data.indexOf('person-validation') != -1",
            executeOnValid: new FuncCall(handleExecuteOnValid, []),
            executeOnInvalid: new FuncCall(handleExecuteOnInvalid, [])
        });
    }

    function handleExecuteOnValid() {
        executeCommonInitialization({
            panelToInitialize: "#panel-" + GeneralPanel.Persons,
            modalToInitialize: "#person-form-modal",
            modalStateExpression: "close",
            showHiddenElements: true,
            hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
        });
        if ($('#person-search-modal').length > 0) {
            commonCloseModalFormInit("#person-search-modal", false)
        }
        if ($('#agent-form-modal').length > 0) {
            commonCloseModalFormInit("#agent-form-modal", false)
        }
    }

    function handleReceptionExecuteOnValid() {
        executeCommonInitialization({
            modalToInitialize: "#person-form-modal",
            modalStateExpression: "close",
        });

        if ($('#person-search-modal').length > 0) {
            commonCloseModalFormInit("#person-search-modal", false)
        }
        if ($('#agent-form-modal').length > 0) {
            commonCloseModalFormInit("#agent-form-modal", false)
        }
    }

    function handleExecuteOnInvalid() {
        if ($('#person-form-modal').length > 0) {
            commonOpenModalFormInit("#person-form-modal", false);
            initSettlementAutocomplete("#person-cityName", true, false, true, true);
        } else {
            if ($('#agent-form-modal').length > 0) {
                commonOpenModalFormInit("#agent-form-modal", false);
                if ($("#autocomplete-agentImport").length > 0) {
                    initImportAgentAutocompelte();
                    $("#autocomplete-agentImport").focus();
                }
            }
        }


    }

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='save-agent']", function (e) {
    let agentCodeAndName = $("#autocomplete-agentImport").val();
    let url = $(this).data('url');
    let personKind = $(this).data('kind');
    let onlyActive = $(this).data('onlyactive');
    let representativeType = $(this).data('representativetype');
    let callParams = {
        agentCodeAndName: agentCodeAndName,
        personKind: personKind,
        onlyActive: onlyActive,
        representativeType: representativeType
    };

    let responseAction;
    let about = $('#agent-modal-content').data('about');
    if (about === 'reception') {
        responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#reception-agents-wrapper",
            updateContainerOnInvalid: "#agent-modal-content",
            validationFailedExpression: "data.indexOf('validation-indication') != -1",
            executeOnValid: new FuncCall(handleSaveAgentReceptionInit, []),
            executeOnInvalid: new FuncCall(handleSaveAgentValidationError, [])
        });
    } else {
        responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
            updateContainerOnInvalid: "#agent-modal-content",
            validationFailedExpression: "data.indexOf('validation-indication') != -1",
            executeOnValid: new FuncCall(handleSaveAgentInit, []),
            executeOnInvalid: new FuncCall(handleSaveAgentValidationError, [])
        });
    }

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    commonAjaxCall(request, responseAction);
});

$(document).on("change", '#person-residenceCountryCode', function (e) {
    let residenceCountryCode = $(this).val();
    let url = $(this).data('url');

    let callParams = {
        residenceCountryCode: residenceCountryCode
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#person-cityName-wrapper",
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid() {
        initSettlementAutocomplete("#person-cityName", true, false, true, true)
    }

    commonAjaxCall(request, responseAction);
});

function handleSaveAgentInit() {
    executeCommonInitialization({
        panelToInitialize: "#panel-" + GeneralPanel.Persons,
        modalToInitialize: "#agent-form-modal",
        modalStateExpression: "close",
        showHiddenElements: true,
        hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
    });
    initImportAgentAutocompelte();
}

function handleSaveAgentReceptionInit() {
    executeCommonInitialization({
        modalToInitialize: "#agent-form-modal",
        modalStateExpression: "close",
    });
    initImportAgentAutocompelte();
}

function handleSaveAgentValidationError() {
    commonOpenModalFormInit("#agent-form-modal", false);
    $("#autocomplete-agentImport").data('onlyactive', true);
    initImportAgentAutocompelte();
}

function initImportAgentAutocompelte() {
    if ($("#autocomplete-agentImport").length > 0) {
        let $autocomplete = $("#autocomplete-agentImport");
        let url = $autocomplete.data('url');
        $autocomplete.autocomplete({
            delay: 500,
            minLength: 1,
            source: function (request, response) {
                let ajaxRequest = new CommonAjaxRequest(url, {
                    requestData: {
                        nameOrCode: request.term,
                        onlyActive: $autocomplete.data('onlyactive'),
                        representativeType: $autocomplete.data('representativetype')
                    },
                    method: "GET",
                    dataType: "json",
                    useSessionId: false
                });
                let responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

                commonAjaxCall(ajaxRequest, responseAction);
            },
            select: function (event, ui) {
                let inputValue = ui.item.agentCode + ", " + ui.item.personName;
                $autocomplete.val(inputValue);
                $(".validation-wrapper").empty();
                return false;
            },
            change: function (event, ui) {
                if (ui.item == null) {
                    $(this).val("");
                    $autocomplete.val("");
                }
            }
        }).autocomplete("instance")._renderItem = function (ul, item) {
            let text = "<a><span class='{colorClass}'><span>" + item.agentCode +
            "</span><span style='margin-left:20px'>" + item.personName + "</span>" +
            "</span></a>";

            let colorClass = item.agentIndInactive ? 'm-red-text' : '';
            let updatedText = text.replace('{colorClass}',colorClass);

            return $("<li></li>")
                .data("item.autocomplete", item)
                .append(updatedText)
                .appendTo(ul);
        };
    }
}

function initPersonAutocomplete(inputSelector, selectOnlyFromList) {
    let $autocomplete = $(inputSelector);
    let url = $autocomplete.data('url');
    $autocomplete.autocomplete({
        delay: 500,
        minLength: 1,
        source: function (request, response) {
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {nameLike: request.term},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
            commonAjaxCall(ajaxRequest, responseAction);
        },
        focus: function (e, ui) {
            return true;
        },
        select: function (event, ui) {
            let inputValue = ui.item.value;
            $autocomplete.val(inputValue);
            $(".validation-wrapper").empty();
            return false;
        },
        change: function (event, ui) {
            if (ui.item == null && selectOnlyFromList === true) {
                $(this).val("");
                $autocomplete.val("");
            }
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span><span>" + item.label + "</span></span></a>").appendTo(ul);
    };

}

function initSettlementAutocomplete(inputSelector, selectOnlyFromList, useFullCityName, showDistrictAndMunicipality, useDistrictAndMunicipality) {
    let $autocomplete = $(inputSelector);
    let url = $autocomplete.data('url');
    let type = $autocomplete.data('type');

    function getAutocompleteValue(ui) {
        let inputValue;

        if (useDistrictAndMunicipality === true) {
            if (ui.item.isdistrict === true) {
                inputValue = ui.item.name;
            } else {
                inputValue = ui.item.name + ", " + ui.item.municipalitycode.name + ", " + ui.item.districtcode.name;
            }

        } else if (useFullCityName === true) {
            inputValue = ui.item.name;
        } else {
            inputValue = ui.item.settlementname;
        }
        return inputValue;
    }

    if (url !== null && url !== undefined && type === 'native') {
        $autocomplete.autocomplete({
            delay: 500,
            minLength: 1,
            source: function (request, response) {
                let ajaxRequest = new CommonAjaxRequest(url, {
                    requestData: {nameLike: request.term},
                    method: "GET",
                    dataType: "json",
                    useSessionId: false
                });
                let responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
                commonAjaxCall(ajaxRequest, responseAction);
            },
            focus: function (e, ui) {
                $autocomplete.val(getAutocompleteValue(ui));
                return false;
            },
            select: function (event, ui) {
                $autocomplete.val(getAutocompleteValue(ui));
                $(".validation-wrapper").empty();
                return false;
            },
            change: function (event, ui) {
                if (ui.item == null && selectOnlyFromList === true) {
                    $(this).val("");
                    $autocomplete.val("");
                }
            }
        }).autocomplete("instance")._renderItem = function (ul, item) {
            let optionTemplate;

            if (showDistrictAndMunicipality === true) {
                optionTemplate = "<a><span><span>" + item.name + ", " + item.municipalitycode.name + ", " + item.districtcode.name + "</span></span></a>";
            } else {
                optionTemplate = "<a><span><span>" + item.name + "</span></span></a>";
            }

            return $("<li></li>")
                .data("item.autocomplete", item)
                .append(optionTemplate).appendTo(ul);
        };
    }

    if (url !== null && url !== undefined && type === 'foreign') {
        $autocomplete.autocomplete({
            delay: 500,
            minLength: 1,
            source: function (request, response) {
                let ajaxRequest = new CommonAjaxRequest(url, {
                    requestData: {
                        nameLike: request.term,
                        country: $autocomplete.data('country')
                    },
                    method: "GET",
                    dataType: "json",
                    useSessionId: false
                });
                let responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
                commonAjaxCall(ajaxRequest, responseAction);
            },
            select: function (event, ui) {
                let inputValue = ui.item.value;
                $autocomplete.val(inputValue);
                $(".validation-wrapper").empty();
                return false;
            },
            change: function (event, ui) {

            }
        }).autocomplete("instance")._renderItem = function (ul, item) {
            let optionTemplate = "<a><span><span>" + item.label + "</span></span></a>";
            return $("<li></li>")
                .data("item.autocomplete", item)
                .append(optionTemplate).appendTo(ul);
        };
    }

}

$(document).on("click", "[data-action='copy-person']", (e) => {
    let $target = $(e.currentTarget);

    let callParams = {
        personKind: $target.data('kind'),
        personCopyType: $target.data('copy')
    };

    let url = $target.data('url');
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
        executeOnValid: new FuncCall(handleExecuteOnValid, []),
    });
    commonAjaxCall(request, responseAction);

    function handleExecuteOnValid() {
        executeCommonInitialization({
            panelToInitialize: "#panel-" + GeneralPanel.Persons,
            showHiddenElements: true,
            hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
        });
    }
});


$(document).on("click", "[data-action='import-gral-person']", (e) => {
    let $target = $(e.currentTarget);

    let callParams = {
        personKind: $target.data('kind')
    };

    let url = $target.data('url');
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
        executeOnValid: new FuncCall(handleExecuteOnValid, []),
    });
    commonAjaxCall(request, responseAction);

    function handleExecuteOnValid() {
        executeCommonInitialization({
            panelToInitialize: "#panel-" + GeneralPanel.Persons,
            showHiddenElements: true,
            hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
        });
    }
});

$(document).on("click", "[data-action='view-connected-old-owner']", function (e) {
    let url = $(this).data('url');
    let personNbr = $(this).data('person');

    let callParams = {
        newOwnerPersonNumber: personNbr,
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#person-info-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#person-info-modal", false])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='open-connect-new-owner-modal']", function (e) {
    const url = $(this).data('url');
    const newOwnerPersonNumber = $(this).data('person');
    const callParams = {
        newOwnerPersonNumber: newOwnerPersonNumber
    };
    const request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    const responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#connect-new-owner-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#connect-new-owner-modal", false])
    });
    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='connect-new-owner']", function (e) {
    const url = $(this).data('url');
    const newOwnerPersonNumber = $(this).data('newowner');
    let connectedPersonNumber = $('input[type=radio][name=\'connect-new-owner-radio\']:checked').val();
    const callParams = {
        newOwnerPersonNumber: newOwnerPersonNumber,
        connectedPersonNumber: connectedPersonNumber
    };

    const request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    const responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#connect-new-owner-modal-wrapper",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid,[]),
        updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#connect-new-owner-modal", true);
    }

    function handleExecuteOnValid() {
        executeCommonInitialization({
            panelToInitialize: "#panel-" + GeneralPanel.Persons,
            showHiddenElements: true,
            hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
        });
        commonCloseModalFormInit("#connect-new-owner-modal", false)
    }
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='import-new-owner-representatives']", (e) => {
    let $target = $(e.currentTarget);

    let callParams = {
        personKind: $target.data('kind')
    };

    let url = $target.data('url');
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#panel-body-" + GeneralPanel.Persons,
        executeOnValid: new FuncCall(handleExecuteOnValid, []),
    });
    commonAjaxCall(request, responseAction);

    function handleExecuteOnValid() {
        executeCommonInitialization({
            panelToInitialize: "#panel-" + GeneralPanel.Persons,
            showHiddenElements: true,
            hiddenElementsWrapper: "#panel-body-" + GeneralPanel.Persons
        });
    }
});

$(document).on("click", "[data-action='open-representative-modal']", function (e) {
    const url = $(this).data('url');
    const onlyActive = $(this).data('onlyactive');
    const personKind = $(this).data('kind');
    const callParams = {
        onlyActive: onlyActive,
        personKind: personKind
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#agent-modal-content",
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid() {
        commonOpenModalFormInit("#agent-form-modal", false);
        initImportAgentAutocompelte();
        $("#autocomplete-agentImport").focus();
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("change", '#representative-select-representativeType', function (e) {
    const url = $('#agent-form-modal-content').data('url');
    const onlyActive = $('#agentForm-onlyActive').val();
    const personKind = $('#agentForm-personKind').val();
    const representativeType = $('#representative-select-representativeType').find("option:selected").val();

    if (representativeType === 'NATURAL_PERSON' || representativeType === 'PARTNERSHIP') {
        $('#agent-form-modal').css('overflow', 'visible');
    } else {
        $('#agent-form-modal').css('overflow', '');
    }
    const callParams = {
        onlyActive: onlyActive,
        personKind: personKind,
        representativeType: representativeType
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#agent-form-modal-content",
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid() {
        if ($("#autocomplete-agentImport").length > 0) {
            initImportAgentAutocompelte();
            $("#autocomplete-agentImport").focus();
        }
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("change", "input[type=radio][name='person-individualIdType'], input[type=radio][name='person-indCompany'], #person-nationalityCountryCode", function (e) {
    let url = $('#person-update-individual-id-type-url').data('url');
    let personFormData = getFormValuesAsJson($('#person-form-modal'));
    let callParams = {
        data: JSON.stringify(personFormData)
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#person-individualIdType-wrapper',
        executeOnValid: new FuncCall(handleExecuteOnValid, []),
    });
    function handleExecuteOnValid() {
        checkForActiveLabels($('#person-form-modal'));
    }
    commonAjaxCall(request, responseAction);
});