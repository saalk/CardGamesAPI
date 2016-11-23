package nl.knikit.cardgames.definitions;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.junit.Cucumber;

/*
In order for JUnit to be aware of Cucumber and read feature files when running,
the Cucumber class must be declared as the Runner. We also need to tell JUnit the place to
search for feature files and step definitions.
*/

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty", "html:target/cucumber"}, // see below
		features = {"src/it/resources/features"})

public class RunCukesTest {
	
	
//	Built-in formatter PLUGIN types: junit,
//	- html, pretty, progress, json, usage, rerun, testng.
//
// Built-in summary PLUGIN types:
//	default_summary, null_summary.
//
// PLUGIN can
//	also be a fully qualified class name, allowing registration of 3rd party plugins.
//
	

}