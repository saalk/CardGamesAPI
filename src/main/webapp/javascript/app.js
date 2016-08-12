//Define an angular module for our cardGamesApp

var cardGamesApp;
cardGamesApp = angular.module('cardGamesApp', ['ngRoute']);

//Define Routing for cardGamesApp
//Uri /home -> partial home.html and Controller HomeController
//Uri /casino -> partial casino.html and Controller CasinoController
cardGamesApp.config(function ($routeProvider) {
$routeProvider.when('/home', {
templateUrl: 'partials/home.html',
controller: 'HomeController'
}).when('/casino', {
templateUrl: 'partials/casino.html',
controller: 'CasinoController'
}).otherwise({
redirectTo: '/home'
});
});