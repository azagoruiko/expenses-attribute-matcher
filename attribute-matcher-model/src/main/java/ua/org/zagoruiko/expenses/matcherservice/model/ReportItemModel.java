package ua.org.zagoruiko.expenses.matcherservice.model;

import ua.org.zagoruiko.expenses.category.model.Tag;

public class ReportItemModel {
    Integer year;
    Integer month;
    Integer amount;
    Tag tag;

    public ReportItemModel(Integer year, Integer month, Integer amount, Tag tag) {
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.tag = tag;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportItemModel)) return false;

        ReportItemModel that = (ReportItemModel) o;

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
