let MarkGroupTypes = {
    NATIONAL_MARK: "1",
    INTERNATIONAL_MARK: "2",
    PUBLIC_MARK: "3"
};

$(document).on("keyup", '#userdoc-ground-national-mark-autocomplete', function (e) {
    let choosenNationalMark =  $('#hidden-ground-national-mark');
    if (choosenNationalMark.val() !='' ){
        $('#userdoc-ground-national-mark-autocomplete').val('');
        $('#root-ground-name-text-on-mark-earlier-type').val('');
        $('#root-ground-filing-date-on-mark-earlier-type').val('');
        $('#root-ground-registration-nbr-on-mark-earlier-type').val('');
        $('#root-ground-registration-date-on-mark-earlier-type').val('');
        $('#userdoc-ground-markSignType').val(null);
        choosenNationalMark.val('');
        executeCommonInitialization({
            initializeFormElementsWrapper: "#mark-earlier-right-type-subsection-on-type-change-div",
            inputsForEditWrapper:"#mark-earlier-right-type-subsection-on-type-change-div",
        }, null);
    }
});

function initGroundNationalMarkAutocomplete(){
    if ($("#userdoc-ground-national-mark-autocomplete").length == 0) {
        return;
    }

    $("#userdoc-ground-national-mark-autocomplete").autocomplete({
        source: function (request, response) {
            let url = $("#userdoc-ground-national-mark-autocomplete").data("url");

            let ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {fileNbr: request.term},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            let responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
            commonAjaxCall(ajaxRequest, responseAction);
        },
        minLength: 3,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;
                }
                $('#userdoc-ground-national-mark-autocomplete').val(ui.item.fileNbr);
                $('#hidden-ground-national-mark').val(ui.item.fileSeq  +'/' + ui.item.fileType +'/' + ui.item.fileSeries +'/' + ui.item.fileNbr);
                $('#root-ground-name-text-on-mark-earlier-type').val(ui.item.title);
                $('#root-ground-filing-date-on-mark-earlier-type').val(ui.item.filingDate);
                $('#root-ground-registration-nbr-on-mark-earlier-type').val(ui.item.registrationNbr);
                $('#root-ground-registration-date-on-mark-earlier-type').val(ui.item.registrationDate);
                $('#userdoc-ground-markSignType').val(ui.item.signWcode);

                executeCommonInitialization({
                    initializeFormElementsWrapper: "#mark-earlier-right-type-subsection-on-type-change-div",
                    inputsForEditWrapper:"#mark-earlier-right-type-subsection-on-type-change-div",
                }, null);
                initBindFirstValidation("#mark-earlier-right-type-subsection-on-type-change-div");
                return false;
            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        let itemText;
        if(item.title !=null){
            itemText="<a><span><span>" + item.fileSeq  +'/' + item.fileType +'/' + item.fileSeries +'/' + item.fileNbr+ ' - ' + item.title +
                "</span></span></a>";
        }else{
            itemText="<a><span><span>" + item.fileSeq  +'/' + item.fileType +'/' + item.fileSeries +'/' + item.fileNbr+
                "</span></span></a>";
        }

        return $("<li></li>")
            .data("item.autocomplete", item)
            .append(itemText).appendTo(ul);
    };

}

function updateGroundMarksSubsectionAjax(url,resultJson){
    $.ajax({
        url: url,
        type: 'POST',
        datatype: 'html',
        data: {
            sessionIdentifier: $('#session-object-identifier').val(),
            data : JSON.stringify(resultJson)
        },
        success: function (data) {
            $("#mark-earlier-right-type-subsection-on-type-change-div").html(data);
            executeCommonInitialization({
                initializeFormElementsWrapper: "#mark-earlier-right-type-subsection-on-type-change-div",
                inputsForEditWrapper:"#mark-earlier-right-type-subsection-on-type-change-div",
            }, null);
            groundImageUploadInit();
            initGroundNationalMarkAutocomplete();
            initBindFirstValidation("#mark-earlier-right-type-subsection-on-type-change-div");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            UIBlocker.unblock();
            openErrorModal(xhr);
        }
    })

}

function resetMarkGroundImportedCheckbox(){
    if ($("#select-mark-ground-type option:selected").val() === MarkGroupTypes.NATIONAL_MARK){
        $( "#ground-mark-imported" ).prop( "disabled", false);
        $( "#ground-mark-imported" ).prop( "checked", true);
    }else{
        $( "#ground-mark-imported" ).prop( "disabled", true);
        $( "#ground-mark-imported" ).prop( "checked", false);
    }
}


$(document).on("change",'#ground-mark-imported', function (e) {
    let resultJson = {};
    let markGroundDataJson = {};
    let url = $(this).attr('data-url');
    let panel = $('#hidden-field-panel').val();
    resultJson['panel']= panel;
    markGroundDataJson['markImportedInd']=$(this).is(':checked');
    resultJson['markGroundData']=markGroundDataJson;
    updateGroundMarksSubsectionAjax(url,resultJson);
});

$(document).on("change",'#select-mark-ground-type', function (e) {
    resetMarkGroundImportedCheckbox();
    if ($("#select-mark-ground-type option:selected").val() === ""){
        $("#mark-earlier-right-type-subsection-on-type-change-div").html("");
    }else{
        let resultJson = {};
        let markGroundDataJson = {};
        let url = $('#hidden-field-on-change-mark-gorund-type-url').attr('data-url');
        let panel = $('#hidden-field-panel').val();
        resultJson['panel']= panel;
        markGroundDataJson['markGroundType'] =  $("#select-mark-ground-type option:selected").val();
        resultJson['markGroundData'] =  markGroundDataJson;
        updateGroundMarksSubsectionAjax(url,resultJson);
    }
});





