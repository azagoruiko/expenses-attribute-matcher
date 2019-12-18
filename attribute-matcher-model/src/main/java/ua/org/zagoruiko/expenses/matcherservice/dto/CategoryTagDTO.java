package ua.org.zagoruiko.expenses.matcherservice.dto;

import java.io.Serializable;

public class CategoryTagDTO implements Serializable {
    private String tag;
    private boolean exclusive = false;

    public CategoryTagDTO() {}

    public CategoryTagDTO(String tag, boolean exclusive) {
        this.tag = tag;
        this.exclusive = exclusive;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }
}
