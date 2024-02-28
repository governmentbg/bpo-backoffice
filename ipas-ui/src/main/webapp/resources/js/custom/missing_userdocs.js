$(function () {
    if ($("#missing-userdocs-progressbar").length > 0) {
        if ($("#execute-missing-userdocs-process").length > 0) {
            let url = $("#execute-missing-userdocs-process").data('url');
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({});
            commonAjaxCall(request, responseAction);
        }
        let urlProgressBar = $("#missing-userdocs-progressbar").data('url');
        updateMissingUserdocsProgressBar(urlProgressBar)

        if ($("#inserted-userdocs-table-wrapper").length > 0) {
            let urlUpdateTable = $("#inserted-userdocs-table-wrapper").data('url');
            updateInsertedUserdocsTable(urlUpdateTable);
        }

    }
});

function updateMissingUserdocsProgressBar(url) {
    let missingUserdocsProgressbarInterval = null;
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(initMissingUserdocsInterval, [])
    });
    commonAjaxCall(request, responseAction);

    function initMissingUserdocsInterval(progressBarData) {
        if (progressBarData.inProgress) {
            missingUserdocsProgressbarInterval = setInterval(function () {
                updateProgressBar(url);
            }, 2000);
        }

        function updateProgressBar(url) {
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({
                executeOnValid: new FuncCall(updateData, [])
            });

            function updateData(progressBarData) {
                if (!progressBarData.inProgress) {
                    clearInterval(missingUserdocsProgressbarInterval);
                    $('[data-action=\'start-missing-userdocs-process\']').removeAttr('disabled');
                    $('[data-action=\'stop-missing-userdocs-process\']').attr('disabled', 'disabled');
                    if (progressBarData.endSuccessful) {
                        $('.progressbar-info').text(messages["missing.userdocs.process.success"]);
                        $('.progressbar-progress').removeClass('blue');
                        $('.progressbar-progress').addClass('m-green lighten-1');
                    } else if (progressBarData.interrupt) {
                        $('.progressbar-info').text(progressBarData.message);
                        $('.progressbar-progress').removeClass('blue');
                        $('.progressbar-progress').addClass('m-red lighten-1');
                    }
                }
                if (progressBarData.progress != 0) {
                    $('.progressbar-progress').css('width', progressBarData.progress + '%');
                    $('.progressbar-progress').text(progressBarData.progress + '%');
                } else {
                    $('.progressbar-progress').css('width', (progressBarData.progress + 1) + '%');
                    $('.progressbar-progress').text((progressBarData.progress + 1) + '%');
                }
                let message = progressBarData.message;
                if (message !== null && $('.progressbar-log:contains(' + message + ')').length <= 0) {
                    $('.progressbar-log').append("<div>" + message + "</div>")
                }
            }

            commonAjaxCall(request, responseAction);
        }
    }
}

function updateInsertedUserdocsTable(url) {
    let insertedUserdocsTableInterval = null;
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(initInsertedUserdocsInterval, [])
    });
    commonAjaxCall(request, responseAction);

    function initInsertedUserdocsInterval(data) {
        if (data.indexOf('update-inserted-userdocs-table') != -1) {
            insertedUserdocsTableInterval = setInterval(function () {
                updateTable(url);
            }, 20000);
        }

        function updateTable(url) {
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({
                updateContainerOnValid: '#inserted-userdocs-table-wrapper',
                executeOnValid: new FuncCall(updateInterval, []),
                executeOnInvalid: new FuncCall(UIBlocker.unblock, []),
            });

            function updateInterval(data) {
                if (data.indexOf('update-inserted-userdocs-table') == -1) {
                    clearInterval(insertedUserdocsTableInterval);
                }

                UIBlocker.unblock();
            }

            commonAjaxCall(request, responseAction);
        }
    }
}

$(document).on("click", "[data-action='start-missing-userdocs-process'], [data-action='stop-missing-userdocs-process']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    RequestUtils.post(url, {})
});