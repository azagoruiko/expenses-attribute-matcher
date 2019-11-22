package ua.org.zagoruiko.expenses.matcherservice.service;

import ua.org.zagoruiko.expenses.category.model.Tag;
import ua.org.zagoruiko.expenses.category.resolver.tags.ContainsTagsFromStringResolver;
import ua.org.zagoruiko.expenses.category.resolver.tags.EqualsTagsFromStringResolver;
import ua.org.zagoruiko.expenses.matcherservice.model.MatcherModel;
import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface MatcherService extends Serializable {
    List<TagsMatcherModel> getMatcherSet(String provider);
    List<String> getTagSuggestions(String pattern);
    List<String> matchTags(String operationDescription);
    int saveTag(Tag tag);
    int[] saveTags(Collection<Tag> tags);
    int saveTagMatcher(TagsMatcherModel matcher);
    int[] saveTagMatchers(Collection<TagsMatcherModel> matchers);
    default String[] getImplementedTagResolverFunctions() {
        return new String[] {
                ContainsTagsFromStringResolver.ID,
                EqualsTagsFromStringResolver.ID
        };
    }
}
