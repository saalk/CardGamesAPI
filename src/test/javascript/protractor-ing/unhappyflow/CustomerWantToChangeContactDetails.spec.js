'use strict';

describe('Main cardholder has 1 card', function () {

    var indexPage = new (require('../pageObjects/IndexPage'))(),
        step1 = indexPage.step(1);

    beforeEach(function () {
        indexPage.setSelectHolderScenario();
        indexPage.setCheckOkScenario();
        indexPage.setsubmitOkScenario();
        indexPage.setResetOkScenario();
        indexPage.setDefaultAddressScenario();
        indexPage.get();
    });

    describe('and wants to change his phone number ', function () {
        beforeEach(function () {
            step1.changePhoneButton.click();
        });

        it('should not be able to continue', function () {
            step1.gotoNext();
            indexPage.verifyActiveStep(1);

            expect(step1.messages.terms.isDisplayed()).toBe(true);
            expect(step1.messages.bkr.isDisplayed()).toBe(true);
            expect(step1.nextButton.isPresent()).toBe(true);
            expect(step1.phoneError.isPresent()).toBe(true);
        });

        it('and finishes should be able to continue', function () {
            step1.phoneCancel.click();
            step1.gotoNext();

            indexPage.verifyActiveStep(2);
        });
    });

    describe('and wants to change his email ', function () {
        beforeEach(function () {
            step1.changeEmailButton.click();
        });

        it('should not be able to continue', function () {
            step1.gotoNext();
            indexPage.verifyActiveStep(1);

            expect(step1.messages.terms.isDisplayed()).toBe(true);
            expect(step1.messages.bkr.isDisplayed()).toBe(true);
            expect(step1.nextButton.isPresent()).toBe(true);
            expect(step1.emailError.isPresent()).toBe(true);
        });

        it('and finishes should be able to continue', function () {
            step1.emailCancel.click();
            step1.gotoNext();

            indexPage.verifyActiveStep(2);
        });
    });
});
