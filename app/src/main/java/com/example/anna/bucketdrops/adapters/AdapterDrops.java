package com.example.anna.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.anna.bucketdrops.R;
import com.example.anna.bucketdrops.beans.Drop;

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
    public static final int ITEM=0;
    public static final int FOOTER=1;
    public AddListener mAddListener;


    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results){

        mInflator = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
    }
    public AdapterDrops(Context context,Realm realm, RealmResults<Drop> results, AddListener listener){

        mInflator = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
        mAddListener =listener;
    }


    public void update(RealmResults<Drop> results){
        mResults = results;
        notifyDataSetChanged();
    }

//    public void setAddListener(AddListener listener){
//        mAddListener = listener;
//    }

    @Override
    public int getItemViewType(int position) {

        if(mResults ==null || position<mResults.size()){
            return ITEM;
        }else{
            return FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        if(viewType==FOOTER){
            View view = mInflator.inflate(R.layout.footer,parent,false);
            FooterHolder holder = new FooterHolder(view,mAddListener);
            return holder;
        }else{
            View view = mInflator.inflate(R.layout.row_drop,parent,false);
            DropHolder holder = new DropHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);

        if(holder instanceof DropHolder){
            Drop drop = mResults.get(position);
            DropHolder dropHolder = (DropHolder) holder;
            dropHolder.textWhat.setText(drop.getWhat());
        }
    }

    @Override
    public int getItemCount() {
        // when no data in db, return 0 , in order to display the empty view
        if(mResults==null || mResults.isEmpty()){
            return 0;
        }else{

        // +1 is because of footer
        return mResults.size()+1;}
    }



    public static class DropHolder extends RecyclerView.ViewHolder{

        TextView textWhat;

        public DropHolder(View itemView) {
            super(itemView);
            textWhat = (TextView)itemView.findViewById(R.id.tv_what);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button btnAdd;
        AddListener mListener;

        public FooterHolder(View itemView) {
            super(itemView);
            btnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            btnAdd.setOnClickListener(this);
        }

        public FooterHolder(View itemView, AddListener listener) {
            super(itemView);
            btnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            btnAdd.setOnClickListener(this);
            mListener =listener;
        }

        @Override
        public void onClick(View view) {
            mListener.add();
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
    }
}
