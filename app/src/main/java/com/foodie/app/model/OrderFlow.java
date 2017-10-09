package com.foodie.app.model;

import android.widget.TextView;

/**
 * Created by Tamil on 10/9/2017.
 */

public class OrderFlow {
    public   int statusImage;
    public  String statusTitle,statusDescription;

    public  OrderFlow(String statusTitle,String statusDescription, int statusImage){
        this.statusImage=statusImage;
        this.statusTitle=statusTitle;
        this.statusDescription=statusDescription;
    }
}
