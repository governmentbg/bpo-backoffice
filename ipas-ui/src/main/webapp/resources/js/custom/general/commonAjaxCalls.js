/*
Represents a function call. Parameters:
    func: is the function name without ()
    args: an array with the function arguments in the order that they are listed in the function signature

Note: the func can be defined to take data as last parameter or not.
The data argument should be added only if the function will be called
with the help of getExecutableFuncCallWithData
 */
function FuncCall(func, args = []){
    this.func = func;
    this.args = args;
}

/*
In order to execute a FuncCall in ajax success/error an executable function is needed.
This method adds the ajax returned data to the call arguments and delegates to getExecutableFuncCall for
the actual applying of the arguments and returning of the function. Parameters:
    funcCall: a FuncCall object
    data: the ajax data. Can be html, can be json
 */
function getExecutableFuncCallWithData(funcCall, data){
    funcCall.args.push(data);
    return getExecutableFuncCall(funcCall);
}

/*
A helper function that returns an executable function that has all of the arguments applied. Parameters:
    funcCall: a FuncCall object
 */
function getExecutableFuncCall(funcCall){
    var args = Array.prototype.slice.call(funcCall.args, 0);
    return function() {
        return funcCall.func.apply(this, args);
    };
}

/*
An object that contains data about the request that is to be made with ajax. Parameters:
    url: the url that is to be called with ajax
    options: an object containing all/some/none of the following optional options:
        - method: the request method. By default "POST"
        - useSessionId: a boolean flag specifying if session id should be taken
            from html and added to the request data. By default true
        - requestData: the actual request data. A javascript object.
        - datatype: the type of the request - 'html', 'json'. By default 'html'

 */
function CommonAjaxRequest(url, options){
    if(options.method == null){
        options.method = "POST";
    }
    if(options.useSessionId == null){
        options.useSessionId = true;
    }
    if(options.requestData == null){
        options.requestData = {};
    }
    if(options.datatype == null){
        options.datatype = "html";
    }
    if(options.blockUI == null){
        options.blockUI = false;
    }
    if(options.processData == null){
        options.processData = true;
    }
    if(options.contentType == null){
        options.contentType = 'application/x-www-form-urlencoded; charset=UTF-8';
    }
    if(options.async == null){
        options.async = true;
    }
    this.url = url;
    this.method = options.method;
    this.requestData = options.requestData;
    this.datatype = options.datatype;
    this.blockUI = options.blockUI;
    this.processData = options.processData;
    this.contentType = options.contentType;
    this.async = options.async;

    if (options.useSessionId) {
        let val = $('#session-object-identifier').val();
        if (val !== undefined){
            this.requestData.sessionIdentifier = val;
        }
    }
}

/*
An object that specifies what should happen when the ajax response is returned - either on success or on error. Parameters:
    options: an object containing all/some/none of the following optional options:
        - validationFailedExpression: an expression to be evaluated in order to determine whether validation has failed.
            Usually the expression is calculated over the response contents - the data.
            Examples: data != null; data.indexOf('someErrorTagId') != -1
        - updateContainerOnValid: a selector for the container/containers to be updated when validationFailedExpression evaluates to false.
            Valid selectors are also with multiple ids, for example: "#someId, #anotherId".
        - updateContainerOnInvalid: a selector for the container/containers to be updated when validationFailedExpression evaluates to true.
            Valid selectors are also with multiple ids, for example: "#someId, #anotherId".
        - executeOnValid: a FuncCall object with a function that will be called with the help of getExecutableFuncCallWithData,
            on success, when validationFailedExpression is null or when validationFailedExpression evaluates to false
        - executeOnInvalid: a FuncCall object with a function that will be called with the help of getExecutableFuncCallWithData,
            on success, when validationFailedExpression evaluates to true
        - postprocessExecute: a FuncCall object with a function that will be called with the help of getExecutableFuncCallWithData,
            on success, always no matter of the validation result
        - executeOnError: a FuncCall object with a function that will be called with the help of getExecutableFuncCallWithData,
            on error occurring

Note: containers are updated only when request's datatype is 'html'
 */
function CommonAjaxResponseAction(options){
    this.validationFailedExpression = options.validationFailedExpression;
    this.updateContainerOnValid = options.updateContainerOnValid;
    this.updateContainerOnInvalid = options.updateContainerOnInvalid;
    this.executeOnValid = options.executeOnValid;
    this.executeOnInvalid = options.executeOnInvalid;
    this.postprocessExecute = options.postprocessExecute;
    this.executeOnError = options.executeOnError;
}

/*
A multipurpose ajax call. Parameters:
    ajaxRequest: an object of type CommonAjaxRequest
    ajaxResponseAction: an object of type CommonAjaxResponseAction

Note: on error a modal with the error info is displayed always and after the provided (if provided)
FuncCall to be executed on error
 */
function commonAjaxCall(ajaxRequest, ajaxResponseAction){
    if(ajaxResponseAction == null){
        ajaxResponseAction = new CommonAjaxResponseAction({});
    }

    if (ajaxRequest.blockUI) {
        UIBlocker.block()
    }

    $.ajax({
        url: ajaxRequest.url,
        type: ajaxRequest.method,
        async: ajaxRequest.async,
        datatype: ajaxRequest.datatype,
        processData: ajaxRequest.processData,
        contentType: ajaxRequest.contentType,
        data: ajaxRequest.requestData,
        success: function (data) {
            if(ajaxResponseAction.validationFailedExpression != null && eval(ajaxResponseAction.validationFailedExpression)){
                if(ajaxResponseAction.updateContainerOnInvalid != null && ajaxRequest.datatype == "html") {
                    $(ajaxResponseAction.updateContainerOnInvalid).html(data);
                    initBindFirstValidation(ajaxResponseAction.updateContainerOnInvalid);
                }

                if(ajaxResponseAction.executeOnInvalid != null) {
                    var execOnInvalid = getExecutableFuncCallWithData(ajaxResponseAction.executeOnInvalid, data);
                    execOnInvalid();
                }
            } else {
                if(ajaxResponseAction.updateContainerOnValid != null && ajaxRequest.datatype == "html") {
                    $(ajaxResponseAction.updateContainerOnValid).html(data);
                    initBindFirstValidation(ajaxResponseAction.updateContainerOnValid);
                }

                if(ajaxResponseAction.executeOnValid != null) {
                    var execOnValid = getExecutableFuncCallWithData(ajaxResponseAction.executeOnValid, data);
                    execOnValid();
                }
            }

            if(ajaxResponseAction.postprocessExecute != null){
                var execPostprocess = getExecutableFuncCallWithData(ajaxResponseAction.postprocessExecute, data);
                execPostprocess();
            }

            if (ajaxRequest.blockUI) {
                UIBlocker.unblock()
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (ajaxRequest.blockUI) {
                UIBlocker.unblock()
            }

            if(ajaxResponseAction.executeOnError != null){
                var execError = getExecutableFuncCallWithData(ajaxResponseAction.executeOnError, xhr);
                execError();
            }
            openErrorModal(xhr);
        }
    })
}

/*
Function to open modal with error information. Parameters:
    xhr: contains info in the form of json.
 */
function openErrorModal(xhr) {
    let responseJson = xhr.responseJSON;
    $('#modal-error-exception-error').text(responseJson.error);
    $('#modal-error-exception-message').text(responseJson.message);
    $('#modal-error-exception-status').text(xhr.status);
    $('#modal-error-exception-trace').text(responseJson.trace);
    M.Modal.getInstance($('#error-modal-exception')).open();
}

function updateWithAjaxAndUnblock(request, updateComponentSelector) {
    let responseAction = new CommonAjaxResponseAction({
        updateContainerOnValid: updateComponentSelector,
        executeOnValid: new FuncCall(UIBlocker.unblock, []),
        executeOnError: new FuncCall(UIBlocker.unblock, [])
    });
    commonAjaxCall(request, responseAction);
}


