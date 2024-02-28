$(function () {
    M.FormSelect.init($('#expired-term-fileType-filter'));
    M.FormSelect.init($('#expired-term-status-filter'));
    M.FormSelect.init($('#waiting-term-fileType-filter'));
    M.FormSelect.init($('#waiting-term-status-filter'));
    M.FormSelect.init($('#last-action-fileType-filter'));
    M.FormSelect.init($('#action-type-kind-filter'));

    executeCommonInitialization({
        initializeFormElementsWrapper: "#my-object-status-filter-wrapper, #expired-term-action-types-filter-wrapper,#userdoc-waiting-terms-filter-wrapper ,#userdoc-correspondence-terms-filter-wrapper,#userdoc-correspondence-terms-objectFileTyp-filter-wrapper,#userdoc-correspondence-terms-status-filter-wrapper ,#my-userdocs-userdocType-filter-wrapper,#last-action-userdocType-filter-wrapper,#my-userdocs-status-filter-wrapper,#my-userdocs-objectFileTyp-filter-wrapper,#reception-userdoc-objectFileTyp-filter-wrapper,#reception-userdoc-userdocType-filter-wrapper,#newly-allocated-userdoc-objectFileTyp-filter-wrapper,#newly-allocated-userdoc-userdocType-filter-wrapper",
    });

    $(".autocomplete-user").each(function (index, input) {
        initUserAutocomplete($(input));
    });

    $(".home_panel" ).each(function() {
        loadHomePanel('#'+$( this ).attr('id'));
    });
});

function checkSessionStorageRequestData(field) {
    if (field === '' || field === 'null' || field === null || field === undefined)
        return false;
    return true;
}

$(document).on("click", ".crumbs a", function (e) {
    let  mainUrl = $(this).attr("href");
    let homePanelProperties = sessionStorage.getItem(mainUrl);
    if(homePanelProperties!=null){
        e.preventDefault();
        let homePanelPropertiesAsObject = JSON.parse(homePanelProperties);
        let data = {};
        for (const property in homePanelPropertiesAsObject.requestData) {
            if(checkSessionStorageRequestData(homePanelPropertiesAsObject.requestData[property])===true){
                data[property] = homePanelPropertiesAsObject.requestData[property];
            }
        }
        sessionStorage.removeItem(mainUrl);
        RequestUtils.get(homePanelPropertiesAsObject.url,data);
    }
});

$("body").on("click", " .home-panel-paginator .paginator .paginator-page.paginatorHref", function (e) {
    let callUrl = $(this).attr("data-url");
    setSessionStorageHomePanelPropertiesFromPaginatorClick(callUrl);
});


$("body").on("change"," .home-panel-paginator .paginator .ui-paginator select", function (e) {
    let callUrl = $(this).find("option:selected").attr("data-url");
    setSessionStorageHomePanelPropertiesFromPaginatorClick(callUrl);
});

function createJsonObjectFromPaginatorClick(url) {
    let requestData = {};
    let URLVariables = url.split('?')[1].split("&");
    for (let i = 0; i < URLVariables.length; i++) {
        let singleVariableArray =  URLVariables[i].split("=");
        requestData[singleVariableArray[0]]=singleVariableArray[1];
    }
    return requestData;
}

function setSessionStorageHomePanelPropertiesFromPaginatorClick(url){
    let requestData = createJsonObjectFromPaginatorClick(url);
    setSessionStorageHomePanelProperties(requestData)
}

function setSessionStorageHomePanelProperties(requestData) {
    let url = window.location.href;
    let data = {url:url,requestData: requestData};
    sessionStorage.setItem(url, JSON.stringify(data));
}

function deleteHomePanelSessionStorageData() {
    let url = window.location.href;
    sessionStorage.removeItem(url);
}

function loadHomePanel(panelId) {
    let url = $(panelId).attr("data-url");
    let callParams = {};
    let request = new CommonAjaxRequest(url, callParams);
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid:panelId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: panelId,
            initializeFormElementsWrapper:panelId,
        }])
    });
    commonAjaxCall(request, responseAction);
}


$(document).on("click", "[data-action='generic-responsible-user-change']", function (e) {
    let checkedProcTypes=[];
    $('.proc-change-ruser-checbox:checkbox:checked').each(function(){
        checkedProcTypes.push($(this).attr("id"));
    });
    let userId = $("#responsibleUser-filter").data('id');
    let redirectUrl = $(this).attr("redirect-url");
    let url = $(this).attr("data-url");
    let callParams = {
        redirectUrl: redirectUrl,
        checkedProcTypes: checkedProcTypes
    };
    if (userId !== undefined) {
        callParams.userId = userId;
    }
    deleteHomePanelSessionStorageData();
    RequestUtils.post(url, callParams);
});

$(document).on("click", "#proc-change-ruser-checbox-all", function (e) {
    if ($(this).is(':checked')) {
        $('.proc-change-ruser-checbox').prop('checked', true);
    }else{
        $('.proc-change-ruser-checbox').prop('checked', false);
    }
});

$(document).on("click", "#reception-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $receptionTableWrapper = $('#reception-table-wrapper');
    let url = $receptionTableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let dataJSON = {};
    dataJSON ['sortOrder'] = checkEmptyField(sortOrder);
    dataJSON ['sortColumn'] = checkEmptyField(sortColumn);

    let callParams = {
        data: JSON.stringify(dataJSON)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#reception-table-wrapper');
});

$(document).on("click", "#reception-userdoc-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $receptionTableWrapper = $('#reception-userdoc-table-wrapper');
    let url = $receptionTableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#reception-userdoc-page').val()),
        pageSize: checkEmptyField($('#reception-userdoc-pageSize').val()),
        userdocType: $('#reception-userdoc-userdocType-filter').val(),
        objectFileTyp: $('#reception-userdoc-objectFileTyp-filter').val(),
        userdocFilingNumber: checkEmptyField($('#reception-userdoc-filingNumber-filter-input').val()),
        userdocFilingDateFrom: checkEmptyField($('#reception-userdoc-filingDateFrom-filter-input').val()),
        userdocFilingDateTo: checkEmptyField($('#reception-userdoc-filingDateTo-filter-input').val()),
        objectFileNbr: checkEmptyField($('#reception-userdoc-objectFileNbr-filter-input').val())
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#reception-userdoc-table-wrapper');
});

$(document).on("click", "#newly-allocated-userdoc-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $tableWrapper = $('#newly-allocated-userdoc-table-wrapper');
    let url = $tableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#newly-allocated-userdoc-page').val()),
        pageSize: checkEmptyField($('#newly-allocated-userdoc-pageSize').val()),
        userdocType: $('#newly-allocated-userdoc-userdocType-filter').val(),
        objectFileTyp: $('#newly-allocated-userdoc-objectFileTyp-filter').val(),
        userdocFilingNumber: checkEmptyField($('#newly-allocated-userdoc-filingNumber-filter-input').val()),
        userdocFilingDateFrom: checkEmptyField($('#newly-allocated-userdoc-filingDateFrom-filter-input').val()),
        userdocFilingDateTo: checkEmptyField($('#newly-allocated-userdoc-filingDateTo-filter-input').val()),
        objectFileNbr: checkEmptyField($('#newly-allocated-userdoc-objectFileNbr-filter-input').val()),
        onlyActiveUsers: $('#responsible-user-indinactive-checkbox').is(":checked"),
        responsibleUser: $('#responsibleUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#newly-allocated-userdoc-table-wrapper');
});

$(document).on("click", "#my-objects-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $myObjectsTableWrapper = $('#my-objects-table-wrapper');
    let url = $myObjectsTableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#my-objects-page').val()),
        pageSize: checkEmptyField($('#my-objects-pageSize').val()),
        statusCode: $('#my-object-status-filter').val(),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        newlyAllocated: $('#my-object-newly-allocated-checkbox').is(":checked"),
        priorityRequest: $('#my-object-priority-request-checkbox').is(":checked"),
        bordero: $('#bordero-filter').val(),
        journalCode: $('#journal-code-filter').val()
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#my-objects-table-wrapper');
});

$(document).on("click", "#my-userdocs-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $myObjectsTableWrapper = $('#my-userdocs-table-wrapper');
    let url = $myObjectsTableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#my-userdocs-page').val()),
        pageSize: checkEmptyField($('#my-userdocs-pageSize').val()),
        userdocGroupName: checkEmptyField($('#my-userdocs-group-name').val()),
        userdocType: $('#my-userdocs-userdocType-filter').val(),
        statusCode: $('#my-userdocs-status-filter').val(),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        objectFileTyp: $('#my-userdocs-objectFileTyp-filter').val(),
        userdocFilingNumber: checkEmptyField($('#my-userdocs-filingNumber-filter-input').val()),
        userdocFilingDateFrom: checkEmptyField($('#my-userdocs-filingDateFrom-filter-input').val()),
        inProduction: $('#my-userdocs-in-production-checkbox').is(":checked"),
        newlyAllocated: $('#my-userdocs-newly-allocated-checkbox').is(":checked"),
        finished: $('#my-userdocs-finished-checkbox').is(":checked"),
        userdocFilingDateTo: checkEmptyField($('#my-userdocs-filingDateTo-filter-input').val()),
        objectFileNbr: checkEmptyField($('#my-userdocs-objectFileNbr-filter-input').val())
    };
    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#my-userdocs-table-wrapper');
});


$(document).on("click", "#expired-term-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $expiredTermTableWrapper = $('#expired-term-table-wrapper');
    let url = $expiredTermTableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#expired-term-page').val()),
        pageSize: checkEmptyField($('#expired-term-pageSize').val()),
        actionType: $('#expired-term-action-types-filter').val(),
        fileType: $('#expired-term-fileType-filter').val(),
        statusCode: $('#expired-term-status-filter').val(),
        responsibleUser: $('#responsibleUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#expired-term-table-wrapper');
});

$(document).on("click", "#waiting-term-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $waitingTermWrapper = $('#waiting-term-table-wrapper');
    let url = $waitingTermWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#waiting-term-page').val()),
        pageSize: checkEmptyField($('#waiting-term-pageSize').val()),
        fileType: $('#waiting-term-fileType-filter').val(),
        statusCode: $('#waiting-term-status-filter').val(),
        responsibleUser: $('#responsibleUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#waiting-term-table-wrapper');
});




// $(document).on("click", "#imark-without-divided-checkbox", function (e) {
//     UIBlocker.block();
//     let url = $(this).data('url');
//     let callParams = {
//         page: checkEmptyField($('#international-marks-term-page').val()),
//         pageSize: checkEmptyField($('#international-marks-pageSize').val()),
//         gazno:checkEmptyField($('#international-marks-gazno').val()),
//         withoutDivided: $('#imark-without-divided-checkbox').is(":checked")
//     };
//
//     let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
//     updateHomePanelsWithAjaxAndUnblock(request, '#international-marks-table-wrapper');
// });




$(document).on("click", "#international-marks-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let wrapper = $('#international-marks-table-wrapper');
    let url = wrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#international-marks-term-page').val()),
        pageSize: checkEmptyField($('#international-marks-pageSize').val()),
        gazno:checkEmptyField($('#international-marks-gazno').val()),
        // withoutDivided: $('#imark-without-divided-checkbox').is(":checked")
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#international-marks-table-wrapper');
});




$(document).on("click", "#userdoc-waiting-terms-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $wrapper = $('#userdoc-waiting-terms-table-wrapper');
    let url = $wrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');
    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#userdoc-waiting-terms-page').val()),
        pageSize: checkEmptyField($('#userdoc-waiting-terms-pageSize').val()),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        userdocType: $('#userdoc-waiting-terms-filter').val(),
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#userdoc-waiting-terms-table-wrapper');
});

$(document).on("click", "#userdoc-correspondence-terms-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $wrapper = $('#userdoc-correspondence-terms-table-wrapper');
    let url = $wrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');
    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#userdoc-correspondence-terms-page').val()),
        pageSize: checkEmptyField($('#userdoc-correspondence-terms-pageSize').val()),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        userdocFilingNumber: $('#userdoc-correspondence-terms-filingNumber-filter-input').val(),
        objectFileTyp: $('#userdoc-correspondence-terms-objectFileTyp-filter').val(),
        statusCode: $('#userdoc-correspondence-terms-status-filter').val(),
        objectFileNbr: $('#userdoc-correspondence-terms-objectFileNbr-filter-input').val(),
        finished: $('#userdoc-correspondence-finished-checkbox').is(":checked"),
        inTerm: $('#userdoc-correspondence-in-term-checkbox').is(":checked"),
        userdocType: $('#userdoc-correspondence-terms-filter').val(),
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#userdoc-correspondence-terms-table-wrapper');
});



$(document).on("click", "[data-action='mark-payment-as-processed']", function (e) {
    let $button = $(this);
    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'));
    } else {
        UIBlocker.block();
        let url = $(this).data('url');
        let filingNumber =   $(this).attr('data-filing-number');
        let id =   $(this).attr('data-id');
        let sortOrder = $(this).attr('data-sort-order');
        let sortColumn = $(this).attr('data-sort-column');
        let callParams = {
            sortOrder: checkEmptyField(sortOrder),
            sortColumn: checkEmptyField(sortColumn),
            page: checkEmptyField($('#last-payments-page').val()),
            pageSize: checkEmptyField($('#last-payments-pageSize').val()),
            dateLastPaymentFrom: $('#last-payments-date-from-filter').val(),
            dateLastPaymentTo: $('#last-payments-date-to-filter').val()
        };
        let request = new CommonAjaxRequest(url.concat('?filingNumber='+filingNumber).concat("&id="+id), {requestData: callParams, useSessionId: false});
        updateHomePanelsWithAjaxAndUnblock(request, '#last-payments-table-wrapper');
    }
});


$(document).on("click", "#last-payments-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $lastPaymentsTableWrapper = $('#last-payments-table-wrapper');
    let url = $lastPaymentsTableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#last-payments-page').val()),
        pageSize: checkEmptyField($('#last-payments-pageSize').val()),
        dateLastPaymentFrom: $('#last-payments-date-from-filter').val(),
        dateLastPaymentTo: $('#last-payments-date-to-filter').val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#last-payments-table-wrapper');
});

$(document).on("click", "#search-last-payments-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#my-objects-page').val()),
        pageSize: checkEmptyField($('#my-objects-pageSize').val()),
        dateLastPaymentFrom: $('#last-payments-date-from-filter').val(),
        dateLastPaymentTo: $('#last-payments-date-to-filter').val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#last-payments-table-wrapper');
});

$(document).on("click", "#search-not-linked-payments-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        dateFrom: $('#not-linked-payments-date-from-filter').val(),
        dateTo: $('#not-linked-payments-date-to-filter').val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#not-linked-payments-table-wrapper');
});


$(document).on("click", "#search-my-objects-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#my-objects-page').val()),
        pageSize: checkEmptyField($('#my-objects-pageSize').val()),
        statusCode: $('#my-object-status-filter').val(),
        newlyAllocated: $('#my-object-newly-allocated-checkbox').is(":checked"),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        priorityRequest: $('#my-object-priority-request-checkbox').is(":checked"),
        bordero: $('#bordero-filter').val(),
        journalCode: $('#journal-code-filter').val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#my-objects-table-wrapper');
});

$(document).on("click", "#search-reception-userdoc-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#reception-userdoc-page').val()),
        pageSize: checkEmptyField($('#reception-userdoc-pageSize').val()),
        userdocType: $('#reception-userdoc-userdocType-filter').val(),
        objectFileTyp: $('#reception-userdoc-objectFileTyp-filter').val(),
        userdocFilingNumber: checkEmptyField($('#reception-userdoc-filingNumber-filter-input').val()),
        userdocFilingDateFrom: checkEmptyField($('#reception-userdoc-filingDateFrom-filter-input').val()),
        userdocFilingDateTo: checkEmptyField($('#reception-userdoc-filingDateTo-filter-input').val()),
        objectFileNbr: checkEmptyField($('#reception-userdoc-objectFileNbr-filter-input').val())
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#reception-userdoc-table-wrapper');
});

$(document).on("click", "#search-newly-allocated-userdoc-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#newly-allocated-userdoc-page').val()),
        pageSize: checkEmptyField($('#newly-allocated-userdoc-pageSize').val()),
        userdocType: $('#newly-allocated-userdoc-userdocType-filter').val(),
        objectFileTyp: $('#newly-allocated-userdoc-objectFileTyp-filter').val(),
        userdocFilingNumber: checkEmptyField($('#newly-allocated-userdoc-filingNumber-filter-input').val()),
        userdocFilingDateFrom: checkEmptyField($('#newly-allocated-userdoc-filingDateFrom-filter-input').val()),
        userdocFilingDateTo: checkEmptyField($('#newly-allocated-userdoc-filingDateTo-filter-input').val()),
        objectFileNbr: checkEmptyField($('#newly-allocated-userdoc-objectFileNbr-filter-input').val()),
        onlyActiveUsers: $('#responsible-user-indinactive-checkbox').is(":checked"),
        responsibleUser: $('#responsibleUser-filter').attr("data-id")
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#newly-allocated-userdoc-table-wrapper');
});


$(document).on("click", "#search-my-userdocs-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#my-userdocs-page').val()),
        pageSize: checkEmptyField($('#my-userdocs-pageSize').val()),
        userdocGroupName: checkEmptyField($('#my-userdocs-group-name').val()),
        userdocType: $('#my-userdocs-userdocType-filter').val(),
        statusCode: $('#my-userdocs-status-filter').val(),
        objectFileTyp: $('#my-userdocs-objectFileTyp-filter').val(),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        userdocFilingNumber: checkEmptyField($('#my-userdocs-filingNumber-filter-input').val()),
        userdocFilingDateFrom: checkEmptyField($('#my-userdocs-filingDateFrom-filter-input').val()),
        inProduction: $('#my-userdocs-in-production-checkbox').is(":checked"),
        newlyAllocated: $('#my-userdocs-newly-allocated-checkbox').is(":checked"),
        finished: $('#my-userdocs-finished-checkbox').is(":checked"),
        userdocFilingDateTo: checkEmptyField($('#my-userdocs-filingDateTo-filter-input').val()),
        objectFileNbr: checkEmptyField($('#my-userdocs-objectFileNbr-filter-input').val())
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#my-userdocs-table-wrapper');
});

$(document).on("click", "#search-userdoc-waiting-terms-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#userdoc-waiting-terms-page').val()),
        pageSize: checkEmptyField($('#userdoc-waiting-terms-pageSize').val()),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        userdocType: $('#userdoc-waiting-terms-filter').val(),
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#userdoc-waiting-terms-table-wrapper');
});

$(document).on("click", "#search-userdoc-correspondence-terms-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#userdoc-correspondence-terms-page').val()),
        pageSize: checkEmptyField($('#userdoc-correspondence-terms-pageSize').val()),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        userdocFilingNumber: $('#userdoc-correspondence-terms-filingNumber-filter-input').val(),
        objectFileTyp: $('#userdoc-correspondence-terms-objectFileTyp-filter').val(),
        statusCode: $('#userdoc-correspondence-terms-status-filter').val(),
        objectFileNbr: $('#userdoc-correspondence-terms-objectFileNbr-filter-input').val(),
        finished: $('#userdoc-correspondence-finished-checkbox').is(":checked"),
        inTerm: $('#userdoc-correspondence-in-term-checkbox').is(":checked"),
        userdocType: $('#userdoc-correspondence-terms-filter').val(),
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#userdoc-correspondence-terms-table-wrapper');
});

$(document).on("click", "#search-expired-term-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#expired-term-page').val()),
        pageSize: checkEmptyField($('#expired-term-pageSize').val()),
        actionType: $('#expired-term-action-types-filter').val(),
        fileType: $('#expired-term-fileType-filter').val(),
        statusCode: $('#expired-term-status-filter').val(),
        responsibleUser: $('#responsibleUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#expired-term-table-wrapper');
});



$(document).on("click", "#last-action-table-wrapper [data-action='table-sort']", function (e) {
    UIBlocker.block();
    let $lastActionTableWrapper = $('#last-action-table-wrapper');
    let url = $lastActionTableWrapper.data('url');
    let sortColumn = $(this).data('sort');
    let sortOrder = $(this).data('order');

    let callParams = {
        sortOrder: checkEmptyField(sortOrder),
        sortColumn: checkEmptyField(sortColumn),
        page: checkEmptyField($('#last-action-page').val()),
        pageSize: checkEmptyField($('#last-action-pageSize').val()),
        fileType: $('#last-action-fileType-filter').val(),
        actionTypeKind: $('#action-type-kind-filter').val(),
        userdocType: $('#last-action-userdocType-filter').val(),
        userdocFilingNumber: checkEmptyField($('#last-action-filingNumber-filter-input').val()),
        objectFileNbr: checkEmptyField($('#last-action-objectFileNbr-filter-input').val()),
        actionDateFrom: checkEmptyField($('#last-action-actionDateFrom-filter-input').val()),
        actionDateTo: checkEmptyField($('#last-action-actionDateTo-filter-input').val()),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        captureUser: $('#captureUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url.concat($('#table-count').val()), {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#last-action-table-wrapper');
});




$(document).on("click", "#search-last-action-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');
    let callParams = {
        page: checkEmptyField($('#last-action-page').val()),
        pageSize: checkEmptyField($('#last-action-pageSize').val()),
        fileType: $('#last-action-fileType-filter').val(),
        actionTypeKind: $('#action-type-kind-filter').val(),
        userdocType: $('#last-action-userdocType-filter').val(),
        userdocFilingNumber: checkEmptyField($('#last-action-filingNumber-filter-input').val()),
        objectFileNbr: checkEmptyField($('#last-action-objectFileNbr-filter-input').val()),
        actionDateFrom: checkEmptyField($('#last-action-actionDateFrom-filter-input').val()),
        actionDateTo: checkEmptyField($('#last-action-actionDateTo-filter-input').val()),
        responsibleUser: $('#responsibleUser-filter').attr("data-id"),
        captureUser: $('#captureUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#last-action-table-wrapper');
});


$(document).on("click", "#search-waiting-term-btn", function (e) {
    UIBlocker.block();
    let url = $(this).data('url');

    let callParams = {
        page: checkEmptyField($('#waiting-term-page').val()),
        pageSize: checkEmptyField($('#waiting-term-pageSize').val()),
        fileType: $('#waiting-term-fileType-filter').val(),
        statusCode: $('#waiting-term-status-filter').val(),
        responsibleUser: $('#responsibleUser-filter').attr("data-id")
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    updateHomePanelsWithAjaxAndUnblock(request, '#waiting-term-table-wrapper');
});


function updateHomePanelsWithAjaxAndUnblock(request, updateComponentSelector) {
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: updateComponentSelector,
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
    setSessionStorageHomePanelProperties(request.requestData);
}

$(document).on("click", "#clear-waiting-term-btn,#clear-last-action-btn,#clear-my-objects-btn,#clear-my-userdocs-btn,#clear-reception-userdoc-btn,#clear-newly-allocated-userdoc-btn,#clear-expired-term-btn,#clear-userdoc-waiting-terms-btn,#clear-userdoc-correspondence-terms-btn,#clear-last-payments-btn", function (e) {
    UIBlocker.block();
    deleteHomePanelSessionStorageData();
});



$(document).on("change",'#responsible-user-indinactive-checkbox', function (e) {
    if ($(this).is(':checked')){
        $("#responsibleUser-filter").attr("data-flag",true);
    }else{
        $("#responsibleUser-filter").attr("data-flag",false);
    }
});