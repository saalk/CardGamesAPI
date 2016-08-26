'use strict';

describe('Main cardholder has 1 card', function () {

    var indexPage = new (require('../pageObjects/IndexPage'))(),
        step1 = indexPage.step(1),
        step2 = indexPage.step(2),
        step3 = indexPage.step(3);

    beforeEach(function () {
        indexPage.setSelectHolderScenario();
        indexPage.setCheckOkScenario();
        indexPage.setsubmitOkScenario();
        indexPage.setDefaultAddressScenario();
        indexPage.get();
    });

    describe('and page 1 is loaded', function () {
        describe('and clicks next', function () {
            beforeEach(function () {
                step1.gotoNext();
            });

            describe('and agrees to the terms', function () {
                beforeEach(function () {
                    step2.clickCheckbox();
                    step2.clickConfirm();
                });

                describe('and fills in wrong TAN and clicks next', function () {
                    beforeEach(function () {
                        step3.tanEnterBadCode();
                        step3.ClickNext();
                    });

                    it('should still be on page 3 with wrong-Tan message', function () {
                        indexPage.verifyActiveStep(3);
                        expect(step3.tanDirective.isPresent()).toBe(true);
                        expect(step3.tanSequenceNumberLabel.isPresent()).toBe(true);
                        expect(step3.tanInput.isPresent()).toBe(true);
                        expect(step3.tanNextButton.isPresent()).toBe(true);
                        expect(step3.errorMessageWrongTan.isPresent()).toBe(true);
                    });
                });

                describe('and clicks next', function () {
                    beforeEach(function () {
                        step3.ClickNext();
                    });

                    it('should still be on page 3 with no-tan message', function () {
                        indexPage.verifyActiveStep(3);
                        expect(step3.tanDirective.isPresent()).toBe(true);
                        expect(step3.tanSequenceNumberLabel.isPresent()).toBe(true);
                        expect(step3.tanInput.isPresent()).toBe(true);
                        expect(step3.tanNextButton.isPresent()).toBe(true);
                        expect(step3.errorMessageNoTan.isPresent()).toBe(true);
                    });
                });
            });
        });
    });
});
