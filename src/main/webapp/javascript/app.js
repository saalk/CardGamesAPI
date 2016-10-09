// Set an angular module for our cardGamesApp
// Define each module with the angular.module(name, [requires]) syntax only once
// Retrieve it for subsequent use with angular.module(name)
//
// 1. 'ngRoute' is for angular-route
// 2. 'ngAnimate', 'toastr' is for angular-toastr (
angular.module('myApp', ['ngRoute', 'ngAnimate', 'toastr']);
angular.module('myApp')
        .config(function($routeProvider, toastrConfig) {

    $routeProvider.when('/home/', {
       templateUrl: 'partials/home.html'
    }).when('/casino/', {
       templateUrl: 'partials/casino.html'
    }).when('/table/', {
       templateUrl: 'partials/table.html'
    }).when('/results/', {
       templateUrl: 'partials/results.html'  
    }).otherwise({
        redirectTo: '/home/'
    });

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