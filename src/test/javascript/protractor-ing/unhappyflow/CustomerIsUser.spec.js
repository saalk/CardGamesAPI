'use strict';

describe('Customer is user', function(){

    var indexPage = new (require('../pageObjects/IndexPage'))(),
        step1 = indexPage.step(1);


    beforeEach(function () {
        indexPage.setUserScenario();
        indexPage.get();
    });


    describe('and page 1 is loaded',function() {

        it('should show the correct messages', function () {
            expect(step1.creditCardSelector.isDisplayed()).toBe(true);
            expect(step1.getNumberOfCreditCardItems()).toBe(1);
            expect(step1.messages.user.isDisplayed()).toBe(true);
            expect(step1.nextButton.isPresent()).toBe(false);
            expect(step1.email.isPresent()).toBe(false);
            expect(step1.phone.isPresent()).toBe(false);
            expect(step1.address.isPresent()).toBe(false);
            expect(step1.changeEmailButton.isPresent()).toBe(false);
            expect(step1.changePhoneButton.isPresent()).toBe(false);
            expect(step1.changeAddressButton.isPresent()).toBe(false);
        });
    });
});
