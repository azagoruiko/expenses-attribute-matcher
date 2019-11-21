package ua.org.zagoruiko.expenses.matcherservice.dto;

import java.io.Serializable;

public abstract class MatcherDTO implements Serializable {
    private String provider;
    private String func;
    private String pattern;

    public MatcherDTO(String provider, String func, String pattern) {
        this.provider = provider;
        this.func = func;
        this.pattern = pattern;
    }

    public MatcherDTO() {
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
