function extend(source) {
    var target = this;
    target = Object.assign(this, source);
}

var Confirmation = {extend: extend};
Confirmation.extend({
    exist: function (button) {
        return button.hasClass('confirm');
    },
    /*
    Opens a dismissible confirm modal that in order to display yes button clones a trigger button/link. Params:
        button: the trigger button that will be cloned and displayed as yes button
        message: the message to be displayed by the confirm modal
     */
    openConfirmCloneBtnModal: function (button, message) {
        let clone = button.clone(true);

        let $confirmModal = $('#confirm-modal');
        $confirmModal.modal({
            onOpenStart: function (modal, source) {
                let div = $(modal).find('#confirm-modal-yes-div');
                $(source).addClass('modal-close');
                $(source).attr('class', 'modal-close button bg__dark-green minw_100');

                let yesText = div.data('yes');
                $(source).html(yesText);

                if (null === message || undefined === message) {
                    let messageDiv = $(modal).find('#confirm-modal-message');
                    messageDiv.empty();
                    messageDiv.append(messageDiv.data('text'));
                } else {
                    let messageDiv = $(modal).find('#confirm-modal-message');
                    messageDiv.empty();
                    messageDiv.append(message);
                }

                div.empty();
                div.append(source);
            },
        });
        UIBlocker.unblock();
        M.Modal.getInstance($confirmModal).open(clone);
    },
    /*
    Opens a non dismissible confirm modal that performs one action on cancel and another action on continue. Params:
        message: the message that the modal will display. If not passed, the message is a general, default one
        onYes: an executable function built with the help of getExecutableFuncCall that will be called when user presses continue btn
        onNo: optional, an executable function built with the help of getExecutableFuncCall that will be called when user presses cancel btn
     */
    openConfirmYesNoFuncsModal: function (message, onYes, onNo, yesBtnLabel, noBtnLabel, closeBtnExist, closeBtnLabel, onClose) {
        let $confirmModal = $('#confirm-funcs-modal');

        $confirmModal.modal({
            dismissible: false,
            onOpenStart: function (modal, modalTrigger) {
                if (null === message || undefined === message) {
                    message = $(modal).find('#confirm-funcs-modal-basic-text').data("message");
                }
                $(modal).find('#confirm-funcs-modal-message').html(message);

                let yesBtn = $(modal).find('#action-continue-btn');
                let noBtn = $(modal).find('#action-cancel-btn');

                if (null === yesBtnLabel || undefined === yesBtnLabel) {
                    yesBtnLabel = yesBtn.data("label");
                }
                yesBtn.text(yesBtnLabel);

                if (null === noBtnLabel || undefined === noBtnLabel) {
                    noBtnLabel = noBtn.data("label");
                }
                noBtn.text(noBtnLabel);

                noBtn.off("click");
                yesBtn.off("click");

                if (null != onNo && undefined != onNo) {
                    noBtn.on("click", function () {
                        onNo();
                    });
                }

                yesBtn.on("click", function () {
                    onYes();
                });

                let closeBtnWrapper = $(modal).find('#action-close-btn-wrapper');
                if (closeBtnExist === true) {
                    closeBtnWrapper.removeClass('none');
                    let closeBtn = $(modal).find('#action-close-btn');
                    if (null === closeBtnLabel || undefined === closeBtnLabel) {
                        closeBtnLabel = closeBtn.data("label");
                    }
                    closeBtn.text(closeBtnLabel);

                    closeBtn.off("click");

                    if (null != onClose && undefined != onClose) {
                        closeBtn.on("click", function () {
                            onClose();
                        });
                    }
                } else {
                    let isHidden = closeBtnWrapper.hasClass('none');
                    if (!isHidden) {
                        closeBtnWrapper.addClass('none');
                    }
                }

            },
            onCloseEnd: function () {
                $('body').css({
                    overflow: 'visible'
                });
            },
        });
        UIBlocker.unblock();
        M.Modal.getInstance($confirmModal).open();
    },
    closeConfirmYesNoFuncsModal: function () {
        let $confirmModal = $('#confirm-funcs-modal');
        M.Modal.getInstance($confirmModal).close();
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