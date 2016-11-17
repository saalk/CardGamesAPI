package nl.knikit.cardgames.definitions;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/*
In order for JUnit to be aware of Cucumber and read feature files when running,
the Cucumber class must be declared as the Runner. We also need to tell JUnit the place to
search for feature files and step definitions.
*/

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty", "html:target/cucumber"},
		features = {"src/it/resources/features"})

public class RunCukesTest {
	
}