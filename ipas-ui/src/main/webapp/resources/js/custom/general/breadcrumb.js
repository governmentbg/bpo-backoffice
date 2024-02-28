$(document).ready(function () {
    let ipObjectFilingNumber = '';
    ipasObjectDetailPageTitles.forEach((element, i) => {
        if (document.title.startsWith(element)) {
            ipObjectFilingNumber = document.title.replace(element, '').trim();
        }
    });
    createBreadcrumb(document.title, document.location.href, ipObjectFilingNumber);
});

const ipasObjectDetailPageTitles = [
    messages["mark.breadcrumb.prefix"],
    messages["gi.breadcrumb.prefix"],
    messages["design.breadcrumb.prefix"],
    messages["eupatent.breadcrumb.prefix"],
    messages["patent.breadcrumb.prefix"],
    messages["plant.breadcrumb.prefix"],
    messages["spc.breadcrumb.prefix"],
    messages["um.breadcrumb.prefix"],
    messages["userdoc.breadcrumb.prefix"],
    messages["offidoc.breadcrumb.prefix"]
];

const excludeBreadcrumbPageTitles = [
    messages["error.title"],
    messages["error.not.found"],
    messages["error.forbidden"],
    messages["error.internal.server.error"],
    messages["existing.object.title"],
];

const resetBreadcrumbPageTitles = [
    {
        title: messages["home.page.title"],
        excludeFrom: []
    },
    {
        title: messages["admin.page.title"],
        excludeFrom: []
    },
    {
        title: messages["search.mark.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.geographical_indications.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.design.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.international-design.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.patent.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.patent-like.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.utility_model.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.eupatent.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.spc.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.plants_and_breeds.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    },
    {
        title: messages["search.userdoc.title"],
        excludeFrom: [
            messages["reception.page.title"]
        ]
    }
];

function isRefresh(data) {
    let breadcrumbData = sessionStorage.breadcrumb;
    if (breadcrumbData) {
        let array = JSON.parse(sessionStorage.breadcrumb);
        let arrayWithLastElement = array.slice(array.length - 1, array.length);
        let lastElementData = arrayWithLastElement[0];
        return JSON.stringify(data) === JSON.stringify(lastElementData);
    } else {
        return false;
    }
}

function removeLastElementFromSession() {
    let breadcrumbData = sessionStorage.breadcrumb;
    if (breadcrumbData) {
        let array = JSON.parse(sessionStorage.breadcrumb);
        let arrayWithLastElement = array.slice(array.length - 1, array.length);
        removeBreadcrumbSessionObjects(JSON.stringify(arrayWithLastElement), false);
    }
}

function removeBreadcrumbSessionObjects(breadcrumbData, excludeLastElement) {
    if (breadcrumbData) {
        let url = applicationContextPath + 'session/remove-breadcrumb-objects';
        let callParams = {
            data: breadcrumbData,
            excludeLastElement: excludeLastElement
        };
        let request = new CommonAjaxRequest(url, {requestData: callParams});
        let responseAction = new CommonAjaxResponseAction({});
        commonAjaxCall(request, responseAction);
    }
}

function createBreadcrumb(text, link, ipObjectFilingNumber) {
    if (typeof (Storage) != "undefined") {
        let data = {text: text, link: link, ipObjectFilingNumber: ipObjectFilingNumber}
        if (!isRefresh(data)) {
            generateBreadcrumbDataInSessionStorage(data);
            appendBreadcrumbToPage();
            removeBreadcrumbSessionObjects(sessionStorage.breadcrumb, true);
        } else {
            appendBreadcrumbToPage();
        }
    }
}

function generateBreadcrumbDataInSessionStorage(data) {
    resetBreadcrumbIfNecessary(data);
    if (sessionStorage.breadcrumb) {
        let array = JSON.parse(sessionStorage.breadcrumb);
        let contains = array.some(element => {
            return JSON.stringify(data) === JSON.stringify(element);
        });
        if (!contains) {
            array = pushNotExcluded(data, array);
        } else {
            removeLastElementFromSession();
            array = removeAllElementsOfArrayWhichIsAfterExistingElement(array, data);
        }
        sessionStorage.breadcrumb = JSON.stringify(array);
    } else {
        sessionStorage.breadcrumb = JSON.stringify([data]);
    }
}

function removeAllElementsOfArrayWhichIsAfterExistingElement(array, data) {
    let index;
    array.forEach((element, i) => {
        if (JSON.stringify(data) === JSON.stringify(element)) {
            index = i;
        }
    });
    array = array.slice(0, index + 1);
    return array;
}

function pushNotExcluded(data, array) {
    let isExcluded = excludeBreadcrumbPageTitles.some(element => {
        return element === data.text;
    });
    if (!isExcluded) {
        let hasDuplicateText = array.some(element => {
            return element.text === data.text;
        });
        if (hasDuplicateText) {
            array = replaceElementWithMatchedText(array, data)
        } else {
            array.push(data);
        }
    }
    return array;
}

function resetBreadcrumbIfNecessary(data) {
    let resetObject = null;
    resetBreadcrumbPageTitles.forEach((element, i) => {
        if (element.title === data.text) {
            resetObject = element;
        }
    });
    if (resetObject !== null) {
        let stopReset = false;
        let lastBreadcrumbElement = selectLastElementOfBreadcrumb();
        if (lastBreadcrumbElement !== null) {
            stopReset = resetObject.excludeFrom.some(element => {
                return element === lastBreadcrumbElement.text;
            });
        }
        if (!stopReset) {
            removeBreadcrumbSessionObjects(sessionStorage.breadcrumb, false);
            sessionStorage.removeItem('breadcrumb');
        }
    }
}

function appendBreadcrumbToPage() {
    let breadcrumbsArray = JSON.parse(sessionStorage.breadcrumb);
    breadcrumbsArray.forEach((element, i) => {
        if (i === (breadcrumbsArray.length - 1)) {
            $('#breadcrumbs:not(".ignore-default-breadcrumb") ol.crumbs').append('<li>' + element.text + '</li>')
        } else {
            $('#breadcrumbs:not(".ignore-default-breadcrumb") ol.crumbs').append('<li>' + '<a class="b0" href="' + element.link + '">' + element.text + '</a></li>')
        }
    });
}

function replaceElementWithMatchedText(array, data) {
    let duplicatedElement = null;
    let duplicatedElementIndex = null;
    array.forEach((element, i) => {
        if (element.text === data.text) {
            duplicatedElement = element;
            duplicatedElementIndex = i;
        }
    });
    array[duplicatedElementIndex] = data;
    return removeAllElementsOfArrayWhichIsAfterExistingElement(array, data);
}

function selectLastElementOfBreadcrumb() {
    if (sessionStorage.breadcrumb) {
        let array = JSON.parse(sessionStorage.breadcrumb);
        if (array === undefined || array.length === 0) {
            return null;
        }
        return array[array.length - 1];
    } else {
        return null;
    }
}