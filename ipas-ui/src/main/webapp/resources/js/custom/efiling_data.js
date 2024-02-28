$(function () {
    if ($("#portal-users-autocomplete").length > 0) {
        initPortalUsersAutocomplete();
    }
});


function initPortalUsersAutocomplete($autocomplete) {
    $autocomplete = $("#portal-users-autocomplete")
    if ($autocomplete.length == 0) {
        return;
    }
    let url = $autocomplete.data('url');
    $autocomplete.autocomplete({
        delay: 500,
        minLength: 3,
        source: function (request, response) {
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {name: request.term},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

            commonAjaxCall(ajaxRequest, responseAction);
        },
        select: function (event, ui) {
            let inputValue = ui.item.fullName;
            $autocomplete.val(inputValue);
            $("#efilingDataLoginName").val(ui.item.login);
            return false;
        },
        change: function (event, ui) {
            if (ui.item == null) {
                $(this).val('');
                $("#efilingDataLoginName").val('');
            }
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span>" + item.fullName + "</span></a>").appendTo(ul);
    };
}