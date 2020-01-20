package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentContext;
import com.my.list.controller.util.SimpleController;
import com.my.list.dto.Node;
import com.my.list.service.search.Query;
import com.my.list.service.search.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/search")
@Authorization
@SimpleController
public class SearchController {

    @PostMapping
    public List<Node> search(
        @RequestBody Query query,
        @CurrentContext SearchService searchService
    ) {
        return searchService.search(query);
    }

    @GetMapping
    public List<Node> getAll(
        @CurrentContext SearchService searchService
    ) {
        return searchService.search(new Query());
    }
}
