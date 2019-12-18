package ua.org.zagoruiko.expenses.matcherservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.org.zagoruiko.expenses.category.matcher.Matcher;
import ua.org.zagoruiko.expenses.category.model.Tag;
import ua.org.zagoruiko.expenses.matcherservice.matcher.MatcherFatory;
import ua.org.zagoruiko.expenses.matcherservice.model.CategoryTag;
import ua.org.zagoruiko.expenses.matcherservice.model.ReportItemModel;
import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;
import ua.org.zagoruiko.expenses.matcherservice.model.UnrecognizedTransactionModel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcMatcherService implements MatcherService, Serializable {
    private final static int MATCHER_REFRESH_INTERVAL = 1000 * 60 * 2;
    private Matcher<String> matcher;
    private long lastUpdatedTs = new Date().getTime();

    private JdbcTemplate jdbcTemplate;

    private void loadMatchers() {
        this.matcher = MatcherFatory.createMatcher(this.getMatcherSet("pb"), "pb");
    }

    // TODO refresh in bg instead of sync
    private synchronized boolean refreshMatchers() {
        if ((new Date().getTime() - this.lastUpdatedTs) >= MATCHER_REFRESH_INTERVAL) {
            this.loadMatchers();
            this.lastUpdatedTs = new Date().getTime();
            return true;
        }
        return false;
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
    public List<TagsMatcherModel> getMatcherSet() {
        return jdbcTemplate.query(
                "SELECT m.provider, m.func, m.pattern, m.is_category, GROUP_CONCAT(rv.value SEPARATOR ',')as val\n " +
                        "FROM `return_values` rv\n" +
                        "                join matchers m on rv.matcher_id = m.id\n" +
                        "where m.is_category <> 1\n" +
                        "group by m.provider, m.func, m.pattern, m.is_category", new Object[] { },
                (rs, rowNum) ->
                        new TagsMatcherModel(rs.getString("provider"),
                                rs.getString("func"),
                                rs.getString("pattern"),
                                new HashSet<>(Arrays.asList(rs.getString("val").split(",")))
                        )
        );
    }

    @Override
    public List<CategoryTag> getCategoryTags() {
        return jdbcTemplate.query(
                "SELECT tag, exclusive\n " +
                        "FROM `category_tags`\n", new Object[] { },
                (rs, rowNum) ->
                        new CategoryTag(rs.getString("tag"),
                                rs.getBoolean("exclusive")
                        )
        );
    }

    @Override
    public CategoryTag setCategoryTag(CategoryTag tag) {
        int updated = this.jdbcTemplate.update("INSERT INTO `category_tags` (`tag`, exclusive) VALUES (?,?)" +
                        "ON DUPLICATE KEY UPDATE `tag` = ?, exclusive = ?",
                tag.getTag().toUpperCase(),
                tag.isExclusive(),
                tag.getTag().toUpperCase(),
                tag.isExclusive()
        );

        return updated > 0 ? jdbcTemplate.query(
                "SELECT tag, exclusive\n " +
                        "FROM `category_tags` where tag = ?", new Object[] { tag.getTag() },
                (rs, rowNum) ->
                        new CategoryTag(rs.getString("tag"),
                                rs.getBoolean("exclusive")
                        )
        ).stream().findAny().orElseGet(() -> null) : null;
    }

    @Override
    public List<String> getTagSuggestions(String pattern) {
        return this.jdbcTemplate.query("SELECT `value` FROM `values` WHERE `value` LIKE ? OR friendly_name LIKE ?",
                new Object[] {"%" + pattern + "%", "%" + pattern + "%"},
                (rs, rowNum) -> rs.getString(1));
    }

    @Override
    public List<String> matchTags(String operationDescription) {
        this.refreshMatchers();
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
    @Transactional
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
    @Transactional
    public Integer saveTagMatcher(TagsMatcherModel matcher) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO matchers (provider, func, pattern) VALUES (?,?,?) " +
                    "ON DUPLICATE KEY UPDATE provider = ?, func = ?, pattern = ?", new String[] {"id"});
            ps.setString(1, matcher.getProvider());
            ps.setString(2, matcher.getFunc());
            ps.setString(3, matcher.getPattern());
            ps.setString(4, matcher.getProvider());
            ps.setString(5, matcher.getFunc());
            ps.setString(6, matcher.getPattern());
            return ps;
        }, keyHolder);
        this.jdbcTemplate.update("DELETE FROM return_values WHERE matcher_id = ?", keyHolder.getKey());
        this.jdbcTemplate.batchUpdate("INSERT INTO return_values (matcher_id, `value`) VALUES (?, ?)",
                matcher.getTags().stream().map(t -> new Object[] {keyHolder.getKey(), t}).collect(Collectors.toList()));

        return keyHolder.getKey().intValue();
    }

    @Override
    @Transactional
    public List<Integer> saveTagMatchers(Collection<TagsMatcherModel> matchers) {
        return matchers.stream().map(m -> saveTagMatcher(m)).collect(Collectors.toList());
    }

    @Override
    public List<ReportItemModel> report(Integer year, Integer month, Collection<String> includeTags, Collection<String> excludeTags) {
        String sql = "select tt.`value`, MONTH(t.transaction_date) mon, ROUND(SUM(t.amount)) amount\n" +
                "FROM transaction_tags tt\n" +
                "    JOIN transactions t on tt.transaction_id = t.id\n" +
                "    -- WHERE tt.value in ('TAXI', 'FOZZY', 'SUPERMARKET', 'HOUSEHOLD')\n" +
                "GROUP BY tt.`value`, MONTH(t.transaction_date) WITH ROLLUP\n" +
                "ORDER BY tt.`value`, MONTH(t.transaction_date), COUNT(*) DESC;";
        return null;
    }

    @Override
    public List<UnrecognizedTransactionModel> unrecognizedTransactions(Collection<String> srcTags) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT t.description, COUNT(*) cnt, ROUND(SUM(t.amount)) amount, GROUP_CONCAT(DISTINCT tt.`value`) " +
                "from transactions t\n" +
                "JOIN transaction_tags tt on t.id = tt.transaction_id\n" +
                "GROUP BY description\n" +
                "HAVING 1=1 \n"
                );
        srcTags.forEach(tag -> sqlBuilder.append("AND GROUP_CONCAT(DISTINCT tt.`value`) = ?\n"));
        sqlBuilder.append("ORDER BY  COUNT(*) DESC;");
        return this.jdbcTemplate.query(sqlBuilder.toString(),
                srcTags.toArray(),
                (rs, rowNum) -> new UnrecognizedTransactionModel(
                        rs.getString(1), rs.getInt(2), rs.getInt(3)));
    }
}
