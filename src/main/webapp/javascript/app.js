// Set an angular module for our cardGamesApp
// Define each module with the angular.module(name, [requires]) syntax only once
// Retrieve it for subsequent use with angular.module(name)
//
// 1. 'ngRoute' is for angular-route
// 2. 'ngAnimate', 'toastr' is for angular-toastr (
angular.module('myApp', ['ngRoute', 'ngAnimate', 'toastr']);
angular.module('myApp')
        .config(function($routeProvider, toastrConfig) {
    $routeProvider.when('/home', {
       templateUrl: 'partials/home.html',
       controller: 'HomeController'
    }).when('/casino', {
       templateUrl: 'partials/casino.html',
       controller: 'CasinoController'
    }).when('/table', {
       templateUrl: 'partials/table.html',
       controller: 'TableController'
    }).when('/results', {
       templateUrl: 'partials/results.html',
       controller: 'ResultsController'       
    }).otherwise({
        redirectTo: '/home'
    });
    angular.extend(toastrConfig, {
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