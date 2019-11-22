package ua.org.zagoruiko.expenses.matcherservice.model;

import java.util.Set;

public class TagsMatcherModel extends MatcherModel {
    private Set<String> tags;

    public TagsMatcherModel(String provider, String func, String pattern, Set<String> tags) {
        super(provider, func, pattern);
        this.tags = tags;
    }

    public Set<String> getTags() {
        return this.tags;
    }
}
