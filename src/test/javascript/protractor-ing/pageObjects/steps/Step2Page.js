'use strict';
module.exports = (function () {

    var Step2Page = function () {

        this.creditcardDir = element(by.css('ing-selected-creditcards-dir'));
        this.creditcardList = element(by.id('selectedCards'));
        this.agreement = element(by.name('voorwaarden'));
        this.checkbox = element(by.id('checkTerms'));
        this.nextButton = element(by.partialButtonText('Volgende'));

        this.email = element(by.id('email'));
        this.phone = element(by.id('phone'));
        this.address = element(by.id('address'));

        this.clickCheckbox = function(){
                this.checkbox.click();
        };        
        
        this.clickConfirm = function(){
                this.nextButton.click();
        };
    };
        Step2Page.prototype = new (require('./StepPage'))(0);

        return Step2Page;
    })();
