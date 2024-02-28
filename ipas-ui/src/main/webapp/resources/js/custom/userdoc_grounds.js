let UserDocGroundPanel = {
    Objection: "objection",
    PatentAnnulmentRequest: "patent-annulment-request",
    SpcAnnulmentRequest: "spc-annulment-request",
    UtilityModelAnnulmentRequest: "utility-model-annulment-request",
    DesignAnnulmentRequest: "design-annulment-request",
    Revocation: "revocation",
    Opposition:"opposition",
    Invalidity:"invalidity",
};

let LegalGroundTypes = {
    EarlierMark: 109,
    EarlierCOMark: 111,
    NotNewDesign : 89,
    NotOriginalDesign13 : 91,
    NotOriginalDesign13A : 93,
    PublicBeforeFilingDate : 101,
    DeclaredBeforeRegistration : 103,

};


function updateLegalGroundsWithAjax(url, callParams,groundPanel) {
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    request.async = false;
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#legal-ground-types-modal-div",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: userdocContainer + " #panel-body-" + groundPanel,
        executeOnValid: new FuncCall(handleLegalGroundsInit, [groundPanel])
    });

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#legal-ground-types-modal", false);
    }
    commonAjaxCall(request, responseAction);
    initGroundNationalMarkAutocomplete();
    initGroundDesignAutocomplete();
    groundImageUploadInit();
}

function handleLegalGroundsInit(groundPanel) {
    executeCommonInitialization({
        inputsForEditWrapper: userdocContainer + " #panel-body-" + groundPanel,
        showHiddenElements: true,
        hiddenElementsWrapper: userdocContainer + " #panel-body-" + groundPanel,
        initializeFormElementsWrapper: userdocContainer + " #panel-body-" + groundPanel,
        modalToInitialize: "#legal-ground-types-modal",
        modalStateExpression: "close",
    });
}

function readUserdocGroundImportedSingleDesigns() {
    let singleDesigns=[];
    if($(".ground-single-design-id-column").length != 0) {
        $('.ground-single-design-id-column').each(function () {
            let singleDesign = {};
            singleDesign.id =  $(this).attr('data-design-id');
            singleDesign.title =   $(this).attr('data-design-title')
            singleDesigns.push(singleDesign);
        });
    }

    return singleDesigns;
}

$(document).on("click", "[data-action='userdoc-ground-add-single-designs']", function (e) {
    if ($('#hidden-ground-design').val()!==''){
        UIBlocker.block();
        let nationalDesignFilingNumber =  $('#hidden-ground-design').val();
        let url = $(this).data('url');
        let resultJson = {};
        resultJson['singleDesigns'] = readUserdocGroundImportedSingleDesigns();
        let request = new CommonAjaxRequest(url, {
            requestData: {
                nationalDesignFilingNumber: nationalDesignFilingNumber,
                data: JSON.stringify(resultJson)
            }
        });
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: '#userdoc-ground-single-designs-div',
            executeOnValid: new FuncCall(UIBlocker.unblock, []),
            executeOnError: new FuncCall(UIBlocker.unblock, [])
        });
        commonAjaxCall(request, responseAction);
    }
});

$(document).on("click", "[data-action='delete-ground-single-design']", function (e) {
    $(this).closest("tr").remove();
    if($(".ground-single-design-row").length == 0) {
        $(".ground-single-design-table").remove();
    }
});

$(document).on("click", "[data-action='open-panel-legal-grounds-modal']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        rootGroundId: $(this).data('groundid'),
        panel: $(this).data('panel')
    };


    let request = new CommonAjaxRequest(url, {requestData: callParams});
    request.async = false;
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#legal-ground-types-modal-div",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#legal-ground-types-modal", false])
    });
    commonAjaxCall(request, responseAction);
    initGroundDesignAutocomplete();
    initGroundNationalMarkAutocomplete();
    groundImageUploadInit();
    UIBlocker.unblock();
});


$(document).on("click", "[data-action='delete-root-ground']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        let panel = $button.data('panel');
        let callParams = {
            rootGroundId: $button.data('groundid'),
        };
        updateLegalGroundsWithAjax(url,callParams,getGroundPanel(panel));
    }
});


$(document).on("click", "[data-action='edit-userdoc-root-grounds']", function (e) {
    UIBlocker.block();
    let $button = $(this);
    let url = $button.data('url');
    let panel = null;

    let resultJson = {};
    let markGroundDataJson = {};
    let patentGroundDataJson = {};
    let selectedGroundTypeIds = [];

    $(('[id*=legal-ground-type-checkbox]')).each(function (index, el) {
        if($(this).is(":checked")) {
            selectedGroundTypeIds.push($(this).attr('data-legal-ground-id'));
        }
    });
    let partialInvalidity = $("input[type='radio'][name='patentGroundData-partialInvalidity']:checked").val();
    patentGroundDataJson['partialInvalidity'] = partialInvalidity;
    let earlierRightType = $("#select-earlier-right-type option:selected").val();
    let niceClassesInd = ifAllNiceClassesOptionSelected();
    resultJson['selectedGroundTypeIds'] = selectedGroundTypeIds;
    resultJson['motives']=$('#root-ground-motives').val();
    resultJson['earlierRightType']=earlierRightType;
    markGroundDataJson['legalGroundCategory'] = $('input[name=legalGroundCategory]:checked').val();
    resultJson['applicantAuthority']=$("#select-applicant-authority option:selected").val();
    resultJson['rootGroundId']=$(this).attr('data-root-ground-id');
    resultJson['singleDesigns'] = readUserdocGroundImportedSingleDesigns();
    markGroundDataJson['niceClassesInd']=niceClassesInd;

    if (niceClassesInd===false){
        markGroundDataJson['niceClasses']=getNiceClassesArray();
    }
    let markGroundType = $("#select-mark-ground-type option:selected").val();
    markGroundDataJson['markGroundType']=markGroundType;


    if (markGroundType===MarkGroupTypes.NATIONAL_MARK || markGroundType===MarkGroupTypes.INTERNATIONAL_MARK || markGroundType===MarkGroupTypes.PUBLIC_MARK){
        if ($('#ground-mark-imported').is(':checked')) {
            markGroundDataJson['filingNumber']=$('#hidden-ground-national-mark').val();
        }else{
            markGroundDataJson['filingNumber']=$('#root-ground-filing-nbr-on-mark-earlier-type').val();
        }
        markGroundDataJson['markImportedInd'] = $('#ground-mark-imported').is(':checked');
        markGroundDataJson['nameText']=$('#root-ground-name-text-on-mark-earlier-type').val();
        markGroundDataJson['filingDate']=$('#root-ground-filing-date-on-mark-earlier-type').val();
        markGroundDataJson['registrationNbr']=$('#root-ground-registration-nbr-on-mark-earlier-type').val();
        markGroundDataJson['registrationDate']=$('#root-ground-registration-date-on-mark-earlier-type').val();
        resultJson['groundCommonText']=$('#root-ground-common-text-on-mark-earlier-type').val();
        markGroundDataJson['markSignTyp']=$("#userdoc-ground-markSignType option:selected").val();
    }else {
        resultJson['groundCommonText']=$('#root-ground-common-text').val();
        markGroundDataJson['registrationDate']=$('#root-ground-registration-date').val();
        markGroundDataJson['registrationNbr']=$('#root-ground-registration-nbr').val();
    }
    markGroundDataJson['countryCode']=$("#root-ground-countryCode option:selected").val();
    markGroundDataJson['geographicalIndTyp']=$("#root-ground-geographicalIndTyp option:selected").val();

    panel = $(this).attr('data-panel');
    resultJson['panel']=panel;
    if (markGroundDataJson.length !=0){
        resultJson['markGroundData']=markGroundDataJson;
    }
    if (patentGroundDataJson.length !=0){
        resultJson['patentGroundData']=patentGroundDataJson;
    }
    let callParams = {
        data: JSON.stringify(resultJson)
    };
    updateLegalGroundsWithAjax(url,callParams,getGroundPanel(panel));
    UIBlocker.unblock();
});


$(document).on("change",'[id*=legal-ground-type-checkbox]', function (e) {
    let legalGroundType = $(this).attr('data-legal-ground-id');
    let callAjaxOnLegalGroundCheck = legalGroundType == LegalGroundTypes.EarlierMark || legalGroundType == LegalGroundTypes.NotNewDesign
        || legalGroundType == LegalGroundTypes.NotOriginalDesign13 || legalGroundType == LegalGroundTypes.NotOriginalDesign13A
        || legalGroundType == LegalGroundTypes.PublicBeforeFilingDate || legalGroundType == LegalGroundTypes.DeclaredBeforeRegistration
    if(callAjaxOnLegalGroundCheck){
        UIBlocker.block();
        let url = $('#check-legal-ground-url').attr('data-url');
        let resultJson = {};
        let selectedGroundTypeIds = [];
        $(('[id*=legal-ground-type-checkbox]')).each(function (index, el) {
            if($(this).is(":checked")) {
                selectedGroundTypeIds.push($(this).attr('data-legal-ground-id'));
            }
        });
        resultJson['selectedGroundTypeIds'] = selectedGroundTypeIds;
        resultJson['motives']=$('#root-ground-motives').val();
        resultJson['rootGroundId'] = $('#hidden-field-rootGroundId').val();
        let panel = $('#hidden-field-panel').val();
        resultJson['panel']= panel;
        resultJson['earlierRightType'] =  $("#select-earlier-right-type option:selected").val();
        resultJson['singleDesigns'] = readUserdocGroundImportedSingleDesigns();

        updateLegalGroundTypesModalAjax(url,resultJson);
    }
});


$(document).on("change",'#select-earlier-right-type', function (e) {
    UIBlocker.block();
    let resultJson = {};
    let markGroundDataJson = {};
    let url = $('#hidden-field-on-change-type-url').attr('data-url');
    let panel = $('#hidden-field-panel').val()
    resultJson['rootGroundId'] = $('#hidden-field-rootGroundId').val();
    resultJson['panel']= panel;
    resultJson['earlierRightType'] =  $("#select-earlier-right-type option:selected").val();
    markGroundDataJson['legalGroundCategory'] = $('input[name=legalGroundCategory]:checked').val();
    resultJson['markGroundData'] = markGroundDataJson;
    updateLegalGroundTypesModalAjax(url,resultJson);
});


function getGroundPanel(panel){
    let groundPanel = null;
    if(panel === UserDocGroundPanel.Objection){
        groundPanel = UserdocPanel.Objection
    }
    if(panel === UserDocGroundPanel.PatentAnnulmentRequest){
        groundPanel = UserdocPanel.PatentAnnulmentRequest
    }
    if(panel === UserDocGroundPanel.SpcAnnulmentRequest){
        groundPanel = UserdocPanel.SpcAnnulmentRequest
    }

    if(panel === UserDocGroundPanel.UtilityModelAnnulmentRequest){
        groundPanel = UserdocPanel.UtilityModelAnnulmentRequest
    }
    if(panel === UserDocGroundPanel.DesignAnnulmentRequest){
        groundPanel = UserdocPanel.DesignAnnulmentRequest
    }
    if(panel === UserDocGroundPanel.Revocation){
        groundPanel = UserdocPanel.Revocation
    }
    if (panel === UserDocGroundPanel.Opposition){
        groundPanel = UserdocPanel.Opposition
    }
    if (panel === UserDocGroundPanel.Invalidity){
        groundPanel = UserdocPanel.Invalidity
    }
    return groundPanel;
}


$(document).on("click", "[data-action='ground-image-href']", function (e) {
    e.preventDefault();
    $("#ground-image-upload").trigger('click');
});

function groundImageUploadInit() {
    $("#ground-image-upload").fileupload({
        url: $(this).data('url'),
        complete: function (response, e) {
            $("#ground-name-data-div").empty();
            $("#ground-name-data-div").append(response.responseText);
            groundImageUploadInit();
        },
        fail: function (data, e) {
            alert('errors occurred');
        }
    }).bind('fileuploadsubmit', function (e, data) {
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val(),
            panel : $("#ground-image-href").attr('data-panel')
        };
    });
}



$(document).on("change","input[name='legalGroundCategory']", function (e) {
    UIBlocker.block();
    let resultJson = {};
    let markGroundDataJson = {};
    let url = $('#url-on-category-change').attr('data-url');
    let panel = $('#panel-on-category-change').val()
    resultJson['rootGroundId'] = $('#rootGroundId-on-category-change').val();
    resultJson['panel']= panel;
    resultJson['earlierRightType'] =  $("#select-earlier-right-type option:selected").val();
    markGroundDataJson['legalGroundCategory'] = $(this).val();
    resultJson['markGroundData'] = markGroundDataJson;
    updateLegalGroundTypesModalAjax(url,resultJson);
});


function updateLegalGroundTypesModalAjax(url,data){
    $.ajax({
        url: url,
        type: 'POST',
        datatype: 'html',
        data: {
            sessionIdentifier: $('#session-object-identifier').val(),
            data : JSON.stringify(data)
        },
        success: function (data) {
            $("#legal-ground-types-modal").html(data);
            executeCommonInitialization({
                initializeFormElementsWrapper: "#legal-ground-types-modal",
                inputsForEditWrapper:"#legal-ground-types-modal"
            }, null);
            groundImageUploadInit();
            initGroundNationalMarkAutocomplete();
            initGroundDesignAutocomplete();
            initBindFirstValidation("#legal-ground-types-modal");
            UIBlocker.unblock()
        },
        error: function (xhr, ajaxOptions, thrownError) {
            UIBlocker.unblock();
            openErrorModal(xhr);
        }
    })
}




