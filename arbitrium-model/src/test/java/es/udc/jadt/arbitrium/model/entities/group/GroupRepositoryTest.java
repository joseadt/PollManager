package es.udc.jadt.arbitrium.model.entities.group;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.udc.jadt.arbitrium.config.TestConfig;
import es.udc.jadt.arbitrium.model.entities.group.specifications.GroupFilters;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles("test")
@Transactional
public class GroupRepositoryTest {

	private static final String GROUP_1_NAME = "grp 1 my own";

	private static final String GROUP_2_NAME = "2  grupo";

	private static final String GROUP_3_NAME = "otherthingshapp own";

	@Autowired
	private GroupRepository repository;

	private static final List<UserGroup> DEFAULT_USERS = new ArrayList<>();

	@BeforeClass
	public static void intializeClass() {
		DEFAULT_USERS.add(new UserGroup(GROUP_1_NAME, null, null));
		DEFAULT_USERS.add(new UserGroup(GROUP_2_NAME, null, null));
		DEFAULT_USERS.add(new UserGroup(GROUP_3_NAME, null, null));
	}

	@Test
	public void findWithPaginationTest() {
		this.repository.saveAll(DEFAULT_USERS);

		Page<UserGroup> groups = this.repository.findAll(GroupFilters.groupKeywordsFilter(null), PageRequest.of(0, 2));
		Page<UserGroup> groups2 = this.repository.findAll(GroupFilters.groupKeywordsFilter(null), PageRequest.of(1, 2));
		Page<UserGroup> groups3 = this.repository.findAll(GroupFilters.groupKeywordsFilter(Arrays.asList("own")),
				PageRequest.of(0, 2));

		assertEquals(2, groups.getTotalPages());
		assertEquals(2, groups.getNumberOfElements());
		assertEquals(DEFAULT_USERS.size(), groups.getTotalElements());

		assertEquals(1, groups2.getNumberOfElements());

		assertEquals(1, groups3.getTotalPages());
		assertEquals(2, groups3.getNumberOfElements());
		assertEquals(DEFAULT_USERS.size() - 1, groups3.getTotalElements());

	}

}
