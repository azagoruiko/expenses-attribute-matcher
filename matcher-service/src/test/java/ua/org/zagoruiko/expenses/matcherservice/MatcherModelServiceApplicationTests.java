package ua.org.zagoruiko.expenses.matcherservice;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import ua.org.zagoruiko.expenses.category.model.SystemTag;
import ua.org.zagoruiko.expenses.category.model.Tag;

import java.util.*;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class MatcherModelServiceApplicationTests {
	@Autowired
	JdbcTemplate jdbcTemplate;

    @Test
	public void run() {
		final Map<String, Set<Tag>> tagContains = new HashMap<>();

		tagContains.put("WFPTAXI", new HashSet<>(Arrays.asList(new Tag[] {
				SystemTag.TAG_TAXI.getTag(),
				SystemTag.TAG_TRANSPORT.getTag()})));

		tagContains.put("fozzy", new HashSet<>(Arrays.asList(new Tag[] {
				SystemTag.TAG_FOZZY.getTag(),
				SystemTag.TAG_SUPERMARKET.getTag(),
				SystemTag.TAG_HAS_DRINKS.getTag()
		})));

		tagContains.put("YIDALNYA", new HashSet<>(Arrays.asList(new Tag[] {
				SystemTag.TAG_YIDALNYA.getTag(),
				SystemTag.TAG_EAT_OUT.getTag()
		})));

		this.jdbcTemplate.execute("DELETE FROM `return_values`");
		this.jdbcTemplate.execute("DELETE FROM `values`");
		this.jdbcTemplate.execute("DELETE FROM `matchers`");
		for (SystemTag tag : SystemTag.values()) {
			this.jdbcTemplate.execute(String.format("INSERT INTO `values` (`value`, friendly_name) values ('%s', '%s')",
					tag.getTag().getName(),
					tag.getTag().getDescription()
			));
		}

		for (Map.Entry<String, Set<Tag>> entry : tagContains.entrySet()) {
			this.jdbcTemplate.execute(String.format("INSERT INTO `matchers` (provider, func, pattern, is_category) values (" +
							"'pb', 'CONTAINS', " +
							"'%s', 0)",
					entry.getKey()
			));
			for (Tag tag : entry.getValue()) {
				this.jdbcTemplate.execute(String.format("INSERT INTO `return_values` (matcher_id, `value`) values (" +
								"(SELECT id FROM `matchers` where `func`='CONTAINS' AND pattern='%s' AND is_category=0), " +
								"'%s'" +
								")",
						entry.getKey(),
						tag.getName()
				));
			}
		}
	}

	@Test
	public void contextLoads() {
	}

}
