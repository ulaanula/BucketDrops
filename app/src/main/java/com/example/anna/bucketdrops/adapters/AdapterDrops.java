package com.example.anna.bucketdrops.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.anna.bucketdrops.AppBucketDrops;
import com.example.anna.bucketdrops.R;
import com.example.anna.bucketdrops.beans.Drop;
import com.example.anna.bucketdrops.extras.Util;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Anna on 26.08.2016.
 */
public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    private LayoutInflater mInflator;
    private RealmResults<Drop> mResults;
    private Realm mRealm;
    public static final String TAG ="ANNA";
    public static final int COUNT_FOOTER=1;
    public static final int COUNT_NO_ITEMS=1;
    public static final int ITEM=0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER=2;
    private AddListener mAddListener;
    private MarkListener mMarkListener;
    private ResetListener mResetListener;
    private int mFilterOption;
    private Context mContext;


    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener listener, MarkListener markListener, ResetListener resetListener){
        mContext = context;
        mInflator = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
        mAddListener =listener;
        mMarkListener = markListener;
        mResetListener = resetListener;

    }

    public void update(RealmResults<Drop> results){
        mResults = results;
        mFilterOption = AppBucketDrops.load(mContext);
        notifyDataSetChanged();
    }
    //Needed for animations
    @Override
    public long getItemId(int position) {

        if(position<mResults.size()){
            //i need unique identifier from Drop.class; in this case it is date added
            return mResults.get(position).getAdded();
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {

        if(!mResults.isEmpty()){
            return mResults.size() + COUNT_FOOTER;
        } else {
            if(mFilterOption == Filter.LEAST_TIME_LEFT
                    || mFilterOption == Filter.MOST_TIME_LEFT
                    || mFilterOption== Filter.NONE){
                return 0;
            }else{
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(!mResults.isEmpty()) {
            if(position < mResults.size()) {
                return ITEM;
            }else{
                return FOOTER;
            }
        }else{
            if(mFilterOption == Filter.COMPLETE ||
                    mFilterOption == Filter.INCOMPLETE){
               if(position==0) {
                   return NO_ITEM;
               }else{
                   return FOOTER;
               }
            }else{
                return ITEM;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        if(viewType==FOOTER){
            View view = mInflator.inflate(R.layout.footer,parent,false);
            return new FooterHolder(view, mAddListener);
        }else if(viewType==NO_ITEM){
            View view = mInflator.inflate(R.layout.no_item,parent,false);
            return new NoItemsHolder(view);
        }
        else{
            View view = mInflator.inflate(R.layout.row_drop,parent,false);
            return new DropHolder(view, mMarkListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);

        if(holder instanceof DropHolder){
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }
    }
    @Override
    public void onSwipe(int position) {
        //because of footer;
        if(position<mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).deleteFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }
        resetFilterIfEmpty();
    }
    //funktioniert nicht
    private void resetFilterIfEmpty() {

        if(mResults.isEmpty() && (mFilterOption == Filter.COMPLETE ||
                mFilterOption == Filter.INCOMPLETE)){
            mResetListener.onReset();
        }
    }

    public void markComplete(int position) {
        if(position<mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextWhat;
        TextView mTextWhen;
        MarkListener mMarkListener;
        Context mContext;
        View mItemView;

        public DropHolder(View itemView, MarkListener markListener) {
            super(itemView);
            mItemView =itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            mTextWhat = (TextView)itemView.findViewById(R.id.tv_what);
            mTextWhen = (TextView)itemView.findViewById(R.id.tv_when);
            mMarkListener = markListener;
            AppBucketDrops.setRalewayRegular(mContext,mTextWhat);
            AppBucketDrops.setRalewayRegular(mContext,mTextWhen);
        }

        @Override
        public void onClick(View view) {
            mMarkListener.onMark(getAdapterPosition());
        }

        public void setWhat(String what) {

            mTextWhat.setText(what);
        }

        public void setBackground(boolean completed) {

            Drawable drawable;
            if(completed){
                drawable =ContextCompat.getDrawable(mContext,R.color.bg_drop_row_light);
            }else{
                drawable =ContextCompat.getDrawable(mContext,R.color.bg_drop_row_dark);
            }
            Util.setBackground(itemView,drawable);
        }

        public void setWhen(long when) {
            mTextWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS,DateUtils.FORMAT_ABBREV_ALL));
        }
    }

    public static class NoItemsHolder extends RecyclerView.ViewHolder{

        public NoItemsHolder(View itemView) {

            super(itemView);
            TextView textNoItem = (TextView)itemView.findViewById(R.id.tv_no_items);
            AppBucketDrops.setRalewayRegular(itemView.getContext(),textNoItem);
        }
    }
    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button btnAdd;
        AddListener mAddListener;

        public FooterHolder(View itemView) {
            super(itemView);
            btnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            AppBucketDrops.setRalewayRegular(itemView.getContext(),btnAdd);
            btnAdd.setOnClickListener(this);
        }

        public FooterHolder(View itemView, AddListener listener) {
            super(itemView);
            btnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            AppBucketDrops.setRalewayRegular(itemView.getContext(),btnAdd);
            btnAdd.setOnClickListener(this);
            mAddListener =listener;
        }

        @Override
        public void onClick(View view) {
            mAddListener.add();
        }
    }

}
