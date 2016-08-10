// configure our routes so we have different views
cardGamesApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/home', {
        templateUrl: 'partials/home.html', controller: 'HomeController' })
    .when('/casino', {
        templateUrl: 'partials/casino.html', controller: 'CasinoController' })
    .otherwise({
        redirectTo: '/home' });
    // use the HTML5 History API to let Angular change the routing and URLs of our pages without
    // refreshing the page
    // change above function($routeProvider, $locationProvider)
    // $locationProvider.html5Mode(true);
    // but then change the jasmine tests as well
}]);
