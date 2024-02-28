function extend(source) {
    var target = this;
    target = Object.assign(this, source);
}

var BaseModal = {extend: extend};
BaseModal.extend({
    openErrorModal: function (message) {
        let $errorModal = $('#error-modal');
        $errorModal.modal({
            onOpenStart: function (modal, modalTrigger) {
                $(modal).css({
                    width: '65%'
                });
                $(modal).find('#error-modal-message').html(message);
            },
            onCloseEnd: function () {
                $('body').css({
                    overflow: 'visible'
                });
            },
        });
        M.Modal.getInstance($errorModal).open();
    },
    openSuccessModal: function (message) {
        let $successModal = $('#success-modal');
        $successModal.modal({
            onOpenStart: function (modal, modalTrigger) {
                $(modal).css({
                    width: '65%'
                });
                $(modal).find('#success-modal-message').html(message);
            },
            onCloseEnd: function () {
                $('body').css({
                    overflow: 'visible'
                });
            },
        });
        M.Modal.getInstance($successModal).open();
    },
    openInfoModal: function (message) {
        let $confirmModal = $('#info-modal');
        $confirmModal.modal({
            onOpenStart: function (modal, modalTrigger) {
                $(modal).css({
                    width: '65%'
                });
                $(modal).find('#info-modal-message').html(message);
            },
            onCloseEnd: function () {
                $('body').css({
                    overflow: 'visible'
                });
            },
        });
        M.Modal.getInstance($confirmModal).open();
    }
});