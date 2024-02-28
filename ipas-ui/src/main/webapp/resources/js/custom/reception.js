$(document).ready(function () {
    if ($('#reception-panel').length > 0) {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#reception-panel",
        });
    }

    if ($('#reception-success-modal').length > 0) {
        commonOpenModalFormInit("#reception-success-modal", true, false)
    }
});

$(document).on("click", "[data-action='delete-reception-person']", function (e) {
    let button = $(this);
    if (Confirmation.exist(button)) {
        Confirmation.openConfirmCloneBtnModal(button, button.data('message'))
    } else {
        let personKind = button.data('kind');
        let url = button.data('url');
        let callParams = {
            personKind: personKind,
            personNbr: button.data('person'),
            addressNbr: button.data('address')
        };
        let request = new CommonAjaxRequest(url, {requestData: callParams});

        let updateContainer;
        if (personKind === 1) { //Applicant
            updateContainer = "#reception-applicant-wrapper";
        } else {
            updateContainer = "#reception-agents-wrapper";
        }

        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: updateContainer,
        });

        commonAjaxCall(request, responseAction);
    }
});

$(document).on("change", '#select-receptionType', function (e) {
    let receptionType = $(this).val();
    let url = $(this).data('url');
    updateReceptionForm(receptionType, url);
});

$(document).on("click", "[data-action='change-reception-type']", function (e) {
    let receptionType = $(this).data('type');
    let url = $('#select-receptionType').data('url');
    updateReceptionForm(receptionType, url);
});

$(document).on("click", "[data-action='new-reception-close-modal']", function (e) {
    let receptionFromEmail = $('#receptionFromEmail').val();
    let receptionExistedRecord = $('#receptionExistedRecord').val();
    if (receptionFromEmail === "true"){
        $("#reception-from-email-div").show();
        $("#reception-existed-record-div").hide();
    }
    if (receptionExistedRecord==="true"){
        $("#reception-from-email-div").hide();
        $("#reception-existed-record-div").show();
    }
});

$(document).on("change", "#select-submissionType", function (e) {
    let receptionType = $("#select-receptionType").val();
    let url = $(this).data('url');
    updateReceptionForm(receptionType, url);
});

function updateReceptionForm(receptionType, url) {
    let dataJSON = getFormValuesAsJson($('#reception-panel'));
    dataJSON['receptionType'] = receptionType;
    let callParams = {
        data: JSON.stringify(dataJSON)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#reception-panel-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#reception-panel-wrapper",
        }])
    });

    commonAjaxCall(request, responseAction);
    $('.global-msg').hide();
}

$(document).on("change", '#select-userdocRelatedObjectGroupType', function (e) {
    let url = $(this).data('url');

    let dataJSON = getFormValuesAsJson($('#reception-panel'));
    let callParams = {
        data: JSON.stringify(dataJSON)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#reception-userdocRelatedObject-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#reception-userdocRelatedObject-wrapper",
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='open-userdoc-related-object-modal']", function (e) {
    let button = $(this);
    let relatedObjectFileTypeGroup = button.data('type');
    let url = button.data('url');
    let callParams = {
        relatedObjectFileTypeGroup: relatedObjectFileTypeGroup
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#search-userdoc-related-object-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#search-userdoc-related-object-modal", true])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("change", "input[type=radio][name='search-userdoc-related-object-radio']", function (e) {
    let $label = $('label[for=\'reception-relatedObjectSearchInput\']');
    let text = $(this).next().text();
    $label.text(text);
    setCookie("search-userdoc-related-object-radio", $(this).val());
});

$(document).on("click", "[data-action='submit-userdoc-related-object-search']", function (e) {
    let searchText = $('#reception-relatedObjectSearchInput').val().trim();
    let searchType = $('input[type=radio][name=\'search-userdoc-related-object-radio\']:checked').val();

    let button = $(this);
    let url = button.data('url');
    let fileTypeGroup = button.data('file-type-group');

    let callParams = {
        fileTypeGroup: fileTypeGroup,
        searchText: searchText,
        searchType: searchType,
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#related-userdoc-result-wrapper",
        // executeOnValid: new FuncCall(commonOpenModalFormInit, ["#search-userdoc-related-object-modal", true])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='add-userdoc-related-object']", function (e) {
    let button = $(this);

    let url = button.data('url');
    let filingNumber = button.data('number');
    let process = button.data('process');

    let callParams = {
        process: process,
        filingNumber: filingNumber,
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#userdoc-related-object-process-modal-wrapper",
        executeOnValid: new FuncCall(handleUserdocRelatedObjectTree, ["userdoc-related-object-process"])
    });

    commonAjaxCall(request, responseAction);
});

function handleUserdocRelatedObjectTree(id) {
    commonOpenModalFormInit("#userdoc-related-object-process-modal", true)

    activateTree(document.getElementById(id));
    $("#" + id + " li").first('li.has-children[aria-hidden="true"]').click();//Open first li
}

$(document).on("click", "[data-action='save-userdoc-related-object']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');
    let filingNumber = $('input[type=radio][name=\'selected-userdoc-related-object\']:checked').val();
    if (filingNumber !== undefined && filingNumber !== '' && filingNumber !== null) {
        let callParams = {
            filingNumber: filingNumber
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#reception-userdocRelatedObject-wrapper",
            executeOnValid: new FuncCall(handleReceptionExecuteOnValid, []),
        });

        function handleReceptionExecuteOnValid() {
            executeCommonInitialization({
                initializeFormElementsWrapper: "#reception-userdocRelatedObject-wrapper",
            });
            $('body').css({overflow: 'visible'});
        }

        commonAjaxCall(request, responseAction);
    }
});

$(document).on("click", "[data-action='delete-userdoc-related-object']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');

    let request = new CommonAjaxRequest(url, {requestData: {}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#reception-userdocRelatedObject-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#reception-userdocRelatedObject-wrapper",
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='change-ipas-working-date']", function (e) {
    e.preventDefault();
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let validationURL = $button.data('validation-url');

        let callParams = {
            newWorkingDate: $("#input-receptionWorkingDate").val(),
        };

        let request = new CommonAjaxRequest(validationURL, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            validationFailedExpression: "data.indexOf('validation-indication') != -1",
            updateContainerOnInvalid: "#change-ipas-working-date-modal-wrapper",
            executeOnInvalid: new FuncCall(commonOpenModalFormInit, ["#change-ipas-working-date-modal", true]),
            executeOnValid: new FuncCall(RequestUtils.post, [url, callParams]),
        });
        commonAjaxCall(request, responseAction);
    }
});

$(document).on("click", "[data-action='open-ipas-working-date-modal']", function (e) {
    e.preventDefault();
    commonOpenModalFormInit("#change-ipas-working-date-modal", true)
});


$(document).on("click", "[data-action='open-eupatent-search-modal']", function (e) {
    let button = $(this);
    let url = button.data('url');
    let request = new CommonAjaxRequest(url, {requestData: {}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#search-eupatent-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#search-eupatent-modal", true])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='submit-eupatent-search']", function (e) {
    let searchText = $('#reception-euPatentSearchInput').val();
    let searchType = $('input[type=radio][name=\'search-eupatent-radio\']:checked').val();
    let button = $(this);
    let url = button.data('url');
    let callParams = {
        searchText: searchText,
        searchType: searchType,
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#eupatent-search-result-wrapper",
    });
    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='add-reception-eupatent']", function (e) {
    let button = $(this);
    let url = button.data('url');
    let filingNumber = button.data('number');

    let callParams = {
        filingNumber: filingNumber,
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#reception-panel-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#reception-panel-wrapper",
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='delete-reception-eupatent']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');
    let request = new CommonAjaxRequest(url, {requestData: {}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#reception-euPatent-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            initializeFormElementsWrapper: "#reception-euPatent-wrapper",
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='reception-submit-btn']", function (e) {
    e.preventDefault();
    let $button = $(this);
    let form = $button.data('form');

    if (Confirmation.exist($button)) {
        UIBlocker.block();
        let url = $button.data('url');
        let dataJSON = getFormValuesAsJson($('#' + form));

        let additionalUserdocs = [];
        $('#reception-panel input[data-type="reception-additionalUserdoc-checkbox"]:checked').each(function () {
            additionalUserdocs.push($(this).val());
        });
        dataJSON ['additionalUserdocs'] = additionalUserdocs;
        let callParams = {
            data: JSON.stringify(dataJSON)
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            executeOnValid: new FuncCall(handleReceptionCheckData, [$button])
        });

        commonAjaxCall(request, responseAction);
    } else {
        isReceptionRequestOrignal($button);
    }

});

function handleReceptionCheckData(button, data) {
    let warnings = data.warnings;
    if (warnings.length > 0) {
        let msg = '';
        warnings.forEach(function myFunction(value, index, array) {
            msg = msg + '<div>' + value + '</div>';
        });
        msg = msg + '<div><b>' + button.data('message') + '</b></div>';
        Confirmation.openConfirmCloneBtnModal(button, msg)
    } else {
        isReceptionRequestOrignal(button);
    }

}

function isReceptionRequestOrignal(button) {
    UIBlocker.block();
    let form = button.data('form');
    let url = button.data('url-original');
    let dataJSON = getFormValuesAsJson($('#' + form));
    let callParams = {
        data: JSON.stringify(dataJSON)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(handleReceptionIsOriginal, [button])
    });
    commonAjaxCall(request, responseAction);

    function handleReceptionIsOriginal(button, receptionOriginal) {
        let form = button.data('form');
        let continueWithSameData = button.data('same-data');
        let originalRequestId = receptionOriginal.originalRequestId;
        if (receptionOriginal.isOriginal) {
            if (receptionOriginal.removeUpdateOriginalButton) {
                let onYes = getExecutableFuncCall(new FuncCall(submitReceptionForm, [form, false, originalRequestId, continueWithSameData, false]));
                let onNo = getExecutableFuncCall(new FuncCall(Confirmation.closeConfirmYesNoFuncsModal));
                Confirmation.openConfirmYesNoFuncsModal(receptionOriginal.message, onYes, onNo, messages['yes'], messages['no']);
            } else {
                let onYes = getExecutableFuncCall(new FuncCall(submitReceptionForm, [form, true, originalRequestId, continueWithSameData, false]));
                let onNo = getExecutableFuncCall(new FuncCall(submitReceptionForm, [form, false, originalRequestId, continueWithSameData, false]));
                let onClose = getExecutableFuncCall(new FuncCall(Confirmation.closeConfirmYesNoFuncsModal));
                Confirmation.openConfirmYesNoFuncsModal(receptionOriginal.message, onYes, onNo, messages['yes'], messages['no'], true, messages['btn.cancel'], onClose);
            }
        } else {
            submitReceptionForm(form, false, originalRequestId, continueWithSameData)
        }
    }
}

function submitReceptionForm(form, isOriginal, originalRequestId, continueWithSameData) {
    UIBlocker.block();
    if (isOriginal) {
        $("#" + form + " input[name='originalRequestId']").val(originalRequestId)
    }

    if (continueWithSameData === true) {
        $('<input>').attr({
            type: 'hidden',
            name: 'continueWithSameData',
            value: true
        }).appendTo('#' + form);

    }
    $("#" + form).submit();
}

$(document).on("click", "[data-action='clear-userdoc-related-object-search']", function (e) {
    $('#reception-relatedObjectSearchInput').val('');
    $('#related-userdoc-result-wrapper').empty();
    $("label[for='reception-relatedObjectSearchInput']").removeClass("active");
});

$(document).on("click", "[data-action='clear-eupatent-search']", function (e) {
    $('#reception-euPatentSearchInput').val('');
    $('#eupatent-search-result-wrapper').empty();
    $("label[for='reception-euPatentSearchInput']").removeClass("active");
});

$(document).on("change", "input[type=radio][name='search-eupatent-radio']", function (e) {
    let $label = $('label[for=\'reception-euPatentSearchInput\']');
    let text = $(this).next().text();
    $label.text(text);
});


$(document).on("change", 'input[type=radio][name=\'mark.figurativeMark\']', function (e) {
    let receptionType = $("#select-receptionType").val();
    let url = $('#figurative-mark-radio-wrapper').data('url');
    updateReceptionForm(receptionType, url);
});

$(document).on("click", "[data-action='scan-reception-file']", function (e) {
    let $scanButton = $(this);
    let registrationNumber = $scanButton.data('regnumber');
    let scannerService = new window.ScannerService();
    isScanApplicationRunning();

    let onGetDevice = function (res) {
        if (res.device) {
            let onScan = function (blob) {
                if (blob.size > 0) {
                    let formData = new FormData();
                    formData.append('file', blob, registrationNumber);
                    formData.append('registrationNumber', registrationNumber);

                    let url = $scanButton.data('url');
                    let request = new CommonAjaxRequest(url, {
                        method: "POST",
                        requestData: formData,
                        processData: false,
                        contentType: false,
                        useSessionId: false,
                        blockUI: true
                    });
                    let responseAction = new CommonAjaxResponseAction({
                        executeOnValid: new FuncCall(handleExecuteOnValid, [])
                    });

                    function handleExecuteOnValid(data) {
                        if (data === true) {
                            UIBlocker.unblock();
                            BaseModal.openSuccessModal(messages["reception.scan.success"]);
                        } else {
                            UIBlocker.unblock();
                            BaseModal.openErrorModal(messages["reception.scan.error"]);
                        }
                    }

                    commonAjaxCall(request, responseAction);
                }
            };
            scannerService.scan(onScan, false, registrationNumber + '.pdf');
        } else {
            BaseModal.openErrorModal(messages["scanner.is.not.configured"]);
            UIBlocker.unblock();
        }
    };
    scannerService.getCurrentDevice(onGetDevice);
});

function isScanApplicationRunning() {
    let requestPort1 = new XMLHttpRequest();
    requestPort1.open("GET", "https://127.0.0.1:12347/api/Scanner/Version", true); // false for synchronous request
    requestPort1.send(null);
    requestPort1.timeout = 250;
    requestPort1.onreadystatechange = function () {
        if (requestPort1.status !== 200) {
            let requestPort2 = new XMLHttpRequest();
            requestPort2.open("GET", "https://127.0.0.1:12346/api/Scanner/Version", true); // false for synchronous request
            requestPort2.send(null);
            requestPort2.timeout = 250;
            requestPort2.onreadystatechange = function () {
                if (requestPort2.status !== 200) {
                    let requestPort3 = new XMLHttpRequest();
                    requestPort3.open("GET", "https://127.0.0.1:12345/api/Scanner/Version", true); // false for synchronous request
                    requestPort3.send(null);
                    requestPort3.timeout = 250;
                    requestPort3.onreadystatechange = function () {
                        if (requestPort3.status !== 200) {
                            BaseModal.openErrorModal(messages["abbaty.scan.not.running"]);
                        }
                    };
                }
            };
        }
    };
}

$(document).on("click", "[data-action='userdoc-reception-process-tree'] li[data-load='false']", function (e) {
    let $target = $(e.target);
    let isClickedOnLoadTreeIcon = $target.hasClass("has-children");
    if (isClickedOnLoadTreeIcon) {
        let $treeNode = $(this);
        let processEventId = $treeNode.data('process');
        let processEventType = $treeNode.data('type');

        let closestLi = $treeNode.closest('ul[class=\'visible-block\']');
        let closestParentLi = closestLi.closest('li[class=\'has-children\'][aria-hidden=\'false\']');
        let parentProcess = closestParentLi.data('process');

        let closestUL = $treeNode.closest('ul[data-action=\'userdoc-reception-process-tree\']');
        let url = closestUL.data('url');
        let mainProcessId = closestUL.data('process');
        UIBlocker.block();
        $.ajax({
            url: url,
            type: 'POST',
            datatype: 'html',
            data: {
                parentProcess: parentProcess,
                mainProcessId: mainProcessId,
                processEventId: processEventId,
                processEventType: processEventType
            },
            success: function (data) {
                $treeNode.replaceWith(data);
                let selectedLi = $("ul[data-action='userdoc-reception-process-tree'] li[data-process='" + processEventId + "']");
                selectedLi.addClass('has-children');
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

$(document).on("change", '#select-euPatentType', function (e) {
    let euPatentType = $(this).val();
    let url = $('#euPatentType-select-wrapper').data('url');

    let callParams = {
        euPatentType: euPatentType
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({});
    commonAjaxCall(request, responseAction);
});
$(document).on("click", "#clear-reception-form", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'));
    } else {
        document.location.href = $button.data("url");
    }
});
$(document).on("click", ".remove-reception-attachment", function (e) {
    let $button = $(this);
    let callParams = {
        attSeqId: $button.data("attseqid")
    };
    let request = new CommonAjaxRequest($button.data("url"), {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#attachments-wrapper"
    });
    commonAjaxCall(request, responseAction);
});
$(document).on("click", ".reload-reception-attachments", function (e) {
    let $button = $(this);
    let callParams = {};
    let request = new CommonAjaxRequest($button.data("url"), {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#attachments-wrapper"
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", '[data-action="print-reception-barcode"]', function (e) {
    e.preventDefault();
    let documentId = $(this).attr('data-docid');
    let url = $(this).attr('data-url');

    UIBlocker.block();
    $.ajax({
        url: url,
        type: 'GET',
        datatype: 'html',
        data: {
            id: documentId
        }
    }).done(function (responseData) {
        var interpolated = responseData;
        var canvas = document.createElement("canvas");
        rasterizeHTML.drawHTML(interpolated, canvas).then(function (renderResult) {
            var canvas2 = document.createElement('canvas');
            canvas2.width = renderResult.image.width;
            canvas2.height = renderResult.image.height;
            var ctx = canvas2.getContext("2d");
            ctx.drawImage(renderResult.image, 0, 0);
            var dataURL = canvas2.toDataURL("image/png");
            var printerService = new window.PrinterService();
            var res = dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
            var onFinish = function () {
            };
            var onError = function () {
                BaseModal.openErrorModal(messages["reception.print.error"]);
            };
            printerService.print(onFinish, onError, res);
        });

    }).fail(function (xhr, ajaxOptions, thrownError) {
        openErrorModal(xhr)
    }).always(function () {
        UIBlocker.unblock();
    });

});