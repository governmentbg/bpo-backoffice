$(document).on("click", "#save-nice-classes-btn", function (e) {
    let url = $(this).data('url');
    let listType = $(this).data("listtype");
    let callParams = {
        niceDescription: $("#nice-class-description-textarea").val(),
        classNbr: $("#nice-class-class-input").val(),
        heading: $("#nice-class-header-input").is(":checked"),
        alpha : $("#nice-class-alpha-input").is(":checked")
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#nice-class-modal-wrapper",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: "#nice-classes-table-"+listType,
        executeOnValid: new FuncCall(clearAndCloseNiceModal, [])
    });

    function clearAndCloseNiceModal() {
        commonCloseModalFormInit("#nice-class-modal", true, "#nice-classes-table-"+listType);
        executeCommonInitialization({
            inputsForEditWrapper: "#nice-classes-table-"+listType
        });
    }

    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#nice-class-modal", false);
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("change", ".nice-declaration-check", function (e) {
    let classNum = $(this).data("num");
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let url = $(this).data('url');
    let callParams = {
        niceClassNbr: classNum,
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: tableId
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='delete-nice-class']", function (e) {
    let url = $(this).data('url');
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let callParams = {niceClassNbr: $(this).data('id')};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: tableId
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='verify-nice-class']", function (e) {

    let isEdit = $("[data-action='delete-nice-class']").is(":visible");
    let url = $(this).data('url');
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let isAllNiceClassesIncluded = $("input[type=radio].userdoc-nice-class-radio:checked").val();
    let callParams = {
        niceClassNbr: $(this).data("id"),
        isEdit: isEdit,
        isAllNiceClassesIncluded: isAllNiceClassesIncluded
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            showHiddenElements: $(tableId+" [data-action='delete-nice-class']").is(":visible"),
            hiddenElementsWrapper: tableId,
            inputsForEditWrapper: isEdit ? tableId : null
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='fetch-class-terms']", function (e) {
    let url = $(this).data('url');
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let callParams = {
        niceClassNbr: $(this).data("id"),
        fetch: $(this).data("fetch")
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        validationFailedExpression: "data.indexOf('fetch-terms-error') != -1",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            showHiddenElements: true,
            hiddenElementsWrapper: tableId,
            inputsForEditWrapper: tableId
        }]),
        executeOnInvalid: new FuncCall(openInfoModalWithFetchError, [])
    });

    function openInfoModalWithFetchError(data) {
        let errTag = $(data).find(".fetch-terms-error");
        let message = $(errTag).data("message");
        Confirmation.openInfoModal(message);
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='delete-nice-class-term']", function (e) {
    let url = $(this).data('url');
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let callParams = {niceClassNbr: $(this).data('id'), termText: $(this).data("text"), termIndex: $(this).data("idx")};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: tableId
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='edit-nice-class-term']", function (e) {
    let url = $(this).data('url');
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let callParams = {niceClassNbr: $(this).data('id'), termText: $(this).data("text"), termIndex: $(this).data("idx")};
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: tableId
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", ".single-selectable-nice", function (e) {
    let target = $(this);
    if (target.is(":checked")) {
        $("#nice-class-description-textarea").val("");
        $("#nice-class-description-textarea").attr("disabled", "disabled");
        $("#nice-class-description-textarea-wrapper").addClass('none');
        $(".single-selectable-nice").each(function(index, el){
            if($(el).attr("id") != target.attr("id") && $(el).is(":checked")) {
                $(el).trigger("click");
            }
        });
    } else {
        if($(".single-selectable-nice:checked").length ==0) {
            $("#nice-class-description-textarea").removeAttr("disabled");
            $("#nice-class-description-textarea-wrapper").removeClass('none');
        }
    }
});


$(document).on("click", "[data-action='open-nice-class-modal']", function (e) {
    let url = $(this).data('url');
    let callParams = {};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#nice-class-modal-wrapper",
        executeOnValid: new FuncCall(handleExecuteOnValid, []),
    });

    function handleExecuteOnValid() {
        commonOpenModalFormInit("#nice-class-modal", false);
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("click","a[data-action='verify-all-nice-classes']", function (e) {
    let isEdit = $(this).closest(".panel").find("a[data-action='save']").is(":visible");
    let url = $(this).data('url');
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let isAllNiceClassesIncluded = $("input[type=radio].userdoc-nice-class-radio:checked").val();

    let callParams = {
        isEdit: isEdit,
        isAllNiceClassesIncluded: isAllNiceClassesIncluded
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            showHiddenElements: isEdit,
            hiddenElementsWrapper: tableId,
            inputsForEditWrapper: isEdit ? tableId : null
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click","a[data-action='edit-terms-text']", function (e) {
    let isEdit = $(this).closest(".panel").find("a[data-action='save']").is(":visible");
    let url = $(this).data('url');
    let isAllNiceClassesIncluded = $("input[type=radio].userdoc-nice-class-radio:checked").val();
    let callParams = {
        isEdit: isEdit,
        niceClassNbr: $(this).data("id"),
        isAllNiceClassesIncluded: isAllNiceClassesIncluded
    };

    let request = new CommonAjaxRequest(url, {method: "GET", requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#nice-class-terms-text-modal-wrapper",
        executeOnValid: new FuncCall(termsTextModalInit, ["#nice-class-terms-text-modal", true]),
    });

    function termsTextModalInit(modal, isEdit){
        commonOpenModalFormInit(modal, isEdit);
        if($(".hiddendiv").length >0 && $(".hiddendiv").width()> $("#nice-class-terms-description-textarea").width()){
            $("#nice-class-terms-description-textarea").width($(".hiddendiv").width());
        }

    }

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='edit-terms-tolower']", function (e) {
    let url = $(this).data('url');
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let callParams = {niceClassNbr: $(this).data('id')};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: tableId
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click","#save-terms-text-btn", function (e) {
    let url = $(this).data('url');
    let listType = $(this).data('listtype');
    let callParams = {
        termsText: $("#nice-class-terms-description-textarea").val(),
        niceClassNbr: $("#nice-class-terms-class-input").val()
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#nice-class-terms-text-modal-wrapper",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(commonOpenModalFormInit, ["#nice-class-terms-text-modal", true]),
        updateContainerOnValid: "#nice-classes-table-"+listType,
        executeOnValid: new FuncCall(clearAndCloseNiceTermTextModal, [])
    });

    function clearAndCloseNiceTermTextModal() {
        commonCloseModalFormInit("#nice-class-terms-text-modal", true, "#nice-classes-table-"+listType);
        executeCommonInitialization({
            inputsForEditWrapper: "#nice-classes-table-"+listType
        });
    }
    commonAjaxCall(request, responseAction);
});

$(document).on("click",".term-editable", function (e) {
    let term = $(this);
    let tableId = "#"+$(this).closest(".nice-classes-table").attr("id");
    let isEdit = term.closest(".panel").find("a[data-action='save']").is(":visible");
    if(isEdit == false || term.closest("td").find(".nice-declaration-check").is(":checked")){
        return false;
    } else {
        if(term.hasClass('editing-term')) {
            return false;
        }
        term.attr("contenteditable", "true");
        term.data("oldcontent", term.text().trim());
        term.addClass("editing-term");
        term.focus();
        term.unbind('blur');
        term.unbind('keyup');

        term.bind("keyup", function(keyEvent){
            if(keyEvent.keyCode === 13){
                keyEvent.preventDefault();
                term.trigger('blur');
                return false;
            }
            return true;
        });

        term.bind("blur", function(){
            if(term.data("oldcontent") != term.text().trim()){
                let url = term.data("url");
                let callParams = {
                    niceClassNbr: term.closest("tr").find(".td-nice-class-nbr").text(),
                    termIndex: term.data("idx"),
                    newTermText: term.text().trim(),
                    isEdit: isEdit
                };

                let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
                let responseAction = new CommonAjaxResponseAction({
                    updateContainerOnValid: tableId,
                    executeOnValid: new FuncCall(executeCommonInitialization, [{
                        inputsForEditWrapper: tableId
                    }])
                });

                commonAjaxCall(request, responseAction);
            } else {
                term.removeAttr("contenteditable");
                term.removeAttr("data-oldcontent");
                term.removeClass("editing-term");
            }
        });
    }
});

$(document).on("click","a[data-action='hide-table']", function (e) {
    let target = $(this).parent().siblings("table").find("tbody");
    $(target).hide();
    $(this).siblings("a[data-action='show-table']").removeClass("none");
    $(this).addClass("none");
});


$(document).on("click","a[data-action='show-table']", function (e) {
    let target = $(this).parent().siblings("table").find("tbody");
    $(target).show();
    $(this).siblings("a[data-action='hide-table']").removeClass("none");
    $(this).addClass("none");
});

function resetNiceClasses(jQueryRadio, wrapper ) {
    let isAllNiceClassesIncluded = jQueryRadio.val();
    let restoreOriginalWrapper = jQueryRadio.closest(".panel").find(".restore-original-nice-classes-wrapper");
    let restoreRequestedWrapper = jQueryRadio.closest(".panel").find(".restore-requested-nice-classes-wrapper");
    let addClassWrapper = jQueryRadio.closest(".panel").find(".add-class-from-original-wrapper");
    let deleteAllWrapper = jQueryRadio.closest(".panel").find(".delete-all-nice-classes-wrapper");
    let translateAllWrapper = jQueryRadio.closest(".panel").find(".translate-all-nice-classes-wrapper");
    let url = $(wrapper).data('url');
    let tableId = "#"+$(wrapper).find(".nice-classes-table").attr("id");

    let callParams = {
        isAllNiceClassesIncluded: isAllNiceClassesIncluded
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: tableId,
        executeOnValid: new FuncCall(handleExecuteOnValid, [tableId])
    });

    function handleExecuteOnValid(tableId) {
        executeCommonInitialization({
            inputsForEditWrapper: tableId
        });
        if (isAllNiceClassesIncluded === 'true') {
            $(restoreOriginalWrapper).addClass('none');
            $(restoreRequestedWrapper).addClass('none');
            $(addClassWrapper).addClass('none');
            $(deleteAllWrapper).addClass('none');
            $(translateAllWrapper).addClass('none');
        } else {
            $(restoreOriginalWrapper).removeClass('none');
            $(restoreRequestedWrapper).removeClass('none');
            $(addClassWrapper).removeClass('none');
            $(deleteAllWrapper).removeClass('none');
            $(translateAllWrapper).removeClass('none');
        }
    }

    commonAjaxCall(request, responseAction);
}