let markContainer = '#' + PanelContainer.Mark;

$(function () {
    if ($(markContainer).length > 0) {
        setLabelToEditedPanels(markContainer);
        markAttachmentsInit();
        openPanelsWithValidationErrors($('#mark-validation-errors'), markContainer);
        ScrollUtils.panelScroll(markContainer);
        loadSeniorityData();
        loadMarkPublicationsPanel();
        usageRuleUploadInit();
    }
});

function loadMarkPublicationsPanel() {
    let publicationPanel = $('#panel-' + MarkPanel.Publication);
    if (publicationPanel.length > 0) {
        let url = publicationPanel.data('url');
        let callParams = {};
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-" + MarkPanel.Publication,
            executeOnValid: new FuncCall(executeCommonInitialization, [{
                initializeFormElementsWrapper: "#panel-" + MarkPanel.Publication,
            }])
        });

        commonAjaxCall(request, responseAction);
    }
}

function loadSeniorityData() {
    let claimsPanel = $('#panel-' + MarkPanel.Claims);
    if (claimsPanel.length > 0) {
        let url = claimsPanel.data('url');

        let callParams = {};
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#mark-seniority-wrapper",
            executeOnValid: new FuncCall(executeCommonInitialization, [{
                initializeFormElementsWrapper: "#mark-seniority-wrapper",
            }])
        });

        commonAjaxCall(request, responseAction);
    }
}

//TODO
$(document).on("click", markContainer + " a[data-action='save']", function (e) {
    let panel = $(this).data('panel');
    let url = $(this).data('url');

    let data = null;
    if (panel === MarkPanel.IdentityData) {
        data = getFormValuesAsJson($(markContainer + ' #panel-' + MarkPanel.IdentityData));
    } else if (panel === MarkPanel.MainData) {
        data = getFormValuesAsJson($(markContainer + ' #panel-' + MarkPanel.MainData));
    } else if (panel === MarkPanel.InternationalData) {
        data = getFormValuesAsJson($(markContainer + ' #panel-' + MarkPanel.InternationalData));
        let replacementRegistrationNbr = $("#imark-replacement-autocomplete-registrationNumber").attr("data-id");
        let replacementFilingNumber = $("#international-replacement-filingNumber").val();
        data = {...data, registrationNumber: replacementRegistrationNbr, replacementFilingNumber: replacementFilingNumber}
    } else if (panel === MarkPanel.AcpAffectedObjectsData) {
        data = getFormValuesAsJson($(markContainer + ' #panel-' + MarkPanel.AcpAffectedObjectsData));
    } else if (panel === MarkPanel.AcpCheckData) {
        data = getFormValuesAsJson($(markContainer + ' #panel-' + MarkPanel.AcpCheckData));
    } else if (panel === MarkPanel.AcpAdministrativePenaltyData) {
        data = getFormValuesAsJson($(markContainer + ' #panel-' + MarkPanel.AcpAdministrativePenaltyData));
    } else if (panel == MarkPanel.Claims) {

        let dataExhibition = getFormValuesAsJson($("#exhibition-subpanel"));
        let dataTransformation = getFormValuesAsJson($("#mark-transformation-subpanel"));
        let hasPriority = $("#object-priority-hasPriority").is(":checked");
        let hasDivisional = $("#divisional-app-hasDivisionalData").is(":checked");
        let dataDivisional = hasDivisional ? getFormValuesAsJson($("#divisional-application-subpanel")) : null;
        data = {
            divisionalData: dataDivisional,
            exhibitionData: dataExhibition,
            transformationData: dataTransformation,
            hasPriority: hasPriority
        };
    }

    let callParams = {
        isCancel: false,
        data: JSON.stringify(data)
    };
    if (($("#identity-panel-reload-page-param").val() === 'true' &&  panel === MarkPanel.IdentityData) ||
        (panel === MarkPanel.Claims && $("#divisional-app-hasDivisionalData").is(":checked") && $("#object-divisional-app-relationshipChanged").val() == 'true') ||
        (panel === MarkPanel.Claims && $("#mark-transformation-hasTransformationData").is(":checked")  && $('#object-relationshipExtended-filingNumber').val().length !== 0 && $('#object-relationshipExtended-filingDate').val().length !== 0
         && ($('#object-relationshipExtended-filingNumber').val()!==$('#object-relationshipExtended-filingNumber-original').val()
         || $('#object-relationshipExtended-filingDate').val()!==$('#object-relationshipExtended-filingDate-original').val()
         || $('#mark-object-relationshipExtended-applicationType').val()!==$('#mark-object-relationshipExtended-applicationType-original').val()))) {
        callParams.sessionIdentifier = $('#session-object-identifier').val();
        callParams.editedPanels = getEditedPanelIds(PanelContainer.Mark);
        RequestUtils.post(url, callParams);
    } else {
        var request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: markContainer + " #panel-body-" + panel,
            executeOnValid: new FuncCall(handleMarkPanelInitialization, [panel, panel === MarkPanel.MainData, false, null, null])
        });

        commonAjaxCall(request, responseAction);
    }


});

function handleMarkPanelInitialization(panel, logoInit, showHidden, showHiddenWrapper, editModeElements) {
    executeCommonInitialization({
        initializeFormElementsWrapper: markContainer + " #panel-" + panel,
        panelToInitialize: markContainer + " #panel-" + panel,
        showHiddenElements: showHidden,
        hiddenElementsWrapper: showHiddenWrapper,
        inputsForEditWrapper: editModeElements
    });
    if (logoInit) {
        markAttachmentsInit();
        usageRuleUploadInit();
    }
    if (panel === MarkPanel.AcpAffectedObjectsData) {
        initAutocompleteByFillingNumber();
        initAutocompleteByRegistrationNumber();
        initTmviewAutocomplete();
    }
    if (panel === MarkPanel.IdentityData) {
        initPortalUsersAutocomplete();
    }
    if (panel === MarkPanel.InternationalData) {
        initReplacementRegNumberAutocomplete();
    }
    initRelationshipAutocompletes();

}

$(document).on("click", markContainer + " a[data-action='cancel']", function (e) {
    let url = $(this).data('url');
    if (url) {
        let panel = $(this).data('panel');
        let callParams = {isCancel: true};

        var request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: markContainer + " #panel-body-" + panel,
            executeOnValid: new FuncCall(handleMarkPanelInitialization, [panel, panel === MarkPanel.MainData, false, null, null])
        });

        commonAjaxCall(request, responseAction);
    }
});

$("body").on("click", markContainer + " a[data-action='edit']", function (e) {
    let panel = $(this).data('panel');
    let url = $(this).data('url');
    if (null != url) {
        let callParams = {
            panel: panel
        };
        var request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        commonAjaxCall(request, null);
    }
});

$(document).on("click", markContainer + " [data-action='vienna-modal']", function (e) {
    let url = $(this).data('url');
    let attachmentIndex = $(this).data('index');
    let attachmentType = $(this).data('type');
    let callParams = {
        attachmentIndex: attachmentIndex,
        attachmentType: attachmentType
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#vienna-modal-content",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#vienna-modal",
            modalToInitialize: "#vienna-modal",
            modalStateExpression: "open"
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("change", "#vienna-category", function (e) {
    let url = $('#vienna-modal').data('division');
    let callParams = {
        category: $(this).val()
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#vienna-division-content',
        executeOnValid: new FuncCall(handleViennaSelectInit, ['#vienna-division-content', '#vienna-section-content'])
    });

    commonAjaxCall(request, responseAction);
});

function handleViennaSelectInit(selectWrapper, emptyElWrapper) {
    executeCommonInitialization({
        initializeFormElementsWrapper: selectWrapper
    });
    $(selectWrapper).removeClass("none");
    if (emptyElWrapper != null) {
        $(emptyElWrapper).empty();
    }

}

$(document).on("change", "#vienna-division", function (e) {
    let url = $('#vienna-modal').data('section');
    let callParams = {
        category: $('#vienna-category').val(),
        division: $(this).val()
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#vienna-section-content',
        executeOnValid: new FuncCall(handleViennaSelectInit, ["#vienna-section-content", null])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='save-vienna-class']", function (e) {
    let url = $(this).data('url');
    let attachmentIndex = $(this).data('index');
    let attachmentType = $(this).data('type');
    let attachmentIdentifier = attachmentType + attachmentIndex;
    let category = $('#vienna-category').val();
    let division = $('#vienna-division').val();
    let section = $('#vienna-section').val();
    let callParams = {
        attachmentIndex: attachmentIndex,
        attachmentType: attachmentType,
        category: category,
        division: division,
        section: section
    };

    function validateViennaClasses() {
        let $viennaCategoryValidationMsg = $('#vienna-category-validation');
        let $viennaDivisionValidationMsg = $('#vienna-division-validation');
        let $viennaSectionValidationMsg = $('#vienna-section-validation');
        $viennaDivisionValidationMsg.addClass('none');
        $viennaCategoryValidationMsg.addClass('none');
        $viennaSectionValidationMsg.addClass('none');

        let hasErrors = false;
        if (category == null || category.length < 1) {
            hasErrors = true;
            $viennaCategoryValidationMsg.removeClass('none');
        } else if (division == null || division.length < 1) {
            hasErrors = true;
            $viennaDivisionValidationMsg.removeClass('none');
        } else if (section == null || section.length < 1) {
            hasErrors = true;
            $viennaSectionValidationMsg.removeClass('none');
        }
        return hasErrors;
    }

    let hasErrors = validateViennaClasses();

    if (!hasErrors) {
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: markContainer + " #vienna-wrapper-" + attachmentIdentifier,
            executeOnValid: new FuncCall(commonCloseModalFormInit, ["#vienna-modal", true, markContainer + " #vienna-wrapper-" + attachmentIdentifier])
        });

        commonAjaxCall(request, responseAction);
    }
});


$(document).on("click", "[data-action='delete-vienna-class']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        const attachmentIdentifier = $button.data('type') + $button.data('index');
        let callParams = {
            category: $button.data('category'),
            division: $button.data('division'),
            section: $button.data('section'),
            attachmentIndex: $button.data('index'),
            attachmentType: $button.data('type')
        };
        const request = new CommonAjaxRequest(url, {requestData: callParams});
        const responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: markContainer + " #vienna-wrapper-" + attachmentIdentifier,
        });

        commonAjaxCall(request, responseAction);
    }
});

$(document).on("change", markContainer + ' #object-file-filingData-applicationType', function (e) {
    let $select = $(this);
    let url = $select.data('url');
    let callParams = {
        applicationType: $select.val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: markContainer + " #applicationSubtype-select-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: markContainer + " #applicationSubtype-select-wrapper",
            inputsForEditWrapper: markContainer + " #applicationSubtype-select-wrapper"
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("change", markContainer + ' #mark-signData-signType', function (e) {
    let markDetailsPanel = markContainer + ' #panel-' + MarkPanel.MainData;
    let $select = $(this);
    let url = $select.data('url');
    let data = getFormValuesAsJson($(markDetailsPanel));

    let callParams = {
        data: JSON.stringify(data)
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: markContainer + " #panel-body-" + MarkPanel.MainData,
        executeOnValid: new FuncCall(handleMarkPanelInitialization, [MarkPanel.MainData, true, true,
            markContainer + " #panel-body-" + MarkPanel.MainData,
            markContainer + " #panel-body-" + MarkPanel.MainData])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='delete-usage-rule']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');

        updateMarkUsageRulePanelWithAjax(url, null, false);
    }
});


$(document).on("click", "[data-action='add-mark-usage-rule']", function (e) {
    e.preventDefault();
    let usageRuleType = $("#mark-usageRule option:selected").val();
    $(markContainer + " #usageRuleType").val(usageRuleType);
    $(markContainer + " #mark-usage-rule-upload").trigger('click');
});


function usageRuleUploadInit() {
    UIBlocker.block();
    $(markContainer + " #mark-usage-rule-upload").fileupload({
        url: $(this).data('url'),
        complete: function (response, e) {
            let uploadedFileErrorDiv = $(markContainer + " #uploaded-usage-rule-errors-div");
            let validationTag = '<span class="none validation-indication"></span>';
            if (response.responseText.indexOf(validationTag) !== -1) {
                $(markContainer + " #mark-usage-rule-upload").val('');
                uploadedFileErrorDiv.empty();
                uploadedFileErrorDiv.append(response.responseText);
            } else {
                uploadedFileErrorDiv.empty();
                let urlOnUpload = $('#mark-usage-rule-upload').data('url-upload');
                updateMarkUsageRulePanelWithAjax(urlOnUpload, null, false);
            }
        },
        fail: function (data, e) {
            alert('errors occurred');
        }
    }).bind('fileuploadsubmit', function (e, data) {
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val(),
            usageRuleType: $('#usageRuleType').val()
        };
    });
    UIBlocker.unblock();
}

function updateMarkUsageRulePanelWithAjax(url, callParams, clearErrors) {
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: markContainer + " #mark-usage-rules-wrapper",
        executeOnValid: new FuncCall(handleUsageRuleUploadInit, [clearErrors])
    });

    commonAjaxCall(request, responseAction);
}


function handleUsageRuleUploadInit(clearErrors) {
    executeCommonInitialization({
        inputsForEditWrapper: markContainer + " #mark-usage-rules-wrapper",
        showHiddenElements: true,
        hiddenElementsWrapper: markContainer + " #mark-usage-rules-wrapper",
        initializeFormElementsWrapper: markContainer + " #mark-usage-rules-wrapper"
    });
    usageRuleUploadInit();
    if (clearErrors) {
        let uploadedFileErrorDiv = $(markContainer + " #uploaded-usage-rule-errors-div");
        uploadedFileErrorDiv.empty();
    }
}





$(document).on("click", "[data-action='vienna-fast-adding']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    const attachmentIndex = $button.data('index');
    const attachmentType = $button.data('type');
    const attachmentIdentifier = attachmentType + attachmentIndex;
    console.log(attachmentIdentifier);
    console.log(('#vienna-fast-adding-' + attachmentIdentifier));
    let viennaClasses = $('#vienna-fast-adding-' + attachmentIdentifier).val();
    console.log(viennaClasses);

    let callParams = {
        viennaClasses: viennaClasses,
        attachmentIndex: attachmentIndex,
        attachmentType: attachmentType
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: markContainer + " #vienna-wrapper-" + attachmentIdentifier,
        updateContainerOnInvalid: markContainer + " #vienna-fast-adding-errors-" + attachmentIdentifier,
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnValid: new FuncCall(handleViennaFastAddInit, [true, attachmentIdentifier]),
        // executeOnInvalid: new FuncCall(handleViennaFastAddInit, [false])
    });

    function handleViennaFastAddInit(isSuccess, attachmentIdentifier) {
        if (isSuccess) {
            executeCommonInitialization({
                showHiddenElements: true,
                hiddenElementsWrapper: markContainer + " #vienna-wrapper-" + attachmentIdentifier,
            });
            $(markContainer + " #vienna-fast-adding-errors-" + attachmentIdentifier).empty();
            $("#vienna-fast-adding-" + attachmentIdentifier).val("");
        }
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='reload-page']", function (e) {
    reloadPage();
});

$(document).on("click", "[data-action='delete-mark-attachment']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let callParams = {
            attachmentIndex: $button.data('index'),
            type: $button.data('type'),
            signType: $('#mark-signData-signType').val(),
        };
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: markContainer + " #mark-attachments-wrapper",
            executeOnValid: new FuncCall(handleExecuteOnValid, [])
        });

        function handleExecuteOnValid() {
            let wrapper = markContainer + " #mark-attachments-wrapper";
            executeCommonInitialization({
                initializeFormElementsWrapper: wrapper,
                showHiddenElements: true,
                hiddenElementsWrapper: wrapper,
                inputsForEditWrapper: wrapper
            });
            markAttachmentsInit();
            usageRuleUploadInit();
        }

        commonAjaxCall(request, responseAction);
    }
});


$(document).on("click", "[data-action='add-new-mark-attachment']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let type = $button.data('type');

    let callParams = {
        signType: $('#mark-signData-signType').val(),
        type: type
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: markContainer + " #mark-attachments-wrapper",
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid(data) {
        let wrapper = markContainer + " #mark-attachments-wrapper";
        executeCommonInitialization({
            initializeFormElementsWrapper: wrapper,
            showHiddenElements: true,
            hiddenElementsWrapper: wrapper,
            inputsForEditWrapper: wrapper
        });
        markAttachmentsInit();
        usageRuleUploadInit();
    }

    commonAjaxCall(request, responseAction);
});

function markAttachmentsInit() {
    $("[data-action='upload-mark-image']").fileupload({
        url: $(this).data('url'),
        complete: function (response, e) {
            let index = response.getResponseHeader("Attachment-Index");
            let $markLogoErrorDiv = $(markContainer + " #mark-logo-error-div-" + index);
            $markLogoErrorDiv.empty();

            let validationTag = '<span class="none validation-indication"></span>';
            if (response.responseText.indexOf(validationTag) !== -1) {
                $markLogoErrorDiv.append(response.responseText);
            } else {
                let $markLogoDiv = $(markContainer + " #mark-image-div-" + index);
                $markLogoDiv.html(response.responseText);

                let $fileName = $(markContainer + " #mark-logo-file-name-" + index);
                let fileNameLabel = $fileName.data('label');
                $fileName.text(fileNameLabel + $(this).get(0).files[0].name);
            }
        },
        fail: function (data, e) {
            console.log('fail');
            //TODO
        }
    }).bind('fileuploadstart', function (e, data) {
        UIBlocker.block();
    }).bind('fileuploadstop', function (e, data) {
        UIBlocker.unblock();
    }).bind('fileuploadsubmit', function (e, data) {
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val(),
            attachmentIndex: $(this).data('index')
        };
    });

    $("[data-action='upload-mark-video']").fileupload({
        url: $(this).data('url'),
        complete: function (response, e) {
            let index = response.getResponseHeader("Attachment-Index");

            let $errorDiv = $(markContainer + " #mark-video-error-div-" + index);
            $errorDiv.empty();

            let validationTag = '<span class="none validation-indication"></span>';
            if (response.responseText.indexOf(validationTag) !== -1) {
                $errorDiv.append(response.responseText);
            } else {
                let $attachmentDiv = $(markContainer + " #mark-video-div-" + index);
                $attachmentDiv.html(response.responseText);

                let $fileName = $(markContainer + " #mark-video-file-name-" + index);
                let fileNameLabel = $fileName.data('label');
                $fileName.text(fileNameLabel + $(this).get(0).files[0].name);
            }
        },
        fail: function (data, e) {
            UIBlocker.unblock();
        }
    }).bind('fileuploadstart', function (e, data) {
        UIBlocker.block();
    }).bind('fileuploadstop', function (e, data) {
        UIBlocker.unblock();
    }).bind('fileuploadsubmit', function (e, data) {
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val(),
            attachmentIndex: $(this).data('index')
        };
    });

    $("[data-action='upload-mark-audio']").fileupload({
        url: $(this).data('url'),
        complete: function (response, e) {
            let index = response.getResponseHeader("Attachment-Index");

            let $errorDiv = $(markContainer + " #mark-audio-error-div-" + index);
            $errorDiv.empty();

            let validationTag = '<span class="none validation-indication"></span>';
            if (response.responseText.indexOf(validationTag) !== -1) {
                $errorDiv.append(response.responseText);
            } else {
                let $audioDiv = $(markContainer + " #mark-audio-div-" + index);
                $audioDiv.html(response.responseText);

                let $fileName = $(markContainer + " #mark-audio-file-name-" + index);
                let fileNameLabel = $fileName.data('label');
                $fileName.text(fileNameLabel + $(this).get(0).files[0].name);
            }
        },
        fail: function (data, e) {
            UIBlocker.unblock();
        }
    }).bind('fileuploadstart', function (e, data) {
        UIBlocker.block();
    }).bind('fileuploadstop', function (e, data) {
        UIBlocker.unblock();
    }).bind('fileuploadsubmit', function (e, data) {
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val(),
            attachmentIndex: $(this).data('index')
        };
    });
}

$(document).on("change", "input[type=radio].mark-additional-nice-class-radio", function (e) {
    let wrapper = $(this).closest(".panel").find(".mark-additional-nice-classes-table-wrapper");
    resetNiceClasses($(this), wrapper)
});

