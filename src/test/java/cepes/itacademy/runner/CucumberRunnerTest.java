package cepes.itacademy.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features") // Aponta para a pasta src/test/resources/features
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "steps") // Aponta para o pacote dos steps
public class CucumberRunnerTest {
    // A classe permanece vazia. Ela serve apenas como âncora para as anotações.
}