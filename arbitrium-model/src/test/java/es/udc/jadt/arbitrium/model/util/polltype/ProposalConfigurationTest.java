package es.udc.jadt.arbitrium.model.util.polltype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polltype.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.entities.vote.Vote;
import es.udc.jadt.arbitrium.util.pollresult.PollResult;
import es.udc.jadt.arbitrium.util.polltype.ConfigurationTest;

/**
 * Test class for ProposalConfiguration The purpouse of this Tests are mostly to
 * mantain integrity against changes, so its easier to notice if we changed
 * something that we didn't have to change
 *
 * @author JADT
 */
@RunWith(JUnit4.class)
public class ProposalConfigurationTest extends ConfigurationTest {

	private static ProposalConfiguration configuration = (ProposalConfiguration) ProposalConfiguration.getInstance();

	private static List<PollOption> defaultPollOptions;

	private static Poll defaultPoll;

	@BeforeClass
	public static void classInitialize() {
		defaultPollOptions = new ArrayList<PollOption>();

		defaultPoll = new Poll();
		defaultPollOptions.add(new PollOption(defaultPoll, "YES"));
		defaultPollOptions.add(new PollOption(defaultPoll, "NO"));
		defaultPoll.setOptions(defaultPollOptions);
	}

	@Override
	@Test
	public void getConfigurationParametersTest() {

		super.checkNumberOfFields();

		ConfigurationParameters cp = configuration.getConfigurationParameters();

		assertNotNull(cp);

		// Check what has to be initiliced
		assertNotNull(cp.getDefaultOptions());
		assertEquals(defaultPollOptions.size(), cp.getDefaultOptions().size());
		assertTrue(defaultPollOptions.containsAll(cp.getDefaultOptions()));

		assertNull(cp.getIsMultiSelection());
		assertNull(cp.getIsUserDefinedOptions());
		assertNull(cp.getMaxOptions());

	}

	@Override
	@Test
	public void getResultsTest() {

		List<Vote> votes = new ArrayList<Vote>();
		int i;
		int j;
		for (i = 0; i < 15; i++) {
			votes.add(new Vote(defaultPollOptions.subList(0, 1), null, null));
		}
		for (j = 0; j < 10; j++) {
			votes.add(new Vote(defaultPollOptions.subList(1, 2), null, null));
		}

		PollResult result = configuration.getResult(defaultPoll, votes);
		assertEquals(defaultPollOptions.get(0), result.getWinnerOption());
		votes.clear();
		for (i = 0; i < 15; i++) {
			votes.add(new Vote(defaultPollOptions.subList(1, 2), null, null));
		}
		for (j = 0; j < 10; j++) {
			votes.add(new Vote(defaultPollOptions.subList(0, 1), null, null));
		}

		result = configuration.getResult(defaultPoll, votes);
		assertEquals(defaultPollOptions.get(1), result.getWinnerOption());
		votes.clear();
		for (i = 0; i < 15; i++) {
			votes.add(new Vote(defaultPollOptions.subList(1, 2), null, null));
		}
		for (j = 0; j < 15; j++) {
			votes.add(new Vote(defaultPollOptions.subList(0, 1), null, null));
		}

		result = configuration.getResult(defaultPoll, votes);
		assertNull(result.getWinnerOption());
	}
}
