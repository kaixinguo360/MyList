package com.my.list.service.search;

import java.util.ArrayList;
import java.util.List;

public class Query {
    List<Condition> conditions = new ArrayList<>();
    List<Sort> sorts;

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }
    public void addCondition(String column, String oper, Object value) {
        addCondition(new Condition(column, oper, value));
    }

    public void addSort(Sort sort) {
        if (sorts == null) sorts = new ArrayList<>();
        sorts.add(sort);
    }
    public void addSort(String property, Sort.Direction direction) {
        addSort(new Sort(property, direction));
    }
    public void addSort(String property) {
        addSort(property, Sort.Direction.ASC);
    }
}
