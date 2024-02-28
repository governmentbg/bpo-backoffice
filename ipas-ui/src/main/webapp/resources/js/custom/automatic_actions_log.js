
$(function () {
    executeCommonInitialization({
        initializeFormElementsWrapper: "#error-filter-wrapper"
    });

    $(document).on("click", "#clear-automatic-actions-log-btn", function (e) {
        UIBlocker.block();
        window.reloadPage()
    });

    $(document).on("click", "#search-automatic-actions-log-btn", function (e) {
        UIBlocker.block();
        let url = $(this).data('url');
        let callParams = {
            timerName: $('#timerName-filter').val(),
            dateFrom: $('#dateFrom-filter').val(),
            dateTo: $('#dateTo-filter').val(),
            error: $('#error-filter').val(),
        };
        let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
        updateWithAjaxAndUnblock(request, '#automatic-actions-log-table-wrapper');
    });

    $(document).on("click", "#automatic-actions-log-table-wrapper [data-action='table-sort']", function (e) {
        UIBlocker.block();
        let url = $('#search-automatic-actions-log-btn').data('url');
        let sortColumn = $(this).data('sort');
        let sortOrder = $(this).data('order');
        let callParams = {
            sortOrder: checkEmptyField(sortOrder),
            sortColumn: checkEmptyField(sortColumn),
            page: checkEmptyField($('#userdoc-reg-number-change-log-page').val()),
            pageSize: checkEmptyField($('#userdoc-reg-number-change-log-pageSize').val()),
            timerName: $('#timerName-filter').val(),
            dateFrom: $('#dateFrom-filter').val(),
            dateTo: $('#dateTo-filter').val(),
            error: $('#error-filter').val(),
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
        updateWithAjaxAndUnblock(request, '#automatic-actions-log-table-wrapper');
    });

    $(document).on("click", "#delete-24h-log-btn", function (e) {
        let $button = $(this);
        if (Confirmation.exist($button)) {
            Confirmation.openConfirmCloneBtnModal($button, $button.data("warning"));
        } else {
            let url = $(this).data('url');
            let callParams = {};
            let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false, blockUI: true} );
            let responseAction = new CommonAjaxResponseAction({
                executeOnValid: new FuncCall(handleExecuteOnValid, [])
            });

            function handleExecuteOnValid() {
                BaseModal.openInfoModal($button.data("confirm"));
            }

            commonAjaxCall(request, responseAction);
        }

    });

});




