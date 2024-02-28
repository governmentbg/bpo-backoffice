let userdocContainer = '#' + PanelContainer.Userdoc;

$(function () {
    if ($(userdocContainer).length > 0) {
        setLabelToEditedPanels(userdocContainer);
        openPanelsWithValidationErrors($('#userdoc-validation-errors'), userdocContainer);
        ScrollUtils.panelScroll(userdocContainer);
        initCourtsAutocomplete();
    }
});

$(document).on("click", userdocContainer + " a[data-action='save']", function (e) {
    let panel = $(this).data('panel');
    let url = $(this).data('url');

    let data = getFormValuesAsJson($(userdocContainer + ' #panel-' + panel));
    data.userdocPanelName = panel;

    if (panel === UserdocPanel.UserdocTypeData) {
        let panels = getEditedPanelIds(PanelContainer.Userdoc);
        let callParams = {
            isCancel: false,
            editedPanels: panels,
            userdocType: data.userdocType,
            sessionIdentifier: $('#session-object-identifier').val()
        };
        RequestUtils.post(url, callParams);
    } else {
        let callParams = {
            isCancel: false,
            data: JSON.stringify(data)
        };

        let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: userdocContainer + " #panel-body-" + panel,
            executeOnValid: new FuncCall(handleUserdocPanelInitialization, [panel, false, null, null])
        });

        commonAjaxCall(request, responseAction);
    }
});


function handleUserdocPanelInitialization(panel, showHidden, showHiddenWrapper, editModeElements) {
    executeCommonInitialization({
        initializeFormElementsWrapper: userdocContainer + " #panel-" + panel,
        panelToInitialize: userdocContainer + " #panel-" + panel,
        showHiddenElements: showHidden,
        hiddenElementsWrapper: showHiddenWrapper,
        inputsForEditWrapper: editModeElements
    });
    if(panel === UserdocPanel.UserdocMainData){
        initPortalUsersAutocomplete();
    }
    initCourtsAutocomplete();
}

$(document).on("click", userdocContainer + " a[data-action='cancel']", function (e) {
    let url = $(this).data('url');
    if (url) {
        let panel = $(this).data('panel');
        let callParams = {isCancel: true};
        let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: userdocContainer + " #panel-body-" + panel,
            executeOnValid: new FuncCall(handleUserdocPanelInitialization, [panel, false, null, null])
        });

        commonAjaxCall(request, responseAction);
    }
});

$("body").on("click", userdocContainer + " a[data-action='edit']", function (e) {
    let panel = $(this).data('panel');
    let url = $(this).data('url');
    if (null != url) {
        let callParams = {
            panel: panel
        };
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        commonAjaxCall(request, null);
    }
});

$(document).on("click", "[data-action='open-change-position-search-modal']", function (e) {
    let button = $(this);
    let url = button.data('url');

    let callParams = {};
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#change-userdoc-position-search-modal-wrapper",
        executeOnValid: new FuncCall(handleChangePositionSearchModal, [])
    });

    function handleChangePositionSearchModal() {
        commonOpenModalFormInit("#userdoc-position-search-modal", true)
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("change", "input[type=radio][name='change-userdoc-radio']", function (e) {
    let $label = $('label[for=\'userdoc-changePositionSearch\']');
    let text = $(this).next().text();
    $label.text(text);
});

$(document).on("click", "[data-action='clear-change-userdoc-position-search']", function (e) {
    $('#userdoc-changePositionSearch').val('');
    $('#objects-result-wrapper').empty();
    $("label[for='userdoc-changePositionSearch']").removeClass("active");
    handleExecuteOnValidUserdocPositionSearchModal();
});

$(document).on("click", "[data-action='submit-change-userdoc-position-search']", function (e) {
    let searchText = $('#userdoc-changePositionSearch').val().trim();
    let searchType = $('input[type=radio][name=\'change-userdoc-radio\']:checked').val();

    let button = $(this);
    let url = button.data('url');
    let fileType = $('#change-userdoc-search-fileType').val().trim();

    let callParams = {
        fileType: fileType,
        searchText: searchText,
        searchType: searchType,
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#objects-result-wrapper",
        executeOnValid: new FuncCall(handleExecuteOnValidUserdocPositionSearchModal, [])
    });
    commonAjaxCall(request, responseAction);

});

function handleExecuteOnValidUserdocPositionSearchModal() {
    let minModalHeightForSelect = 475;
    let $modal = $('#userdoc-position-search-modal');
    let currentModalHeight = $modal.height();
    if (minModalHeightForSelect >= currentModalHeight) {
        $modal.css("overflow", 'visible');
    } else {
        $modal.css("overflow", '');
    }
}

$(document).on("click", "[data-action='open-change-position-modal']", function (e) {
    let button = $(this);
    let url = button.data('url');
    let process = button.data('process');

    let callParams = {
        process: process,
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#change-userdoc-position-modal-wrapper",
        executeOnValid: new FuncCall(handleChangeUserdocPositionOpenModal, ["change-userdoc-position-process-tree"])
    });

    function handleChangeUserdocPositionOpenModal(id) {
        commonOpenModalFormInit("#change-userdoc-position-modal", true)
        activateTree(document.getElementById(id));

        let $currentUserdocLi = $("#change-userdoc-position-process-tree .current-userdoc");
        if ($currentUserdocLi.length > 0) {
            openClosestNode($currentUserdocLi);

            let $find = $("#change-userdoc-position-process-tree .current-userdoc").closest('ul').find('li');
            if ($find.length === 1) {
                $currentUserdocLi.closest('li.has-children').click();
                $currentUserdocLi.closest('li.has-children').removeClass('has-children');
            }
        } else {
            $("#" + id + " li").first('li.has-children[aria-hidden="true"]').click();//Open first li
        }
    }

    function openClosestNode(startPointSelected) {
        let closest = startPointSelected.closest('li.has-children');
        closest.click();

        let closestParent = closest.closest('li.has-children[aria-hidden="true"]');
        if (closestParent.length > 0) {
            openClosestNode(closestParent)
        }
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("change", "input[type=radio][name=\'selected-userdoc-parent\']", function (e) {
    $('#change-userdoc-position-modal-validation-msg').addClass('none');
    $('#validation-errors-wrapper').empty();
});

$(document).on("click", "[data-action='change-userdoc-position-submit']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');
    let parentProcessId = $('input[type=radio][name=\'selected-userdoc-parent\']:checked').val();
    if (parentProcessId === undefined) {
        $('#change-userdoc-position-modal-validation-msg').removeClass('none');
        return;
    }

    let callParams = {
        parentProcessId: parentProcessId
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        updateContainerOnInvalid: "#validation-errors-wrapper",
        updateContainerOnValid: '#userdoc-parent-wrapper',
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid() {
        commonCloseModalFormInit("#change-userdoc-position-modal", true, userdocContainer + " #userdoc-parent-wrapper");
        commonCloseModalFormInit("#userdoc-position-search-modal", false);
    }

    commonAjaxCall(request, responseAction);
});

function initCourtsAutocomplete() {
    let $autocomplete = $("input[data-autocomplete='courts']");
    if ($autocomplete.length > 0) {
        let inputId = $autocomplete.attr('id');
        let url = $autocomplete.data('url');
        $autocomplete.autocomplete({
            delay: 500,
            minLength: 1,
            source: function (request, response) {
                let ajaxRequest = new CommonAjaxRequest(url, {
                    requestData: {name: request.term},
                    method: "GET",
                    dataType: "json",
                    useSessionId: false
                });
                let responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

                commonAjaxCall(ajaxRequest, responseAction);
            },
            select: function (event, ui) {
                $autocomplete.val(ui.item.name);
                return false;
            },
            change: function (event, ui) {
                if (ui.item == null) {
                    $(this).val("");
                    $autocomplete.val("");
                    $("label[for='" + inputId + "']").removeClass("active");
                }
            }
        }).autocomplete("instance")._renderItem = function (ul, item) {
            return $("<li></li>")
                .data("item.autocomplete", item)
                .append("<a><span><span>" + item.name + "</span></span></a>").appendTo(ul);
        };
    }
}

$(document).on("change", "input[type=radio].userdoc-nice-class-radio", function (e) {
    let wrapper = $(this).closest(".panel").find(".userdoc-nice-classes-table-wrapper");
    resetNiceClasses($(this), wrapper)
});


$(document).on("change", "input[type=radio].userdoc-single-design-radio", function (e) {
    let isAllSingleDesignsIncluded = $(this).val();
    let wrapper = $(this).closest(".panel").find(".userdoc-single-designs-table-wrapper");
    let restoreOriginalWrapper = $(this).closest(".panel").find(".restore-original-single-designs-wrapper");
    let addClassWrapper = $(this).closest(".panel").find(".add-design-from-original-wrapper");
    let deleteAllWrapper = $(this).closest(".panel").find(".delete-all-single-designs-wrapper");
    let url = $(wrapper).data('url');
    let tableId = "#"+$(wrapper).find(".userdoc-single-design-table").attr("id");

    let callParams = {
        isAllSingleDesignsIncluded: isAllSingleDesignsIncluded
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
        if (isAllSingleDesignsIncluded === 'true') {
            $(restoreOriginalWrapper).addClass('none');
            $(addClassWrapper).addClass('none');
            $(deleteAllWrapper).addClass('none');
        } else {
            $(restoreOriginalWrapper).removeClass('none');
            $(addClassWrapper).removeClass('none');
            $(deleteAllWrapper).removeClass('none');
        }
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='restore-nice-classes']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');
    let listType = button.data('listtype');

    let callParams = {};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#nice-classes-table-'+listType,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: '#nice-classes-table-'+listType
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='restore-single-designs']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');

    let callParams = {};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#userdoc-single-design-table',
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: '#userdoc-single-design-table'
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='delete-userdoc-single-design']", function (e) {
    let url = $(this).data('url');
    let filingNumber = $(this).data('id');
    let callParams = {
        filingNumber: filingNumber
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#userdoc-single-design-table',
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: '#userdoc-single-design-table'
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='select-from-class']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');
    let niceNbr = button.data('id');
    let callParams = {niceClassNbr : niceNbr};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#nice-class-terms-choose-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#nice-class-terms-choose-modal", true]),
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", ".chooseAllTerms", function(e){
   let selected = $(this).is(":checked");
   if(selected){
       $(this).closest("#nice-class-terms-choose-modal").find(".term-choice").prop("checked", true);
   } else {
       $(this).closest("#nice-class-terms-choose-modal").find(".term-choice").prop("checked", false);
   }
});

$(document).on("click", ".save-terms-choice", function (e) {
    e.preventDefault();
    let button = $(this);
    let chosen = $("#nice-class-terms-choose-modal .term-choice:checked");

    if(chosen.length ==0){
        $(".errorNoTerms").show();
        return;
    }

    let url = button.data('url');
    let niceNbr = button.data('id');
    let action = button.data('action');
    let listType = button.data('listtype');
    let builtText = "";

    for(var i=0; i< chosen.length; i++){
        builtText = builtText+$(chosen[i]).data("val");
        if(i < chosen.length-1){
            builtText = builtText+";";
        }
    }

    let callParams = {
        niceClassNbr : niceNbr,
        action: action,
        termsText:builtText
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#nice-classes-table-'+listType,
        executeOnValid: new FuncCall(clearAndCloseNiceTermChooseModal, [listType])
    });

    function clearAndCloseNiceTermChooseModal(listType) {
        commonCloseModalFormInit("#nice-class-terms-choose-modal", true, "#nice-classes-table-"+listType);
        executeCommonInitialization({
            inputsForEditWrapper: "#nice-classes-table-"+listType
        });
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='select-add-original-class']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');

    let request = new CommonAjaxRequest(url, {requestData: {}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#nice-class-class-choose-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#nice-class-class-choose-modal", true]),
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='translate-nice-classes']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');
    let id = $(this).data("id");
    let requestData = {};
    if(id){
        requestData.niceClassNbr = id;
    }

    let request = new CommonAjaxRequest(url, {requestData: requestData, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#nice-class-class-translate-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#translated-nice-classes-modal", true]),
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='select-add-original-design']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');

    let request = new CommonAjaxRequest(url, {requestData: {}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#userdoc-single-design-choose-modal-wrapper",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#single-design-choose-modal", true]),
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", ".save-class-choice", function (e) {
    e.preventDefault();
    let button = $(this);
    let chosen = $("#nice-class-choice").val();
    let url = button.data('url');
    let listType = button.data('listtype');
    let callParams = {
        niceClassNbr : chosen
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#nice-classes-table-'+listType,
        executeOnValid: new FuncCall(clearAndCloseNiceClassChooseModal, [listType])
    });

    function clearAndCloseNiceClassChooseModal(listType) {
        commonCloseModalFormInit("#nice-class-class-choose-modal", true, "#nice-classes-table-"+listType);
        executeCommonInitialization({
            inputsForEditWrapper: "#nice-classes-table-"+listType
        });
    }

    commonAjaxCall(request, responseAction);
});

$(document).on("click", ".save-design-choice", function (e) {
    e.preventDefault();
    let button = $(this);
    let chosen = $("#userdoc-single-design-choice").val();
    let url = button.data('url');
    let callParams = {
        filingNumber : chosen
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#userdoc-single-design-table',
        executeOnValid: new FuncCall(clearAndCloseNiceClassChooseModal, [])
    });

    function clearAndCloseNiceClassChooseModal() {
        commonCloseModalFormInit("#single-design-choose-modal", true, "#userdoc-single-design-table");
        executeCommonInitialization({
            inputsForEditWrapper: '#userdoc-single-design-table'
        });
    }

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='delete-all-nice-classes']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');
    let listType = button.data('listtype');

    let callParams = {};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#nice-classes-table-'+listType,
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: '#nice-classes-table-'+listType
        }])
    });

    commonAjaxCall(request, responseAction);
});

$(document).on("click", "[data-action='delete-all-single-designs']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');

    let callParams = {};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#userdoc-single-design-table',
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: '#userdoc-single-design-table'
        }])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("change", "input[type=radio].license-territory-radio", function (e) {
    let isForWholeTerritory = $(this).val();
    let $licenseTerritoryWrapper = $('#license-territory-inputs-wrapper');
    if (isForWholeTerritory === 'true') {
        $licenseTerritoryWrapper.addClass('none');
        $licenseTerritoryWrapper.find('.territory-input').val('');
        checkForActiveLabels($licenseTerritoryWrapper)
    } else {
        $licenseTerritoryWrapper.removeClass('none');
    }
});

$(document).on("change", "input[type=radio].license-sublicense-radio", function (e) {
    let isSublicense = $(this).val();
    let $sublicenseInputsWrapper = $('#license-sublicense-inputs-wrapper');
    if (isSublicense === 'false') {
        $sublicenseInputsWrapper.addClass('none');
        $sublicenseInputsWrapper.find('input').val('');
        checkForActiveLabels($sublicenseInputsWrapper)
    } else {
        $sublicenseInputsWrapper.removeClass('none');
    }
});

$(document).on("change", "input[type=radio].license-expiration-date-radio", function (e) {
    let isCustomExpirationDate = $(this).val();
    let $expirationDateInputsWrapper = $('#license-expiration-date-inputs-wrapper');
    if (isCustomExpirationDate === 'false') {
        $expirationDateInputsWrapper.addClass('none');
        $expirationDateInputsWrapper.find('input').val('');
        checkForActiveLabels($expirationDateInputsWrapper)
    } else {
        $expirationDateInputsWrapper.removeClass('none');
    }
});


$(document).on("click", "[data-action='calculate-new-expiration-date']", function (e) {
    e.preventDefault();
    let button = $(this);
    let url = button.data('url');

    let callParams = {};

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(handleExecuteOnValid, [])
    });

    function handleExecuteOnValid(data) {
        if (data.length > 0) {
            let $renewalExpirationDateWrapper = $('#renewal-expiration-date-wrapper');
            $renewalExpirationDateWrapper.find('input').val(data);
            checkForActiveLabels($renewalExpirationDateWrapper);
        }
    }

    commonAjaxCall(request, responseAction);
});