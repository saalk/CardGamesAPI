// Set an angular module for our cardGamesApp
// Define each module with the angular.module(name, [requires]) syntax only once
// Retrieve it for subsequent use with angular.module(name)
//
// 1. 'ngRoute' is for angular-route
// 2. 'ngAnimate', 'toastr' is for angular-toastr (
angular.module('myApp', ['ngRoute', 'ngResource', 'ngAnimate', 'toastr']);
angular.module('myApp')
        .config(function($httpProvider, $routeProvider, $locationProvider, toastrConfig) {
            
    $httpProvider.defaults.headers.common = {};
    $httpProvider.defaults.headers.post = {};
    $httpProvider.defaults.headers.put = {};
    $httpProvider.defaults.headers.patch = {};


    $routeProvider.when('/player', {
       templateUrl: 'partials/player.html'
    }).when('/game', {
       templateUrl: 'partials/game.html'
    }).when('/casino', {
       templateUrl: 'partials/casino.html'
    }).when('/results', {
       templateUrl: 'partials/results.html'  
    }).otherwise({
        redirectTo: '/player'
    });

    // use the HTML5 History API
    $locationProvider.html5Mode(true);

    angular.extend(toastrConfig, {
        newestOnTop: false,
        preventDuplicates: true,
        allowHtml: true,
        closeButton: false,
        closeHtml: '<button>&times;</button>',
        extendedTimeOut: 1000,
        iconClasses: {
          error: 'toast-error',
          info: 'toast-info',
          success: 'toast-success',
          warning: 'toast-warning'
        },
        messageClass: 'toast-message',
        onHidden: null,
        onShown: null,
        onTap: null,
        progressBar: true,
        tapToDismiss: true,
        templates: {
          toast: 'directives/toast/toast.html',
          progressbar: 'directives/progressbar/progressbar.html'
        },
        timeOut: 1500,
        titleClass: 'toast-title',
        toastClass: 'toast'
    });

});