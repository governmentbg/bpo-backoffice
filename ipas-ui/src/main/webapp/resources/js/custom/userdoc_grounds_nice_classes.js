function getNiceClassesArray(){
    let niceClasses = [];
    $('[id*=ground-nice-code]').each(function (index, el) {
        let niceClass = {};
        niceClass.niceClassCode = $("#"+el.id).text();
        let niceDescId=$("#"+el.id).attr("data-ground-nice-description-id");
        niceClass.niceClassDescription = $("#" +niceDescId ).text()
        niceClasses.push(niceClass);
    });

    return niceClasses;

}

function ifAllNiceClassesOptionSelected(){
    let allClassesRadio = $('[name ="niceClassesInd-all"]').is(':checked');
    let partiallyClassesRadio = $('[name ="niceClassesInd-partially"]').is(':checked');

    if(allClassesRadio ===true){
        return allClassesRadio;
    }else if(partiallyClassesRadio === true){
        return false;
    }
    else return null;
}

$(document).on("click", "[name =\"niceClassesInd-all\"]", function (e) {
    $('[name ="niceClassesInd-partially"]').prop('checked', false);
    $('#ground-nice-classes-table').hide();
});

$(document).on("click", "[name =\"niceClassesInd-partially\"]", function (e) {
    $('[name ="niceClassesInd-all"]').prop('checked', false);

    $('#ground-nice-classes-table').show();
});

$(document).on("click", "[data-action='delete-ground-nice-class']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let niceClassIdAttr = $button.data('nice-class-id');
    let niceClassCode = $("#"+niceClassIdAttr).text();
    let resultJson = {};
    let markGroundDataJson = {};
    markGroundDataJson['niceClasses']=getNiceClassesArray();
    resultJson['markGroundData']=markGroundDataJson;
    groundNiceClassesAjaxCall(url,resultJson,niceClassCode,null);
});

$(document).on("click", "[data-action='add-ground-nice-class']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let niceClassCode=$("#root-ground-new-nice-code").val();
    let niceClassDescription =$("#root-ground-new-nice-description").val();
    let resultJson = {};
    let markGroundDataJson = {};
    markGroundDataJson['niceClasses']=getNiceClassesArray();
    resultJson['markGroundData']=markGroundDataJson;
    groundNiceClassesAjaxCall(url,resultJson,niceClassCode,niceClassDescription);

});

function groundNiceClassesAjaxCall(url,resultJson,niceClassCode,niceClassDescription) {
    $.ajax({
        url: url,
        type: 'POST',
        datatype: 'html',
        data: {
            sessionIdentifier: $('#session-object-identifier').val(),
            niceClassCode:niceClassCode,
            niceClassDescription:niceClassDescription,
            data : JSON.stringify(resultJson)
        },
        success: function (data) {
            $("#ground-nice-classes-table").html(data);
            executeCommonInitialization({
                initializeFormElementsWrapper: "#ground-nice-classes-table",
                inputsForEditWrapper:"#ground-nice-classes-table"
            }, null);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            UIBlocker.unblock();
            openErrorModal(xhr);
        }
    })
}