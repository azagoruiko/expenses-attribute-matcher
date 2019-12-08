package ua.org.zagoruiko.expenses.matcherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.org.zagoruiko.expenses.matcherservice.dto.CategoryMatcherDTO;
import ua.org.zagoruiko.expenses.matcherservice.dto.MatcherSetDTO;
import ua.org.zagoruiko.expenses.matcherservice.dto.TagsMatcherDTO;
import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;
import ua.org.zagoruiko.expenses.matcherservice.service.MatcherService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MatcherController {
    @Autowired
    MatcherService matcherService;

    @RequestMapping(value = "/matchers/{provider}", method = RequestMethod.GET)
    public MatcherSetDTO matchers(@PathVariable String provider) {
        Set<CategoryMatcherDTO> categoryMatchers = new HashSet<>();
        Set<TagsMatcherDTO> tagsMatcher = new HashSet<>();

        this.matcherService.getMatcherSet(provider).forEach((matcher) -> {
            tagsMatcher.add(TagsMatcherDTO.fromModel(matcher));
        } );

        return new MatcherSetDTO(categoryMatchers,
                tagsMatcher);
    }

    @RequestMapping(value = "/matchers", method = RequestMethod.GET)
    public MatcherSetDTO matchers() {
        Set<CategoryMatcherDTO> categoryMatchers = new HashSet<>();
        Set<TagsMatcherDTO> tagsMatcher = new HashSet<>();

        this.matcherService.getMatcherSet().forEach((matcher) -> {
            tagsMatcher.add(TagsMatcherDTO.fromModel(matcher));
        } );

        return new MatcherSetDTO(categoryMatchers,
                tagsMatcher);
    }

    @RequestMapping(value = "/matchers/match", method = RequestMethod.GET)
    public Collection<String> match(@RequestParam(name = "text") String text) {
        return this.matcherService.matchTags(text);
    }

    @RequestMapping(value = "/matchers/available_functions", method = RequestMethod.GET)
    public Collection<String> availableFunctions() {
        return Arrays.asList(this.matcherService.getImplementedTagResolverFunctions());
    }

    @RequestMapping(value = "/matchers", method = RequestMethod.PUT)
    public TagsMatcherDTO tag(@RequestBody TagsMatcherDTO matcher) {
        int done = this.matcherService.saveTagMatcher(new TagsMatcherModel(
                matcher.getProvider(), matcher.getFunc(), matcher.getPattern(), matcher.getTags()
        ));
        if (done > 0) {
            return matcher;
        }
        else {
            throw new RuntimeException("Nothing was saved");
        }
    }

    @RequestMapping(value = "/matchers/addmany", method = RequestMethod.PUT)
    public Collection<TagsMatcherDTO> tag(@RequestBody Collection<TagsMatcherDTO> matchers) {
        List<Integer> done = this.matcherService.saveTagMatchers(matchers.stream().map(matcher ->
                new TagsMatcherModel(
                matcher.getProvider(), matcher.getFunc(), matcher.getPattern(), matcher.getTags()
        )).collect(Collectors.toList()));
        if (done.size() > 0) {
            return matchers;
        }
        else {
            throw new RuntimeException("Nothing was saved");
        }
    }

}
