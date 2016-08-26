'use strict';
module.exports = (function () {

    var Step3Page = function () {
        this.tanDirective = element(by.id('progressService.cramRequestDto.cramId'));
        this.tanSequenceNumberLabel = element(by.id('tanSequenceNumberLabel'));
        this.tanInput = element(by.id('gauthorizeValidationInput'));
        this.tanNextButton = element(by.cssContainingText('.btn', 'Verzenden'));
        this.errorMessageWrongTan = element(by.cssContainingText('.message', 'U heeft de TAN-code niet juist ingevuld. De TAN-code dient uit 6 cijfers te bestaan. Vul de TAN-code opnieuw in.'));
        this.errorMessageNoTan = element(by.cssContainingText('.message', 'U heeft de TAN-code niet juist ingevuld. De TAN-code dient uit 6 cijfers te bestaan. Vul de TAN-code opnieuw in.'));

        this.tanEnterBadCode = function () {
            this.tanInput.sendKeys('abcdef');
        };

        this.tanEnterGoodCode = function () {
            this.tanInput.sendKeys('123456');
        };

        this.ClickNext = function () {
            this.tanNextButton.click();
        };        
    };

        Step3Page.prototype = new (require('./StepPage'))(0);

        return Step3Page;
    })();
