package ua.org.zagoruiko.expenses.matcherservice.model;

public class CategoryMatcherModel extends MatcherModel {
    private String category;

    public CategoryMatcherModel(String provider, String func, String pattern, String category) {
        super(provider, func, pattern);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }


}
