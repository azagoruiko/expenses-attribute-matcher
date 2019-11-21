package ua.org.zagoruiko.expenses.matcherservice.service;

import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;

import java.io.Serializable;
import java.util.List;

public interface MatcherService extends Serializable {
    List<TagsMatcherModel> getMatcherSet(String provider);
    List<String> getTagSuggestions(String pattern);
    List<String> matchTags(String operationDescription);
}
