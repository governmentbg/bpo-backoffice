$(function () {
    if ($("#expiring-marks-progressbar").length > 0) {
        if ($("#expiring-marks-notifications-execute-process").length > 0) {
            let url = $("#expiring-marks-notifications-execute-process").data('url');
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({});
            commonAjaxCall(request, responseAction);
        }
        let url = $("#expiring-marks-progressbar").data('url');
        updateExpiringMarksProgressBar(url)
    }
});


$(document).on("click", "#search-expiring-marks-btn", function (e) {
    let url = $(this).data('url');
    let callParams = {
        expirationDateFrom: checkEmptyField($('#expiring-marks-dateFrom-filter-input').val()),
        expirationDateTo: checkEmptyField($('#expiring-marks-dateTo-filter-input').val()),
    };

    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#expiring-marks-filters-wrapper",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: "#expiring-marks-table-wrapper",
        executeOnValid: new FuncCall(handleExecuteOnValid, []),
    });

    function handleExecuteOnInvalid() {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#expiring-marks-filters-wrapper",
        });
    }

    function handleExecuteOnValid() {
        $("#expiring-marks-filters-wrapper .validation-error").remove()
    }

    let request = new CommonAjaxRequest(url, {
        requestData: callParams,
        useSessionId: false,
        blockUI: true,
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "#expiring-marks-checbox-all", function (e) {
    if ($(this).is(':checked')) {
        $('.expiring-marks-checbox').prop('checked', true);
    } else {
        $('.expiring-marks-checbox').prop('checked', false);
    }
});


$(document).on("click", "#start-expiring-marks-notifications-process", function (e) {
    let array = [];
    $('.expiring-marks-checbox:checkbox:checked').each(function () {
        array.push($(this).attr("id"));
    });

    if (array.length === 0) {

    } else {
        UIBlocker.block();
        let url = $(this).attr("data-url");
        RequestUtils.post(url, {marks: array})
    }
});

$(document).on("click", "#stop-expiring-marks-notifications-process", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    RequestUtils.post(url, {})
});

function updateExpiringMarksProgressBar(url) {
    let progressbarInterval = null;
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(initInterval, [])
    });
    commonAjaxCall(request, responseAction);

    function initInterval(progressBarData) {
        if (progressBarData.inProgress) {
            progressbarInterval = setInterval(function () {
                updateProgressBar(url);
            }, 1000);
        }

        function updateProgressBar(url) {
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({
                executeOnValid: new FuncCall(updateData, [])
            });

            function updateData(progressBarData) {
                if (!progressBarData.inProgress) {
                    clearInterval(progressbarInterval);
                    if (progressBarData.endSuccessful) {
                        $('.progressbar-info').text('Successful !');
                        $('.progressbar-progress').removeClass('blue');
                        $('.progressbar-progress').addClass('m-green lighten-1');
                    } else if (progressBarData.interrupt) {
                        $('.progressbar-info').text(progressBarData.message);
                        $('.progressbar-progress').removeClass('blue');
                        $('.progressbar-progress').addClass('m-red lighten-1');
                    }
                }
                $('.progressbar-progress').css('width', progressBarData.progress + '%');
                $('.progressbar-progress').text(progressBarData.progress + '%');
                let message = progressBarData.message;
                if (message !== null && $('.progressbar-log:contains(' + message + ')').length <= 0) {
                    $('.progressbar-log').append("<div>" + message + "</div>")
                }

                if (progressBarData.progress === 100) {
                    document.location.href = $('#expiring-marks-progressbar').attr("data-baseurl");
                }

                console.log(123);
                updateGenerationResultData();
            }


            function updateGenerationResultData() {
                let url = $('#expiring-marks-generation-result-wrapper').data('url');

                let responseAction = new CommonAjaxResponseAction({
                    updateContainerOnValid: "#expiring-marks-generation-result-wrapper",
                });

                let request = new CommonAjaxRequest(url, {
                    requestData: {},
                    useSessionId: false
                });

                commonAjaxCall(request, responseAction);

            }

            commonAjaxCall(request, responseAction);
        }
    }
}