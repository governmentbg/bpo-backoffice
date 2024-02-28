$(document).ready(function (){
   if($("#offidoc-notifications-form").length >0){
       executeCommonInitialization({
           initializeFormElementsWrapper: "#offidoc-notifications-form",
       });
   }
});

$(document).on("click", "#search-offidoc-notifications-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');

    let request = new CommonAjaxRequest(url, {requestData: $("#offidoc-notifications-form").serialize(), useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#offidoc-notifications-table-wrapper',
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "#offidoc-notifications-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $wrapper = $('#offidoc-notifications-table-wrapper');
    let url = $wrapper.data('url');
    let callParams = {
        isFiltering: false,
        sortColumn: $(this).data('sort'),
        sortOrder: $(this).data('order'),
        tableTotal: $("#offidocNotificationsTotal").html()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateWithAjaxAndUnblock(request, '#offidoc-notifications-table-wrapper');

});
