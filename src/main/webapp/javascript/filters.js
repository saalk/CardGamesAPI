// filters for generic purpose
angular
    .module('myApp')
    .filter('leadingZeros', function(){
    return function(input, size) {
        var zero = (size ? size : 4) - input.toString().length + 1;
        return Array(+(zero > 0 && zero)).join("0") + input;
    };
});