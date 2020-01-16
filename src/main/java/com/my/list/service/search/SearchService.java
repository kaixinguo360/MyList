package com.my.list.service.search;

import com.my.list.domain.User;
import com.my.list.dto.Node;
import com.my.list.dto.NodeDTO;
import com.my.list.service.DataException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
    
    private final Long userId;
    private final SearchMapper searchMapper;

    public SearchService(User user, SearchMapper searchMapper) {
        if (user == null) throw new DataException("User is null");
        if (user.getId() == null) throw new DataException("Id of user is null");
        this.userId = user.getId();
        this.searchMapper = searchMapper;
    }

    public List<Node> simpleSearch(Query query) {
        return searchMapper.simpleSearch(query.conditions, query.sorts)
            .stream().map(NodeDTO::new).collect(Collectors.toList());
    }

    @Service
    public static class SearchServiceFactory {

        private final SearchMapper searchService;

        public SearchServiceFactory(SearchMapper searchService) {
            this.searchService = searchService;
        }

        public SearchService create(User user) {
            return new SearchService(user, searchService);
        }
    }

}
