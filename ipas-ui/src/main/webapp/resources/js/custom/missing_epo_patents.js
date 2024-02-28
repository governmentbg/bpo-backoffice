$(document).on("change", "input[type=radio][name='search-missing-epopatent-radio']", function (e) {
    let $label = $('label[for=\'missing-epopatentInput\']');
    let text = $(this).next().text();
    $label.text(text);
});

$(document).on("click", "[data-action='clear-missing-epopatent-search']", function (e) {
    $('#missing-epopatentInput').val('');
    $('#missing-epopatent-search-result-wrapper').empty();
    $("label[for='missing-epopatentInput']").removeClass("active");
    $(".validation-error").remove();
});

$(document).on("click", "[data-action='submit-missing-epopatent-search']", function (e) {
    $(".validation-error").remove();
    let searchText = $('#missing-epopatentInput').val();
    let searchType = $('input[type=radio][name=\'search-missing-epopatent-radio\']:checked').val();
    let button = $(this);
    let url = button.data('url');
    let callParams = {
        searchText: searchText,
        searchType: searchType,
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#missing-epopatent-input-wrapper",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: "#missing-epopatent-search-result-wrapper",
    });

    function handleExecuteOnInvalid() {
        let searchTypeLabel = $('input[type=radio][name=\'search-missing-epopatent-radio\']:checked').next().text();
        $('label[for=\'missing-epopatentInput\']').text(searchTypeLabel);
        checkForActiveLabels($('#missing-epopatent-input-wrapper'));
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='save-missing-epo-patent']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, messages["add.missing.epo.patent.confirm.msg"])
    } else {
        UIBlocker.block();
        let url = $button.data('url');
        let filingNumber = $button.data('number');
        let callParams = {
            filingNumber: filingNumber
        };
        RequestUtils.post(url, callParams);
    }
});