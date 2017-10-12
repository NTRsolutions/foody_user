package com.foodie.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamil on 28-08-2017.
 */

public class FilterModel {
    String header;
    List<Cuisine> filters = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Cuisine> getFilters() {
        return filters;
    }

    public void setFilters(List<Cuisine> filters) {
        this.filters = filters;
    }

}
