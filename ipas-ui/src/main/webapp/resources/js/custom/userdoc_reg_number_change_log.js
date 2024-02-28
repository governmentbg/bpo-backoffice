$(document).on("click", "#clear-userdoc-reg-number-change-log-btn", function (e) {
    UIBlocker.block();
    window.reloadPage()
});

$(document).on("click", "#search-userdoc-reg-number-change-log-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateWithAjaxAndUnblock(request, '#userdoc-reg-number-change-log-table-wrapper');
});

$(document).on("click", "#userdoc-reg-number-change-log-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $wrapper = $('#userdoc-reg-number-change-log-table-wrapper');
    let url = $wrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');
    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#userdoc-reg-number-change-log-page').val()),
        pageSize: checkEmptyField($('#userdoc-reg-number-change-log-pageSize').val()),
        responsibleUser: $('#responsibleUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateWithAjaxAndUnblock(request, '#userdoc-reg-number-change-log-table-wrapper');
});