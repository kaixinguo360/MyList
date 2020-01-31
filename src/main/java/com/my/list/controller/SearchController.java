package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentContext;
import com.my.list.controller.util.SimpleController;
import com.my.list.dto.Node;
import com.my.list.service.data.ListService;
import com.my.list.service.filter.Filter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/search")
@Authorization
@SimpleController
public class SearchController {

    @GetMapping public List<Node> getAll(@CurrentContext ListService listService) {
        return listService.getAll(new Filter());
    }
    @PostMapping public List<Node> getAll(@RequestBody Filter filter, @CurrentContext ListService listService) {
        return listService.getAll(filter);
    }

    @PostMapping("tag") public List<Node> getTags(@RequestBody Filter filter, @CurrentContext ListService listService) {
        return listService.getAll(
            filter.addCondition("node_type", "=", "'tag'")
        );
    }
}
