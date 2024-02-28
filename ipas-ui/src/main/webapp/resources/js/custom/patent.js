let patentContainer = '#' + PanelContainer.Patent;

$(function () {
    if ($(patentContainer).length > 0) {
        setLabelToEditedPanels(patentContainer);
        openPanelsWithValidationErrors($('#patent-validation-errors'), patentContainer);
        ScrollUtils.panelScroll(patentContainer);
        loadPatentPublicationsPanel();
    }
});

function loadPatentPublicationsPanel() {
    let publicationPanel = $('#panel-' + PatentPanel.Publication);
    let url = publicationPanel.data('url');
    if (url) {
        let callParams = {};
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: "#panel-" + PatentPanel.Publication,
            executeOnValid: new FuncCall(executeCommonInitialization, [{
                initializeFormElementsWrapper: "#panel-" + PatentPanel.Publication,
            }])
        });
        commonAjaxCall(request, responseAction);
    }
}

$("body").on("click", patentContainer + " a[data-action='edit']", function (e) {
    let blockUI = false;
    let panel = $(this).data('panel');
    let url = $(this).data('url');

    if (panel === PatentPanel.DesignDrawingsData || (panel === PatentPanel.PublishedDrawingsData)) {
        blockUI = true;
    }

    setBookmarkButtonsMode(panel,true);

    if (null != url) {
        let callParams = {
            panel: panel
        };
        let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: blockUI});
        let response = new CommonAjaxResponseAction({
            executeOnValid: new FuncCall(hideViewVisibleOnlyElements, [".has-view-visible-only"])
        })
        commonAjaxCall(request, response);
    }
    if (panel === PatentPanel.SpcIdentityData) {
        initMainPatentHref(true);
    }
});

function hideViewVisibleOnlyElements(wrapperSelector) {
    $(wrapperSelector + " .view-visible-only").hide();
}

$(document).on("click", patentContainer + " a[data-action='save']", function (e) {
    let blockUI = false;
    let panel = $(this).data('panel');
    let url = $(this).data('url');
    let data = null;
    if (panel === PatentPanel.IdentityData) {
        data = getFormValuesAsJson($(patentContainer + ' #panel-' + PatentPanel.IdentityData));
    }
    if (panel === PatentPanel.MainData) {
        data = getFormValuesAsJson($(patentContainer + ' #panel-' + PatentPanel.MainData));
    }
    if (panel === PatentPanel.PlantMainData) {
        data = getFormValuesAsJson($(patentContainer + ' #panel-' + PatentPanel.PlantMainData));
    }

    if (panel === PatentPanel.SpcMainData) {
        data = getFormValuesAsJson($(patentContainer + ' #panel-' + PatentPanel.SpcMainData));
    }
    if (panel === PatentPanel.SpcIdentityData) {
        data = getFormValuesAsJson($(patentContainer + ' #panel-' + PatentPanel.SpcIdentityData));
    }

    if (panel === PatentPanel.DesignDrawingsData) {
        data = getSingleDesignsDataAsJSON($(patentContainer + ' #panel-' + PatentPanel.DesignDrawingsData));
        blockUI = true;
    }


    if (panel === PatentPanel.RightsData) {
        let dataPct = getFormValuesAsJson($("#pct-subpanel"));
        let dataExhibition = getFormValuesAsJson($("#exhibition-subpanel"));
        let dataTransformation = getFormValuesAsJson($("#patent-transformation-subpanel"));
        let hasPriority = $("#object-priority-hasPriority").is(":checked");
        let hasDivisional = $("#divisional-app-hasDivisionalData").is(":checked");
        let dataDivisional = hasDivisional ? getFormValuesAsJson($("#divisional-application-subpanel")) : null;
        data = {
            divisionalData: dataDivisional,
            exhibitionData: dataExhibition,
            dataPct: dataPct,
            transformationData: dataTransformation,
            hasPriority: hasPriority,
        };
        if ($("#um-parallel-data-subpanel").length) {
            data.utilityModelParallelData = getFormValuesAsJson($("#um-parallel-data-subpanel"));
        }
    }
    if (panel === PatentPanel.IpcData) {
        data = getFormValuesAsJson($(patentContainer + ' #panel-' + PatentPanel.IpcData));
    }
    if (panel === PatentPanel.PublishedDrawingsData) {
        data = getFormValuesAsJson($(patentContainer + ' #panel-' + PatentPanel.PublishedDrawingsData));
        blockUI = true;
    }

    let callParams = {
        isCancel: false,
        data: JSON.stringify(data)
    };

    function isPageForReloadOnSave() {
        // check 1 - if MAIN view page should be reload on spc identity panel SAVE
        if ((panel === PatentPanel.SpcIdentityData && $('#spc-isPageForReload').val() == 'true') ||

            // check 2 - if MAIN view page should be reload on international transformation/parallel um panel SAVE
            (panel === PatentPanel.RightsData && ($("#patent-transformation-hasTransformationData").is(":checked") || $("#patent-parallel-data-hasParallelData").is(":checked")) && $("#patent-object-relationshipExtended-applicationType").length > 0
                && $("#patent-object-relationshipExtended-applicationType").val() == 'WO' && $('#int-relationshipExtended-filingNumber').val().length !== 0 && $('#int-relationshipExtended-filingDate').val().length !== 0
                && ($('#int-relationshipExtended-filingNumber').val() !== $('#int-relationshipExtended-filingNumber-original').val() || $('#int-relationshipExtended-filingDate').val() !== $('#int-relationshipExtended-filingDate-original').val())) ||

            // check 3 - if MAIN view page should be reload on pct panel SAVE
            (panel === PatentPanel.RightsData && $("#pct-hasPctData").is(":checked") && $('#patent-pctApplicationData-pctApplicationId').val().length !== 0 && $('#patent-pctApplicationData-pctApplicationDate').val().length !== 0
                && ($('#patent-pctApplicationData-pctApplicationId').val() !== $('#patent-pctApplicationData-pctApplicationId-original').val() || $('#patent-pctApplicationData-pctApplicationDate').val() !== $('#patent-pctApplicationData-pctApplicationDate-original').val())) ||

            // check 4 - if MAIN view page should be reload on divisional panel SAVE
            (panel === PatentPanel.RightsData && $("#divisional-app-hasDivisionalData").is(":checked") && $("#object-divisional-app-relationshipChanged").val() == 'true') ||

            // check 5 - if MAIN view page should be reload on NATIONAL/EP transformation/parallel panel SAVE
            (panel === PatentPanel.RightsData && ($("#patent-transformation-hasTransformationData").is(":checked") || $("#patent-parallel-data-hasParallelData").is(":checked")) && $("#patent-object-relationshipExtended-applicationType").length > 0
                && ($("#patent-object-relationshipExtended-applicationType").val() == 'EP' || $("#patent-object-relationshipExtended-applicationType").val() == 'BG')
                && ($("#ep-relationship-ext-changed").val() == 'true' || $("#object-national-patent-relationship-relationshipChanged").val() == 'true'))) {

            return true
        }
        return false;
    }

    if (isPageForReloadOnSave()) {
        callParams.sessionIdentifier = $('#session-object-identifier').val();
        callParams.editedPanels = getEditedPanelIds(PanelContainer.Patent);
        RequestUtils.post(url, callParams);
    } else {
        let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: blockUI});
        let responseAction = new CommonAjaxResponseAction({
            updateContainerOnValid: patentContainer + " #panel-body-" + panel,
            executeOnValid: new FuncCall(handlePatentInit, [panel])
        });
        commonAjaxCall(request, responseAction);
    }
    if (panel === PatentPanel.SpcIdentityData) {
        initMainPatentHref(false);
    }


});

function handlePatentInit(panel) {
    executeCommonInitialization({
        initLazyImages: true,
        panelToInitialize: patentContainer + " #panel-" + panel,
        initializeFormElementsWrapper: patentContainer + " #panel-" + panel
    });
    if (panel === PatentPanel.MainData) {
        attachmentUploadInit();
    }

    if (panel === PatentPanel.PlantMainData) {
        initTaxonNomenclatureAutocomplete();
        attachmentUploadInit();
    }

    if (panel === PatentPanel.SpcIdentityData) {
        initSpcMainPatentAutocomplete();
        initPortalUsersAutocomplete();
    }

    if (panel === PatentPanel.IdentityData) {
        initPortalUsersAutocomplete();
    }

    if (panel === PatentPanel.IpcData) {
        initIpcAutocomplete();
    }
    if (panel === PatentPanel.CpcData) {
        initCpcAutocomplete();
    }
    if (panel === PatentPanel.PublishedDrawingsData) {
        drawingsUpload();
    }

    if (panel === PatentPanel.DesignDrawingsData) {
        if ($(".locarno-autocomplete").length > 0) {
            initLocarnoClassAutocomplete();
        }
        singleDesignDrawingsUpload();
    }

    initRelationshipAutocompletes();
    initEuropeanPatentConversionAutocomplete();
}


$(document).on("click", patentContainer + " a[data-action='cancel']", function (e) {
    let blockUI = false;
    let panel = $(this).data('panel');
    let url = $(this).data('url');
    let callParams = {isCancel: true};

    if (panel === PatentPanel.DesignDrawingsData || (panel === PatentPanel.PublishedDrawingsData)) {
        blockUI = true;
    }
    setBookmarkButtonsMode(panel,false);

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: blockUI});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: patentContainer + " #panel-body-" + panel,
        executeOnValid: new FuncCall(handlePatentInit, [panel])
    });

    commonAjaxCall(request, responseAction);
    if (panel === PatentPanel.SpcIdentityData) {
        initMainPatentHref(false);
    }
});

$(document).on("change", patentContainer + ' #object-file-filingData-applicationType', function (e) {
    let $select = $(this);
    let url = $select.data('url');
    let callParams = {
        applicationType: $select.val()
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: patentContainer + " #applicationSubtype-select-wrapper",
        executeOnValid: new FuncCall(executeCommonInitialization, [{
            inputsForEditWrapper: patentContainer + " #applicationSubtype-select-wrapper",
            initializeFormElementsWrapper: patentContainer + " #applicationSubtype-select-wrapper",
        }])
    });

    commonAjaxCall(request, responseAction);
});


