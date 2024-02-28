$(function () {
    if ($("#imark-replacement-autocomplete-registrationNumber").length > 0) {
        initReplacementRegNumberAutocomplete();
    }
});

function initReplacementRegNumberAutocomplete(){
    if ($("#imark-replacement-autocomplete-registrationNumber").length == 0) {
        return;
    }

    let $autocomplete = $('#imark-replacement-autocomplete-registrationNumber');
    $autocomplete.autocomplete({
        minLength: 1,
        delay: 500,
        source: function (request, response) {
            var url = $autocomplete.data('url');

            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {registrationNumber: request.term},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});
            commonAjaxCall(ajaxRequest, responseAction);
        },
        focus: function (event, ui) {
            $autocomplete.val(ui.item.registrationNumber);
            return false;
        },
        select: function (event, ui) {
            $autocomplete.val(ui.item.registrationNumber);
            $autocomplete.attr("data-id", ui.item.registrationNumber);
            $("#imark-replacement-nice-classes-section").show();
            $("#imark-replacement-mark-details-section").show();
            $("#imark-replacement-mark-details-section").find("label").addClass("active");
            $("#international-replacement-filingNumber").attr('value', ui.item.filingNumber);

            if (ui.item.markName) {
                $("#international-replacement-markName-div").show();
                $("#international-replacement-markName").attr('value', ui.item.markName);
            } else {
                $("#international-replacement-markName-div").hide();
                $("#international-replacement-markName").attr('value', '');
            }

            return false;
        },
        change: function (event, ui) {
            if (ui.item == null) {
                $(this).val('');
                $(this).attr("data-id", '');
                $(this).siblings('label').removeClass('active');
                $("#imark-replacement-nice-classes-section").hide();
                $("#imark-replacement-mark-details-section").hide();
                $("#international-replacement-filingNumber").attr('value', '');
                $("#international-replacement-markName").attr('value', '');
            }
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        let markName = item.markName ? item.markName : '-';
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span>" + item.registrationNumber + ' [ ' + item.filingNumber + ' (' + markName + ') ] ' + "</span></a>").appendTo(ul);
    };
}