let drawingsHelperObj={};

function reinitDrawingsHelperObj() {
    drawingsHelperObj.uploadedImgErrorsDivContent = [];
    drawingsHelperObj.percentComplete = 0;
    drawingsHelperObj.hasSuccessResponse =false;
}

$(function () {
    reinitDrawingsHelperObj();
    if($(patentContainer).length > 0) {
        drawingsUpload();
        singleDesignDrawingsUpload();
    }
});

$(document).on("click", "[data-action='delete-patent-drawing']", function (e) {
    let callParams = {
        drawingn: $(this).data('drawingn')
    };
    let $button = $(this);
    deleteDrawing($button ,callParams);
});

$(document).on("click", "[data-action='global-swap-drawing-position']", function (e) {
    let drawingNbrFirstParam = $("#change-drawing-positions-div input:nth-child(1)").val();
    let drawingNbrSecondParam = $("#change-drawing-positions-div input:nth-child(2)").val();
    let callParams = {
        drawingNbrFirstParam: drawingNbrFirstParam,
        drawingNbrSecondParam: drawingNbrSecondParam
    };
    let $button = $(this);
    drawingSwapPositions($button,callParams);
});

$(document).on("click", "[data-action='swap-drawing-position']", function (e) {
    let callParams = {
        isHigherPosition: $(this).data('higher-position'),
        drawingNbr: $(this).data('drawingnbr')
    };
    let $button = $(this);
    drawingSwapPositions($button,callParams);
});

function drawingsUpload() {
    $(patentContainer + " #drawing-upload").fileupload({
        sequentialUploads: true,
        url: $(this).data('url'),
        complete: function (response, e) {
            fillErrorsOnDrawingsUpload(response);
            if(drawingsHelperObj.percentComplete === 1){
                $(patentContainer + " #uploaded-drawing-errors-div").empty();
                if (drawingsHelperObj.hasSuccessResponse){
                    let callParams = {
                        async:false,
                        sessionIdentifier: $('#session-object-identifier').val()
                    };
                    let urlUpload  =  $(this).attr('urlUpload');
                    updateDrawingListWithAjax(urlUpload, callParams, true,false);
                }
                appendErrorsOnDrawingsUpload($(patentContainer + " #uploaded-drawing-errors-div"))
            }
        },
        fail: function (data, e) {
            alert('errors occurred');
        }
    }).bind('fileuploadsubmit', function (e, data) {
        UIBlocker.block()
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val()
        };
    }).bind('fileuploadprogressall',function (e,data) {
        drawingsHelperObj.percentComplete = data.loaded / data.total;
    });
}

function updateDrawingListWithAjax(url, callParams, clearErrors,async) {

    function handleDrawingInit(clearErrors) {
        executeCommonInitialization({
            initLazyImages:true,
            inputsForEditWrapper: patentContainer + " #panel-body-" + PatentPanel.PublishedDrawingsData,
            showHiddenElements: true,
            hiddenElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.PublishedDrawingsData,
        });
        drawingsUpload();
        if (clearErrors) {
            let editDrawingListErrorDiv = $(patentContainer + " #uploaded-drawing-errors-div");
            editDrawingListErrorDiv.empty();
        }
    }

    let request = new CommonAjaxRequest(url, {requestData: callParams,async: async,blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: patentContainer + " #panel-body-" + PatentPanel.PublishedDrawingsData,
        executeOnValid: new FuncCall(handleDrawingInit, [clearErrors])
    });
    commonAjaxCall(request, responseAction);
}


function handleSingleDesignsInit(initLazyImages) {
    executeCommonInitialization({
        initLazyImages:initLazyImages,
        inputsForEditWrapper: patentContainer + " #panel-body-" + PatentPanel.DesignDrawingsData,
        showHiddenElements: true,
        hiddenElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.DesignDrawingsData,
        panelToInitialize: patentContainer + " #panel-body-" + PatentPanel.DesignDrawingsData,
        initializeFormElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.DesignDrawingsData,
    });
}

function updateSingleDesignDrawingListWithAjax(url, callParams, clearErrors,filingNumber, async) {
    let request = new CommonAjaxRequest(url, {requestData: callParams, async: async,blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "[data-single-design-nbr='" + filingNumber + "']",
        executeOnValid: new FuncCall(handleSingleDesignsInit,[true])
    });
    commonAjaxCall(request, responseAction);
}

$(document).on("click", "[data-action='single-design-delete-drawing']", function (e) {
    let drawingn= $(this).data('drawingnbr');
    let filingNumber = $(this).data('filingnumber');
    let data = getSingleDesignDataAsJSON($(patentContainer + ' #panel-' + PatentPanel.DesignDrawingsData),filingNumber);
    let callParams = {
        drawingn: drawingn,
        filingNumber: filingNumber,
        data: JSON.stringify(data)
    };
    let $button = $(this);
    deleteDrawing($button,callParams,filingNumber);
});

$(document).on("click", "[data-action='single-design-swap-drawing-position']", function (e) {
    let filingNumber = $(this).data('filingnumber');
    let drawingNbr= $(this).data('drawingnbr');
    let data = getSingleDesignDataAsJSON($(patentContainer + ' #panel-' + PatentPanel.DesignDrawingsData),filingNumber);
    let callParams = {
        isHigherPosition: $(this).data('higher-position'),
        drawingNbr: drawingNbr,
        filingNumber: filingNumber,
        data: JSON.stringify(data)
    };
    let $button = $(this);
    drawingSwapPositions($button,callParams,filingNumber);
});

$(document).on("click", "[data-action='single-design-global-swap-drawing-position']", function (e) {
    let filingNumber = $(this).data('filingnumber');
    let drawingNbrFirstParam = $("[number-change-position-first='" + filingNumber + "']").val();
    let drawingNbrSecondParam = $("[number-change-position-second='" + filingNumber + "']").val();
    let data = getSingleDesignDataAsJSON($(patentContainer + ' #panel-' + PatentPanel.DesignDrawingsData),filingNumber);
    let callParams = {
        drawingNbrFirstParam: drawingNbrFirstParam,
        drawingNbrSecondParam: drawingNbrSecondParam,
        filingNumber: filingNumber,
        data: JSON.stringify(data)
    };

    let $button = $(this);
    drawingSwapPositions($button,callParams,filingNumber);
});

$(document).on("click", "[data-action='single-design-drawing-upload-href']", function (e) {
    e.preventDefault();
    let $button = $(this);
    let uploadButtonId = $button.data('triggered-action');
    $(" #"+uploadButtonId).trigger('click');
});

$(document).on("change", '[id*=designSingleStatusCode],[id*=designSingleApplSubType],[id*=designSingleTitle],[id*=designSingleTitleEn],[id*=viewType],[id*=imagePublished],[id*=imageRefused]', function (e) {
    let filingNumber = $(this).closest('[id*=single-design-area]').attr('data-single-design-area-filling-number');
    addDesignFilingNumberToUpdatedDesignsSet(filingNumber)
});

function singleDesignDrawingsUpload(fileUploadButtonToInitId) {
    let fileUploadButtonId
    if (typeof fileUploadButtonToInitId === "undefined"){
        fileUploadButtonId=$('[id*=single-design-drawing-upload]');
    }else{
        fileUploadButtonId  = $('#'+fileUploadButtonToInitId);
    }
    fileUploadButtonId.fileupload({
        sequentialUploads: true,
        url: $(this).data('url'),
        complete: function (response, e) {
            let filingNumber = $(this).attr('filingNumber');
            fillErrorsOnDrawingsUpload(response);
            if(drawingsHelperObj.percentComplete === 1){

                $("[data-single-design-drawing-errors-div='" + filingNumber + "']").empty();
                if (drawingsHelperObj.hasSuccessResponse){
                    let callParams = {
                        async:false,
                        sessionIdentifier: $('#session-object-identifier').val(),
                        filingNumber: filingNumber,
                        data : JSON.stringify(getSingleDesignDataAsJSON($(patentContainer + ' #panel-' + PatentPanel.DesignDrawingsData),filingNumber))
                    };
                    let urlUpload  =  $(this).attr('urlUpload');
                    updateSingleDesignDrawingListWithAjax(urlUpload, callParams, true,filingNumber,false);
                }
                appendErrorsOnDrawingsUpload($("[data-single-design-drawing-errors-div='" + filingNumber + "']"));
            }
            addDesignFilingNumberToUpdatedDesignsSet(filingNumber);
        },
        fail: function (data, e) {
            alert('errors occurred');
        }
    }).bind('fileuploadsubmit', function (e, data) {
        UIBlocker.block()
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val(),
            filingNumber : $(this).attr('data-filing-number')
        };
    }).bind('fileuploadprogressall',function (e,data) {
        drawingsHelperObj.percentComplete = data.loaded / data.total;
    });
}

function fillErrorsOnDrawingsUpload(response){
    let validationTag = '<span class="none validation-indication"></span>';
    if (response.responseText.indexOf(validationTag) !== -1) {
        $(this).val('');
        drawingsHelperObj.uploadedImgErrorsDivContent.push(response.responseText)
    }else{
        drawingsHelperObj.hasSuccessResponse=true
    }
}

function appendErrorsOnDrawingsUpload(errorDiv){
    for (let i = 0; i < drawingsHelperObj.uploadedImgErrorsDivContent.length; i++) {
        errorDiv.append(drawingsHelperObj.uploadedImgErrorsDivContent[i]);
    }
    UIBlocker.unblock();
    reinitDrawingsHelperObj();
}

function drawingSwapPositions($button,callParams,filingNumber){
    let url = $button.data('url');
    if (null != url) {
        if (typeof filingNumber === "undefined") {
            updateDrawingListWithAjax(url, callParams, true);
            addDesignFilingNumberToUpdatedDesignsSet(filingNumber)
        }
        else{
            updateSingleDesignDrawingListWithAjax(url, callParams, true,filingNumber);
        }
    }
}

function deleteDrawing($button,callParams,filingNumber){
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');
        if (typeof filingNumber === "undefined") {
            updateDrawingListWithAjax(url, callParams, true);
            addDesignFilingNumberToUpdatedDesignsSet(filingNumber)
        }
        else{
            updateSingleDesignDrawingListWithAjax(url, callParams, true,filingNumber);
        }
    }
}






