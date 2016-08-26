'use strict';

var mocksModule = angular.module('pCreditcardsSwitchRepaymentOffMocks', []);

mocksModule.service('MockCardRepository', function() {
    this.CreditCard1 = {
        'cardId': '2991',
        'name': 'C VAN DREUMELL',
        'maskedCreditcardNumber': '1234.****.****.2991',
        'type': 'Primary',
        'productType': 'SilverCreditcard',
        'ibanNumber': 'NL30INGB0747784787',
        'startDate': '2008-11-13',
        'status': 'Active',
        'statusReason': '',
        'accountNumber': '0003393040',
        'currentAccount': '0003393040',
        'linkedMainCardId': '-1'
    };

    this.CreditCard2 = {
        'cardId': '2992',
        'name': 'H VAN DREUMELL',
        'maskedCreditcardNumber': '1234.****.****.2992',
        'type': 'Secondary',
        'productType': 'SilverCreditcard',
        'ibanNumber': 'NL30INGB0747784787',
        'startDate': '2008-11-13',
        'status': 'Active',
        'statusReason': '',
        'accountNumber': '0003393040',
        'currentAccount': '0003393040',
        'linkedMainCardId': '2991'
    };

    this.CreditCard3 = {
        'name': 'C VAN DREUMEL',
        'accountNumber': '747784787',
        'maskedCreditcardNumber': '1234.****.****.2990',
        'type': 'Primary',
        'productType': 'PlatinumCreditcard',
        'ibanNumber': 'NL30INGB0747784787',
        'ibanPrefix': 'NL30INGB',
        'cardId': '2990',
        'status': 'Active',
        'statusReason': '',
        'linkedMainCardId': '-1'
    };

    this.CreditCard4 = {
        'name': 'F VAN DREUMEL',
        'accountNumber': '747784787',
        'maskedCreditcardNumber': '****.****.****.2993',
        'type': 'Secondary',
        'productType': 'ExtraCreditcard',
        'ibanNumber': 'NL30INGB0747784787',
        'ibanPrefix': 'NL30INGB',
        'cardId': '2993',
        'status': 'Blocked',
        'statusReason': 'Block_Reason_Three_Error_One',
        'linkedMainCardId': '-1'
    };

});

mocksModule.service('MockCheckService', function() {
    this.OK = {
        'requestId': '8cec62e4-f36d-4ce7-a7bf-c8472036a0c8',
        'cramId': 'null',
        'creditCards': [{
            'cardId': '2991',
            'name': 'C VAN DREUMELL',
            'maskedCreditcardNumber': '1234.****.****.2991',
            'type': 'Primary',
            'productType': 'SilverCreditcard',
            'ibanNumber': 'NL30INGB0747784787',
            'startDate': '2008-11-13',
            'status': 'Active',
            'statusReason': '',
            'accountNumber': '0003393040',
            'currentAccount': '0003393040'
        }],
        'agreement': {
            'role': 'HOLDER'
        }
    };

    this.ERROR_REQUESTOR = {
        'requestId': '8cec62e4-f36d-4ce7-a7bf-c8472036a0c8',
        'cramId': 'null',
        'creditCards': [{
            'cardId': '2991',
            'name': 'C VAN DREUMELL',
            'maskedCreditcardNumber': '1234.****.****.2991',
            'type': 'Primary',
            'productType': 'SilverCreditcard',
            'ibanNumber': 'NL30INGB0747784787',
            'startDate': '2008-11-13',
            'status': 'Active',
            'statusReason': '',
            'accountNumber': '0003393040',
            'currentAccount': '0003393040'
        }],
        'agreement': {
            'role': 'HOLDER'
        },
        'errorCode': '1'
    };

    this.ERROR_PENDING = {
        'requestId': '8cec62e4-f36d-4ce7-a7bf-c8472036a0c8',
        'cramId': 'null',
        'creditCards': [{
            'cardId': '2991',
            'name': 'C VAN DREUMELL',
            'maskedCreditcardNumber': '1234.****.****.2991',
            'type': 'Primary',
            'productType': 'SilverCreditcard',
            'ibanNumber': 'NL30INGB0747784787',
            'startDate': '2008-11-13',
            'status': 'Active',
            'statusReason': '',
            'accountNumber': '0003393040',
            'currentAccount': '0003393040'
        }],
        'agreement': {
            'role': 'HOLDER'
        },
        'errorCode': '3'
    };

});

mocksModule.service('MockSelectService', function(MockCardRepository) {
    this.httpError = {'httpStatusCode': 500};

    this.OK = {
        'requestId': '8cec62e4-f36d-4ce7-a7bf-c8472036a0c8',
        'cramId': 'null',
        'creditCard': MockCardRepository.CreditCard1,
        'agreement': {
            'role': 'HOLDER'
        }
    };

    this.OK_USER = {
        'requestId': '8cec62e4-f36d-4ce7-a7bf-c8472036a0c8',
        'cramId': 'null',
        'creditCard': MockCardRepository.CreditCard4,
        'agreement': {
            'role': 'USER'
        }
    };

});

mocksModule.service('MockListService', function(MockCardRepository) {
    this.fourCards = [
        MockCardRepository.CreditCard1,
        MockCardRepository.CreditCard2,
        MockCardRepository.CreditCard3,
        MockCardRepository.CreditCard4
    ];

    this.cardsForSwitchOff = [
        {
            'card': MockCardRepository.CreditCard1,
            'image': 'img/creditcards_platinumcard.png'
        },
        {
            'card': MockCardRepository.CreditCard2,
            'image': 'img/creditcards_creditcard.png'
        }
    ];
});

mocksModule.service('MockSubmitService', function() {
    this.OK =
        {
            'cramId': '457'
        };
});

mocksModule.service('MockResetService', function() {
    this.OK = '';
});
