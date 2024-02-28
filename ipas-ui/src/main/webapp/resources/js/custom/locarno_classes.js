let locarnoClassesHelperObj={};
function reinitLocarnoClassesHelperObj() {
    drawingsHelperObj.editionCodeIdElementFill = '';
    drawingsHelperObj.editionCode='';
}
$(function () {
    if($(patentContainer).length > 0) {
        if ($(".locarno-autocomplete").length>0){
            initLocarnoClassAutocomplete();
        }
    }
});

$(document).on("click", "[data-action='add-locarno-class']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let locarnoClassCode = $('#'+$button.data('locarno-class-autocomplete')).val();
    let locarnoEditionCode = $('#'+$button.data('locarno-edition-code')).val();
    let filingNumber = $button.data('filingnumber');

    if (null != url) {
        let callParams = {
            async:false,
            sessionIdentifier: $('#session-object-identifier').val(),
            filingNumber:filingNumber,
            locarnoClassCode:locarnoClassCode,
            locarnoEditionCode:locarnoEditionCode,
        };
        updateLocarnoClassPanelWithAjax(url,callParams,null,filingNumber,true,$button);
    }
});


$(document).on("keyup", patentContainer + " .locarno-autocomplete", function (e) {
    let editionCodeId = $(this).attr('data-locarno-edition-code');
    $('#'+editionCodeId).val('');
});


function updateLocarnoClassPanelWithAjax(url, callParams, clearErrors,filingNumber, async,$button) {
    $("div").find("[data-locarno-error-div-single-design-nbr='" + filingNumber + "']").html('');
    $(".verification-error").html('');
    function handleDesignLocarnoClassesInit($button) {
        executeCommonInitialization({
            hiddenElementsWrapper: patentContainer + " #panel-body-" + PatentPanel.DesignDrawingsData,
            showHiddenElements: true
        });
        if ($button!==null){
            $('#'+$button.data('locarno-edition-code')).val('');
            $('#'+$button.data('locarno-class-autocomplete')).val('');
        }
    }
    let request = new CommonAjaxRequest(url, {requestData: callParams, async: async,blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "[data-locarno-single-design-nbr='" + filingNumber + "']",
        updateContainerOnInvalid: "[data-locarno-error-div-single-design-nbr='" + filingNumber + "']",
        validationFailedExpression: "data.indexOf('<span class=\"none validation-indication\"></span>') != -1",
        executeOnValid: new FuncCall(handleDesignLocarnoClassesInit,[$button])
    });
    commonAjaxCall(request, responseAction);
}



$(document).on("click", "[data-action='delete-locarno-class']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let locarnoClassCode =$button.data('locarno-class-code');
    let locarnoEditionCode = $button.data('locarno-edition-code');
    let filingNumber = $button.data('filingnumber');
    if (null != url) {
        let callParams = {
            async:false,
            sessionIdentifier: $('#session-object-identifier').val(),
            filingNumber:filingNumber,
            locarnoClassCode:locarnoClassCode,
            locarnoEditionCode:locarnoEditionCode,
        };
        updateLocarnoClassPanelWithAjax(url,callParams,null,filingNumber,true,null);
    }
});

function initLocarnoClassAutocomplete() {

    $(".locarno-autocomplete").autocomplete({
        source: function (request, response) {
            reinitLocarnoClassesHelperObj();
            let url =  $(this.element).attr('data-url');
            locarnoClassesHelperObj.editionCodeIdElementFill = $(this.element).attr('data-locarno-edition-code');
            $.ajax({
                url: url,
                data:  {locarnoClassCode: request.term},
                type: "GET",
                dataFilter: function (data) { return data; },
                success: function (result) {
                    response( $.map( result, function( item ) {
                        locarnoClassesHelperObj.editionCode = item.locarnoEditionCode;
                        return {
                            label: [ item.locarnoClassCode, item.locarnoName].join( ', ' ),
                            value: item.locarnoClassCode
                        };
                    } ) );
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
        },
        minLength: 1,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;
                }
                $('#'+locarnoClassesHelperObj.editionCodeIdElementFill).val(locarnoClassesHelperObj.editionCode);

                return false;
            }, 100);
        }
    });
}