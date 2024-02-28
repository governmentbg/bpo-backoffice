$(function () {
    if ($("#patentCertificate-paidFees-progressbar").length > 0) {
        if ($("#patentCertificate-paidFees-execute-process").length > 0) {
            let url = $("#patentCertificate-paidFees-execute-process").data('url');
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({});
            commonAjaxCall(request, responseAction);
        }
        let url = $("#patentCertificate-paidFees-progressbar").data('url');
        updatePatentCertificatePaidFeesProgressBar(url)
    }
});

$(document).on("click", "#search-patentCertificate-paidFees-btn", function (e) {
    let url = $(this).data('url');
    let callParams = {
        paymentDateFrom: checkEmptyField($('#patentCertificate-paidFees-dateFrom-filter-input').val()),
        paymentDateTo: checkEmptyField($('#patentCertificate-paidFees-dateTo-filter-input').val()),
        payer: checkEmptyField($('#patentCertificate-paidFees-payer-filter-input').val()),
        abdocsDoc: checkEmptyField($('#patentCertificate-paidFees-abdocsDoc').val()),
    };

    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#patentCertificate-paidFees-filters-wrapper",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: "#patentCertificate-paidFees-table-wrapper",
        executeOnValid: new FuncCall(handleExecuteOnValid, []),
    });

    function handleExecuteOnInvalid() {
        executeCommonInitialization({
            initializeFormElementsWrapper: "#patentCertificate-paidFees-filters-wrapper",
        });
    }

    function handleExecuteOnValid() {
        $("#patentCertificate-paidFees-filters-wrapper .validation-error").remove()
    }

    let request = new CommonAjaxRequest(url, {
        requestData: callParams,
        useSessionId: false,
        blockUI: true,
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "#patentCertificate-paidFees-checkbox-all", function (e) {
    if ($(this).is(':checked')) {
        $('.patentCertificate-paidFees-checkbox').prop('checked', true);
    } else {
        $('.patentCertificate-paidFees-checkbox').prop('checked', false);
    }
});

$(document).on("click", "#start-patentCertificate-paidFees-process", function (e) {
    let array = [];
    $('.patentCertificate-paidFees-checkbox:checkbox:checked').each(function () {
        array.push($(this).attr("id"));
    });

    if (array.length === 0) {

    } else {
        UIBlocker.block();
        let url = $(this).attr("data-url");
        RequestUtils.post(url, {paidFees: array, abdocsDoc: $('#selected-abdocsDoc').val()})
    }
});

$(document).on("click", "#stop-patentCertificate-paidFees-process", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    RequestUtils.post(url, {})
});

function updatePatentCertificatePaidFeesProgressBar(url) {
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
                    document.location.href = $('#patentCertificate-paidFees-progressbar').attr("data-baseurl");
                }

                updateGenerationResultData();
            }
            function updateGenerationResultData() {
                let url = $('#patentCertificate-paidFees-result-wrapper').data('url');
                let responseAction = new CommonAjaxResponseAction({
                    updateContainerOnValid: "#patentCertificate-paidFees-result-wrapper",
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