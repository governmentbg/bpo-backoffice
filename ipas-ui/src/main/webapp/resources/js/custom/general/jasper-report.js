function runJasperReport(baseUrl, fileType){
    UIBlocker.block();

    var intervalID;
    var curData;

    $.ajax({
        type: 'get',
        url: baseUrl +
            "/create-report" +
            "?export-file-format=" + fileType,
        success: function (data) {
            curData = data;
            intervalID = setInterval(callCheckReport, 5000);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            UIBlocker.unblock();
        }
    });

    function callCheckReport() {
        $.ajax({
            type: 'get',
            url: baseUrl +
                "/check-report" +
                "?request-id=" + curData.requestId +
                "&export-id=" + curData.exportId +
                "&j_cookie=" + curData.cookie[0],
            success: function (data) {
                if (data.value === "ready" || data.value === "failed") {
                    clearInterval(intervalID);

                    UIBlocker.unblock();
                }
                if (data.value === "ready") {
                    var destinationUrl = baseUrl +
                        "/download" +
                        "?request-id=" + curData.requestId +
                        "&export-id=" + curData.exportId +
                        "&j_cookie=" + curData.cookie[0] +
                        "&export-file-format=" + fileType;

                    document.location = destinationUrl;
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                UIBlocker.unblock();
            }
        });
    }
}