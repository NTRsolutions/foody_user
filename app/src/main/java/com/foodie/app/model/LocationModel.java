package com.foodie.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-08-2017.
 */

public class LocationModel {
    String header;
    List<Location> locations = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> persons) {
        this.locations = persons;
    }
}
