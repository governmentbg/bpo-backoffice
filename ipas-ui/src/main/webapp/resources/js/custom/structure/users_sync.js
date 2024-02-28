$(document).ready(function () {
    if ($('#users-sync-success-modal').length > 0) {
        commonOpenModalFormInit("#users-sync-success-modal", true, false)
    }
});

$(document).on("click", "#users-sync-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $tableWrapper = $('#users-sync-table-wrapper');
    let url = $tableWrapper.data('url');

    let callParams = {
        sortOrder: checkEmptyField($(this).data('order')),
        sortColumn: checkEmptyField($(this).data('sort'))
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateWithAjaxAndUnblock(request, '#users-sync-table-wrapper');
});

$(document).on("click", "[data-action='sync-user']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, messages['user.sync.confirm.msg'])
    } else {
        UIBlocker.block();
        let url = $button.data('url');
        let callParams = {
            screenName: checkEmptyField($button.data('user')),
            sortOrder: checkEmptyField($('#users-sync-order').val()),
            sortColumn: checkEmptyField($('#users-sync-sort').val())
        };
        RequestUtils.post(url,callParams);
    }
});

$(document).on("click", "[data-action='sync-all-users']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, messages['user.sync.all.confirm.msg'])
    } else {
        UIBlocker.block();
        let url = $button.data('url');
        let callParams = {
            sortOrder: checkEmptyField($('#users-sync-order').val()),
            sortColumn: checkEmptyField($('#users-sync-sort').val())
        };
        RequestUtils.post(url,callParams);
    }
});
