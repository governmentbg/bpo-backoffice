$(function () {
    if ($("#missing-intl-marks-progressbar").length > 0) {
        if ($("#execute-missing-intl-marks-process").length > 0) {
            let url = $("#execute-missing-intl-marks-process").data('url');
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({});
            commonAjaxCall(request, responseAction);
        }
        let urlProgressBar = $("#missing-intl-marks-progressbar").data('url');
        updateMissingIntlMarksProgressBar(urlProgressBar)

    }
});

function updateMissingIntlMarksProgressBar(url) {
    let missingIntlMarksProgressbarInterval = null;
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(initMissingIntlMarksInterval, [])
    });
    commonAjaxCall(request, responseAction);

    function initMissingIntlMarksInterval(progressBarData) {
        if (progressBarData.inProgress) {
            missingIntlMarksProgressbarInterval = setInterval(function () {
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
                    clearInterval(missingIntlMarksProgressbarInterval);
                    $('[data-action=\'start-missing-intl-marks-process\']').removeAttr('disabled');
                    $('[data-action=\'stop-missing-intl-marks-process\']').attr('disabled', 'disabled');
                    if (progressBarData.endSuccessful) {
                        $('.progressbar-info').text(messages["missing.international.marks.process.success"]);
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

$(document).on("click", "[data-action='start-missing-intl-marks-process'], [data-action='stop-missing-intl-marks-process']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    RequestUtils.post(url, {})
});