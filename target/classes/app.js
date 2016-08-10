// this is where the module is created with name mainApp
// also include ngRoute for all our routing needs
var mainApp = angular.module('app', ['ngRoute']);

// configure our routes so we have different views
mainApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider

    .when('/', {
        templateUrl: 'partials/home.html', controller: 'homeController'
    })

    .when('/casino', {
        templateUrl: 'partials/casino.html', controller: 'casinoController'
    })
    .when('/about', {
        templateUrl: 'partials/about.html', controller: 'aboutController'
    })

    .when('/contact', {
        templateUrl: 'partials/contact.html', controller: 'contactController'
    })

    .otherwise({
        redirectTo: '/'
    });

    // use the HTML5 History API to let Angular change the routing and URLs of our pages without
    // refreshing the page
    // change above function($routeProvider, $locationProvider)
    // $locationProvider.html5Mode(true);
    // but then change the jasmine tests as well

}]);

// create the controller and inject Angular's $scope so we have ng controllers for the views
mainApp.controller("homeController", ['$scope', function ($scope) {
    $scope.debug = false;

    // link to a http resource and get the data
    $scope.title = 'Start ';
    // $http.get("service/home").success(function (data) {
    //    $scope.data = data;
    //    $scope.title += $scope.data.message;
    // });

    $scope.message = 'Enter the Galactic Casino. All ages are welcome.';
    $scope.toggleDebug = function () {
        $scope.debug = !$scope.debug;
    };
}]);

// create the controller and inject Angular's $scope so we have ng controllers for the views
mainApp.controller("casinoController", ['$scope', '$http', function ($scope, $http) {
    $scope.debug = false;

    // link to a http resource and get the data
    $scope.title = 'Hello ';
    $http.get("service/casino").success(function (data) {
        $scope.data = data;
        $scope.title += $scope.data.message;
    });

    $scope.message = 'Welcome to The Galactic Casino, space-traveller. What is your origin?';
    $scope.toggleDebug = function () {
        $scope.debug = !$scope.debug;
    };
}]);

mainApp.controller("aboutController", ['$scope', '$http', function ($scope, $http) {
    $scope.debug = false;

    $scope.title = 'About';
    $scope.message = 'A casino for card games. All ages and origins are welcome.';
    $scope.toggleDebug = function () {
        $scope.debug = !$scope.debug;
    };
}]);

mainApp.controller("contactController", ['$scope', '$http', function ($scope, $http) {
    $scope.debug = false;
    $scope.title = 'Contact';
    $scope.message = 'This is work in progress.';
    $scope.toggleDebug = function () {
        $scope.debug = !$scope.debug;
    };
}]);