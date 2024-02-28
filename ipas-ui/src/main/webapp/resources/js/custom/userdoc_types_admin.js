$(function () {
    if ($('#userdoc-types-page-wrapper').length > 0) {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#userdoc-types-page-wrapper"
        });
    }

    if ($('#userdoc-type-edit-panel-container').length > 0) {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#userdoc-type-main-panel,#userdoc-type-configurations-panel-content, #detail-page-userdoc-panels-panel, #userdoc-person-roles-panel, #userdoc-reception-relation-panel, #userdoc-abdocs-document-panel"
        });
        initUserdocTypesAutocomplete();
    }

});

$("#userdoc-types-page-wrapper").on('keydown',function(e) {
    if(e.which == 13) {
        $("[data-action='submit-userdoc-types-search']").click();
    }
});

$(document).on("click", "[data-action='clear-userdoc-types-search']", function (e) {
    UIBlocker.block();
});

$(document).on("click", "[data-action='submit-userdoc-types-search']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        userdocName: $('#userdocName-filter').val(),
        indInactive: $("#userdocStatus-filter").val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#userdoc-types-table-wrapper',
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "#userdoc-types-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $wrapper = $('#userdoc-types-table-wrapper');
    let url = $wrapper.data('url');
    let callParams = getFormValuesAsJson($('#userdoc-types-filter-params'));
    callParams['sortColumn'] = checkEmptyField($(this).data('sort'));
    callParams['sortOrder'] = checkEmptyField($(this).data('order'));
    callParams['indInactive'] = $('#userdoc-types-indInactive').val();

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateWithAjaxAndUnblock(request, '#userdoc-types-table-wrapper');

});

$(document).on("click", "#userdoc-types-table-wrapper [data-action='edit-userdoc-type'], [data-action='userdoc-types-back']", function (e) {
    let url = $(this).data('url');
    let callParams = getFormValuesAsJson($('#userdoc-types-filter-params'));
    callParams['indInactive'] = $('#userdoc-types-indInactive').val();
    RequestUtils.get(url, callParams);
});

function initUserdocTypesAutocomplete(){
    if ($("#userdoc-types-autocomplete").length == 0) {
        return;
    }

    let $autocomplete = $('#userdoc-types-autocomplete');

    $autocomplete.autocomplete({
        minLength: 1,
        delay: 500,
        source: function (request, response) {
            var url = $autocomplete.data('url');
            var userdocType = $('#userdocType').val();

            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {invalidateType: request.term, userdocType: userdocType},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
            commonAjaxCall(ajaxRequest, responseAction);
        },
        focus: function (event, ui) {
            $autocomplete.val(ui.item.userdocName);
            return false;
        },
        select: function (event, ui) {
            $autocomplete.val(ui.item.userdocName);
            $autocomplete.attr("data-id", ui.item.userdocType);
            return false;
        },
        change: function (event, ui) {
            if (ui.item == null) {
                $(this).val('');
                $(this).attr("data-id", '');
                $(this).siblings('label').removeClass('active');
            }
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span>" + item.userdocName + "</span></a>").appendTo(ul);
    };
}

$(document).on("click", "[data-action='add-invalidated-userdoc-type']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let autocomplete = $("#userdoc-types-autocomplete");
        let id = autocomplete.attr("data-id");

        if (id == "" || id == null || id === undefined) {
            return;
        }
        let url = $button.data("url");
        addDeleteInvalidationUserdoc(url, id);
    }
});

$(document).on("click", "[data-action='delete-invalidated-userdoc-type']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data("url");
        let id = $(this).data("id");
        addDeleteInvalidationUserdoc(url, id);
    }
});




$(document).on("click", "[data-action='save-configuration-data']", function (e) {
    let url = $(this).data('url');
    let departmentIds = [];
    $('.userdoc-type-department-checkbox').each(function(){
        if($(this).is(":checked")) {
            departmentIds.push($(this).attr('id'));
        }
    });
    let dataJSON = {
        userdocType: checkEmptyField($("#userdoc").data('userdoc-type')),
        registerToProcess: $('#registerToProcess').val(),
        inheritResponsibleUser: $('#inheritResponsibleUser').val(),
        markInheritResponsibleUser: $('#markInheritResponsibleUser').val(),
        abdocsUserTargetingOnResponsibleUserChange: $('#abdocsUserTargetingOnResponsibleUserChange').is(":checked"),
        abdocsUserTargetingOnRegistrationId: $('#abdocsUserTargetingOnRegistration').attr("data-id"),
        departmentIds:departmentIds,
        hasPublicLiabilities: $('#hasPublicLiabilities').is(":checked")
    };
    let data = JSON.stringify(dataJSON);

    let callParams = {
        data: data
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI:true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#userdoc-type-configurations-panel",
        executeOnValid: new FuncCall(initUserdocTypeConfigurationPanel)
    });
    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='main-panel-save']", function (e) {
   let url = $(this).data('url');
   let dataJSON = getFormValuesAsJson($("#userdoc-type-main-panel"));
   let data = JSON.stringify(dataJSON);
    let callParams = {
        data: data
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI:true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#userdoc-main-data-panel",
        executeOnValid: new FuncCall(initMainUserdocPanel)
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='save-abdocs-document']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        abdocsDoc: $("#selectAbdocsDocument option:selected").val(),
        registrationType: $("#selectRegistrationType option:selected").val(),
        userdocType: $("#userdoc").data('userdoc-type')
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#abdocs-document-panel",
        executeOnValid: new FuncCall(initAbdocsDocumentPanel)
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='add-panel-for-userdoc-type']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let panel = $("#selectPanels option:selected").val();

        if (panel == "" || panel == null || panel === undefined) {
            return;
        }
        let url = $button.data("url");
        addDeletePanelsFromUserdoc(url, panel);
    }
});

$(document).on("click", "[data-action='delete-panel-for-userdoc-type']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data("url");
        let id = $(this).data("id");
        addDeletePanelsFromUserdoc(url, id);
    }
});

$(document).on("click", "[data-action='add-userdoc-role']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let role = $("#selectPersonRoles option:selected").val();

        if (role == "" || role == null || role === undefined) {
            return;
        }
        let url = $button.data("url");
        addDeletePersonRoleFromUserdoc(url, role);
    }
});

$(document).on("click", "[data-action='delete-userdoc-role']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data("url");
        let id = $(this).data("id");
        addDeletePersonRoleFromUserdoc(url, id);
    }
});

$(document).on("click", "[data-action='add-reception-relation']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let mainType = $("#mainTypesSelect option:selected").val();

        if (mainType == "" || mainType == null || mainType === undefined) {
            return;
        }
        let url = $button.data("url");
        addDeleteUserdocReceptionRelation(url, mainType);
    }
});

$(document).on("click", "[data-action='delete-reception-relation']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data("url");
        let id = $(this).data("id");
        addDeleteUserdocReceptionRelation(url, id);
    }
});

$(document).on("click", "[data-action='open-reception-relation-modal']", function (e) {
    let url = $(this).data('url');
    let mainType = $(this).data('id');
    let userdocType = $("#userdoc").data('userdoc-type');

    let callParams = {
        mainType: mainType,
        userdocType: userdocType
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#reception-relation-edit-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#reception-relation-edit-modal", false])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='edit-reception-relation']", function (e) {
    let url = $(this).data('url');
    let mainType = $(this).data('main-type');
    let userdocType = $(this).data('userdoc-type');
    let isVisible = $("input[type='radio'][name='relation-isVisible-radio']:checked").val();

    let callParams = {
        mainType: mainType,
        userdocType: userdocType,
        isVisible: isVisible
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#reception-relation-panel",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#userdoc-reception-relation-panel"
        }])
    });

    commonAjaxCall(request, responseAction);
});


function addDeleteInvalidationUserdoc(url, invalidationUserdocId) {
    let callParams = {
        userdocType: $("#userdocType").val(),
        invalidateType: invalidationUserdocId
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        executeOnValid: new FuncCall(initUserdocTypesAutocomplete, []),
        updateContainerOnValid: "#invalidated-userdoc-types-panel"
    });
    commonAjaxCall(request, responseAction);
}

function addDeletePanelsFromUserdoc(url, panel) {
    let callParams = {
        panel: panel,
        userdocType: $("#userdoc").data('userdoc-type')
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#userdoc-panels-panel",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#detail-page-userdoc-panels-panel"
        }])
    });
    commonAjaxCall(request, responseAction);
}

function addDeletePersonRoleFromUserdoc(url, role) {
    let callParams = {
        role: role,
        userdocType: $("#userdoc").data('userdoc-type')
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#person-roles-panel",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#userdoc-person-roles-panel"
        }])
    });
    commonAjaxCall(request, responseAction);
}

function addDeleteUserdocReceptionRelation(url, mainType) {
    let callParams = {
        mainType: mainType,
        userdocType: $("#userdoc").data('userdoc-type'),
        isVisible: $("input[type='radio'][name='relation-isVisible']:checked").val()
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        updateContainerOnValid: "#reception-relation-panel",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#userdoc-reception-relation-panel"
        }])
    });
    commonAjaxCall(request, responseAction);
}

function initMainUserdocPanel() {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#userdoc-type-main-panel"
    });

    initToastCloseBtn();
}

function initUserdocTypeConfigurationPanel() {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#userdoc-type-configurations-panel-content"
    });

    initToastCloseBtn();
}

function initAbdocsDocumentPanel() {
    executeCommonInitialization({
       initializeFormElementsWrapper: "#userdoc-abdocs-document-panel"
    });

    initToastCloseBtn();
}



