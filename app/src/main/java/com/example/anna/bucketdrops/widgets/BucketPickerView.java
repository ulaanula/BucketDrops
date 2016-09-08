package com.example.anna.bucketdrops.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.bucketdrops.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Anna on 05.09.2016.
 */
public class BucketPickerView extends LinearLayout implements View.OnTouchListener {

    private TextView mTextDay;
    private TextView mTextMonth;
    private TextView mTextYear;
    private Calendar mCalendar;
    private SimpleDateFormat mFormatter;
    public static final String TAG="ANNA";
    public static final int LEFT=0;
    public static final int TOP=1;
    public static final int RIGHT=2;
    public static final int BOTTOM=3;
    private boolean mIncrement;
    private boolean mDecrement;
    private int MESSAGE_WHAT=123;
    private int mActiveTextViewID;
    public static final int DELAY=250;

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {

            if(mIncrement){
                increment(mActiveTextViewID);
            }
            if(mDecrement){
                decrement(mActiveTextViewID);
            }

            if(mIncrement || mDecrement){
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
            }

            return true;
        }
    });

    //for creation bucketPickerView only in code
    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }
    //for initializing buckerPicker from XML
    public BucketPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    //for initializing buckerPicker from XML + style for widget
    public BucketPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view,this);
        mCalendar= Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("super", super.onSaveInstanceState());
            bundle.putInt("day",mCalendar.get(Calendar.DATE));
            bundle.putInt("month",mCalendar.get(Calendar.MONTH));
            bundle.putInt("year", mCalendar.get(Calendar.YEAR));

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Parcelable){
            Bundle bundle = (Bundle)state;
            state = bundle.getParcelable("super");
            int day = bundle.getInt("day");
            int month= bundle.getInt("month");
            int year= bundle.getInt("year");

            update(day,month,year,0,0,0);
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextDay = (TextView) this.findViewById(R.id.tv_day);
        mTextMonth = (TextView) this.findViewById(R.id.tv_month);
        mTextYear = (TextView) this.findViewById(R.id.tv_year);
        mTextDay.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);

        int day = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        update(day,month,year,0,0,0);
    }

    private void update(int day, int month, int year, int hour, int minute, int second){
        mCalendar.set(Calendar.DATE, day);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR,hour);
        mCalendar.set(Calendar.MINUTE,minute);
        mCalendar.set(Calendar.SECOND,second);
        mTextDay.setText(day + "");
        mTextYear.setText(year + "");
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
    }

    public long getTime(){
        return mCalendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch(view.getId()){

            case R.id.tv_day:
                processEventsFor(mTextDay,event);
                break;
            case R.id.tv_month:
                processEventsFor(mTextMonth,event);
                break;
            case R.id.tv_year:
                processEventsFor(mTextYear,event);
                break;
        }
//        switch(event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG,"Down: x: "+ event.getX()+ " y: "+ event.getY()+ " raw x: " + event.getRawX()+ " ray y: "+event.getRawY());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG,"Move: x: "+ event.getX()+ " y: "+ event.getY()+ " raw x: " + event.getRawX()+ " ray y: "+event.getRawY());
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.d(TAG,"Up: x: "+ event.getX()+ " y: "+ event.getY()+ " raw x: " + event.getRawX()+ " ray y: "+event.getRawY());
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d(TAG,"Cancel: x: "+ event.getX()+ " y: "+ event.getY()+ " raw x: " + event.getRawX()+ " ray y: "+event.getRawY());
//                break;
//        }

        return true;
    }

    private void processEventsFor(TextView textView, MotionEvent event){

        Drawable[] drawables = textView.getCompoundDrawables();

        if(hasDrawableTop(drawables)&& hasDrawableBottom(drawables)){
            Rect topBounds = drawables[TOP].getBounds();
            Rect bottomBounds = drawables[BOTTOM].getBounds();

            mActiveTextViewID = textView.getId();
            float x = event.getX();
            float y = event.getY();

            //incrementing value; if/else make it possible to determine that user can hit only one buttom at a time
            if(topDrawableHit(textView,topBounds.height(),x,y)){
                Toast.makeText(getContext(), "topHit for: " + textView.getId(), Toast.LENGTH_SHORT).show();

                if(isActionDown(event)){
                    mIncrement=true;
                    increment(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
                    toggleDrawable(textView,true);
                }
                if(isActionUpOrCancel(event)){
                    mIncrement=false;
                    toggleDrawable(textView,false);
                }

            // decrementing value
            }else if(bottomDrawableHit(textView,bottomBounds.height(),x,y)){
                Toast.makeText(getContext(), "bottomHit for: " + textView.getId(), Toast.LENGTH_SHORT).show();

                if(isActionDown(event)){
                    mDecrement=true;
                    decrement(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT,DELAY);
                    toggleDrawable(textView,true);

                }
                if(isActionUpOrCancel(event)){
                    mDecrement=false;
                    toggleDrawable(textView,false);
                }
            }else{
                mIncrement= false;
                mDecrement=false;
                toggleDrawable(textView,false);
            }
        }
    }

    private void increment(int id){

        switch((id)){
            case R.id.tv_day:
                mCalendar.add(Calendar.DATE,1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH,1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR,1);
                break;
        }
        set(mCalendar);
    }

    private void set(Calendar calendar) {

        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);

        mTextDay.setText(day+"");
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
        mTextYear.setText(year+"");

    }

    private void decrement(int id){

        switch((id)){
            case R.id.tv_day:
                mCalendar.add(Calendar.DATE,-1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH,-1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR,-1);
                break;
        }
        set(mCalendar);
    }

    private boolean isActionDown(MotionEvent event){
        return event.getAction()==MotionEvent.ACTION_DOWN;
    }

    private boolean isActionUpOrCancel(MotionEvent event){
        return event.getAction()== MotionEvent.ACTION_UP ||
               event.getAction() == MotionEvent.ACTION_CANCEL;
    }

    private boolean topDrawableHit(TextView textView, int drawableHeight, float x, float y){

        int xmin = textView.getPaddingLeft() ;
        int xmax = textView.getWidth()-textView.getPaddingRight();
        int ymin = textView.getPaddingTop();
        int ymax = textView.getPaddingTop() + drawableHeight;

        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean bottomDrawableHit(TextView textView, int drawableHeight,float x, float y){

        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth()- textView.getPaddingRight();
        int ymin = textView.getHeight()-textView.getPaddingBottom()-drawableHeight;
        int ymax = textView.getHeight()-textView.getPaddingBottom();

        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean hasDrawableTop(Drawable[]drawables){

        return drawables[TOP]!=null;
    }
    private boolean hasDrawableBottom(Drawable[]drawables){

        return drawables[BOTTOM]!=null;
    }

    private void toggleDrawable(TextView textView, boolean pressed){

        if(pressed){
            if(mIncrement){

                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_pressed,0,R.drawable.down_normal);
            }
            if(mDecrement){
                textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_pressed);
            }

        }else{
            textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.up_normal,0,R.drawable.down_normal);
        }
    }


}
