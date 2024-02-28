var UIBlocker = {
    block: function () {
        let $block = $('div.blockUI.blockOverlay');
        if ($block.length === 0) {
            $.blockUI({
                message: $('#block-ui-div'),
                baseZ: 9999,
                css:
                    {
                        padding: 0,
                        margin: 0,
                        width: '100%',
                        top: '0',
                        left: '0',
                        textAlign: 'center',
                        border: "none",
                        backgroundColor: '#000',
                        opacity: 0.8,
                    }
            });
        }
    },
    unblock: function () {
        $.unblockUI();
    },
};

$(document).on("click", ".crumbs a", function (e) {
    if ($(this).attr('href') !== undefined){
        UIBlocker.block();
    }
});

$(document).on("click", "[data-blockui='true']", function (e) {
    UIBlocker.block();
});

$(document).on("click", "a.logo-home", function (e) {
    UIBlocker.block();
});