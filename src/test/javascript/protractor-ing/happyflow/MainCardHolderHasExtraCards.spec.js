'use strict';

describe('Main cardholder has 4 cards', function(){

    var indexPage = new (require('../pageObjects/IndexPage'))(),
        step1 = indexPage.step(1),
        step2 = indexPage.step(2),
        step3 = indexPage.step(3),
        step4 = indexPage.step(4);

    beforeEach(function () {
        indexPage.setSelectHolderMultiCardsScenario();
        indexPage.setCheckOkScenario();
        indexPage.setsubmitOkScenario();
        indexPage.setDefaultAddressScenario();
        indexPage.get();
    });


    describe('and page 1 is loaded',function() {

        it('should show the list of creditcards', function () {
            expect(step1.creditCardSelector.isDisplayed()).toBe(true);
            expect(step1.getNumberOfCreditCardItems()).toBe(4);
            expect(step1.nextButton.isPresent()).toBe(true);
        });

        describe('and selects a creditcard', function(){
            beforeEach(function(){
                step1.creditCardSelector.click();
                step1.chooseCreditCardItem(1);
            });

            it('should show the correct messages', function(){
                expect(step1.messages.terms.isDisplayed()).toBe(true);
                expect(step1.messages.bkr.isDisplayed()).toBe(true);
                expect(step1.messages.extra.isDisplayed()).toBe(true);
                expect(step1.nextButton.isPresent()).toBe(true);
                expect(step1.email.isPresent()).toBe(true);
                expect(step1.phone.isPresent()).toBe(true);
                expect(step1.address.isPresent()).toBe(true);
                expect(step1.changeEmailButton.isPresent()).toBe(true);
                expect(step1.changePhoneButton.isPresent()).toBe(true);
                expect(step1.changeAddressButton.isPresent()).toBe(true);
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

                describe('and agrees to the terms', function(){
                    beforeEach(function(){
                        step2.clickCheckbox();
                        step2.clickConfirm();
                    });

                    it('should be on page 3', function(){
                        indexPage.verifyActiveStep(3);
                        expect(step3.tanDirective.isPresent()).toBe(true);
                        expect(step3.tanSequenceNumberLabel.isPresent()).toBe(true);
                        expect(step3.tanInput.isPresent()).toBe(true);
                        expect(step3.tanNextButton.isPresent()).toBe(true);
                    });

                    describe('and fills in TAN and clicks next', function(){
                        beforeEach(function(){
                            step3.tanEnterGoodCode();
                            step3.ClickNext();
                        });

                        it('should be on confirm', function(){
                            indexPage.verifyActiveStep(4);
                            expect(step4.confirmcreditcardsuccess.isPresent()).toBe(true);
                        });
                    });
                });
            });
            
            
            
            
            
        });
    });

});
