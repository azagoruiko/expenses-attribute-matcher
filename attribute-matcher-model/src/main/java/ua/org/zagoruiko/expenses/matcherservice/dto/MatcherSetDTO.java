package ua.org.zagoruiko.expenses.matcherservice.dto;

import java.io.Serializable;
import java.util.Set;

public class MatcherSetDTO  implements Serializable {
    private Set<CategoryMatcherDTO> categoryMatcher;
    private Set<TagsMatcherDTO> tagsMatcher;

    public MatcherSetDTO(Set<CategoryMatcherDTO> categoryMatcher, Set<TagsMatcherDTO> tagsMatcher) {
        this.categoryMatcher = categoryMatcher;
        this.tagsMatcher = tagsMatcher;
    }

    public MatcherSetDTO() {
    }

    public Set<CategoryMatcherDTO> getCategoryMatcher() {
        return categoryMatcher;
    }

    public Set<TagsMatcherDTO> getTagsMatcher() {
        return tagsMatcher;
    }
}
