'use strict';

var Locator = {};

Locator.findByText = function (text, optParentElement) {
    var using = optParentElement || document;
    var matches = [];

    function addMatchingLeaves(element) {

        if (element.children) {
            if (element.children.length === 0 && element.textContent.match(text)) {
                matches.push(element);
            }
            for (var i = 0; i < element.children.length; ++i) {
                addMatchingLeaves(element.children[i]);
            }
        }
    }

    addMatchingLeaves(using);
    return matches;
};

module.exports = Locator;
