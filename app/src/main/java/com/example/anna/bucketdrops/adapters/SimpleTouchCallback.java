package com.example.anna.bucketdrops.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by Anna on 01.09.2016.
 */
public class SimpleTouchCallback extends ItemTouchHelper.Callback {

    private SwipeListener mSwipeListener;
    private final String TAG="ANNA";

    public SimpleTouchCallback(SwipeListener listener) {
        mSwipeListener=listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.END);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    // prevent the footer from swiping by overriding onChildDraw and onChildDrawOver
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDraw: "+ dX + " " + dY);

        if(viewHolder instanceof AdapterDrops.DropHolder){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.d(TAG, "onChildDrawOver: "+ dX + " " + dY);

        if(viewHolder instanceof AdapterDrops.DropHolder){
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if(viewHolder instanceof AdapterDrops.DropHolder){
            mSwipeListener.onSwipe(viewHolder.getAdapterPosition());
        }

    }
}
