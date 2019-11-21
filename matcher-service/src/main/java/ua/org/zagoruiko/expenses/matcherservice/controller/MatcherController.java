package ua.org.zagoruiko.expenses.matcherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.org.zagoruiko.expenses.matcherservice.dto.CategoryMatcherDTO;
import ua.org.zagoruiko.expenses.matcherservice.dto.MatcherSetDTO;
import ua.org.zagoruiko.expenses.matcherservice.dto.TagsMatcherDTO;
import ua.org.zagoruiko.expenses.matcherservice.service.MatcherService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    @RequestMapping(value = "/matchers/match", method = RequestMethod.GET)
    public Collection<String> match(@RequestParam(name = "text") String text) {
        return this.matcherService.matchTags(text);
    }

}
