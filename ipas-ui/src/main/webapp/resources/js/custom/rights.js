$(function () {
    initRelationshipAutocompletes();
    initEuropeanPatentConversionAutocomplete();
});

function initRelationshipAutocompletes(){
    if ($(".relationship-autocomplete").length == 0) {
        return;
    }
    $(".relationship-autocomplete").on("change", function(e) {
        // console.log("Inside .relationship-autocomplete. " + $(this).data("autocomplete-changed"));
        //tyj kato tozi event se vika i pri izbor na file ot autocomplete-a (na teoriq ako se tyrsi po chast ot nomera moje v autocomplete-a da e napisano 123, no da se izbere marka 123456 - togava trqbva da se updete-na field-a na ekrana, koeto predizvikva onchange event)
        //tam se vdiga flag autocomplete-changed = true i tuk se proverqva dali flaga e vdignat - ako da - ne se resetva formata nanovo, zashtoto tova shte q izprazni!
        if ($(this).data("autocomplete-changed") == 'true') {
            $(this).data("autocomplete-changed", 'false');
        } else {
            resetRelationshipDataForm($(this), null);
        }
    });
    $(".relationship-autocomplete").each(function(i, el){
        $(el).autocomplete({
            source: function (request, response) {
                var url = $(this.element).data("url");
                var _masterFileType = $(this.element).data("master-file-type");
                var ajaxRequest = new CommonAjaxRequest(url, {requestData: {fileNbr: request.term, masterFileType: _masterFileType}, method: "GET", dataType: "json", useSessionId: false});
                var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

                commonAjaxCall(ajaxRequest, responseAction);
            },
            minLength: 1,
            delay:1000,
            select: function (event, ui) {
                // console.log("On Select....");
                resetRelationshipDataForm($(this), ui.item);
                return false;//very important!
            }, focus: function(event, ui) {
                // console.log("On Focus....");
                $(this).val(ui.item.fileNbr);
                return false;
            }
        }).autocomplete("instance")._renderItem = function (ul, item) {
            let objectName = '';
            if(item.name != null){
                objectName = item.name;
            }
            console.log("Selected relationship data:" + JSON.stringify(item));
            return $("<li></li>")
                .data("item.autocomplete", item)
                .append("<a><span><span>" +item.fileSeq+"/"+item.fileType+"/"+item.fileSeries+"/"+ item.fileNbr +
                    "</span><span style='margin-left:20px'>"+objectName+"</span>"+
                    "</span></a>").appendTo(ul);
        };
    })
}
function initEuropeanPatentConversionAutocomplete() {
    if ($("#ep-relationshipExtended-filingNumber").length == 0) {
        return;
    }
    $("#ep-relationshipExtended-filingNumber").on("change", function() {
        //sy6toto kato pri relationship-autocomplete!
        if ($(this).data("autocomplete-changed") == 'true') {
            $(this).data("autocomplete-changed", 'false');
        } else {
            resetEuropeanPatentConversionDataForm($(this), null);
        }
    });
    $("#ep-relationshipExtended-filingNumber").each(function(i, el){
        $(el).autocomplete({
            source: function (request, response) {
                var url = $(this.element).data("url");
                var ajaxRequest = new CommonAjaxRequest(url, {requestData: {filingNumber: request.term}, method: "GET", dataType: "json", useSessionId: false});
                var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

                commonAjaxCall(ajaxRequest, responseAction);
            },
            minLength: 3,
            delay:1000,
            select: function (event, ui) {
                resetEuropeanPatentConversionDataForm($(this), ui.item);
                return false;//very important!
            }, focus: function (e, ui) {
                $(this).val(ui.item.filingNumber);
                return false;
            }
        }).autocomplete("instance")._renderItem = function (ul, item) {
            let objectName = '';
            if(item.title != null){
                objectName = item.title;
            }
            console.log("Selected epo patent data:" + JSON.stringify(item));
            return $("<li></li>")
                .data("item.autocomplete", item)
                .append("<a><span><span>" +item.filingNumber +
                    "</span><span style='margin-left:20px'>"+objectName+"</span>"+
                    "</span></a>").appendTo(ul);
        };
    })

}
function resetEuropeanPatentConversionDataForm(autocompleteElement, item) {
    if (item != null) {
        $("#ep-relationshipExtended-filingNumber").val(item == null ? "" : item.filingNumber);
        $("#ep-relationshipExtended-filingNumber").data("autocomplete-changed", 'true');
        $("#ep-relationshipExtended-filingNumber").blur();
    }
    $("#ep-relationshipExtended-filingDate").val(item == null ? "" : item.filingDate);
    $("#ep-relationship-ext-changed").val($("#ep-relationship-ext-original-filing-number").val() != (item == null ? $("#ep-relationshipExtended-filingNumber").val() : item.filingNumber));
    checkForActiveLabels($("#ep-relationship-ext-div"));

}
function resetRelationshipDataForm(autocompleteElement, item) {
    let prefix = autocompleteElement.attr("id").replace("-autocomplete", "");
    let nbrPrefix = item == null ? "" : item.fileSeq + "/" + item.fileType + "/" + item.fileSeries + "/";
    $("#" + prefix + "-nonempty-lbl").text(nbrPrefix);
    if (item != null) {
        $("#" + prefix + "-nonempty-lbl").show();
        $("#" + prefix + "-empty-lbl").hide();
    } else {
        $("#" + prefix + "-nonempty-lbl").hide();
        $("#" + prefix + "-empty-lbl").show();
    }
    $("#" + prefix + "-file-id-fileSeries").val(item == null ? "" : item.fileSeries);
    $("#" + prefix + "-file-id-fileType").val(item == null ? "" : item.fileType);
    $("#" + prefix + "-file-id-fileSeq").val(item == null ? "" : item.fileSeq);
    $("#" + prefix + "-name").val(item == null ? "" : item.name);
    $("#" + prefix + "-filingDate").val(item == null ? "" : item.filingDateStr);
    $("#" + prefix + "-registrationDate").val(item == null ? "" : item.registrationDateStr);

    $("#" + prefix + "-file-id-fileNbr").val(item == null ? autocompleteElement.val() : item.fileNbr);
    if (item != null) {
        $("#" + prefix + "-autocomplete").val(item == null ? "" : item.fileNbr);
        $("#" + prefix + "-autocomplete").data("autocomplete-changed", 'true');
        $("#" + prefix + "-autocomplete").blur();
    }
    $("#" + prefix + "-relationshipChanged").val($("#" + prefix + "-original-fullNumber").val() != (item == null ? "" : item.fullNumber));

    checkForActiveLabels($("#" + prefix + "-details"));
}
$(document).on("change", "#mark-object-relationshipExtended-applicationType", function (e) {
    markTransformationTypeSetup($(this).val());
});


$(document).on("change", "#patent-object-relationshipExtended-applicationType", function (e) {
    patentRelExtApplTypeSetup($(this).val());
});


function patentRelExtApplTypeSetup(val){
    let isInternationalRelationshipExt = false;
    let isEpRelationshipExt = false;
    let isNationalPatentRelationship = false;

    if(val == "WO"){
        isInternationalRelationshipExt = true;
        // $("#int-relationshipExtended-registrationCountry").select2();//select2 should be reinitialized!!!
    } else if (val == "EP"){
        isEpRelationshipExt = true;
    } else if (val == 'BG') {
        isNationalPatentRelationship = true;
    }
    resetEpRelExtFields(isEpRelationshipExt);
    resetInternationalRelExtFields(isInternationalRelationshipExt);
    resetNationalPatentRelationshipFields(isNationalPatentRelationship);
}
function resetEpRelExtFields(visible) {
    if (!visible) {
        $("#ep-relationship-ext-div").hide();
        $("#ep-relationshipExtended-filingNumber").val("").addClass("form-ignore");
        $("#ep-relationshipExtended-filingDate").val("").addClass("form-ignore");
    } else {
        $("#ep-relationship-ext-div").show();
        $("#ep-relationshipExtended-filingNumber").removeClass("form-ignore");
        $("#ep-relationshipExtended-filingDate").removeClass("form-ignore");
    }
}
function resetInternationalRelExtFields(visible) {
    if (!visible) {
        $("#international-relationship-ext-div").hide();
        $("#int-relationshipExtended-registrationCountry").val("").addClass("form-ignore");
        $("#int-relationshipExtended-registrationDate").val("").addClass("form-ignore");
        $("#int-relationshipExtended-registrationNumber").val("").addClass("form-ignore");
        $("#int-relationshipExtended-filingNumber").val("").addClass("form-ignore");
        $("#int-relationshipExtended-filingDate").val("").addClass("form-ignore");
    } else {
        $("#international-relationship-ext-div").show();
        $("#int-relationshipExtended-registrationCountry").removeClass("form-ignore");
        $("#int-relationshipExtended-registrationDate").removeClass("form-ignore");
        $("#int-relationshipExtended-registrationNumber").removeClass("form-ignore");
        $("#int-relationshipExtended-filingNumber").removeClass("form-ignore");
        $("#int-relationshipExtended-filingDate").removeClass("form-ignore");
    }
}
function resetNationalPatentRelationshipFields(visible) {
    if (!visible) {
        $("#national-patent-relationship-ext-div").hide();
        $("#object-national-patent-relationship-autocomplete").val("");
        $("#object-national-patent-relationship-nonempty-lbl").hide();
        $("#object-national-patent-relationship-empty-lbl").show();
        $("#object-national-patent-relationship-name").val("");
        $("#object-national-patent-relationship-filingDate").val("");
        $("#object-national-patent-relationship-registrationDate").val("");
        $("#object-national-patent-relationship-file-id-fileNbr").val("");
        $("#object-national-patent-relationship-file-id-fileSeq").val("");
        $("#object-national-patent-relationship-file-id-fileSeries").val("");
        $("#object-national-patent-relationship-file-id-fileType").val("");
    } else {
        $("#national-patent-relationship-ext-div").show();
    }

}


function markTransformationTypeSetup(val){
    if(val == "EM"){
        $("#object-relationshipExtended-cancellationDate").val("");
        $("label[for='object-relationshipExtended-cancellationDate']").removeClass("active");
        $("#transformation-cancellationDate-wrap").hide();

        $("#object-relationshipExtended-registrationDate").val("");
        $("label[for='object-relationshipExtended-registrationDate']").removeClass("active");
        $("#transformation-registrationDate-wrap").hide();

        $("#transformation-priorityDate-wrap").show();
        $("#transformation-serveMessageDate-wrap").show();
    } else if (val == "WO"){
        $("#transformation-cancellationDate-wrap").show();
        $("#transformation-registrationDate-wrap").show();

        $("#object-relationshipExtended-priorityDate").val("");
        $("label[for='object-relationshipExtended-priorityDate']").removeClass("active");
        $("#transformation-priorityDate-wrap").hide();
        $("#transformation-serveMessageDate-wrap").hide();
    }
}

$(document).on("click", " [data-action='priority-form']", function (e) {
    let url = $(this).data('url');

    let callParams = {
        index: $(this).data("index")
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#priority-modal-content",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#priority-form-modal", true])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", ".rights-checkbox", function (e) {
    if($(this).is(":checked")){
        if ($(this).is("[data-disables]")) {
            //list of the element ids that should be disabled
            let disableIds = "#" + $(this).data("disables").split(",").join(", #");
            //list of the checked element ids
            let checkedDisabledIds = $(disableIds).filter(":checked");
            if (checkedDisabledIds.length > 0) {
                //the generated label
                let label = $.map(checkedDisabledIds, function(el) {
                    return $(el).next().html();
                }).join(", ");
                let $message = messages['rights.disable.linked.panels'].replace("{PANELS}", label);

                let trigger = $(this);
                let onYes = getExecutableFuncCall(new FuncCall(hideDisabledRightsDetailsAndEnableCurrent, [checkedDisabledIds, trigger]));
                let onNo = getExecutableFuncCall(new FuncCall(toggleCheckbox, [trigger, false]));
                Confirmation.openConfirmYesNoFuncsModal($message, onYes, onNo);

            } else {
                activateRightsCheckbox($(this));
            }
        } else {
            activateRightsCheckbox($(this));
        }

    } else {
        let trigger = $(this);
        let onYes = getExecutableFuncCall(new FuncCall(hideRightsDetails, [trigger]));
        let onNo = getExecutableFuncCall(new FuncCall(toggleCheckbox, [trigger, true]));
        let msg = $("#rights-remove-claim-data").data("message");
        Confirmation.openConfirmYesNoFuncsModal(msg, onYes, onNo);
    }
});
function activateRightsCheckbox(el) {
    $("#"+$(el).data("target")).show();
    let id = $(el).attr("id");
    if(id == 'mark-transformation-hasTransformationData'){
        markTransformationTypeSetup($("#mark-object-relationshipExtended-applicationType").val());
    }
    if(id == 'patent-transformation-hasTransformationData' || id =='patent-parallel-data-hasParallelData'){

        function handleInitOnRightsBodyLoad(detailPanelId) {
            executeCommonInitialization({
                inputsForEditWrapper: '#'+detailPanelId,
                showHiddenElements: true,
                hiddenElementsWrapper: '#'+detailPanelId,
                panelToInitialize: '#'+detailPanelId,
                initializeFormElementsWrapper: '#'+detailPanelId,
            });
            initRelationshipAutocompletes();
            initEuropeanPatentConversionAutocomplete();
        }

         if ($('#patent-parallel-data-hasParallelData').length){
             $.ajax({
                 url: $("#"+$(el).data("target")).attr("data-load-rights-body-url"),
                 async:false,
                 type: 'POST',
                 datatype: 'html',
                 data: {
                     sessionIdentifier: $('#session-object-identifier').val()
                 },
                 success: function (data) {
                     if (id == 'patent-transformation-hasTransformationData'){
                         $('#um-parallel-data-details').html('');
                     }
                     if (id =='patent-parallel-data-hasParallelData'){
                         $('#patent-transformation-details').html('');
                     }
                     $("#"+$(el).data("target")).html(data);
                     handleInitOnRightsBodyLoad($("#"+$(el).data("target")).attr('id'));
                 },
                 error: function (xhr, ajaxOptions, thrownError) {
                     UIBlocker.unblock();
                     openErrorModal(xhr);
                 }
             })
         }
        patentRelExtApplTypeSetup($("#patent-object-relationshipExtended-applicationType").val());
    }
}
function hideRightsDetails(trigger){
    $("#"+trigger.data("target")).hide();
}

function hideDisabledRightsDetailsAndEnableCurrent(idsToDisable, trigger) {
    idsToDisable.each(function (key, value) {
        hideRightsDetails($(value));
        $( value ).prop( "checked", false );
    });
    activateRightsCheckbox(trigger);
}

$(document).on("click", " [data-action='delete-priority']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data('url');

        let callParams = {
            index: $(this).data("index")
        };

        var request = new CommonAjaxRequest(url, {requestData: callParams});
        var responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#priority-table"
        });
        commonAjaxCall(request, responseAction);
    }
});

$(document).on("click", "#save-priority-btn", function (e){
    let url = $(this).data('url');
    let priorityFormData = getFormValuesAsJson($('#priority-form-modal'));
    let callParams = {
        data: JSON.stringify(priorityFormData),
        index: $(this).data("index")
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#priority-table",
        updateContainerOnInvalid: "#priority-modal-content",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(commonOpenModalFormInit, ["#priority-form-modal", true]),
        executeOnValid: new FuncCall(commonCloseModalFormInit, ["#priority-form-modal", true, "#priority-table"])
    });

    commonAjaxCall(request, responseAction);
});
