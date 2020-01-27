package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentContext;
import com.my.list.controller.util.SimpleController;
import com.my.list.dto.Node;
import com.my.list.service.data.NodeService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/node")
@Authorization
@SimpleController
public class NodeController {

    @PostMapping
    public Node post(
        @RequestBody Node node,
        @CurrentContext NodeService nodeService
    ) {
        nodeService.add(node);
        return node;
    }

    @GetMapping("/{id}")
    public Node get(
        @PathVariable Long id,
        @CurrentContext NodeService nodeService
    ) {
        return nodeService.get(id);
    }

    @PutMapping
    public Node put(
        @RequestBody Node node,
        @CurrentContext NodeService nodeService
    ) {
        nodeService.update(node);
        return node;
    }

    @DeleteMapping("/{id}")
    public void delete(
        @PathVariable Long id,
        @CurrentContext NodeService nodeService
    ) {
        nodeService.remove(id);
    }
}
