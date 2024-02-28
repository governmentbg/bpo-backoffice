$(document).on("click", "[data-action='add-reviewer']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let userId = $('#autocomplete-add-reviewer').data('id');
    if (null != url) {
        let callParams = {
            userId: userId
        };
        updateReviewersAjaxCall(url,callParams);
    }
});

$(document).on("click", "[data-action='delete-reviewer']", function (e) {
    let $button = $(this);
    let url = $button.data('url');
    let userId = $button.attr("data-id");
    if (null != url) {
        let callParams = {
            userId: userId
        };
        updateReviewersAjaxCall(url,callParams);
    }
});


$(document).on("click", ".reviewer-main-checkbox", function (e) {
    let userId = $(this).attr('id');
    let mainSelected;
    let url = $(this).data('url');
    if ($(this).is(':checked')) {
        mainSelected=true;
    }else{
        mainSelected=false;
    }
    if (null != url) {
        let callParams = {
            userId: userId,
            mainSelected: mainSelected
        };
        updateReviewersAjaxCall(url,callParams);
    }
});

function updateReviewersAjaxCall(url,callParams) {
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: userdocContainer + " #reviewer-squad-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: userdocContainer + " #reviewer-squad-wrapper",
            initializeFormElementsWrapper: userdocContainer + " #reviewer-squad-wrapper",
        }])
    });
    commonAjaxCall(request, responseAction);
}