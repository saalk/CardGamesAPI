'use strict';
module.exports = (function () {

    var Step1Page = function () {

        this.creditCardSelector= element(by.id('creditcard-selector'));
        this.creditCardItems = element.all(by.repeater('item in selectorItems'));
        this.email = element(by.id('email-info'));
        this.phone = element(by.id('service-phone-info'));
        this.address = element(by.id('correspondence-address'));
        this.changeEmailButton = element(by.id('emailChangeLink'));
        this.changePhoneButton = element(by.id('phoneChangeLink'));
        this.changeAddressButton = element(by.id('addressChangeLink'));
        this.emailInput = element(by.id('emailInput'));
        this.phoneInput = element(by.id('servicePhoneInput'));
        this.emailCancel = element(by.id('emailCancelLink'));
        this.phoneCancel = element(by.id('phoneCancelLink'));
        this.emailError = element(by.id('email-explanation'));
        this.phoneError = element(by.id('errorNotification'));

        //Error ids
        this.errorIds = {
            meldCode: element(by.css('span.meldcode'))
            
        };

        this.messages = {
            terms: element(by.id('termsId')),
            bkr : element(by.id('bkrMsgId')),
            user: element(by.id('roleUserId')),
            extra: element(by.id('extraCardId')),
            errorCode: element(by.id('checkErrorId'))
            
        };

       /* this.happyFlowToNextPage = function () {
            this.creditCardItemsPullDown.click();
            this.chooseCreditCardItem(0);
            this.gotoNext();
        };

*/
        this.chooseCreditCardItem = function (index) {
            var creditCardItem = element(by.id('creditcard-selector-' + index));
            return this.waitForElementPresent(creditCardItem).then(function() {
                creditCardItem.element(by.xpath('..')).click(); //Clicking the parent or else it won't work for pulldown.
                return null;
            });
        };

        this.getNumberOfCreditCardItems = function () {
            return this.creditCardItems.count();
        };

        /*this.verifyHasNoErrors = function () {
            expect(this.errorIds.noCreditcardsAlert.isPresent()).toBe(false);
            expect(this.errorIds.errorAlert.isPresent()).toBe(false);
            expect(this.errorIds.creditcardSelectAlert.isDisplayed()).toBe(false);
            expect(this.errorIds.requestIdResponseErrorFlagAlert.isDisplayed()).toBe(false);
        };

        this.verifyErrorMessagePresent = function (errorMessage) {
            expect(element(by.xpath('//div[@id="requestIdResponseErrorFlag"]/div/div')).getText()).toContain(errorMessage);
        };

        this.verifyHasError = function(){
            expect(this.errorIds.errorAlert.isDisplayed()).toBe(true);
            expect(element(by.id('error_msg')).isPresent()).toBe(true);
        };*/
    };

    Step1Page.prototype = new (require('./StepPage'))(0);

    return Step1Page;
})();
