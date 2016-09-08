package com.example.anna.bucketdrops.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.anna.bucketdrops.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anna on 28.08.2016.
 */
public class BucketRecyclerView extends RecyclerView {

    //NonEmptyViews are going to be displayed when adapter is not empty
    private List<View> mNonEmptyViews = Collections.emptyList();

    //Empty Views are going to be displayed when adapter is empty
    private List<View> mEmptyViews = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {

        if(getAdapter()!=null && !mEmptyViews.isEmpty()&& !mNonEmptyViews.isEmpty()){
            if(getAdapter().getItemCount() == 0){ // adapter hat keine daten aus db drin

                //show all the views which are meant to be visible when there are no drops to display
                Util.showViews(mEmptyViews);

                //hide the recycler view itself
                setVisibility(View.GONE);

                //hide all the views which are meant to be hidden e.g. toolbar
                Util.hideViews(mNonEmptyViews);
            } else {
                //show all needed views  when drops are being displayed
                Util.showViews(mNonEmptyViews);

                //when we do have items, we show the recycler view
                setVisibility(View.VISIBLE);

                //hide all views which are meant to be displayed when no drops
                Util.hideViews(mEmptyViews);
            }
        }
    }
    //This constructor initializes recyclerView from code
    public BucketRecyclerView(Context context) {
        super(context);
    }

    //This constructor allows to initialize constructer view from xml
    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    //This constructor allows to initialize constructer view from xml; it defines also a custom style
    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter){
        super.setAdapter(adapter);
        if(adapter!=null){
            adapter.registerAdapterDataObserver(mObserver);
        }
        mObserver.onChanged();
    }

    public void hideIfEmpty(View ... views) {
        mNonEmptyViews = Arrays.asList(views);
    }

    public void showIfEmpty(View ... emptyViews) {
        mEmptyViews = Arrays.asList(emptyViews);
    }
}
