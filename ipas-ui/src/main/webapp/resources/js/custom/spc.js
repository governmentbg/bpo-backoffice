$(function () {
    if($(patentContainer).length > 0) {
        if ($("#spc-main-patent-autocomplete").length > 0) {
            initSpcMainPatentAutocomplete();
        }
    }
});

function initSpcMainPatentAutocomplete(){
    if ($("#spc-main-patent-autocomplete").length == 0) {
        return;
    }

    $("#spc-main-patent-autocomplete").autocomplete({
        source: function (request, response) {
            var url = $("#spc-main-patent-autocomplete").data("url");

            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {fileNbr: request.term, fileType: $( "#spc-main-patent-type option:selected" ).val()},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
            commonAjaxCall(ajaxRequest, responseAction);
        },
        minLength: 3,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;
                }

                $('#spc-main-patent-autocomplete').val(ui.item.fileSeq  +'/' + ui.item.fileType +'/' + ui.item.fileSeries +'/' + ui.item.fileNbr+' - '+ui.item.statusTitle);
                $('#spc-mainPatent').val(ui.item.fileSeq  +'/' + ui.item.fileType +'/' + ui.item.fileSeries +'/' + ui.item.fileNbr);

                return false;
            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span><span>" + item.fileSeq  +'/' + item.fileType +'/' + item.fileSeries +'/' + item.fileNbr+ ' - ' + item.title +
                "</span></span></a>").appendTo(ul);
    };

}

$(document).on("keyup", '#'+PanelContainer.Patent + " #spc-main-patent-autocomplete", function (e) {
    let mainPatent =  $('#spc-mainPatent');
    if (mainPatent.val() !='' ){
        $('#spc-main-patent-autocomplete').val('');
        mainPatent.val('');
        $('#spc-isPageForReload').val(true);
    }
});


$(document).on("change",'#'+PanelContainer.Patent + " #spc-main-patent-type", function (e) {
    $('#spc-main-patent-autocomplete').val('');
    $('#spc-mainPatent').val('');
    $('#spc-isPageForReload').val(true);
});


function initMainPatentHref(isEdit) {
    if (isEdit){
        $('#spc-main-patent-href').addClass('none');
    }else{
        $('#spc-main-patent-href').removeClass('none')
    }
}
