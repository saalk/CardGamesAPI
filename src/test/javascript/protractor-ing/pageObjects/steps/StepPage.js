module.exports = function (stepIndex) {
    'use strict';

    this.stepIndex = stepIndex;
    this.nextButton = element(by.partialButtonText('Volgende'));
    this.previousButton = element(by.partialButtonText('Vorige'));

    this.getCurrentStep = function () {
        return this.stepIndex;
    };

    this.gotoPrevious = function () {
        return this.previousButton.click();
    };

    this.gotoNext = function () {
        return this.nextButton.click();
    };

    this.waitForElementDisplayed = function (elementToWaitFor) {
        return this.waitForElementPresent().then(function () {
            browser.wait(function () {
                return elementToWaitFor.isDisplayed();
            }, 2000, 'Timeout waiting for element ' + elementToWaitFor.locator() + ' to be displayed');
        });
    };

    this.waitForElementPresent = function (elementToWaitFor) {
        return browser.wait(function () {
            return elementToWaitFor.isPresent();
        }, 3000, 'Timeout waiting for element ' + elementToWaitFor.locator() + ' to become present');
    };

};
