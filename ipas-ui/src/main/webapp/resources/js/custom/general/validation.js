function validateBaseFormFields(element) {
    let $elements = element.find("input.number, input.amount, input.date, input.datetime, input[data-type=datepicker], input[data-regex]");
    let $result = true;
    $elements.each(function (index, element) {
        $(element).removeClass("invalid");
        $(element).removeAttr("title");
        if (!$(element).is(":disabled") && $(element).val() != '') {
            let $valid = true;
            let $message = "";
            if ($(element).hasClass("date") || $(element).attr("data-type") == 'datepicker') {
                $valid = isDate($(element).val(), "d.m.yyyy");
                $message = messages['wrong.date'];
            } else if ($(element).hasClass("datetime")) {
                $valid = isDate($(element).val(), "d.m.yyyy H:n");
                $message = messages['wrong.datetime'];
            } else if ($(element).hasClass("amount")) {
                let reg = new RegExp('^(\\d*\\.?\\d{0,2})$')
                $valid = reg.test($(element).val());
                $message = messages['wrong.amount'];
            } else if ($(element).hasClass("number")) {
                let reg = new RegExp('^[0-9]+$');
                $valid = reg.test($(element).val());
                $message = messages['wrong.number'];
                //if there is a start / end / number defined:
                if ($valid && ($(element).is("[data-start]") || $(element).is("[data-end]"))) {
                    let val = parseInt($(element).val());
                    $message += $(element).is("[data-start]") ? messages['wrong.number.start'] + $(element).data("start") + ". " : "";
                    $message += $(element).is("[data-end]") ? messages['wrong.number.end'] + $(element).data("end") + ". " : "";
                    $valid = $valid && ($(element).is("[data-start]") ? val >= parseInt($(element).data("start")) : true);
                    $valid = $valid && ($(element).is("[data-end]") ? val <= parseInt($(element).data("end")) : true);
                }
            } else if ($(element).is("[data-regex]")) {
                let $regex = new RegExp($(element).data("regex"));
                $valid = $regex.test($(element).val());
                $message = messages['wrong.regex'] + $regex;
            }
            if (!($valid)) {
                $(element).addClass("invalid");
                $(element).attr("title", $message);
            }
            // console.log($(element).attr("id") + "..." + $valid);
            $result = $result && $valid;
        }
    });
    return $result;
}


$(document).ready(function () {
    initBindFirstValidation(null);
});

function initBindFirstValidation(container) {
    let selector = '[data-validation-block]';
    if (container != null && container !== '') {
        selector = container + ' [data-validation-block]'
    }
    $(selector).bindFirst('click', function (e) {
        let $element = $(this);
        let validationBlock = $element.data('validation-block');
        if (!validateBaseFormFields($(validationBlock))) {
            Confirmation.openInfoModal(messages['please.fix.invalid.fields']);
            e.stopImmediatePropagation();
        }
    });
}