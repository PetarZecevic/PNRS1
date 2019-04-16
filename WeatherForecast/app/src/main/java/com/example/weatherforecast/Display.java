package com.example.weatherforecast;

import java.util.HashMap;

import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;

public class Display {
    public HashMap<String, TextView> items;
    public HashMap<String, String> values;
    public LinearLayout itemsHolder;

    public void show(Boolean yes) {
        itemsHolder.setVisibility(yes ? View.VISIBLE : View.INVISIBLE);
    }

    public Display(){
        items = new HashMap<String, TextView>();
        values = new HashMap<String, String>();
    }

    /**
     * Method assumes that both maps have the same keys.
     */
    public void setValues(){
        for(String k : values.keySet()) {
            if (items.containsKey(k)) {
                items.get(k).setText(values.get(k));
            }
        }
    }
}
