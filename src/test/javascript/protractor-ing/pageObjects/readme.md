#PageObjects
Everything in this folder following the pattern `'*PageObject.js', '**/*PageObject.js'` will be copied to the bower package

#Example

```
//view1PageObject.js

function View2() {
    this.message = element(by.binding('view2Ctrl.message.message'));
}

exports = module.exports = {
    View2: View2
};

```