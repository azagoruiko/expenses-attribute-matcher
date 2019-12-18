package ua.org.zagoruiko.expenses.matcherservice.service;

import ua.org.zagoruiko.expenses.category.model.Tag;
import ua.org.zagoruiko.expenses.category.resolver.tags.ContainsTagsFromStringResolver;
import ua.org.zagoruiko.expenses.category.resolver.tags.EqualsTagsFromStringResolver;
import ua.org.zagoruiko.expenses.matcherservice.model.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface MatcherService extends Serializable {
    List<TagsMatcherModel> getMatcherSet(String provider);
    List<TagsMatcherModel> getMatcherSet();
    List<CategoryTag> getCategoryTags();
    CategoryTag setCategoryTag(CategoryTag tag);
    List<String> getTagSuggestions(String pattern);
    List<String> matchTags(String operationDescription);
    int saveTag(Tag tag);
    int[] saveTags(Collection<Tag> tags);
    Integer saveTagMatcher(TagsMatcherModel matcher);
    List<Integer> saveTagMatchers(Collection<TagsMatcherModel> matchers);
    List<ReportItemModel> report(Integer year, Integer month, Collection<String> includeTags, Collection<String> excludeTags);
    List<UnrecognizedTransactionModel> unrecognizedTransactions(Collection<String> srcTags);

    default String[] getImplementedTagResolverFunctions() {
        return new String[] {
                ContainsTagsFromStringResolver.ID,
                EqualsTagsFromStringResolver.ID
        };
    }
}
