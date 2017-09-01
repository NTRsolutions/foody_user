package com.foodie.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-08-2017.
 */

public class FilterModel {
    String header;
    List<String> filters = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

}
