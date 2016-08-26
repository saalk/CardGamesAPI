'use strict';

describe('Main cardholder has 1 card', function(){

    var indexPage = new (require('../pageObjects/IndexPage'))(),
        step1 = indexPage.step(1),
        step2 = indexPage.step(2);


    beforeEach(function () {
        indexPage.setSelectHolderScenario();
        indexPage.setCheckOkScenario();
        indexPage.setDefaultAddressScenario();
        indexPage.get();
    });


    describe('and page 1 is loaded',function() {

        it('should show the creditcard and correct messages', function () {
            expect(step1.creditCardSelector.isDisplayed()).toBe(true);
            expect(step1.getNumberOfCreditCardItems()).toBe(1);
            expect(step1.messages.terms.isDisplayed()).toBe(true);
            expect(step1.messages.bkr.isDisplayed()).toBe(true);
            expect(step1.nextButton.isPresent()).toBe(true);
        });

        describe('and clicks next', function(){
            beforeEach(function(){
                step1.gotoNext();
            }) ;

            it('should be on page 2', function(){
                indexPage.verifyActiveStep(2);
                expect(step2.creditcardDir.isPresent()).toBe(true);
                expect(step2.creditcardList.isPresent()).toBe(true);
                expect(step2.agreement.isPresent()).toBe(true);
                expect(step2.checkbox.isPresent()).toBe(true);
                expect(step2.email.isPresent()).toBe(true);
                expect(step2.phone.isPresent()).toBe(true);
                expect(step2.address.isPresent()).toBe(true);
            });

            describe('and does not agree to the terms', function(){
                beforeEach(function() {
                    step2.clickConfirm();
                });

                it('should stay on page 2', function(){
                    indexPage.verifyActiveStep(2);
                });

            });
        });
    });
});
