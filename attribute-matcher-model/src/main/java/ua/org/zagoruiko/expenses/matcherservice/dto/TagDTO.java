package ua.org.zagoruiko.expenses.matcherservice.dto;

public class TagDTO {
    private String name;
    private String description;

    public TagDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public TagDTO() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ua.org.zagoruiko.expenses.category.model.Tag toTag() {
        return new ua.org.zagoruiko.expenses.category.model.Tag(this.name, this.description);
    }
}
