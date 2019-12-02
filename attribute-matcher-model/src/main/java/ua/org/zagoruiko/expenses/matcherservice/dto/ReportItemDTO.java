package ua.org.zagoruiko.expenses.matcherservice.dto;

import ua.org.zagoruiko.expenses.matcherservice.model.ReportItemModel;

import java.io.Serializable;

public class ReportItemDTO implements Serializable {
    Integer year;
    Integer month;
    Integer amount;
    TagDTO tag;

    public ReportItemDTO(Integer year, Integer month, Integer amount, TagDTO tag) {
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.tag = tag;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getAmount() {
        return amount;
    }

    public TagDTO getTag() {
        return tag;
    }

    public static ReportItemDTO fromModel(ReportItemModel model) {
        return new ReportItemDTO(
                model.getYear(),
                model.getMonth(),
                model.getAmount(),
                new TagDTO(model.getTag().getName(), model.getTag().getDescription())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportItemDTO)) return false;

        ReportItemDTO that = (ReportItemDTO) o;

        if (year != null ? !year.equals(that.year) : that.year != null) return false;
        if (month != null ? !month.equals(that.month) : that.month != null) return false;
        if (!amount.equals(that.amount)) return false;
        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        int result = year != null ? year.hashCode() : 0;
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + amount.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }
}
