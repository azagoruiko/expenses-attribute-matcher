package ua.org.zagoruiko.expenses.matcherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.org.zagoruiko.expenses.matcherservice.dto.CategoryMatcherDTO;
import ua.org.zagoruiko.expenses.matcherservice.dto.MatcherSetDTO;
import ua.org.zagoruiko.expenses.matcherservice.dto.TagsMatcherDTO;
import ua.org.zagoruiko.expenses.matcherservice.dto.UnrecognizedTransactionDTO;
import ua.org.zagoruiko.expenses.matcherservice.model.TagsMatcherModel;
import ua.org.zagoruiko.expenses.matcherservice.service.MatcherService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ReportController {
    @Autowired
    MatcherService matcherService;

    @RequestMapping(value = "/report/unrecognized", method = RequestMethod.GET)
    public List<UnrecognizedTransactionDTO> matchers(@RequestParam String tags) {
        return this.matcherService.unrecognizedTransactions(Arrays.asList(tags.split(",")))
                .stream().map(x -> UnrecognizedTransactionDTO.fromModel(x)).collect(Collectors.toList());
    }

}
