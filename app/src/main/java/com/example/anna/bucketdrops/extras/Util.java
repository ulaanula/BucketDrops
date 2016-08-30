package com.example.anna.bucketdrops.extras;

import android.view.View;

import java.util.List;

/**
 * Created by Anna on 30.08.2016.
 */
public class Util {

    public static void showViews(List<View> views){

        for(View view: views){
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideViews(List<View> views){

        for(View view: views){
            view.setVisibility(View.GONE);
        }
    }
}
