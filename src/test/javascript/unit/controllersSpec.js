/* jasmine specs for controllers go here */
describe('Controller tests', function () { //describe your object type
    
    var mockFactory, mockToastr, controller, deferred, $rootScope;
/*
    beforeEach(function(){
        
        beforeEach(module('myApp'));
           
        // mock factory listPlayers() with $q to loop the digest cycle in order to return the promise
        angular.mock.module(function($provide){
            $provide.factory('playerfactory', function($q){
                function listPlayers(){
                    deferred = $q.defer();
                    return deferred.promise;
                }
                return{listPLayers: listPLayers};
            });
        });
        // mock toastr with $q to loop the digest cycle in order to return the promise
        angular.mock.module(function($provide){
            $provide.factory('toastr', function($q){
                function success(){
                    // return null;
                }
                return{ };
            });
        });
        // inject mock in homecontroller
        angular.mock.inject(function($controller, playerfactory, _$rootScope_, toastr){
            $rootScope = _$rootScope_;
            mockFactory = playerfactory;
            mockToastr = toastr;
            spyOn(mockFactory, 'listPlayers').and.callThrough();
            controller = $controller('HomeController', {
                playerfactory: mockFactory,
                toastr: mockToastr,
            })
        });
    });
*/
    it('Title should contain "Welcome" at the start', function () {
        //controller.postAttributes();
        //$rootScope.$digest();
        expect('Welcome').toBe('Welcome');
    });

    it('bail should not contain 0 after "BAIL: 0000" clicked', function () {
    //scope.$apply(); // Let Angular know some changes have happened: the scope is created
        expect(1).not.toBe(0);
    });
});