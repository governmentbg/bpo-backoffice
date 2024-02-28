$(function () {
    if ($("#split-person-progressbar").length > 0) {
        if ($("#execute-split-person-process").length > 0) {
            let url = $("#execute-split-person-process").data('url');
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({});
            commonAjaxCall(request, responseAction);
        }
        let url = $("#split-person-progressbar").data('url');
        updateSplitPersonProgressBar(url)
    }

    if ($("#remove-person-progressbar").length > 0) {
        if ($("#execute-remove-person-process").length > 0) {
            let url = $("#execute-remove-person-process").data('url');
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({});
            commonAjaxCall(request, responseAction);
        }
        let url = $("#remove-person-progressbar").data('url');
        updateRemoveNonUsedPersonProgressBar(url);
    }

    if ($("#duplicate-person-progressbar").length > 0) {
        if ($("#execute-duplicate-person-process").length > 0) {
            let url = $("#execute-duplicate-person-process").data('url');
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({});
            commonAjaxCall(request, responseAction);
        }
        let url = $("#duplicate-person-progressbar").data('url');
        updateDuplicatePersonProgressBar(url);
    }

    if ($("#merge-person-search-result-wrapper").length > 0) {
        loadMergePersonsList();
    }
});

function loadMergePersonsList() {
    let url = $("#merge-person-search-result-wrapper").attr("data-url");
    let callParams = {};
    let request = new CommonAjaxRequest(url, callParams);
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#merge-person-search-result-wrapper'
    });
    commonAjaxCall(request, responseAction);
}

function updateSplitPersonProgressBar(url) {
    let splitPersonProgressbarInterval = null;
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(initSplitPersonInterval, [])
    });
    commonAjaxCall(request, responseAction);

    function initSplitPersonInterval(progressBarData) {
        if (progressBarData.inProgress) {
            splitPersonProgressbarInterval = setInterval(function () {
                updateProgressBar(url);
            }, 800);
        }

        function updateProgressBar(url) {
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({
                executeOnValid: new FuncCall(updateData, [])
            });

            function updateData(progressBarData) {
                if (!progressBarData.inProgress) {
                    clearInterval(splitPersonProgressbarInterval);
                    $('[data-action=\'start-split-person-process\']').removeAttr('disabled');
                    $('[data-action=\'stop-split-person-process\']').attr('disabled', 'disabled');
                    if (progressBarData.endSuccessful) {
                        $('.progressbar-info').text(messages["split.person.process.success"]);
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
            }

            commonAjaxCall(request, responseAction);
        }
    }
}

function updateRemoveNonUsedPersonProgressBar(url) {
    let removeNotUsedPersonProgressbarInterval = null;
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(initRemoveNotUsedPersonInterval, [])
    });
    commonAjaxCall(request, responseAction);

    function initRemoveNotUsedPersonInterval(progressBarData) {
        if (progressBarData.inProgress) {
            removeNotUsedPersonProgressbarInterval = setInterval(function () {
                updateProgressBar(url);
            }, 800);
        }

        function updateProgressBar(url) {
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({
                executeOnValid: new FuncCall(updateData, [])
            });

            function updateData(progressBarData) {
                if (!progressBarData.inProgress) {
                    clearInterval(removeNotUsedPersonProgressbarInterval);
                    $('[data-action=\'start-remove-person-process\']').removeAttr('disabled');
                    $('[data-action=\'stop-remove-person-process\']').attr('disabled', 'disabled');
                    if (progressBarData.endSuccessful) {
                        $('.progressbar-info').text(messages["remove.person.process.success"]);
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
                    $('.progressbar-progress').css('width', progressBarData.progress + 1 + '%');
                    $('.progressbar-progress').text(progressBarData.progress + 1 + '%');
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

function updateDuplicatePersonProgressBar(url) {
    let duplicateProgressbarInterval = null;
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(initDuplicatePersonInterval, [])
    });
    commonAjaxCall(request, responseAction);

    function initDuplicatePersonInterval(progressBarData) {
        if (progressBarData.inProgress) {
            duplicateProgressbarInterval = setInterval(function () {
                updateProgressBar(url);
            }, 800);
        }

        function updateProgressBar(url) {
            let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
            let responseAction = new CommonAjaxResponseAction({
                executeOnValid: new FuncCall(updateData, [])
            });

            function updateData(progressBarData) {
                if (!progressBarData.inProgress) {
                    clearInterval(duplicateProgressbarInterval);
                    $('[data-action=\'start-duplicate-person-process\']').removeAttr('disabled');
                    $('[data-action=\'stop-duplicate-person-process\']').attr('disabled', 'disabled');
                    if (progressBarData.endSuccessful) {
                        $('.progressbar-info').text(messages["duplicate.person.process.success"]);
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
                    $('.progressbar-progress').css('width', progressBarData.progress + 1 + '%');
                    $('.progressbar-progress').text(progressBarData.progress + 1 + '%');
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

$(document).on("click", "[data-action='start-split-person-process'], [data-action='stop-split-person-process'], [data-action='start-remove-person-process'], [data-action='stop-remove-person-process'], [data-action='start-duplicate-person-process'], [data-action='stop-duplicate-person-process']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    RequestUtils.post(url, {})
});

$(document).on("click", "[data-action='clear-merge-person-search']", function (e) {
    UIBlocker.block();
    $('#personName-filter').val('');
    $('#personAddress-filter').val('');

    let url = $(this).data('url');
    let request = new CommonAjaxRequest(url, {requestData: {}, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#merge-person-search-result-wrapper',
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='submit-merge-person-search']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        personName: $('#personName-filter').val(),
        personAddress: $('#personAddress-filter').val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#merge-person-search-result-wrapper',
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='update-duplicate-persons']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    RequestUtils.post(url, {});
});

$(document).on("click", "[data-action='person-merge-info-modal']", function (e) {
    let url = $(this).data('url');
    let personNbr = $(this).data('person');
    let addressNbr = $(this).data('address');

    let callParams = {
        personNbr: personNbr,
        addressNbr: addressNbr
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#person-merge-info-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#person-merge-info-modal", false])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='execute-person-merge']", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let mainPersonNbr = $(this).data('main-person');
    let mainAddressNbr = $(this).data('main-address');
    let checkText = $("#merge-person-check").val();

    let callParams = {
        mainPersonNbr: mainPersonNbr,
        mainAddressNbr: mainAddressNbr,
        checkText: checkText
    };

    RequestUtils.post(url, callParams);

});

$(document).on("click", function () {
    $(".merge-result").empty();
});