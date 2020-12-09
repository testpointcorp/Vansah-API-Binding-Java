

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
features ={"src/test/resources/login.feature"},
plugin = {"html:target/cucumber-html-report", "pretty:target/cucumber-json-report.json"},
strict = true,
glue={"au.com.MavenCucumberJenkins.src.test.java.TestRegression.java.stepDenfn2.java"}
)
public class TestRegression {

}
