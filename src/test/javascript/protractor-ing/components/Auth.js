'use strict';

var Auth = {};

Auth.login = function (username, password, targetUrl) {
    browser.driver.get('https://mijn.ing.nl/internetbankieren/SesamLoginServlet');

    browser.sleep(2000);

    browser.driver.findElement(by.xpath('//form[@id="login"]//div[@id="gebruikersnaam"]/input')).clear();
    browser.driver.findElement(by.xpath('//form[@id="login"]//div[@id="gebruikersnaam"]/input')).sendKeys(username);

    browser.driver.findElement(by.xpath('//form[@id="login"]//div[@id="wachtwoord"]//input')).clear();
    browser.driver.findElement(by.xpath('//form[@id="login"]//div[@id="wachtwoord"]//input')).sendKeys(password);

    browser.driver.findElement(by.xpath('//button[@title="Inloggen"]')).click();

    browser.sleep(3000);

    browser.driver.get('https://bankieren.mijn.ing.nl/' + targetUrl);
};

Auth.logout = function () {
    element(by.xpath('//a[@title="Direct veilig uitloggen"]')).click();
};

module.exports = Auth;
