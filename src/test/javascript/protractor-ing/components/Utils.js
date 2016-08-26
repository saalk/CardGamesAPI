'use strict';

var fs = require('fs');
var path = require('path');

var Utils = {};

Utils.checkIfIdDisplayed = function (elementId) {
    expect(element(by.id(elementId)).isDisplayed()).toBe(true);
    return elementId;
};

Utils.checkIfIdPresent = function (id) {
    expect(element(by.id(id)).isPresent()).toBe(true);
    return id;
};

Utils.checkIfIdNotDisplayed = function (id) {
    expect(element(by.id(id)).isDisplayed()).toBe(false);
    return id;
};

Utils.checkIfIdNotPresent = function (id) {
    expect(element(by.id(id)).isPresent()).toBe(false);
    return id;
};

Utils.checkIfLinkContainsURL = function (id, url) {
    expect(element(by.id(id)).isDisplayed()).toBe(true);
    expect(element(by.id(id)).getAttribute('href')).toBe(url);
    return id;
};

Utils.checkIfLinkTextDisplayed = function (linkText) {
    expect(element(by.linkText(linkText)).isDisplayed()).toBe(true);
    return linkText;
};

Utils.checkIfNamePresent = function (name) {
    expect(element(by.name(name)).isPresent()).toBe(true);
    return name;
};

Utils.checkIfTextDisplayedByClassName = function (className, text) {
    expect(element(by.className(className)).isDisplayed()).toBe(true);
    expect(element(by.className(className)).getText()).toContain(text);
    return className;
};

Utils.checkIfTextDisplayedByClassNameWithinName = function (name, className, text) {
    expect(element(by.name(name)).element(by.className(className)).isDisplayed()).toBe(true);
    expect(element(by.name(name)).element(by.className(className)).getText()).toContain(text);
    return name;
};

Utils.checkIfTextDisplayedById = function (elementId, text) {
    expect(element(by.id(elementId)).isDisplayed()).toBe(true);
    expect(element(by.id(elementId)).getText()).toContain(text);
    return elementId;
};

Utils.checkIfTextDisplayedByName = function (name, text) {
    expect(element(by.name(name)).isDisplayed()).toBe(true);
    expect(element(by.name(name)).getText()).toContain(text);
    return name;
};

Utils.checkIfTextNotDisplayed = function (text) {
    expect(element(by.text(text)).isDisplayed()).toBe(false);
    return text;
};

Utils.checkIfTextNotDisplayedByName = function (name) {
    expect(element(by.name(name)).isDisplayed()).toBe(false);
    return name;
};

Utils.checkIfTextDisplayedByXpath = function (xpath, text) {
    expect(element(by.xpath(xpath)).getText()).toContain(text);
};

Utils.checkIfXpathPresent = function (xpath) {
    expect(element(by.xpath(xpath)).isPresent()).toBe(true);
};

Utils.previous = function () {
    Utils.wait();
    element(by.partialButtonText('Vorige')).click().then(function() {
        console.log('=================== clicked previous');
    });
    Utils.wait();
};

Utils.next = function () {
    Utils.wait();
    element(by.partialButtonText('Volgende')).click().then(function() {
        console.log('=================== clicked next');
    });
    Utils.wait();
};

Utils.selectFirstCreditcard = function () {
    element(by.id('creditcard-selector-0')).click().then(function () {
        console.log('=================== more than 1 card');
    }, function () {
        console.log('=================== 1 card');
    });
};

Utils.selectSecondCreditcard = function () {
    element(by.id('creditcard-selector-1')).click();
};

Utils.clickCheckbox = function() {
    element(by.id('checkTerms')).click();
};

Utils.goToUrl = function (targetUrl) {
    browser.driver.get('https://bankieren.mijn.ing.nl/' + targetUrl);
    Utils.wait();
};

Utils.sendKeys = function (keys, elementId) {
    element(by.id(elementId)).sendKeys(keys);
};

Utils.takeScreenshot = function (filename) {
    var defer = protractor.promise.defer();

    var specName = jasmine.getEnv().currentSpec.description.replace(/ /g, '-');
    var screenshotsDir = path.resolve('./reports/screenshots/');

    if (!fs.existsSync(screenshotsDir)) {
        fs.mkdirSync(screenshotsDir);
    }

    var file = path.resolve(screenshotsDir + '/' + specName + '-' + filename + '.png');
    browser.takeScreenshot().then(function (png) {
        fs.writeFileSync(file, png, {encoding: 'base64'});
        defer.fulfill();
    });

    return defer;
};

Utils.getEnv = function() {
        return jasmine.getEnv().environment;
};

Utils.wait = function () {
    browser.sleep(2500);
};

module.exports = Utils;
