'use strict';
//TODO: create multiple error specs when all error codes are available
describe('Main cardholder has 1 card', function(){

    var indexPage = new (require('../pageObjects/IndexPage'))(),
        step1 = indexPage.step(1);

    beforeEach(function () {
        indexPage.setSelectHolderScenario();
        indexPage.setCheckUnknownErrorScenario();
        indexPage.setDefaultAddressScenario();
        indexPage.get();
    });

    describe('and page 1 is loaded with ..... error ',function() {

        it('should be on page 1', function(){
            indexPage.verifyActiveStep(1);
        });

            describe('and clicks next', function(){
                beforeEach(function(){
                    step1.gotoNext();
                }) ;

                it('should still be on page 1 with error message', function(){
                    indexPage.verifyActiveStep(1);
                    expect(step1.nextButton.isPresent()).toBe(false);
                    expect(step1.messages.errorCode.isDisplayed()).toBe(true);
                });
            });




    });
    
});
