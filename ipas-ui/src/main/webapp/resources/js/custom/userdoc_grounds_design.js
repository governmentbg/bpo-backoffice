

$(document).on("keyup", '#userdoc-ground-design-autocomplete', function (e) {
    let choosenNationalDesign =  $('#hidden-ground-design');
    if (choosenNationalDesign.val() !='' ){
        $('#userdoc-ground-design-autocomplete').val('');
        choosenNationalDesign.val('');
    }
});


function initGroundDesignAutocomplete(){
    if ($("#userdoc-ground-design-autocomplete").length == 0) {
        return;
    }

    $("#userdoc-ground-design-autocomplete").autocomplete({
        source: function (request, response) {
            let url = $("#userdoc-ground-design-autocomplete").data("url");

            let ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {registrationNbr: request.term},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            let responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
            commonAjaxCall(ajaxRequest, responseAction);
        },
        minLength: 1,
        delay: 1000,
        select: function (event, ui) {
            setTimeout(function () {
                if (ui.item == null) {
                    return;
                }

                if(ui.item.title !=null){
                    $('#userdoc-ground-design-autocomplete').val(ui.item.fileSeq  +'/' + ui.item.fileType +'/' + ui.item.fileSeries +'/' + ui.item.fileNbr+', '+ui.item.registrationNbr+' - ' + ui.item.title);
                }else{
                    $('#userdoc-ground-design-autocomplete').val(ui.item.fileSeq  +'/' + ui.item.fileType +'/' + ui.item.fileSeries +'/' + ui.item.fileNbr+', '+ui.item.registrationNbr);
                }

                $('#hidden-ground-design').val(ui.item.fileSeq  +'/' + ui.item.fileType +'/' + ui.item.fileSeries +'/' + ui.item.fileNbr);
                return false;
            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        let itemText;
        if(item.title !=null){
            itemText="<a><span><span>" + item.fileSeq  +'/' + item.fileType +'/' + item.fileSeries +'/' + item.fileNbr+ ', '+ item.registrationNbr + ' - ' + item.title +
                "</span></span></a>";
        }else{
            itemText="<a><span><span>" + item.fileSeq  +'/' + item.fileType +'/' + item.fileSeries +'/' + item.fileNbr+ ', '+ item.registrationNbr +
                "</span></span></a>";
        }

        return $("<li></li>")
            .data("item.autocomplete", item)
            .append(itemText).appendTo(ul);
    };

}