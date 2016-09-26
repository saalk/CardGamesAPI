package nl.knikit.cardgames.definitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Annotation {
        WebDriver driver = null;

        @Given("^I have open the browser$")
        public void openBrowser() {
            driver = new FirefoxDriver();
        }

        @When("^I open knikit website$")
        public void goToKnikit() {
            driver.navigate().to("http://knikit.nl/");
        }

        @Then("^Home button should exits$")
        public void loginButton() {
            if(driver.findElement(By.id("home")).isEnabled()) {
                System.out.println("Test 1 Pass");
            } else {
                System.out.println("Test 1 Fail");
            }
            driver.close();
        }

}