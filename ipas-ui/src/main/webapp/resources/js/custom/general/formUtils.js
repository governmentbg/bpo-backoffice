function getFormValuesAsJson(htmlPart) {
    let resultJson = {};

    let textInputs = htmlPart.find('input[type=text][id],input[type=number][id],input[type=checkbox][id],input[type=hidden][id],input[data-type=datepicker][id]');
    textInputs.each(function (index, element) {
        let type = $(element).attr("type");
        let id = $(element).attr("id");
        let key = id.substr((id.lastIndexOf('-') + 1), id.length);

        if (!$(element).hasClass('form-ignore')) {
            if ('checkbox' === type)
                resultJson [key] = $(element).is(':checked');
            else
                resultJson [key] = checkEmptyField($(element).val());

            let isRevertValue = $(element).data('revert');
            if (isRevertValue === true) {
                resultJson [key] = revertCheckboxValue(resultJson [key])
            }
        }
    });

    let selectInputs = htmlPart.find('select[id]');
    selectInputs.each(function (index, element) {
        let id = $(element).attr("id");
        let key = id.substr((id.lastIndexOf('-') + 1), id.length);
        resultJson [key] = checkEmptyField($(element).val());
    });

    let textareaInputs = htmlPart.find('textarea[id]');
    textareaInputs.each(function (index, element) {
        let id = $(element).attr("id");
        let key = id.substr((id.lastIndexOf('-') + 1), id.length);
        resultJson [key] = checkEmptyField($(element).val());
    });

    let radioButtonInputs = htmlPart.find('input[type=radio][name]:checked');
    radioButtonInputs.each(function (index, element) {
        let name = $(element).attr("name");

        let lastIndexOfDot = name.lastIndexOf('.');
        if (lastIndexOfDot !== -1) {
            let key = name.substr((lastIndexOfDot + 1), name.length);
            resultJson [key] = checkEmptyField($(element).val());
        } else {
            let lastIndexOfDash = name.lastIndexOf('-');
            let key = name.substr((lastIndexOfDash + 1), name.length);
            resultJson [key] = checkEmptyField($(element).val());
        }
    });
    return resultJson;
}

function openPanelsWithValidationErrors(containErrorsCheckDiv, container) {
    if (!(null === containErrorsCheckDiv || undefined === containErrorsCheckDiv)) {
        let size = containErrorsCheckDiv.data('size');
        if (undefined !== size) {
            let $validationErrorsDiv = $(".validation-error");
            let panels = new Set();
            $validationErrorsDiv.each(function (index, element) {
                let id = $(element).data('id');
                let panelId = $('#' + id).closest('.panel').attr('id');
                panels.add(panelId);
            });
            panels.forEach(function (value) {
                $(container + ' #' + value).find('a[data-action="edit"]').click()
            });
        }
    }
}


function setLabelToEditedPanels() {
    let panels = $('span.edited-panel');
    panels.each(function (index, element) {
        $('#' + $(element).data('value') + ' span.edited.none').removeClass('none');
    });
    if (panels.length > 0) {
        $("#submitPanel").show();
    }
}

function checkEmptyField(field) {
    if (field === '' || field === undefined)
        return null;
    return field.trim();
}

function previewFile(file, img) {
    if (file.files && file.files[0]) {
        let reader = new FileReader();
        reader.onload = function (e) {
            img.attr('src', e.target.result);
        };
        reader.readAsDataURL(file.files[0]);
    }
}

function reloadPage() {
    window.location.reload();
}

function revertCheckboxValue(value) {
    if (value)
        value = false;
    else
        value = true;
    return value;
}

function getEditedPanelIds(panelContainer) {
    let editedPanels = $('#' + panelContainer + ' span.edited:not(.none)').closest('.panel');
    let panels = [];
    editedPanels.each(function (index, element) {
        let id = $(element).attr('id');
        panels.push(id);
    });
    return panels;
}

var ScrollUtils = {
    panelScroll: function (panelContainer) {
        if ($(panelContainer).length > 0) {
            let $scrollToPanel = $('#scroll-to-panel');
            if ($scrollToPanel.length > 0) {
                let paneName = $scrollToPanel.val();
                if (paneName) {
                    let panelSelector = panelContainer + " #panel-" + paneName;
                    let panel = $(panelSelector);
                    $('html, body').animate({
                        scrollTop: $(panel).offset().top - ($(".app-header").height() + $("#breadcrumbs").height() + 10)
                    }, 1000);
                }
            }
        }
    },
};

var RequestUtils = {
    post: function (url, parameters) {
        UIBlocker.block();
        let myForm = document.createElement("form");
        myForm.method = "post";
        myForm.action = url;
        for (let k in parameters) {
            let myInput = document.createElement("input");
            myInput.setAttribute("name", k);
            myInput.setAttribute("value", parameters[k]);
            myForm.appendChild(myInput);
        }
        document.body.appendChild(myForm);
        myForm.submit();
        document.body.removeChild(myForm);
    },
    get: function (url, parameters) {
        UIBlocker.block();
        let myForm = document.createElement("form");
        myForm.method = "get";
        myForm.action = url;
        for (let k in parameters) {
            let myInput = document.createElement("input");
            myInput.setAttribute("name", k);
            myInput.setAttribute("value", parameters[k]);
            myForm.appendChild(myInput);
        }
        document.body.appendChild(myForm);
        myForm.submit();
        document.body.removeChild(myForm);
    },
};

function toggleCheckbox(el, checked) {
    $(el).prop("checked", checked);
}

function setSessionIdentifier(callParams) {
    let sessionIdentifier = $('#session-object-identifier').val();
    if (sessionIdentifier !== undefined) {
        callParams['sessionIdentifier'] = sessionIdentifier
    }
}