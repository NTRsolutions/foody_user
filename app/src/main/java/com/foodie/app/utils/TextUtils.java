package com.foodie.app.utils;

import android.widget.EditText;

/**
 * Created by Tamil on 9/21/2017.
 */

public class TextUtils {

    //Check empty edit text
    public static boolean isEmpty(String strText) {
        return strText.length() == 0;
    }

    //check Valid Mail address
    public final static boolean isValidEmail(String strText) {
        if (strText == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(strText).matches();
        }
    }

}