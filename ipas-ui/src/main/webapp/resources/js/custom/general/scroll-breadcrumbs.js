window.onscroll = function() {myFunction()};

function myFunction() {
    if (window.pageYOffset > 0) {
        $('#breadcrumbs').addClass('trans');
    } else {
        $('#breadcrumbs').removeClass('trans');
    }
}