package ua.org.zagoruiko.expenses.matcherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.org.zagoruiko.expenses.category.model.Tag;
import ua.org.zagoruiko.expenses.matcherservice.dto.TagDTO;
import ua.org.zagoruiko.expenses.matcherservice.service.MatcherService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TagsController {
    @Autowired
    MatcherService matcherService;

    @RequestMapping(value = "/tags/suggest", method = RequestMethod.GET)
    public List<String> matchers(@RequestParam(name = "q") String pattern) {
        return this.matcherService.getTagSuggestions(pattern);
    }

    @RequestMapping(value = "/tags", method = RequestMethod.PUT)
    public TagDTO tag(@RequestBody TagDTO tag) {
        int done = this.matcherService.saveTag(tag.toTag());
        if (done > 0) {
            return tag;
        }
        else {
            throw new RuntimeException("Nothing was saved");
        }
    }

    @RequestMapping(value = "/tags/addmany", method = RequestMethod.PUT)
    public Collection<TagDTO> tags(@RequestBody Collection<TagDTO> tags) {
        int[] done = this.matcherService.saveTags(tags.stream().map(t -> t.toTag()).collect(Collectors.toList()));
        if (done.length > 0) {
            return tags;
        }
        else {
            throw new RuntimeException("Nothing was saved");
        }
    }

}
