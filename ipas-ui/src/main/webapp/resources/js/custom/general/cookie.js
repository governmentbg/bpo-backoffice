function delCookie(name) {
    let date = new Date();
    date.setTime(date.getTime() - (24 * 60 * 60 * 1000));
    document.cookie = name + "= ; expires=" + date.toGMTString();
}

function setCookie(name, value) {
    document.cookie = name + "=" + value + " ; expires=; expires=Fri, 31 Dec 9999 23:59:59 GMT;";
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (i = 0; i < ca.length; i++) {
        c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function getCookieAsJson(cname) {
    let cookie = getCookie(cname);
    if (cookie === "") {
        return {};
    }
    return JSON.parse(getCookie(cname));
}

$(document).ready(function () {
    let cookie_name = $("#detail-list").attr("data-group");
    let sess_obj = getCookieAsJson(cookie_name);

    if (!(cookie_name === undefined || cookie_name === '')) {
        $(".sb-ckb").each(function () {
            let data_group = $(this).parent().attr("data-group");
            let data_checked = $(this).parent().attr("data-checked");
            if (sess_obj[data_group] === undefined) {
                sess_obj[data_group] = !!data_checked;
            }
        });

        setCookie(cookie_name, JSON.stringify(sess_obj));
    }
});
