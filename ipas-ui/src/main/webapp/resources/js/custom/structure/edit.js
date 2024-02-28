/**
 * elements - list of li elements
 * hide/show inactive li elements (elements with data-status=inactive) depending on the visibleInactiveElements flag. The structure elements might be departments/sections/users
 */
function toggleInactiveStructureElements(elements, visibleInactiveElements) {
    $(elements).each(function() {
        var el = $(this);
        if (el.data('status') == 'inactive') {
            el.removeClass(visibleInactiveElements ? 'hidden-block' : 'visible-block');
            el.addClass(visibleInactiveElements ? 'visible-block' : 'hidden-block');
        }
    });
}


// $('input.required, select.required').each(function () {
//     $(this).before('*');
// });


//hides/shows/ all the inactive users/sections/departments
$('#activeOnlyUsers, #sectionsActiveOnly, #departmentsActiveOnly').on('click',function () {
    let id = $(this).attr("id") + "List";
    toggleInactiveStructureElements('#' + id + ' li', !$(this).is(":checked"));
})


$('#transferAllUsers').on('click', function () {//transfer all users button clicked
    M.Modal.getInstance($("#transfer-user-modal")).open();
    $("#usernameToTransfer").html(messages['all']);
    let userIds = [];
    $(".users-transfer-button").each(function() {
        userIds.push($(this).attr("data"));
    });
    $("#transfer_user_ids").val(JSON.stringify(userIds));
    console.log("UserIds:" + $("#transfer_user_ids").val());
});

$('.users-transfer-button').on('click', function() {//transfer single user button clicked
    M.Modal.getInstance($("#transfer-user-modal")).open();
    let userIds  = [$(this).attr("data")];
    $("#usernameToTransfer").html($("#objectName-" + userIds[0]).html());
    $("#transfer_user_ids").val(JSON.stringify(userIds));
    console.log("UserIds:" + $("#transfer_user_ids").val());
});

//transfer user submit button clicked!
$('#transferUserBtn').on('click', function(e) {
    e.preventDefault();
    var structureNew = $('#structure-select').val();
    var currentStructure = $("#divisionCode").val() + '-' + $("#departmentCode").val();
    if (checkCanTransferStructure(currentStructure, structureNew)) {
        let userIds = JSON.parse($("#transfer_user_ids").val());
        transferUser(userIds, currentStructure, structureNew);
    }

});

//transfer all sections button clicked
$('#sectionsTransferAll').on('click', function () { //transfer all sections button clicked
    M.Modal.getInstance($("#transfer-section-modal")).open();
    $("#sectionToTransfer").html(messages['all']);
    let sectionIds = [];
    $(".sections-transfer-button").each(function() {
        sectionIds.push(generateStructureKey($(this).attr("data")));
    });
    $("#transfer_section_ids").val(JSON.stringify(sectionIds));
    console.log("SectionIds:" + $("#transfer_section_ids").val());

});
//transfer single section button clicked
$('.sections-transfer-button').on('click', function () { //transfer single section button clicked
    M.Modal.getInstance($("#transfer-section-modal")).open();
    let key = $(this).attr("data");
    let sectionIds = [generateStructureKey(key)];
    $("#sectionToTransfer").html($("#objectName-" + key).html());
    $("#transfer_section_ids").val(JSON.stringify(sectionIds));
    console.log("SectionIds:" + $("#transfer_section_ids").val());
});

//transfer section submit button clicked
$('#sectionTransferBtn').on('click', function(e) {
    e.preventDefault();
    var structureNew = $('#department-select').val();
    var currentStructure = $("#divisionCode").val() + '-' + $("#departmentCode").val();
    if (checkCanTransferStructure(currentStructure, structureNew)) {
        let url = $("#sectionTransferBtn").data("url");
        let sectionIds = JSON.parse($("#transfer_section_ids").val());
        transferStructure(url, sectionIds, currentStructure, structureNew);
    }

});

//transfer all departments button clicked
$('#departmentsTransferAll').on('click', function () { //transfer all departments button clicked
    M.Modal.getInstance($("#transfer-department-modal")).open();
    $("#departmentToTransfer").html(messages['all']);
    let departmentIds = [];
    $(".departments-transfer-button").each(function() {
        departmentIds.push(generateStructureKey($(this).attr("data")));
    });
    $("#transfer_department_ids").val(JSON.stringify(departmentIds));
    console.log("departmentIds:" + $("#transfer_department_ids").val());

});
//transfer single section button clicked
$('.departments-transfer-button').on('click', function () { //transfer single department button clicked
    M.Modal.getInstance($("#transfer-department-modal")).open();
    let key = $(this).attr("data");
    let departmentIds = [generateStructureKey(key)];
    $("#departmentToTransfer").html($("#objectName-" + key).html());
    $("#transfer_department_ids").val(JSON.stringify(departmentIds));
    console.log("departmentIds:" + $("#transfer_department_ids").val());
});

//transfer department submit button clicked
$('#departmentTransferBtn').on('click', function(e) {
    e.preventDefault();
    var structureNew = $('#division-select').val();
    var currentStructure = $("#divisionCode").val();
    if (checkCanTransferStructure(currentStructure, structureNew)) {
        let url = $("#departmentTransferBtn").data("url");
        let departmentIds = JSON.parse($("#transfer_department_ids").val());
        transferStructure(url, departmentIds, currentStructure, structureNew);
    }

});


function checkCanTransferStructure(currentStructure, structureNew) {
    if (structureNew == '') {
        return false;
    }
    return true;
}

function transferUser(userIds, originalStructure, newStructure) {
    var url = $("#transferUserBtn").data("url");

    let oldStructureKey = generateStructureKey(originalStructure);
    let newStructureKey = generateStructureKey(newStructure);

    let data = {
        oldStructureId: oldStructureKey,
        newStructureId: newStructureKey,
        userIds: userIds
    };
    let callParams = {
        data: JSON.stringify(data)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onTransferStructureError, [$(this)]),
        executeOnValid: new FuncCall(reloadPage)
    });
    commonAjaxCall(request, responseAction);
}

function transferStructure(url, sectionIds, originalStructure, newStructure) {

    let oldStructureKey = generateStructureKey(originalStructure);
    let newStructureKey = generateStructureKey(newStructure);

    let data = {
        newStructureId: newStructureKey,
        oldStructureId: oldStructureKey,
        structureIds: sectionIds
    };
    let callParams = {
        data: JSON.stringify(data)
    };

    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onTransferStructureError, [$(this)]),
        executeOnValid: new FuncCall(reloadPage)
    });
    commonAjaxCall(request, responseAction);
}
function onTransferStructureError(el, data) {
    $("#validation-errors-modal").replaceWith(data);
    commonOpenModalFormInit("#validation-errors-modal", false);
}
function generateStructureKey(structureCode) {
    let structureCodeParts = structureCode == null ? [] : structureCode.split("-");
    return {
        officeDivisionCode : structureCodeParts.length > 0 ? structureCodeParts[0] : null,
        officeDepartmentCode : structureCodeParts.length > 1 ? structureCodeParts[1] : null,
        officeSectionCode : structureCodeParts.length > 2 ? structureCodeParts[2] : null,
    }
}

$(document).on("change", "#division-select", function(e){
    $("#divisionCode").val($(this).val());
    $("#departmentCode").val("");

    $("#sectionCode").val("");
    if ($("#department-select-div").length == 0) {
        return;//there is no department dropdown
    }
    $("#section-select-div").html("");//removes the sections select div (if any)
    let url = $(this).data("url");
    let callParams = {
        divisionCode: $(this).val()
    }
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(updateDepartmentsSelect, [$(this)])
    });
    commonAjaxCall(request, responseAction);
});
$(document).on("change", "#department-select", function(e){
    $("#departmentCode").val($(this).val());
    $("#sectionCode").val("");
    if ($("#section-select-div").length == 0) {
        return;//there is no department dropdown
    }
    let url = $(this).data("url");
    let callParams = {
        divisionCode: $("#division-select").val(),
        departmentCode: $(this).val()
    }
    let request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(updateSectionsSelect, [$(this)])
    });
    commonAjaxCall(request, responseAction);
});
$(document).on("change", "#section-select", function(e){
    $("#sectionCode").val($(this).val());
});
function updateDepartmentsSelect(el, data) {
    $("#department-select-div").html(data);
    executeCommonInitialization({
        initializeFormElementsWrapper: "#department-select-div"
    })
}
function updateSectionsSelect(el, data) {
    $("#section-select-div").html(data);
    executeCommonInitialization({
        initializeFormElementsWrapper: "#section-select-div"
    })
}


//Search
$('input#request_place').on("input", search);

// TODO : exclude href text from search
function search() {
    var searchTerms = $(this).val();
    console.log('Input change');

    $('.Linked-List li').each(function () {
        var $li = $(this);
        var hasMatch = searchTerms.length == 0 || $li.text().toLowerCase().indexOf(searchTerms.toLowerCase()) > 0;

        $li.toggle(hasMatch);
        if ($li.children("ul").hasClass("hidden-block")) {

            $.each($li.children("ul"), function (index, el) {
                if (el.className == "visible-block") {
                    el.setAttribute("aria-hidden", "true");
                    el.className = "hidden-block";
                } else {
                    el.setAttribute("aria-hidden", "false");
                    el.className = "visible-block";
                }
            })
        }
    });
}


