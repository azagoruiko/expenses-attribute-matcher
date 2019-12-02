package ua.org.zagoruiko.expenses.matcherservice.model;

public class UnrecognizedTransactionModel {
    String description;
    Integer count;
    Integer amount;

    public UnrecognizedTransactionModel(String description, Integer count, Integer amount) {
        this.description = description;
        this.count = count;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getAmount() {
        return amount;
    }
}
