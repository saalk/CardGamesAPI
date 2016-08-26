module.exports = function () {
    'use strict';

    //////////////
    // Scenarios
    //////////////

    var scenario = {};

    function setScenario(scenarioName, scenarioValue) {
        scenario[scenarioName] = scenarioValue || true;
    }

    this.setListScenario = function (scenarioValue) {
        setScenario('list', scenarioValue);
    };

    this.setSelectScenario = function (scenarioValue) {
        setScenario('select', scenarioValue);
    };

    this.setCheckScenario = function (scenarioValue) {
        setScenario('check', scenarioValue);
    };

    this.setSubmitScenario = function (scenarioValue) {
        setScenario('submit', scenarioValue);
    };

    this.setResetScenario = function (scenarioValue) {
        setScenario('reset', scenarioValue);
    };

    this.setAddressScenario = function (scenarioValue) {
        setScenario('address', scenarioValue);
    };

    this.setSelectHolderScenario = function () {
        this.setListScenario('1');
        this.setSelectScenario('OK');
    };

    this.setSelectHolderMultiCardsScenario = function () {
        this.setListScenario('4');
        this.setSelectScenario('OK');
    };

    this.setUserScenario = function () {
        this.setListScenario('1');
        this.setSelectScenario('OK_USER');
    };

    this.setCheckOkScenario = function () {
        this.setCheckScenario('OK');
    };

    this.setCheckUnknownErrorScenario = function () {
        this.setCheckScenario('ERROR_UNKNOWN');
    };

    this.setCheckPendingRequestcenario = function () {
        this.setCheckScenario('ERROR_PENDING');
    };

    this.setsubmitOkScenario = function () {
        this.setSubmitScenario('OK');
    };

    this.setResetOkScenario = function () {
        this.setResetScenario('OK');
    };

    this.setDefaultAddressScenario = function () {
        this.setAddressScenario('default');
    };

    //////////
    // Steps
    //////////

    var steps = {
        '1': require('./steps/Step1Page'),
        '2': require('./steps/Step2Page'),
        '3': require('./steps/Step3Page'),
        '4': require('./steps/StepConfirmPage')

    };

    this.step = function (index) {
        return new (steps[index])();
    };

    this.verifyActiveStep = function (expectedStep) {
        element.all(by.repeater('step in steps')).then(function (steps) {
            steps.forEach(function (step, index) {
                step.getAttribute('class').then(function (classNames) {
                    classNames.split(' ').forEach(function (className) {
                        if (className === 'active') {
                            expect(index + 1).toBe(expectedStep);
                        }
                    });
                });
            });
        });
    };

    this.get = function () {
        var url = 'index.html#?';
        for (var i in scenario) {
            if (scenario.hasOwnProperty(i)) {
                url += '&' + i + '=' + scenario[i];
            }
        }
        browser.get(url);
        console.log(url);
    };

};
