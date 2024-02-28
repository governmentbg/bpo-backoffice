/*
      * This libary is for froms.
      * author: Vencislav Nikolov
      * use:
      *     for initialization panel: Panel.init($("#panel-workflow"));
      *     for initialization input-field: FF.init($("#panel-workflow")); FF -> Form Field
      * */
function extend(source){
    var target = this;
    target = Object.assign(this, source);
}

var Panel = {extend: extend};

Panel.extend({
    init: function(p_name){
        var panel_name = p_name;

        var edit_link = panel_name.find("[data-action='edit']");
        var save_link = panel_name.find("[data-action='save']");
        var cancel_link = panel_name.find("[data-action='cancel']");
        var edit_span = panel_name.find("span.edit");
        var edited_span = panel_name.find("span.edited");

        edit_link.click(function(){
            panel_name.find(".input-field:not(.readonly)").each(function (index, input_field) {
                FF.edit($(input_field));
            });

            panel_name.find(".hidden-element").each(function (index, input_field) {
                $(this).removeClass('none');
            });

            edit_link.addClass('none');
            save_link.removeClass('none');
            cancel_link.removeClass('none');

            edit_span.removeClass('none');
            if (edited_span.length > 0 && !edited_span.hasClass("none")) {
                edit_span.addClass('edited');
                edited_span.addClass('none');
            }

            panel_name.attr('data-mode','edit');
        });

        save_link.click(function(){
            panel_name.find(".input-field").each(function (index, input_field) {
                FF.save($(input_field));
            });

            panel_name.find(".hidden-element").each(function (index, input_field) {
                $(this).addClass('none');
            });

            edit_link.removeClass('none');
            save_link.addClass('none');
            cancel_link.addClass('none');

            edited_span.removeClass('none');
            edit_span.addClass('none');

            panel_name.attr('data-mode','view');
        });

        cancel_link.click(function(){
            panel_name.find(".input-field").each(function (index, input_field) {
                FF.cancel($(input_field));
            });

            panel_name.find(".hidden-element").each(function (index, input_field) {
                $(this).addClass('none');
            });

            edit_link.removeClass('none');
            save_link.addClass('none');
            cancel_link.addClass('none');

            edit_span.addClass('none');
            if (edit_span.hasClass("edited")) {
                edit_span.removeClass('edited');
                edited_span.removeClass('none');
            }

            panel_name.attr('data-mode','view');
        });
        executeCommonInitialization({
            initializeFormElementsWrapper: "#"+$(panel_name).attr('id'),
        });
        // let init_panel = $(panel_name).get(0);
        // M.AutoInit(init_panel);

        panel_name.find(".input-field:not(.readonly, .search)").each(function (index, input_field) {
            FF.init($(input_field));
        });

        panel_name.find(".hidden-element").each(function (index, button) {
            $(this).addClass('none');
        });
    }
});

var FF = {extend: extend};
FF.extend({
    tagName: function(elem) {
        return elem.prop("tagName");
    },
    getFromElement: function(elem){
        if(elem.find("input").length === 1){
            return elem.find("input");
        }
        if(elem.find("select").length === 1){
            return elem.find("select");
        }
        if(elem.find("textarea").length === 1){
            return elem.find("textarea");
        }
    },
    edit: function(elem) {
        var form_element = FF.getFromElement(elem);
        if (elem.parent().hasClass('none')) {
            elem.parent().removeClass('none');
        }

        if (form_element.attr("data-type") === "datepicker") {
            initDatepicker(form_element);
            form_element.removeAttr('disabled');
        } else if ($(elem).children().hasClass('select-wrapper disabled')) {
            $(elem).find('.select-wrapper').removeClass('disabled');
            $(elem).find('input[type="text"]').removeAttr('disabled');
            $(elem).find('select').removeAttr('disabled');
            if ($(elem).find('ul.select-dropdown').attr('tabindex') === undefined) {
                $(elem).find('ul.select-dropdown').attr('tabindex', 0);
                $(elem).find('ul.select-dropdown').children().each(function (index, input_field) {
                    $(input_field).attr('tabindex', 0);
                });
            }
            $(elem).find('input.select-dropdown').dropdown({'hover': false, 'closeOnClick': true});
        } else {
            form_element.removeAttr('disabled');
        }
    },
    cancel: function(elem) {

        var form_element = FF.getFromElement(elem);
        if (form_element !== undefined) {
            if (form_element.val() === ''){
                elem.parent().addClass('none');
            }

            if (form_element.attr("data-type") === "datepicker") {
                form_element.datepicker("destroy");
                form_element.attr("disabled", "true");
            } else if ($(elem).children().hasClass('select-wrapper')) {
                $(elem).find('select').attr('disabled', true);
                $(elem).find('input[type="text"]').attr('disabled', true);
                $(elem).find('.select-wrapper').addClass('disabled');
            } else {
                form_element.attr("disabled", "true");
            }
        }
    },
    save: function(elem) {
        FF.cancel(elem);
    },
    init: function(el) {
        FF.cancel(el);
    }

});