package com.my.list.service.search;

import com.my.list.dto.Node;

import java.util.ArrayList;
import java.util.List;

public class Query {
    
    List<Condition> conditions = null;
    List<Sort> sorts = null;
    Permission permission = null;
    Boolean nsfw = null;
    Boolean like = null;
    Boolean hide = null;

    public Query addCondition(Condition condition) {
        if (conditions == null) conditions = new ArrayList<>();
        conditions.add(condition);
        return this;
    }
    public Query addCondition(String column, String oper, Object value) {
        addCondition(new Condition(column, oper, value));
        return this;
    }

    public Query addSort(Sort sort) {
        if (sorts == null) sorts = new ArrayList<>();
        sorts.add(sort);
        return this;
    }
    public Query addSort(String property, Sort.Direction direction) {
        addSort(new Sort(property, direction));
        return this;
    }
    public Query addSort(String property) {
        addSort(property, Sort.Direction.ASC);
        return this;
    }

    public Query setPermission(Permission permission) {
        this.permission = permission;
        return this;
    }
    public Query setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }
    public Query setLike(Boolean like) {
        this.like = like;
        return this;
    }
    public Query setHide(Boolean hide) {
        this.hide = hide;
        return this;
    }
    
    public List<Node> search(SearchService searchService) {
        return searchService.simpleSearch(this);
    }
}
