package ua.org.zagoruiko.expenses.matcherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.org.zagoruiko.expenses.matcherservice.service.MatcherService;

import java.util.List;

@RestController
public class TagsController {
    @Autowired
    MatcherService matcherService;

    @RequestMapping(value = "/tags/suggest", method = RequestMethod.GET)
    public List<String> matchers(@RequestParam(name = "q") String pattern) {
        return this.matcherService.getTagSuggestions(pattern);
    }

}
