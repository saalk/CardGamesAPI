//Set an angular module for our cardGamesApp
angular
    .module('cardGamesApp', ['ngRoute']);

//Set Routing for cardGamesApp angular module
angular
    .module('cardGamesApp')
    .config(config);

function config($routeProvider) {
            $routeProvider.when('/home', {
               templateUrl: 'partials/home.html',
               controller: 'HomeController'
            }).when('/casino', {
               templateUrl: 'partials/casino.html',
               controller: 'HomeController'
            }).otherwise({
                redirectTo: '/home'
            });
}