#Mocks
Everything in this folder with `.js` & `.json` will be copied to the bower package

#Example

```
// mock.js
(function(pCreditcardsSwitchRepaymentOff){
    pCreditcardsSwitchRepaymentOff.mock = {
        message: 'Awesome api call'
    };
})(window.pCreditcardsSwitchRepaymentOff = window.pCreditcardsSwitchRepaymentOff || {});
```