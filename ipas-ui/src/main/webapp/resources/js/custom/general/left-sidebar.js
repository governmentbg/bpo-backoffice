$(document).ready(function () {
    $(document).ajaxSuccess(function () {
        if (!$(document).find("span.edited:not(.none)").length == 0) {
            $("#submitPanel").show()
        }
    });

    $("#submitPanel").hide()

    Object.values(PanelContainer).forEach(value => {
        let panelContainer = $('#' + value);
        if (panelContainer.length > 0) {
            initPanelContainer(value);
        }
    });
});

function initPanelContainer(element) {
    let panelContainer = '#' + element;

    /*Initial setup of all sidebar toggles*/
    $('#detail-list .draggable').draggable({
        cursor: "move",
        distance: "20",
        cursorAt: {top: 0, left: 0},
        connectToSortable: panelContainer,
        addClasses: false,
        refreshPositions: true,
        zIndex: 8888,
        helper: function () {
        }
    });

    $(panelContainer).sortable({
        dropOnEmpty: false,
        distance: 30,
        cursor: "move",
        handle: "h3",
        receive: function (event, ui) {
            var data_group = ui.item.attr('data-group');
            $('#detail-list [data-group="' + data_group + '"]').trigger('c_drag');
        },
        update: function (event, ui) {
        }
    }).droppable();

    $('ul, li').disableSelection();

    let cookie_name = $("#detail-list").attr("data-group");
    let sess_obj = getCookieAsJson(cookie_name);

    $(".sb-ckb").each(function () {
        var data_group = $(this).parent().attr("data-group");
        var panel = $(panelContainer + " #panel-" + data_group);

        if (data_group !== undefined && sess_obj !== undefined && sess_obj[data_group] !== undefined) {
            let is_checked = sess_obj[data_group];
            $(this).attr("checked", is_checked);
            if (is_checked) {
                $(panel).show();
            } else {
                $(panel).hide();
            }
        } else {
            $(this).attr("checked", true);
        }

        /*Init panels*/
        if ($(this).is(':checked') && !$(this).parent().attr("data-type")) {
            Panel.init($(panelContainer + " #panel-" + data_group));
        }
    });

    $(document).on("change", ".sb-ckb", function (e) {
        var data_group = $(this).parent().attr("data-group");
        var panel = $(panelContainer + " #panel-" + data_group);
        if ($(this).is(':checked')) {
            $(panel).show();
            if (!$(this).parent().attr("data-type")) {
                Panel.init($(panel));
            }
//         if($(this).attr("class").indexOf('scrollTo') != -1){
            $('html, body').animate({
                scrollTop: $(panel).offset().top - ($(".app-header").height() + $("#breadcrumbs").height() + 10)
            }, 1000);
//          }
        } else {
            $(panel).hide()
        }
        if (sess_obj !== undefined && sess_obj[data_group] !== undefined) {
            var is_checked = $(this).is(':checked');
            sess_obj[data_group] = is_checked;
            setCookie(cookie_name, JSON.stringify(sess_obj));
        }

    });

    $(document).on("click", "label.empty", function (e) {
        var data_group = $(this).parent().attr("data-group");
        var panel = $(panelContainer + " #panel-" + data_group);

        if ($(panel).find("span.edited").length > 0 && $(panel).find("span.edited.none").length == 0) {
            alert('Има направени промени!')
            e.preventDefault()
        }
    });


    /*Scroll to panel on label click*/
    $(document).on("click", ".sb-ckb-cEvent", function () {
        var data_group = $(this).parent().attr("data-group");
        var panel = $(panelContainer + " #panel-" + data_group);
        $('html, body').animate({
            scrollTop: $(panel).offset().top - ($(".app-header").height() + $("#breadcrumbs").height() + 10)
        }, 1000);
    })
}

$(document).on("click", ".left-sidebar a", function (e) {
    UIBlocker.block()
});
