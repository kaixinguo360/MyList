package com.my.list.entity.filter;

import com.my.list.entity.MainData;
import com.my.list.entity.Node;
import com.my.list.service.SearchService;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class Filter {

    public boolean filter(Node node) {
        MainData mainData = node.getMainData();
        return (
            (this.part == null || this.part == mainData.getPart())
                && (this.collection == null || this.collection == mainData.getCollection())
                && (this.nsfw == null || this.nsfw == mainData.getNsfw())
                && (this.like == null || this.like == mainData.getLike())
                && (this.hide == null || this.hide == mainData.getHide())
                && (this.types == null || this.types.contains(mainData.getType()))
        );
    }

    List<String> types = null;

    Boolean cascade = null;
    Boolean part = null;
    Boolean collection = null;

    Permission permission = null;
    Boolean nsfw = null;
    Boolean like = null;
    Boolean hide = null;

    List<Condition> conditions = null;
    public Filter addCondition(String column, String oper, Object value) {
        return addCondition(new Condition(column, oper, value));
    }
    public Filter addCondition(Condition condition) {
        if (conditions == null) conditions = new ArrayList<>();
        conditions.add(condition);
        return this;
    }

    List<Sort> sorts = null;
    public Filter addSort(String property, Sort.Direction direction) {
        return addSort(new Sort(property, direction));
    }
    public Filter addSort(Sort sort) {
        if (sorts == null) sorts = new ArrayList<>();
        sorts.add(sort);
        return this;
    }

    Set<Tag> andTags = null;
    Set<Tag> orTags = null;
    Set<Tag> notTags = null;
    public Filter addAndTag(Tag tag) {
        if (andTags == null) andTags = new HashSet<>();
        andTags.add(tag);
        return this;
    }
    public Filter addOrTag(Tag tag) {
        if (orTags == null) orTags = new HashSet<>();
        orTags.add(tag);
        return this;
    }
    public Filter addNotTag(Tag tag) {
        if (notTags == null) notTags = new HashSet<>();
        notTags.add(tag);
        return this;
    }

    Set<String> andKeywords = null;
    Set<String> orKeywords = null;
    Set<String> notKeywords = null;
    public Filter addAndKeyword(String keyword) {
        if (andKeywords == null) andKeywords = new HashSet<>();
        andKeywords.add(keyword);
        return this;
    }
    public Filter addOrKeyword(String keyword) {
        if (orKeywords == null) orKeywords = new HashSet<>();
        orKeywords.add(keyword);
        return this;
    }
    public Filter addNotKeyword(String keyword) {
        if (notKeywords == null) notKeywords = new HashSet<>();
        notKeywords.add(keyword);
        return this;
    }

    public List<Node> getAll(SearchService searchService) {
        return searchService.getAll(this);
    }
}
