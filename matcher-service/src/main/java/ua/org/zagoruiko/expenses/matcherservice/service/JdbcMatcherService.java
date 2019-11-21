package ua.org.zagoruiko.expenses.matcherservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.org.zagoruiko.expenses.category.matcher.Matcher;
import ua.org.zagoruiko.expenses.matcherservice.matcher.MatcherFatory;
import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcMatcherService implements MatcherService, Serializable {
    private Matcher<String> matcher;

    private JdbcTemplate jdbcTemplate;

    private void loadMatchers() {
        this.matcher = MatcherFatory.createMatcher(this.getMatcherSet("pb"), "pb");
    }

    @Autowired
    public JdbcMatcherService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.loadMatchers();
    }

    @Override
    public List<TagsMatcherModel> getMatcherSet(String provider) {
        return jdbcTemplate.query(
                "SELECT m.func, m.pattern, m.is_category, GROUP_CONCAT(rv.value SEPARATOR ',')as val\n " +
                "FROM `return_values` rv\n" +
                "                join matchers m on rv.matcher_id = m.id\n" +
                "where m.provider = ? AND m.is_category <> 1\n" +
                "group by m.func, m.pattern, m.is_category", new Object[] { provider },
                (rs, rowNum) ->
                         new TagsMatcherModel(provider,
                            rs.getString("func"),
                            rs.getString("pattern"),
                            new HashSet<>(Arrays.asList(rs.getString("val").split(",")))
                         )
        );
    }

    @Override
    public List<String> getTagSuggestions(String pattern) {
        return this.jdbcTemplate.query("SELECT `value` FROM `values` WHERE `value` LIKE ? OR friendly_name LIKE ?",
                new Object[] {"%" + pattern + "%", "%" + pattern + "%"},
                (rs, rowNum) -> rs.getString(1));
    }

    @Override
    public List<String> matchTags(String operationDescription) {
        return this.matcher.match(operationDescription).stream().map(t -> t.getName()).collect(Collectors.toList());
    }
}
