package ua.org.zagoruiko.expenses.matcherservice.dto;

import ua.org.zagoruiko.expenses.matcherservice.model.CategoryMatcherModel;

import java.io.Serializable;

public class CategoryMatcherDTO extends MatcherDTO implements Serializable {
    private String category;

    public CategoryMatcherDTO(String provider, String func, String pattern, String category) {
        super(provider, func, pattern);
        this.category = category;
    }

    public CategoryMatcherDTO() {
    }

    public String getCategory() {
        return category;
    }

    public static CategoryMatcherDTO fromModel(CategoryMatcherModel categoryMatcher) {
        return new CategoryMatcherDTO(
                categoryMatcher.getProvider(),
                categoryMatcher.getFunc(),
                categoryMatcher.getPattern(),
                categoryMatcher.getCategory());
    }
}
