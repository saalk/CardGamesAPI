// Set all the services on the `cardGamesApp` module using factories and not services
//
// Separating the data services moves the logic on how to get it to the data service, and lets the
// controller be simpler and more focused on the view.
//
// Strict mode changes previously accepted "bad syntax" into real errors.
// mistyping a variable name creates a new global variable. In strict mode, this will throw an
// error, making it impossible to accidentally create a global variable.

angular
    .module('cardGamesApp')
    .factory('logger', logger);

logger.$inject = ['$log', 'toastr'];

function logger($log, toastr) {
        var service = {
            showToasts: true,

            error   : error,
            info    : info,
            success : success,
            warning : warning,

            // straight to console; bypass toastr
            log     : $log.log
        };

        return service;
        ///////////////////// use with for example logger.error('message')

        function error(message, data, title) {
            toastr.error(message, title);
            $log.error('Error: ' + message, data);
        }

        function info(message, data, title) {
            toastr.info(message, title);
            $log.info('Info: ' + message, data);
        }

        function success(message, data, title) {
            toastr.success(message, title);
            $log.info('Success: ' + message, data);
        }

        function warning(message, data, title) {
            toastr.warning(message, title);
            $log.warn('Warning: ' + message, data);
        }
}
