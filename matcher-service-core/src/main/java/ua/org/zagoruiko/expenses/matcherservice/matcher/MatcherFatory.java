package ua.org.zagoruiko.expenses.matcherservice.matcher;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import ua.org.zagoruiko.expenses.category.matcher.Matcher;
import ua.org.zagoruiko.expenses.category.matcher.SimplePbMatcher;
import ua.org.zagoruiko.expenses.category.model.Tag;
import ua.org.zagoruiko.expenses.category.resolver.ResolverFromString;
import ua.org.zagoruiko.expenses.category.resolver.tags.ContainsTagsFromStringResolver;
import ua.org.zagoruiko.expenses.category.resolver.tags.EqualsTagsFromStringResolver;
import ua.org.zagoruiko.expenses.matcherservice.dto.TagsMatcherDTO;
import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MatcherFatory {

    private static final String CONTAINS = ContainsTagsFromStringResolver.ID;
    private static final String EQUAL = EqualsTagsFromStringResolver.ID;
    private static final String REGEX = "REGEX";

    private static Set<Tag> tagSetFromTagModel(TagsMatcherModel model) {
        return tagSetFromTagModel(TagsMatcherDTO.fromModel(model));
    }

    private static Set<Tag> tagSetFromTagModel(TagsMatcherDTO model) {
        return model.getTags().stream().map(t -> new Tag(t, t)).collect(Collectors.toSet());
    }

    private static ResolverFromString<Set<Tag>> tagResolverFromModel(TagsMatcherModel model) {
        return tagResolverFromModel(TagsMatcherDTO.fromModel(model));
    }

    private static ResolverFromString<Set<Tag>> tagResolverFromModel(TagsMatcherDTO model) {
        switch (model.getFunc()) {
            case CONTAINS:
                return new ContainsTagsFromStringResolver(model.getPattern(),
                        tagSetFromTagModel(model));
            case EQUAL:
                return new EqualsTagsFromStringResolver(model.getPattern(),
                        tagSetFromTagModel(model));
        }
        throw new NotImplementedException();
    }

    public static Matcher<String> createMatcher(Collection<TagsMatcherModel> models, String providerId) {
        return new SimplePbMatcher(models.stream()
                .filter(m -> providerId.equals(m.getProvider()))
                .map(m -> tagResolverFromModel(m))
                .collect(Collectors.toList()));
    }

    public static Matcher<String> createMatcherFromDTO(Collection<TagsMatcherDTO> models, String providerId) {
        return new SimplePbMatcher(models.stream()
                .filter(m -> providerId.equals(m.getProvider()))
                .map(m -> tagResolverFromModel(m))
                .collect(Collectors.toList()));
    }
}
