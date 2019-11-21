package ua.org.zagoruiko.expenses.matcherservice.model;

public abstract class MatcherModel {
    protected String provider;
    protected String func;
    protected String pattern;

    public MatcherModel(String provider, String func, String pattern) {
        this.provider = provider;
        this.func = func;
        this.pattern = pattern;
    }

    public String getProvider() {
        return provider;
    }

    public String getFunc() {
        return func;
    }

    public String getPattern() {
        return pattern;
    }
}
