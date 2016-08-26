'use strict';
module.exports = (function () {

    var StepConfirmPage = function () {
        this.confirmcreditcardsuccess = element(by.id('confirmcreditcardsuccess'));
    };

    StepConfirmPage.prototype = new (require('./StepPage'))(0);

    return StepConfirmPage;
})();



