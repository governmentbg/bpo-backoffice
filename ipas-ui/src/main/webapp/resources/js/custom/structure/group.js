$(document).on("click", "[data-action='add-group-user']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let el = $(".autocomplete-user");
        let id = el.attr("data-id");

        if (id == "" || id == null || id === undefined) {
            return;
        }
        let url = $button.data("url");
        groupAddDeleteUser(url, id);
    }
});
$(document).on("click", "[data-action='delete-group-user']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $(this).data("url");
        let userId = $(this).data("user");
        groupAddDeleteUser(url, userId);
    }
});

function groupAddDeleteUser(url, _userId) {
    let callParams = {
        groupId: $("#groupId").val(),
        userId: _userId
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onModifyDataError),
        executeOnValid: new FuncCall(reloadPage)
    });
    commonAjaxCall(request, responseAction);
}
function onModifyDataError(data) {
    $("#validation-errors-modal").replaceWith(data);
    commonOpenModalFormInit("#validation-errors-modal", false);
}