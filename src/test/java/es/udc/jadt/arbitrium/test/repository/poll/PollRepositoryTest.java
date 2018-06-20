package es.udc.jadt.arbitrium.test.repository.poll;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollRepository;
import es.udc.jadt.arbitrium.model.entities.poll.specification.PollFilters;
import es.udc.jadt.arbitrium.test.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles("test")
@Transactional
public class PollRepositoryTest {


	@Autowired
	private PollRepository repository;


	private static final String DEFAULT_POLL_NAME = "DEFAULT POLL NAME";
	@Test
	public void FindByKeywordsInNameTest() {
		Poll poll = new Poll();
		poll.setName(DEFAULT_POLL_NAME);

		poll = repository.save(poll);
		
		List<Poll> polls = repository.findAll(PollFilters.pollKeywordsFilter(Arrays.asList("DEFAULT"), false));
		
		assertTrue(polls.contains(poll));
	}

	@Test
	public void FindByKeywordsInDescriptionTest() {
		Poll poll = new Poll();
		poll.setName(DEFAULT_POLL_NAME);
		poll.setDescription("DEsCriptiON");
		poll = repository.save(poll);

		List<Poll> polls = repository
				.findAll(PollFilters.pollKeywordsFilter(ArgumentMatchers.<String>anyList(), true));

		assertTrue(polls.contains(poll));
	}
}
