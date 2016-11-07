package nl.knikit.cardgames.definitions;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/*
In order for JUnit to be aware of Cucumber and read feature files when running,
the Cucumber class must be declared as the Runner. We also need to tell JUnit the place to
search for feature files and step definitions.
*/

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:Feature")

public class RunCukesTest {
}