package es.udc.jadt.arbitrium.model.util.polltype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polltype.ConfigurationParameters;
import es.udc.jadt.arbitrium.util.polltype.ConfigurationTest;

@RunWith(JUnit4.class)
public class BasicPollConfigurationTest extends ConfigurationTest {

	private static BasicPollConfiguration configuration = BasicPollConfiguration.getInstance();

	private static List<PollOption> optionsToTest;

	private static Poll poll;

	@BeforeClass
	public static void initializeClass() {
		optionsToTest = new ArrayList<PollOption>();
		optionsToTest.add(new PollOption(null, "Opt1"));
		optionsToTest.add(new PollOption(null, "Opt2"));
		optionsToTest.add(new PollOption(null, "Opt3"));
		optionsToTest.add(new PollOption(null, "Opt4"));
		optionsToTest.add(new PollOption(null, "Opt5"));

		poll = new Poll(null, optionsToTest, PollType.PROPOSAL, Instant.now().plus(Duration.ofDays(1L)));
	}

	@Override
	@Test
	public void getConfigurationParametersTest() {

		super.checkNumberOfFields();

		ConfigurationParameters cp = configuration.getConfigurationParameters();

		assertNotNull(cp);
		assertTrue(cp.getIsUserDefinedOptions());

		assertNull(cp.getDefaultOptions());
		assertNull(cp.getIsMultiSelection());
		assertNull(cp.getMaxOptions());
	}

	@Override
	@Test
	public void getResultsTest() {
		// TODO Auto-generated method stub

	}

}
