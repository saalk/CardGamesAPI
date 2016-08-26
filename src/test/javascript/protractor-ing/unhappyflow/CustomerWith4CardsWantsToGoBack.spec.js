'use strict';

describe('Customer has extra cards and goes to page 2', function () {

    var indexPage = new (require('../pageObjects/IndexPage'))(),
        step1 = indexPage.step(1),
        step2 = indexPage.step(2),
        step3 = indexPage.step(3);

    beforeEach(function () {
        indexPage.setSelectHolderMultiCardsScenario();
        indexPage.setCheckOkScenario();
        indexPage.setsubmitOkScenario();
        indexPage.setResetOkScenario();
        indexPage.setDefaultAddressScenario();
        indexPage.get();

        step1.creditCardSelector.click();
        step1.chooseCreditCardItem(1);
        step1.gotoNext();
    });

    describe('and goes back to page 1', function () {
        beforeEach(function () {
            step2.gotoPrevious();
        });

        it('should be on page 1', function () {
            expect(step1.creditCardSelector.isDisplayed()).toBe(true);
            expect(step1.getNumberOfCreditCardItems()).toBe(4);
            expect(step1.messages.terms.isDisplayed()).toBe(true);
            expect(step1.messages.bkr.isDisplayed()).toBe(true);
            expect(step1.messages.user.isPresent()).toBe(false);
            expect(step1.messages.extra.isPresent()).toBe(true);
            expect(step1.messages.errorCode.isPresent()).toBe(false);
            expect(step1.nextButton.isPresent()).toBe(true);
            expect(step1.email.isPresent()).toBe(true);
            expect(step1.phone.isPresent()).toBe(true);
            expect(step1.address.isPresent()).toBe(true);
            expect(step1.changeEmailButton.isPresent()).toBe(true);
            expect(step1.changePhoneButton.isPresent()).toBe(true);
            expect(step1.changeAddressButton.isPresent()).toBe(true);
        });
    });
    describe('and goes to page 3', function () {
        beforeEach(function () {
            step2.clickCheckbox();
            step2.clickConfirm();
        });

        describe('and goes back to page 2', function () {
            beforeEach(function () {
                step3.gotoPrevious();
            });

            it('should be on page 2', function () {
                indexPage.verifyActiveStep(2);
                expect(step2.creditcardDir.isPresent()).toBe(true);
                expect(step2.creditcardList.isPresent()).toBe(true);
                expect(step2.agreement.isPresent()).toBe(true);
                expect(step2.checkbox.isPresent()).toBe(true);
                expect(step2.checkbox.isSelected()).toBe(false);
                expect(step2.email.isPresent()).toBe(true);
                expect(step2.phone.isPresent()).toBe(true);
                expect(step2.address.isPresent()).toBe(true);
            });
        });
    });

});
