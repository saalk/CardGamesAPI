# Protractor tests
Everything in this folder following the pattern `**/*.spec.js` will be run by protractor

# Example
```
//view1.spec.js

'use strict';

require('node-path')(module, [browser.params.dependencyPath]);
var Mq = require('spectingular-core/test/PageObjects/responsivePageObject').Mq;
var View2 = require('../pageObjects/view2PageObject').View2;

describe('View2', function() {
    var mq = new Mq(),
        view2 = new View2();

    beforeEach(function() {
        browser.get('index.html');

        mq.setSm();
    });

    it('should SHRUG', function() {
        var span1 = element(by.css('.goodbye'));
        expect(span1.getText()).toBe('Carry on busy bees ¯\\_(ツ)_/¯');
    });

    it('should show a notification', function(){
        expect(view2.message.getText()).toBe('Awesome api call');
    });
});
```