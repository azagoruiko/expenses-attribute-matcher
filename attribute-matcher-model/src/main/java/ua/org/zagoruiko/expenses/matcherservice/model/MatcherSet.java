package ua.org.zagoruiko.expenses.matcherservice.model;

import java.util.Set;

public class MatcherSet {
    private Set<CategoryMatcherModel> categoryMatcher;
    private Set<TagsMatcherModel> tagsMatcher;

    public MatcherSet(Set<CategoryMatcherModel> categoryMatcher, Set<TagsMatcherModel> tagsMatcher) {
        this.categoryMatcher = categoryMatcher;
        this.tagsMatcher = tagsMatcher;
    }

    public Set<CategoryMatcherModel> getCategoryMatcher() {
        return categoryMatcher;
    }

    public Set<TagsMatcherModel> getTagsMatcher() {
        return tagsMatcher;
    }
}
