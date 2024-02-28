$(document).ready(function () {
    $('#btn-session-objects').on('click', function () {
        $("body").toggleClass("nav-dropdown-session-show").trigger('updateSessionObjects');   // session-sidebar-show
        $("body").removeClass("nav-dropdown-show");   // hide right sidebar
        $("body").removeClass("nav-dropdown-search-show");   // hide right sidebar
    });

    showSessionObjects();

    let sessionObjectsUpdateInterval = null;
    $('body').on('updateSessionObjects', function () {
        let hasClass = $(this).hasClass('nav-dropdown-session-show');
        if (!hasClass) {
            clearInterval(sessionObjectsUpdateInterval);
        } else {
            sessionObjectsUpdateInterval = setInterval(function () {
                showSessionObjects();
            }, 2000);
        }
    });
});      // $(document).ready...

function showSessionObjects() {
    let $sessionObjectPanel = $('#session-objects-panel');
    if ($sessionObjectPanel.length > 0) {
        let callParams = {};
        let url = $sessionObjectPanel.data('url');
        updateSessionObjectsWithAjax(url, callParams);
    }
}

$(document).on("click", "[data-action='remove-session-object']", function (e) {
    let url = $(this).data('url');
    let callParams = {
        sessionObject: $(this).data('id')
    };

    updateSessionObjectsWithAjax(url, callParams);
});

$(document).on("click", "[data-action='open-existing-session-object']", function (e) {
    $(this).attr("disabled", "disabled");
    $('#open-existing-input').val($(this).data('value'))
    $('#open-existing-object-form').submit();
});


function updateSessionObjectsWithAjax(url, callParams) {
    let $activeSessionObject = $('#session-object-identifier');
    if ($activeSessionObject !== undefined) {
        callParams.activeSessionObject = $activeSessionObject.val()
    }

    var request = new CommonAjaxRequest(url, {requestData: callParams, useSessionId: false});
    var responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: '#session-objects-panel, #max-session-object-panel',
    });

    commonAjaxCall(request, responseAction);
}