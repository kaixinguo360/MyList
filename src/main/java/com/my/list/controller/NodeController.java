package com.my.list.controller;

import com.my.list.controller.util.Authorization;
import com.my.list.controller.util.CurrentContext;
import com.my.list.controller.util.SimpleController;
import com.my.list.dto.Node;
import com.my.list.service.data.ListService;
import com.my.list.service.data.NodeService;
import com.my.list.service.data.PartService;
import com.my.list.service.filter.Filter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/node")
@Authorization
@SimpleController
public class NodeController {
    
    
    // ------------ Single Node ------------ //

    @PostMapping
    @Transactional
    public NodeOutputWrap post(
        @RequestBody NodeInputWrap input,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        nodeService.add(input.node);
        Long id = input.node.getMainData().getId();
        if (input.tags != null) partService.setParents(id, input.tags);
        
        return new NodeOutputWrap(input.node, partService.getParents(id));
    }

    @GetMapping("/{id}")
    public NodeOutputWrap get(
        @PathVariable Long id,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        return new NodeOutputWrap(nodeService.get(id), partService.getParents(id));
    }

    @PutMapping
    @Transactional
    public NodeOutputWrap put(
        @RequestBody NodeInputWrap input,
        @RequestParam(required = false, value = "simple") Boolean isSimple,
        @RequestParam(required = false, value = "tag") String tagAction,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        isSimple = (isSimple != null && isSimple);
        Long id;
        
        if (isSimple) {
            if (input.node != null) {
                id = input.node.getMainData().getId();
                nodeService.update(input.node, true);
            } else {
                id = input.getId();
            }
        } else {
            id = input.node.getMainData().getId();
            nodeService.update(input.node, false);
        }
        
        if (input.tags != null) {
            tagAction = (tagAction == null) ? "set" : tagAction;
            switch (tagAction) {
                case "add": partService.addParents(id, input.tags); break;
                case "remove": partService.removeParents(id, input.tags); break;
                case "set": default: partService.setParents(id, input.tags); break;
            }
        }
        
        return new NodeOutputWrap(input.node, partService.getParents(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(
        @PathVariable Long id,
        @CurrentContext NodeService nodeService
    ) {
        nodeService.remove(id);
    }

    
    // ------------ Batch Node ------------ //

    @PostMapping("batch")
    @Transactional
    public List<NodeOutputWrap> post(
        @RequestBody List<NodeInputWrap> inputs,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        return inputs.stream()
            .map(input -> post(input, nodeService, partService))
            .collect(Collectors.toList());
    }

    @GetMapping("search")
    public List<Node> getAll(@CurrentContext ListService listService) {
        return listService.getAll(new Filter());
    }
    
    @PostMapping("search")
    @Transactional
    public List<Node> getAll(@RequestBody Filter filter, @CurrentContext ListService listService) {
        return listService.getAll(filter);
    }

    @PutMapping("batch")
    @Transactional
    public List<NodeOutputWrap> put(
        @RequestBody List<NodeInputWrap> inputs,
        @RequestParam(required = false, value = "simple") Boolean isSimple,
        @RequestParam(required = false, value = "tag") String tagAction,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        return inputs.stream()
            .map(input -> put(input, isSimple, tagAction, nodeService, partService))
            .collect(Collectors.toList());
    }
    
    @PutMapping("tag")
    @Transactional
    public List<NodeOutputWrap> tag(
        @RequestBody List<Long> nodeIds,
        @RequestParam(required = false, value = "id") List<Long> tagIds,
        @RequestParam(required = false, value = "action") String tagAction,
        @CurrentContext NodeService nodeService,
        @CurrentContext PartService partService
    ) {
        return nodeIds.stream()
            .map(nodeId -> put(new NodeInputWrap(nodeId, tagIds), true, tagAction, nodeService, partService))
            .collect(Collectors.toList());
    }

    @DeleteMapping("batch")
    @Transactional
    public void delete(
        @RequestBody List<Long> ids,
        @CurrentContext NodeService nodeService
    ) {
        ids.forEach(id -> delete(id, nodeService));
    }


    // ------------ Wrap Class ------------ //

    static class NodeInputWrap {
        
        Long id;
        Node node;
        List<Long> tags;

        public NodeInputWrap() {}
        public NodeInputWrap(Long id, List<Long> tags) {
            this.id = id;
            this.tags = tags;
        }

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
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
    
        public NodeOutputWrap() {}
        public NodeOutputWrap(Node node, List<Node> tags) {
            this.node = node;
            this.tags = tags;
        }
    
        public Node getNode() {
            return node;
        }
        public List<Node> getTags() {
            return tags;
        }
    }
}
