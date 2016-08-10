// filters for generic purpose
cardGamesApp.filter('leadingZeros', function(){
    return function(input, size) {
        var zero = (size ? size : 4) - input.toString().length + 1;
        return Array(+(zero > 0 && zero)).join("0") + input;
    }
});