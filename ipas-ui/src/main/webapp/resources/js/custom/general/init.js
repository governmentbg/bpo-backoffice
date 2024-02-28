/*
Initializations called on document being ready
 */
var applicationContextPath = $('meta[name="_ctx"]').attr("content");//Important

$(document).ready(function () {

    /*===== Materialbox and Tooltips ===*/
    initMaterialBox();
    initTooltips();
    initLazyImages();

    /*===== Modals ========*/
    initMaterializeModal($('.modal'));
    $('.collapsible').collapsible();

    /* === Date Picker === */
    $.datepicker.regional['bg'] = {
        closeText: 'Затвори', // set a close button text
        currentText: 'Днес', // set today text
        monthNames: ['Януари', 'Февруари', 'Март', 'Април', 'Май', 'Юни', 'Юли', 'Август', 'Септември', 'Октомври', 'Ноември', 'Декември'], // set month names
        monthNamesShort: ['Яну', 'Фев', 'Март', 'Апр', 'Май', 'Юни', 'Юли', 'Авг', 'Септ', 'Окт', 'Ноем', 'Дек'], // set short month names
        dayNames: ['Неделя', 'Понеделник', 'Вторник', 'Сряда', 'Четвъртък', 'Петък', 'Събота'], // set days names
        dayNamesShort: ['Нд', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'], // set short day names
        dayNamesMin: ['Н', 'П', 'В', 'С', 'Ч', 'Пт', 'Сб'], // set more short days names
        dateFormat: 'dd.mm.yy', // set format date
        firstDay: 1   // Default value is 0; Sunday 0; Monday 1; Tuesday 2; Wednesday 3; ...
    };

    $.datepicker.setDefaults($.datepicker.regional['bg']);

    /* === Perfect Scroll Bar === */
    const ps = new PerfectScrollbar('#psb', {
        wheelSpeed: 2,
        wheelPropagation: false,   // true => scroll in all divs,
        minScrollbarLength: 10
    });

    /* === Sidebars === */
    $('#btn-menu').on('click', function () {
        $("body").toggleClass("left-sidebar-show");
    });
    $('#btn-aside').on('click', function () {
        $("body").toggleClass("nav-dropdown-show");   // right-sidebar-show
        $("body").removeClass("nav-dropdown-search-show");   // hide right sidebar
        $("body").removeClass("nav-dropdown-session-show");   // hide right sidebar

    });
    $('#btn-search').on('click', function () {
        $("body").removeClass("nav-dropdown-show");   // hide right-sidebar-show
        $("body").toggleClass("nav-dropdown-search-show");   // right-sidebar-show
        $("body").removeClass("nav-dropdown-session-show");   // hide right sidebar

    });

    /* === DropDown Main Menu === */
    $('.sidebar-nav ul li.has-children > a').click(function () {
        $(this).parent().toggleClass('open');
        return false;
    });

    $(document).keydown(function (e) {
        if (e.keyCode == 27)//Block Esc button
            return false;
        if (e.keyCode == 13) {//Block Enter button
            if (e.target.classList.contains("materialize-textarea")) {
                return;
            }
            e.preventDefault();
            return false;
        }

    });

    setCorrectAutocompleteWidth();
    $(".number").inputFilter(function (value) {
        return /^\d*$/.test(value);    // Allow digits only, using a RegExp
    });
});

/*
Function that adds handler to the modal onCloseEnd. Written to fix overflow problems
 */
function initMaterializeModal(selector, dismissible) {
    if (dismissible == null || undefined == dismissible)
        dismissible = true;

    selector.modal({
        preventScrolling: true,
        dismissible: dismissible,
        onOpenStart: function (modal) {
            $(modal).css({
                width: '65%'
            })
        },
        onCloseEnd: function (el) {
            $('body').css({
                overflow: 'visible'
            });
            if($(el).hasClass('remove-on-close')){
                $(el).remove();
            }
        },
    });
}

/*
Initializes materialboxes (image boxes)
 */
function initMaterialBox() {
    $('.materialboxed').materialbox();
}


function initDatepicker($element) {
    let label = $element.siblings('label');
    let maxDate = $element.data('max-date');
    let minDate = $element.data('min-date');
    $element.datepicker({
        maxDate: maxDate,
        minDate: minDate,
        beforeShow: function (textbox, instance) {
            if ($(textbox).hasClass("modal-date")) {
                setTimeout(function () {
                    var newTop = $(textbox).offset().top - $(window).scrollTop() + $(textbox).height() + 2;
                    instance.dpDiv.css({
                        top: newTop + 'px',
                    });
                }, 20);
            }
        },
        onSelect: function () {
            if (label.length > 0) {
                $(label[0]).addClass('active');
            }
        }
    });
    $element.focusin(function () {
        if (label.length > 0) {
            $(label[0]).addClass('active');
        }
    });
    $element.focusout(function () {
        if (label.length > 0 && $element.val() == '') {
            $(label[0]).removeClass('active');
        }
    });
}

function initDatePickers() {
    let $datepickers = $("[data-type='datepicker']");
    $datepickers.each(function (index, element) {
        initDatepicker($(element));
    });
}

/*
Initializes tooltips
 */
function initTooltips() {
    $('.tooltip').tooltip();

    $('.tooltip-dark').tooltip({
        tooltipClass: 'dark',
        open: function (event, ui) {
            ui.tooltip.hover(function () {
                $(this).fadeTo("slow", 0.5);
            });
        }
    });

    $('.tooltip-warning').tooltip({
        tooltipClass: 'warning',
        open: function (event, ui) {
            ui.tooltip.hover(function () {
                $(this).fadeTo("slow", 0.5);
            });
        }
    });

    $('.tooltip-danger').tooltip({
        tooltipClass: 'danger',
        open: function (event, ui) {
            ui.tooltip.hover(function () {
                $(this).fadeTo("slow", 0.5);
            });
        }
    });

}

/*
Activates labels when necessary
 */

function initLazyImages(){
    $('img[data-lazysrc]').each( function(){
            //* set the img src from data-src
            $( this ).attr( 'src', $( this ).attr( 'data-lazysrc' ) );
        }
    );
}


function checkForActiveLabels(element) {
    let $result = element.find("input[type=text], input[type=number], .materialize-textarea, input[data-type=datepicker]");
    $result.each(function (index, element) {
        if ($(element).val().length > 0 || $(element).is(':focus') || element.autofocus || $(element).attr('placeholder') !== undefined) {
            $(element).siblings('label').addClass('active');
        } else {
            $(element).siblings('label').removeClass('active');
        }
    });
}

/*
Function used for common initializations. Specific initializations - like drawing init or something else that is not applicable in
many cases should be written as separate functions and not added here! Only the things that apply for most of the cases should be part
of this implementation. Function params:
    data: the ajax response data
    options: initialization options
        - modalStateExpression: the state of the modal to be initialized with. Valid statuses are "open"/"close" or some expression
            that evaluates to "open" or "close". Example: "data != null ? 'open' : 'close'"
        - modalToInitialize: the modal selector for the modal that needs to be initialized
        - panelToInitialize: when initializing a panel - this is the panel selector
        - inputsForEditWrapper: the selector of the wrapper that contains inputs that will be initialized for edit (with FF.edit)
        - initializeFormElementsWrapper: the selector of the wrapper that contains form elements that need to be initialized.
            This option is used to activate labels, resize textareas and initialize selects
        - hiddenElementsWrapper: selector used with showHiddenElements, when both provided the hidden elements under this wrapper are shown/hidden
        - showHiddenElements: boolean used with hiddenElementsWrapper, when both provided the hidden elements under this wrapper are shown/hidden
        - initLazyImages: boolean used to load images asynchronously

 */
function executeCommonInitialization(options, data) {
    if (options.initLazyImages === true){
        initLazyImages();
    }

    if (options.modalStateExpression != null) {
        if (options.modalStateExpression != 'open' && options.modalStateExpression != 'close') {
            options.modalStateExpression = eval(options.modalStateExpression);
        }
        switch (options.modalStateExpression) {
            case "close":
                if ($(options.modalToInitialize).length > 0) {
                    M.Modal.getInstance($(options.modalToInitialize)).close();
                    $('body').css({
                        overflow: 'visible'
                    });
                }
                break;
            case "open":
                if ($(options.modalToInitialize).length > 0) {
                    initMaterializeModal($(options.modalToInitialize), options.modalDismissible);
                    M.Modal.getInstance($(options.modalToInitialize)).open();
                }
                break;
            default:
                console.log("Invalid modal action")
        }
    }

    if (options.panelToInitialize != null) {
        Panel.init($(options.panelToInitialize));
    }

    if (options.inputsForEditWrapper != null) {
        $(options.inputsForEditWrapper).find(".input-field:not(.readonly)").each(function (index, input_field) {
            FF.edit($(input_field));
        });
    }

    if (options.initializeFormElementsWrapper != null) {
        checkForActiveLabels($(options.initializeFormElementsWrapper));

        $(options.initializeFormElementsWrapper).find(".materialize-textarea").each(function (index, textArea) {
            let el = $("#" + $(this).attr("id"));
            M.textareaAutoResize(el);
        });

        $(options.initializeFormElementsWrapper).find(" select:not(.search-box)").each(function (index, select) {
            let el = $(select);
            M.FormSelect.init(el);
        });
        $(options.initializeFormElementsWrapper).find(" select.search-box").each(function (index, select) {
            let el = $(select);
            el.next('label').addClass('active');
            el.select2({
                "language": "bg",
                dropdownParent: el.parent().parent()
            });
        });
        $(options.initializeFormElementsWrapper).find(" input.autocomplete-user").each(function (index, input) {
            initUserAutocomplete($(input));
        });

        $(options.initializeFormElementsWrapper).find(".default-focus").each(function (index, element) {
            $(element).focus();
        });

    }

    if (options.hiddenElementsWrapper != null) {
        if (options.showHiddenElements != null) {
            if (options.showHiddenElements) {
                $(options.hiddenElementsWrapper).find(".hidden-element").removeClass("none");
            } else {
                $(options.hiddenElementsWrapper).find(".hidden-element").addClass("none");
            }
        }
    }


    initMaterialBox();
    initTooltips();
    initDatePickers();
}

/*
Utility method that helps initialize modal on open. Params:
    modal: the modal selector
    initEdit: boolean that specifies iw the inputs should be initialized for edit (usually used for datepickers and so on)
    data: ajax data response
 */
function commonOpenModalFormInit(modal, initEdit, dismissible, data) {
    executeCommonInitialization({
        modalStateExpression: "open",
        modalDismissible: dismissible,
        modalToInitialize: modal,
        initializeFormElementsWrapper: modal,
        inputsForEditWrapper: initEdit ? modal : null
    }, data);
}

/*
Utility method that helps close modal and show some hidden elements. Params:
    modal: the modal selector
    showHidden: boolean that specifies if hidden elements should be shown
    showHiddenWrapper: selector of a wrapping element that contains hidden elements
    data: the ajax response data
 */
function commonCloseModalFormInit(modal, showHidden, showHiddenWrapper, data) {
    executeCommonInitialization({
        modalStateExpression: "close",
        modalToInitialize: modal,
        showHiddenElements: showHidden,
        hiddenElementsWrapper: showHiddenWrapper
    }, data);
}

// --- This function traverses the list and add links to nested list items
function activateTree(oList, addEventListenerToOList = true) {
    if (!oList) {
        return;
    }
    // Collapse the tree
    for (var i = 0; i < oList.getElementsByTagName("ul").length; i++) {
        oList.getElementsByTagName("ul")[i].className = "hidden-block"
    }
    if (addEventListenerToOList) {
        // Add the click-event handler to the list items
        if (oList.addEventListener) {
            oList.addEventListener("click", toggleBranch, false);
        } else if (oList.attachEvent) { // For IE
            oList.attachEvent("onclick", toggleBranch);
        }
    }
    // Make the nested items look like links
    addLinksToBranches(oList);
}

// --- This is the click-event handler
function toggleBranch(event) {
    var oBranch, cSubBranches;
    if (event.target) {
        oBranch = event.target;
    } else if (event.srcElement) { // For IE
        oBranch = event.srcElement;
    }

    if (oBranch.className !== "has-children")
        return;

    cSubBranches = oBranch.getElementsByTagName("ul");
    if (cSubBranches.length > 0) {

        let isHidden = false;
        if (cSubBranches[0].className == "visible-block") {
            oBranch.setAttribute("aria-hidden", "true");
            cSubBranches[0].className = "hidden-block";
            isHidden = true;
        } else {
            oBranch.setAttribute("aria-hidden", "false");
            cSubBranches[0].className = "visible-block";
        }

        var i, n;
        for (i = 0, n = cSubBranches.length; i < n; i++) {
            let isVisible = $(cSubBranches[i]).data('visible');
            if (true === isVisible && !isHidden) {
                cSubBranches[i].className = "visible-block";
            }
        }

        // if (cSubBranches.length > 1) {
        //     console.log("better")
        //     if (cSubBranches[1].className == "visible-block") {
        //         oBranch.setAttribute("aria-hidden", "true");
        //         cSubBranches[1].className = "hidden-block";
        //     } else {
        //         oBranch.setAttribute("aria-hidden", "false");
        //         cSubBranches[1].className = "visible-block";
        //     }
        // }
    }
}

/* toggleBranch */

// --- This function makes nested list items look like links
function addLinksToBranches(oList) {
    let cBranches;
    if (oList.tagName === 'li') {
        cBranches = oList;
    } else {
        cBranches = oList.getElementsByTagName("li")
    }

    var i, n, cSubBranches;
    if (cBranches.length > 0) {
        for (i = 0, n = cBranches.length; i < n; i++) {
            cSubBranches = cBranches[i].getElementsByTagName("ul");
            if (cSubBranches.length > 0) {
                addLinksToBranches(cSubBranches[0]);
                cBranches[i].className = "has-children";
                cBranches[i].setAttribute("aria-hidden", "true");
            }
        }
    }
}

$(document).on("click", "[data-action='save-ipobject']", function (e) {
    e.preventDefault();
    let panelContainer = $(this).data('container');
    let editModePanels = $('#' + panelContainer).find('.panel[data-mode="edit"]');
    if (editModePanels.length > 0) {
        let titles = [];
        editModePanels.each(function (index, element) {
            let title = $(element).find(".panel-title").text();
            if (title !== undefined && title !== '') {
                titles.push(title)
            }
        });
        let joinTitles = titles.join(', ');
        Confirmation.openInfoModal(messages['open.panels.error.message'] + joinTitles);
    } else {
        UIBlocker.block();
        let callParams = {
            editedPanels: getEditedPanelIds(panelContainer),
            sessionIdentifier: $('#session-object-identifier').val()
        };
        let url = $(this).data('url');
        RequestUtils.post(url, callParams);
    }
});

function initUserAutocomplete($autocomplete) {
    if ($autocomplete.length == 0) {
        return;
    }
    let url = $autocomplete.data('url');
    $autocomplete.autocomplete({
        delay: 500,
        minLength: 1,
        source: function (request, response) {
            var ajaxRequest = new CommonAjaxRequest(url, {
                requestData: {name: request.term, onlyActiveUsers : $autocomplete.attr("data-flag")},
                method: "GET",
                dataType: "json",
                useSessionId: false
            });
            var responseAction = new CommonAjaxResponseAction({executeOnValid: new FuncCall(response, [])});

            commonAjaxCall(ajaxRequest, responseAction);
        },
        select: function (event, ui) {
            let inputValue = ui.item.userName;
            $autocomplete.val(inputValue);
            $autocomplete.attr("data-id", ui.item.userId);
            return false;
        },
        change: function (event, ui) {
            if (ui.item == null) {
                $(this).val('');
                $(this).attr("data-id", '');
                $(this).siblings('label').removeClass('active');
            }
        }
    }).autocomplete("instance")._renderItem = function (ul, item) {
        return $("<li></li>")
            .data("item.autocomplete", item)
            .append("<a><span>" + item.userName + "</span></a>").appendTo(ul);
    };
}

function setCorrectAutocompleteWidth() {
    jQuery.ui.autocomplete.prototype._resizeMenu = function () {
        var ul = this.menu.element;
        ul.outerWidth(this.element.outerWidth());
    }
}

// Restricts input for the set of matched elements to the given inputFilter function.
(function ($) {
    $.fn.inputFilter = function (inputFilter) {
        return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function () {
            if (inputFilter(this.value)) {
                this.oldValue = this.value;
                this.oldSelectionStart = this.selectionStart;
                this.oldSelectionEnd = this.selectionEnd;
            } else if (this.hasOwnProperty("oldValue")) {
                this.value = this.oldValue;
                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
            } else {
                this.value = "";
            }
        });
    };
}(jQuery));

function initToastCloseBtn() {
    jQuery('.toast__close').click(function (e) {
        e.preventDefault();
        let parent = $(this).parent('.toast');
        parent.fadeOut("slow", function () {
            $(this).remove();
        });
    });
}

jQuery(document).ready(function () {
    initToastCloseBtn();

    if($('.right-message-panel').length > 0){
        let inner = $('.right-message-panel').closest(".inner");
        inner.addClass('p-change');
    }

});