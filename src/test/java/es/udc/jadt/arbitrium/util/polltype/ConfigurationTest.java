package es.udc.jadt.arbitrium.util.polltype;

import static org.junit.Assert.assertEquals;

import es.udc.jadt.arbitrium.model.entities.pollconfig.ConfigurationParameters;

public abstract class ConfigurationTest {

	abstract void getConfigurationParametersTest();

	abstract void getResultsTest();

	void checkNumberOfFields() {
		/*
		 * This is only for maintain integrity of the class ConfigurationParameters. To
		 * be alerted when the number of fields changes, so we need to change the test
		 * too. CURRENT FIELDS TO CHECK : 4 PLUS THE ID, THE TOTAL FIELDS IS 5
		 */
		assertEquals(5, ConfigurationParameters.class.getDeclaredFields().length);
	}

}