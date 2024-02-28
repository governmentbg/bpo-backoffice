//show not-loaded division clicked
$(document).on("click", "li[data-division][data-load='false']", function (e) {
    if (e.target !== this)
        return;//clicked some of the children

    var url = $(this).data('url');
    var callParams = {
        divisionCode: $(this).data('division')
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    var responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(updateStructureNode, [$(this)])
    });
    commonAjaxCall(request, responseAction);
});

//show not-loaded department clicked
$(document).on("click", "li[data-department][data-load='false']", function (e) {
    if (e.target !== this)
        return;//clicked some of the children

    var url = $(this).data('url');
    var callParams = {
        departmentCode: $(this).data('department'),
        divisionCode: $(this).data('department-division')
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    var responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(updateStructureNode, [$(this)])
    });
    commonAjaxCall(request, responseAction);
});

//show not-loaded section clicked
$(document).on("click", "li[data-section-department][data-load='false']", function (e) {
    if (e.target !== this)
        return;//clicked some of the children

    var url = $(this).data('url');
    var callParams = {
        sectionCode: $(this).data('section'),
        departmentCode: $(this).data('section-department'),
        divisionCode: $(this).data('section-division')
    };

    var request = new CommonAjaxRequest(url, {requestData: callParams, blockUI: true});
    var responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(updateStructureNode, [$(this)])
    });
    commonAjaxCall(request, responseAction);
});

//delete division/section/department clicked!
$(document).on("click", ".delete", function (e) {
    let url = $(this).attr("href");
    let request = new CommonAjaxRequest(url, {requestData: {}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        validationFailedExpression: "data.indexOf('validation-errors-modal') != -1",
        executeOnInvalid: new FuncCall(onDeleteStructureError, [$(this)]),
        executeOnValid: new FuncCall(onDeleteStructureSuccess, [$(this)])
    });
    commonAjaxCall(request, responseAction);
});

//activate division/department/section clicked!
$(document).on("click", ".activate", function (e) {
    let url = $(this).attr("href");
    let request = new CommonAjaxRequest(url, {requestData: {}, blockUI: true});
    let responseAction = new CommonAjaxResponseAction({
        executeOnValid: new FuncCall(onActivateStructureSuccess, [$(this)])
    });
    commonAjaxCall(request, responseAction);
});

function updateStructureNode(el, data) {
    $(el).replaceWith(data);
    let selectedLi = $("li[data-li='" + $(el).data("li") + "']");
    activateTree(selectedLi.get(0), false);
    selectedLi.click();
}
function onDeleteStructureError(el, data) {
    $("#modify_structure_status").html(data);
    commonOpenModalFormInit("#validation-errors-modal", false);
}

function onDeleteStructureSuccess(el, data) {
    $("#modify_structure_status").html(data);
    commonOpenModalFormInit("#action_success", false);

    //hiding the edit and delete buttons and showing the activate one!
    $(".edit[data-nav='" + el.data("nav") + "']").hide();
    $(".delete[data-nav='" + el.data("nav") + "']").hide();
    $(".activate[data-nav='" + el.data("nav") + "']").show();
    $("#status-"  + el.data("nav")).text(messages['status.inactive']);
}
function onActivateStructureSuccess(el, data) {
    $("#modify_structure_status").html(data);
    commonOpenModalFormInit("#action_success", false);
    //hiding the activate button and showing the edit/delete ones!
    $(".edit[data-nav='" + el.data("nav") + "']").show();
    $(".delete[data-nav='" + el.data("nav") + "']").show();
    $(".activate[data-nav='" + el.data("nav") + "']").hide();
    $("#status-"  + el.data("nav")).text(messages['status.active']);
}




//--- Add this to the onload event of the BODY element
function addStructureEvents() {
    activateTree(document.getElementById("LinkedList"));
}

function updateNav(el) {
    let nav = $(el).data('nav');
    console.log("Updating nav..." + nav);
    let url = $("#data-nav-url").data("nav-url");
    let callParams = {
        nav: nav
    };
    let request = new CommonAjaxRequest(url, {requestData: callParams});
    commonAjaxCall(request);
    return true;
};

function resetStructureNav() {
    let nav = $("#nav").data("nav");
    console.log("Inside resetStructureNav...Nav:" + nav);
    if (nav !== undefined && nav != null && nav != "") {
        let liEl = $("[data-li='" + nav + "']");
        // selects all the parent li elements from the current element to the element with id = LinkedList in reverse order and clicks them!
        let liElements = liEl.parentsUntil($("#LinkedList"), "li");
        $(liElements.get().reverse()).each(function (el) {
            $(this).click();
        });
        liEl.click();
        scrollIntoView(liEl[0], 0);
    }
}

function scrollIntoView(selector, offset = 0) {
    window.scroll(0, selector.offsetTop - offset);
}

function collapseStructureNode() {
    $("li.has-children[data-load='true']").each(function() {
        let $parent = $(this);
        $parent.find("ul").each(function() {
            if ($(this).find("li").length > 0) {
                $(this).addClass("visible-block");
                $(this).removeClass("hidden-block");
                $parent.attr("aria-hidden", "false");
            }
        });

    });
}
//search structure clicked...
$(document).on("click", "#search_structure", function (e) {
    let partOfName = $("#partOfName").val();
    if (partOfName == '') {
        window.location.href = $("#clear_search_structure").attr("href");
    } else {
        $("#searchForm").submit();
    }
});