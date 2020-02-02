package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentContext;
import com.my.list.controller.util.SimpleController;
import com.my.list.dto.Node;
import com.my.list.service.data.ListService;
import com.my.list.service.data.NodeService;
import com.my.list.service.data.PartService;
import com.my.list.service.filter.Filter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/node")
@Authorization
@SimpleController
public class NodeController {

    static class NodeInputWrap {
        private Node node;
        private List<Long> tags;

        public Node getNode() {
            return node;
        }
        public void setNode(Node node) {
            this.node = node;
        }
        public List<Long> getTags() {
            return tags;
        }
        public void setTags(List<Long> tags) {
            this.tags = tags;
        }
    }
    static class NodeOutputWrap {
        Node node;
        List<Node> tags;

        public Node getNode() {
            return node;
        }
        public List<Node> getTags() {
            return tags;
        }
    }

    @PostMapping
    public NodeOutputWrap post(
        @RequestBody NodeInputWrap input,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        nodeService.add(input.node);
        Long id = input.node.getMainData().getId();
        if (input.tags != null) partService.setParents(id, input.tags);
        
        NodeOutputWrap output = new NodeOutputWrap();
        output.node = input.node;
        output.tags = partService.getParents(id);
        return output;
    }

    @GetMapping("/{id}")
    public NodeOutputWrap get(
        @PathVariable Long id,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        NodeOutputWrap output = new NodeOutputWrap();
        output.node = nodeService.get(id);
        output.tags = partService.getParents(id);
        return output;
    }

    @PutMapping
    public NodeOutputWrap put(
        @RequestBody NodeInputWrap input,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        nodeService.update(input.node);
        Long id = input.node.getMainData().getId();
        if (input.tags != null) partService.setParents(id, input.tags);

        NodeOutputWrap output = new NodeOutputWrap();
        output.node = input.node;
        output.tags = partService.getParents(id);
        return output;
    }

    @DeleteMapping("/{id}")
    public void delete(
        @PathVariable Long id,
        @CurrentContext NodeService nodeService
    ) {
        nodeService.remove(id);
    }

    @GetMapping("search") public List<Node> getAll(@CurrentContext ListService listService) {
        return listService.getAll(new Filter());
    }
    @PostMapping("search") public List<Node> getAll(@RequestBody Filter filter, @CurrentContext ListService listService) {
        return listService.getAll(filter);
    }
}
