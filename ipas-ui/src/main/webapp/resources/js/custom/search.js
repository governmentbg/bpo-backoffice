function getSearchUrlParameter(url, name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\)]/, '\\]');
    let regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    let results = regex.exec(url);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}
function preselectUserField(fieldId, selectedId) {

    if(selectedId != '') {
        let ids = [];
        ids.push(selectedId);
        preselectedItemsWithAjax(ids,
            $('#' + fieldId),
            $('#' + fieldId).data("get-url"),
            "userId",
            function (data) {
                return data.userName;
            },
            function (data) {
                return data.userId;
            });
    }
}

function preselectedItemsWithAjax(fields, field_select, url, req_param, fn_option_text, fn_option_value) {
    // Fetch the preselected item, and add to the control
    if (fields !== null) {
        for (i = 0; i < fields.length; i++) {
            let field = fields[i];
            let req_data = {};
            req_data[req_param] = field;
            $.ajax({
                type: 'GET',
                url: url,
                data: req_data,
                success: function (data) {
                    let first_data;
                    if (data instanceof Array && data.length > 0) {
                        first_data = data[0];
                    } else {
                        first_data = data;
                    }

                    // create the option and append to Select2
                    let option;
                    if (first_data === undefined || first_data === null) {
                        option = new Option(field, field, false, true);
                    } else {
                        option = new Option(fn_option_text(first_data), fn_option_value(first_data), false, true);
                    }

                    if (option !== undefined) {
                        field_select.append(option).trigger('change');

                        // manually trigger the `select2:select` event
                        field_select.trigger({
                            type: 'select2:select',
                            params: {
                                data: data
                            }
                        });
                    }
                }
            });
        }
    }
}

function initSearchUserField(fieldId) {
    $('#' + fieldId).select2({
        placeholder: "",
        allowClear: true,
        dropdownParent: $('#dropdown-panel'),
        ajax: {
            url: $('#' + fieldId).data("search-url"),
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    userName: params.term
                };
            },
            processResults: function (data, params) {
                // parse the results into the format expected by Select2
                // since we are using custom formatting functions we do not need to
                // alter the remote JSON data, except to indicate that infinite
                // scrolling can be used
                params.page = params.page || 1;

                let cJson = [];

                data.forEach(function (a) {
                    let obj = {};

                    obj['id'] = a.userId;
                    obj['text'] = a.userName.trim();

                    cJson.push(obj);
                }, Object.create(null));

                return {
                    results: cJson,
                    pagination: {
                        more: (params.page * 30) < data.total_count
                    }
                };
            },
            cache: true
        },
        escapeMarkup: function (markup) {
            return markup;
        },
        templateResult: function (repo) {
            if (repo.loading) {
                return repo.text;
            }
            let markup = "<div class='select2-result-repository clearfix'>" +
                "<div style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + repo.text + "</div>" +
                "</div>";

            return markup;
        }
    });

    $('#' + fieldId).on('select2:open', function (e) {
        let label = $(this).attr("id").replace("field-", "#label-");
        $(label).addClass("active");
    });
    $('#' + fieldId).on('select2:close', function (e) {
        if ($(this).find(':selected').length === 0) {
            let label = $(this).attr("id").replace("field-", "#label-");
            $(label).removeClass("active");
            $(this).parent().find('.select2-selection__rendered').removeAttr("title");
        }
    });

    $('#' + fieldId).on('change', function (e) {
        if ($(this).find(':selected').val() == undefined || $(this).find(':selected').val() == "") {
            let label = $(this).attr("id").replace("field-", "#label-");
            $(label).removeClass("active");
            $(this).parent().find('.select2-selection__rendered').removeAttr("title");
        } else {
            let label = $(this).attr("id").replace("field-", "#label-");
            $(label).addClass("active");
        }
    });
}