package ua.org.zagoruiko.expenses.matcherservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.org.zagoruiko.expenses.category.matcher.Matcher;
import ua.org.zagoruiko.expenses.category.model.Tag;
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

    @Override
    public int saveTag(Tag tag) {
        return this.jdbcTemplate.update("INSERT INTO `values` (`value`, friendly_name) VALUES (?,?)" +
                "ON DUPLICATE KEY UPDATE `value` = ?, friendly_name = ?",
                    tag.getName().toUpperCase(),
                    tag.getDescription(),
                    tag.getName().toUpperCase(),
                    tag.getDescription()
        );
    }

    @Override
    public int[] saveTags(Collection<Tag> tags) {
        return this.jdbcTemplate.batchUpdate("INSERT INTO `values` (`value`, friendly_name) VALUES (?,?)" +
                "ON DUPLICATE KEY UPDATE `value` = ?, friendly_name = ?",
                tags.stream().map(t -> new String[] {
                        t.getName().toUpperCase(),
                        t.getDescription(),
                        t.getName().toUpperCase(),
                        t.getDescription()
                })
                .collect(Collectors.toList()));
    }

    @Override
    public int saveTagMatcher(TagsMatcherModel matcher) {
        return this.jdbcTemplate.update("INSERT INTO matchers (provider, func, pattern) VALUES (?,?,?) " +
                        "ON DUPLICATE KEY UPDATE provider = ?, func = ?, pattern = ?",
                matcher.getProvider(),
                matcher.getFunc(),
                matcher.getPattern(),
                matcher.getProvider(),
                matcher.getFunc(),
                matcher.getPattern());
    }

    @Override
    public int[] saveTagMatchers(Collection<TagsMatcherModel> matchers) {
        return this.jdbcTemplate.batchUpdate("INSERT INTO matchers (provider, func, pattern) VALUES (?,?,?) " +
                        "ON DUPLICATE KEY UPDATE provider = ?, func = ?, pattern = ?",
                matchers.stream().map(matcher -> new String[] {
                        matcher.getProvider(),
                        matcher.getFunc(),
                        matcher.getPattern(),
                        matcher.getProvider(),
                        matcher.getFunc(),
                        matcher.getPattern()
                })
                        .collect(Collectors.toList()));
    }
}
