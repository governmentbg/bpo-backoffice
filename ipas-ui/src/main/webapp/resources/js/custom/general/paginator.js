$("body").on("change"," .paginator .ui-paginator select", function (e) {
    var tableContainerDiv = $(this).find("option:selected").attr("data-div");
    var callUrl = $(this).find("option:selected").attr("data-url");

    callTableUpdateWithAjax(callUrl,"#"+tableContainerDiv);
});

$("body").on("click", " .paginator .paginator-page.paginatorHref", function (e) {
    e.preventDefault();

    if ($(this).hasClass("paginator-href-disabled")) {
        return false;
    }

    var tableContainerDiv = $(this).attr("data-div");
    var callUrl = $(this).attr("data-url");

    callTableUpdateWithAjax(callUrl,"#"+tableContainerDiv);
});

function callTableUpdateWithAjax(callUrl, updateComponentSelector){
    UIBlocker.block();
    var callParams = {
        partial: true
    };

    var request = new CommonAjaxRequest(callUrl, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: updateComponentSelector,
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });

    commonAjaxCall(request, responseAction);

}