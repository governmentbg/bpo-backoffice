$(function () {
    if($(patentContainer).length > 0) {
        if ($("#taxonNomenclatureAutocomplete").length > 0) {
            initTaxonNomenclatureAutocomplete();
        }
    }
});

function initTaxonNomenclatureAutocomplete(){
    if ($("#taxonNomenclatureAutocomplete").length == 0) {
        return;
    }

    $("#taxonNomenclatureAutocomplete").autocomplete({
        source: function (request, response) {

            var url = $("#taxonNomenclatureAutocomplete").data("url");
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {taxonNomData: request.term},
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

                $('#plantNumenclature-taxonCode').val(ui.item.taxonCode);
                $('#plantNumenclature-commonClassifyBul').val(ui.item.commonClassifyBul);
                $('#plantNumenclature-commonClassifyEng').val(ui.item.commonClassifyEng);
                $('#plantNumenclature-latinClassify').val(ui.item.latinClassify);
                $('#plantNumenclature-id').val(ui.item.id);
                $('#taxonNomenclatureAutocomplete').addClass("none");
                $('#taxonNomenclature-delete-button').removeClass("none")
                checkForActiveLabels($('#plantTaxonNomenclature-area'));

                return false;
            }, 100);
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span><span>" + item.taxonCode  +'; ' + item.commonClassifyBul +'; ' + item.commonClassifyEng +'; ' + item.latinClassify+
                "</span></span></a>").appendTo(ul);
    };

}

$(document).on("click", "[data-action='taxonNomenclature-delete']", function (e) {

    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        $('#plantNumenclature-taxonCode').val("");
        $('#plantNumenclature-commonClassifyBul').val("");
        $('#plantNumenclature-commonClassifyEng').val("");
        $('#plantNumenclature-latinClassify').val("");
        $('#plantNumenclature-id').val("");
        $('#taxonNomenclatureAutocomplete').removeClass("none");
        $('#taxonNomenclature-delete-button').addClass("none")
    }
});