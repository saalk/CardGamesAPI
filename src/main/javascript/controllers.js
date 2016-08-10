// repeatController with the players list
angular.module('ngRepeat', ['ngAnimate']).controller('repeatController', function($scope) {
  $scope.players = [
    // this is an array of objects with property name, fiches and aiLevel
    {name:'John', fiches:25, aiLevel:'human'},
    {name:'Jessie', fiches:30, aiLevel:'medium'},
    {name:'Johanna', fiches:28, aiLevel:'medium'},
    {name:'Joy', fiches:15, aiLevel:'medium'},
    {name:'Mary', fiches:28, aiLevel:'medium'},
    {name:'Peter', fiches:95, aiLevel:'human'},
    {name:'Sebastian', fiches:50, aiLevel:'human'},
    {name:'Erika', fiches:27, aiLevel:'medium'},
    {name:'Patrick', fiches:40, aiLevel:'human'},
    {name:'Samantha', fiches:60, aiLevel:'medium'}
  ];
});

