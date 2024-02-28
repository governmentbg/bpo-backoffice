$(document).ready(function () {
    let designDrawingsPanel = $('#panel-' + PatentPanel.DesignDrawingsData);
    if (designDrawingsPanel.length > 0) {
        let url = designDrawingsPanel.data('url');
        if (typeof url !== 'undefined') {
            let callParams = {};
            let request = new CommonAjaxRequest(url, {requestData: callParams});
            let responseAction = new CommonAjaxResponseAction({
                updateContainerOnValid: "#panel-" + PatentPanel.DesignDrawingsData,
                executeOnValid: new FuncCall(handleMainDesignPanelInit,[])
            });
            commonAjaxCall(request, responseAction);
        }
    }
});

function handleMainDesignPanelInit() {
    Panel.init($(" #panel-" + PatentPanel.DesignDrawingsData));
    initLazyImages();
    singleDesignDrawingsUpload();
    if ($(".locarno-autocomplete").length>0){
        initLocarnoClassAutocomplete();
    }
}

let updatedDesignsSet={};

function addDesignFilingNumberToUpdatedDesignsSet(filingNumber){
    updatedDesignsSet[filingNumber] = true;
}
function reinitUpdatedDesignsSet(){
    updatedDesignsSet = {}
}

function fillSingleDesignData(el){
    let singleDesign = {};
    let filingNumber = $('#'+el.id).find('[id*=filingNumber]').val();
    let designSingleTitle = $('#'+el.id).find('[id*=designSingleTitle]').val();
    let designSingleTitleEn= $('#'+el.id).find('[id*=designSingleTitleEn]').val();
    let designSingleStatusCode = $('#'+el.id).find('[id*=designSingleStatusCode]').val();
    let designSingleApplSubType = $('#'+el.id).find('[id*=designSingleApplSubType]').val();
    singleDesign.filingNumber = checkEmptyField(filingNumber);
    singleDesign.designSingleTitle = checkEmptyField(designSingleTitle);
    singleDesign.designSingleTitleEn = checkEmptyField(designSingleTitleEn);
    singleDesign.designSingleStatusCode = checkEmptyField(designSingleStatusCode);
    singleDesign.designSingleApplSubType = checkEmptyField(designSingleApplSubType);

    let singleDesignDrawings=[];
    $('#'+el.id).find('[id*=single-design-drawing-area]').each(function () {
        let singleDesignDrawing = {};
        let drawingNbr = $(this).find('[id*=drawingNbr]').val();
        let viewType = $(this).find('[id*=viewType]').val();
        let imagePublished = $(this).find('[id*=imagePublished]').is(':checked');
        let imageRefused = $(this).find('[id*=imageRefused]').is(':checked');
        singleDesignDrawing.drawingNbr = checkEmptyField(drawingNbr);
        singleDesignDrawing.viewType = checkEmptyField(viewType);
        singleDesignDrawing.imagePublished = imagePublished;
        singleDesignDrawing.imageRefused = imageRefused;
        singleDesignDrawings.push(singleDesignDrawing);
    });
    singleDesign.singleDesignDrawings = singleDesignDrawings;
    return singleDesign;
}

function getSingleDesignsDataAsJSON(htmlPart) {
    let resultJson = {};
    let singleDesigns = [];
    $(htmlPart.find('[id*=single-design-area]')).each(function (index, el) {
       let filingNumber = $('#'+el.id).find('[id*=filingNumber]').val();
       if (updatedDesignsSet[filingNumber]){
           singleDesigns.push(fillSingleDesignData(el));
       }
    });
    resultJson['singleDesigns'] = singleDesigns;
    resultJson['designMainTitle'] =checkEmptyField($('#designMainTitle').val());
    resultJson['designMainTitleEn'] =checkEmptyField($('#designMainTitleEn').val());
    reinitUpdatedDesignsSet();
    return resultJson;
}

function getSingleDesignDataAsJSON(htmlPart,filingNumber){
    let singleDesign = {};
    $(htmlPart.find("[data-single-design-area-filling-number='" + filingNumber + "']")).each(function (index, el) {
        singleDesign = fillSingleDesignData(el)
    });
    return singleDesign;
}

$(document).on("click", "[data-action='delete-single-design']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let filingNumber = $(this).data('filingnumber');
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    }else{
        $.ajax({
            url: url,
            type: 'POST',
            datatype: 'html',
            data: {
                sessionIdentifier: $('#session-object-identifier').val(),
                filingNumber : filingNumber
            },
            success: function (data) {
                $("[data-single-design-area-filling-number='" + filingNumber + "']").replaceWith(data);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                UIBlocker.unblock();
                openErrorModal(xhr);
            }
        })

    }
});

$(document).on("click", "[data-action='create-single-design']", function (e) {
    let $button = $(this);
    let url = $button.data('url');

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        $.ajax({
            url: url,
            type: 'POST',
            datatype: 'html',
            data: {
                sessionIdentifier: $('#session-object-identifier').val()
            },
            success: function (data) {
                $('#single-designs').append(data);
                handleSingleDesignsInit();
                $(data).find('[id*=single-design-drawing-upload]').each(function (index, el) {
                    if ($(".locarno-autocomplete").length>0){
                        initLocarnoClassAutocomplete();
                    }
                    let fileUploadButtonToInitId = $(el).attr('id');
                    singleDesignDrawingsUpload(fileUploadButtonToInitId);
                    addDesignFilingNumberToUpdatedDesignsSet(fileUploadButtonToInitId.replace('single-design-drawing-upload-',''));
                });
            },
            error: function (xhr, ajaxOptions, thrownError) {
                UIBlocker.unblock();
                openErrorModal(xhr);
            }
        })
    }
});



$(document).on("click", "[data-action='verify-single-product']", function (e) {
    let target = "#poduct-term-"+$(this).data("index");
    let url = $(this).data("url");

    showProductVerifyLoader($(this).data("index"));
    let hideLoaderFunc = new FuncCall(hideProductVerifyLoader, [$(this).data("index")]);

    let request = new CommonAjaxRequest(url, {});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: target,
        executeOnInvalid: hideLoaderFunc,
        executeOnError: hideLoaderFunc
    });
    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='verify-all-products']", function (e) {
    $("[data-action='verify-single-product']").each(function (index, el){
        $(el).trigger("click");
    });
});

function showProductVerifyLoader(index) {
    let targetLoader = "#product-verify-loader-"+index;
    $(targetLoader).show();
}

function hideProductVerifyLoader(index){
    let targetLoader = "#product-verify-loader-"+index;
    $(targetLoader).hide();
}