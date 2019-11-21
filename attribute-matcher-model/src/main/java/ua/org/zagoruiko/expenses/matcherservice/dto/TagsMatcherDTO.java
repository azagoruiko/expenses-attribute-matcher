package ua.org.zagoruiko.expenses.matcherservice.dto;

import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;

import java.io.Serializable;
import java.util.Set;

public class TagsMatcherDTO extends MatcherDTO implements Serializable {
    private Set<String> tags;

    public TagsMatcherDTO(String provider, String func, String pattern, Set<String> tags) {
        super(provider, func, pattern);
        this.tags = tags;
    }

    public TagsMatcherDTO() {
    }

    public Set<String> getTags() {
        return tags;
    }

    public static TagsMatcherDTO fromModel(TagsMatcherModel tagsMatcher) {
        return new TagsMatcherDTO(
                tagsMatcher.getProvider(),
                tagsMatcher.getFunc(),
                tagsMatcher.getPattern(),
                tagsMatcher.getTags());
    }
}
