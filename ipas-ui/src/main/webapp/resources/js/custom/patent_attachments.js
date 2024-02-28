$(function () {
    if ($(patentContainer).length > 0) {
        attachmentUploadInit();
    }
});

function setBookmarkButtonsMode(panel, enable) {
    if (panel === PatentPanel.MainData) {
        let bookmarkButtons = $('.bookmarkButton')
        Array.from(bookmarkButtons).forEach((bookmarkButton) => {
            if (enable) {
                bookmarkButton.style.pointerEvents = null;
            } else {
                bookmarkButton.style.pointerEvents = "none";
            }
        });
    }
}

$(document).on("click", "[data-action='delete-patent-attachment']", function (e) {
    let $button = $(this);

    if (Confirmation.exist($button)) {
        Confirmation.openConfirmCloneBtnModal($button, $button.data('message'))
    } else {
        let url = $button.data('url');

        updateAttachmentsWithAjax(url, null, false);
    }
});


$(document).on("click", "[data-action='edit-bookmarks-dialog-open']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        id: $(this).data('id'),
        attachmentTypeId: $(this).data('type-id'),
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: "#bookmarks-modal-content",
        executeOnValid: new FuncCall(commonOpenModalFormInit, ["#bookmarks-modal", false])
    });

    commonAjaxCall(request, responseAction);
});


$(document).on("click", "[data-action='edit-bookmarks']", function (e) {
    let bookmarks = [];
    let bookmarkElements = $('.bookmark');

    Array.from(bookmarkElements).forEach((bookmark) => {
        let pageNumber = $(bookmark).val();
        let bookmarkName = $(bookmark).data("id");
        let bookmarkRequired = $(bookmark).data("flag");
        let bookmarkOrder = $(bookmark).data("order");
        bookmarks.push({bookmarkName, pageNumber, bookmarkRequired, bookmarkOrder})
    });
    let attachmentId = $('#attachment-id').val();
    let attachmentTypeId = $('#attachment-type-id').val();

    let url = $(this).data('url');
    let callParams = {
        id: attachmentId,
        attachmentTypeId: attachmentTypeId,
        editedBookmarks: JSON.stringify(bookmarks)
    };

    updateAttachmentsOnEditBookmarks(url, callParams, false);
});


$(document).on("click", "[data-action='add-patent-attachment']", function (e) {
    e.preventDefault();
    let attachmentType = $("#patent-attachmentType option:selected").val();
    $(patentContainer + " #attachmentType").val(attachmentType);
    $(patentContainer + " #patent-attachment-upload").trigger('click');
});


$(document).on("click", "[data-action='drawing-upload-href']", function (e) {
    e.preventDefault();
    $(patentContainer + " #drawing-upload").trigger('click');
});

function attachmentUploadInit() {
    UIBlocker.block();
    $(patentContainer + " #patent-attachment-upload").fileupload({
        url: $(this).data('url'),
        complete: function (response, e) {
            let uploadedImgErrorsDiv = $(patentContainer + " #uploaded-attachment-errors-div");
            let validationTag = '<span class="none validation-indication"></span>';
            if (response.responseText.indexOf(validationTag) !== -1) {
                $(patentContainer + " #patent-attachment-upload").val('');
                uploadedImgErrorsDiv.empty();
                uploadedImgErrorsDiv.append(response.responseText);
            } else {
                uploadedImgErrorsDiv.empty();
                let urlOnUpload = $('#patent-attachment-upload').data('url-upload');
                updateAttachmentsWithAjax(urlOnUpload, null, false);
            }
        },
        fail: function (data, e) {
            alert('errors occurred');
        }
    }).bind('fileuploadsubmit', function (e, data) {
        data.formData = {
            sessionIdentifier: $('#session-object-identifier').val(),
            attachmentType: $('#attachmentType').val()
        };
    });
    UIBlocker.unblock();
}


function updateAttachmentsOnEditBookmarks(url, callParams, clearErrors) {
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnInvalid: "#bookmarks-modal-content",
        validationFailedExpression: "data.indexOf('validation-indication') != -1",
        executeOnInvalid: new FuncCall(handleExecuteOnInvalid, []),
        updateContainerOnValid: patentContainer + " #patent-attachments-wrapper",
        executeOnValid: new FuncCall(handleAttachmentsInit, [clearErrors])
    });


    function handleExecuteOnInvalid() {
        commonOpenModalFormInit("#bookmarks-modal", false);
    }

    commonAjaxCall(request, responseAction);
}

function updateAttachmentsWithAjax(url, callParams, clearErrors) {
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: patentContainer + " #patent-attachments-wrapper",
        executeOnValid: new FuncCall(handleAttachmentsInit, [clearErrors])
    });

    commonAjaxCall(request, responseAction);
}

function handleAttachmentsInit(clearErrors) {
    executeCommonInitialization({
        inputsForEditWrapper: patentContainer + " #patent-attachments-wrapper",
        showHiddenElements: true,
        hiddenElementsWrapper: patentContainer + " #patent-attachments-wrapper",
        initializeFormElementsWrapper: patentContainer + " #patent-attachments-wrapper",
        modalToInitialize: "#bookmarks-modal",
        modalStateExpression: "close",
    });
    attachmentUploadInit();
    setBookmarkButtonsMode(PatentPanel.MainData, true)
    if (clearErrors) {
        let ediAttachmentsErrorDiv = $(patentContainer + " #uploaded-attachment-errors-div");
        ediAttachmentsErrorDiv.empty();
    }
}