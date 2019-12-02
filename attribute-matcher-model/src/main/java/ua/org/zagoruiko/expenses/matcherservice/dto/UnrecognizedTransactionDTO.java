package ua.org.zagoruiko.expenses.matcherservice.dto;

import ua.org.zagoruiko.expenses.matcherservice.model.UnrecognizedTransactionModel;

import java.io.Serializable;

public class UnrecognizedTransactionDTO implements Serializable {
    String description;
    Integer count;
    Integer amount;

    public UnrecognizedTransactionDTO(String description, Integer count, Integer amount) {
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

    public static UnrecognizedTransactionDTO fromModel(UnrecognizedTransactionModel model) {
        return new UnrecognizedTransactionDTO(model.getDescription(), model.getCount(), model.getAmount());
    }
}
