package com.my.list.service.node;

import com.my.list.domain.NodeMapper;
import com.my.list.domain.ProcedureMapper;
import com.my.list.dto.ListItem;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.DataException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class ListDataService {

    private final NodeMapper nodeMapper;
    private final ProcedureMapper procedureMapper;
    
    ListDataService(NodeMapper nodeMapper, ProcedureMapper procedureMapper) {
        this.nodeMapper = nodeMapper;
        this.procedureMapper = procedureMapper;
    }

    // ---- List ---- //
    void save(Long listId, List<ListItem> list, NodeService nodeService) {
        if (listId == null) throw new DataException("Input listId is null");
        if (list == null) throw new DataException("Input list is null.");
        procedureMapper.clean_list(listId);
        list.forEach(item -> {
            Long partId = null;
            switch (item.itemStatus) {
                case NEW:
                    Node newNode = item.node;
                    nodeService.add(newNode);
                    partId = newNode.getMainData().getId();
                    break;
                case UPDATE:
                    Node node = item.node;
                    nodeService.update(node);
                    partId = node.getMainData().getId();
                    break;
                case EXIST:
                    partId = item.node.getMainData().getId();
                    break;
            }
            procedureMapper.add_part(listId, partId);
        });
        procedureMapper.clean_nodes();
    }
    List<ListItem> get(Long listId) {
        if (listId == null) throw new DataException("Input listId is null");
        return nodeMapper.selectAllByListId(listId)
            .stream().map(NodeDTO::new).map(node -> new ListItem(node, ListItem.ItemStatus.EXIST)).collect(Collectors.toList());
    }
    void remove(Long listId) {
        if (listId == null) throw new DataException("Input listId is null");
        procedureMapper.delete_list(listId);
    }
}
