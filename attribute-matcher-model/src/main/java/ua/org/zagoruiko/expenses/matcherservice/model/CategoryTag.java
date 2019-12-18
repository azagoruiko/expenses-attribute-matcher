package ua.org.zagoruiko.expenses.matcherservice.model;

public class CategoryTag {
    private String tag;
    private boolean exclusive = false;

    public CategoryTag(String tag, boolean exclusive) {
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
