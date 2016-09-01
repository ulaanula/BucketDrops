package com.example.anna.bucketdrops.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.anna.bucketdrops.R;

/**
 * Created by Anna on 30.08.2016.
 */
public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrientation;
    public static final String TAG = "ANNA";

    public Divider(Context context, int orientation ){

        mDivider = ContextCompat.getDrawable(context, R.drawable.divider);

        if(orientation != LinearLayout.VERTICAL){
            throw new IllegalArgumentException("This item decoration can be used only with a RecyclerView that uses LinearLayoutManager with vertical orientaion ");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if(mOrientation== LinearLayout.VERTICAL){

            drawHorizontalDivider(c,parent, state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left, top, right, bottom;
        left = parent.getPaddingLeft();
        right = parent.getWidth()- parent.getPaddingRight();
        int count = parent.getChildCount();

        for(int i = 0; i < count; i++){

            if(AdapterDrops.FOOTER!= parent.getAdapter().getItemViewType(i)) {
                View current = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) current.getLayoutParams();
                top = current.getTop()-params.topMargin; // current.getTop()-params.topMargin; current.getBottom()-parent.bottomMargin
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
                Log.d(TAG, "drawHorizontalDivider: " + left + "," + top + "," + right + "," + bottom);
            }
        }
    }

    //(0,0,0,0)
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
       // super.getItemOffsets(outRect, view, parent, state);
        if(mOrientation== LinearLayoutManager.VERTICAL){
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }
    }
}
