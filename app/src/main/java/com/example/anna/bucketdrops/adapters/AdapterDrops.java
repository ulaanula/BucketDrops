package com.example.anna.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anna.bucketdrops.R;
import com.example.anna.bucketdrops.beans.Drop;

import io.realm.RealmResults;

/**
 * Created by Anna on 26.08.2016.
 */
public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private LayoutInflater mInflator;
    private RealmResults<Drop> mResults;
    public static final String TAG ="ANNA";

    public AdapterDrops(Context context, RealmResults<Drop> results){

        mInflator = LayoutInflater.from(context);
        update(results);
    }

    public void update(RealmResults<Drop> results){
        mResults = results;
        notifyDataSetChanged();
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view = mInflator.inflate(R.layout.row_drop,parent,false);

       DropHolder holder = new DropHolder(view);

        Log.d(TAG, "onCreateViewHolder: ");

        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);

        Drop drop = mResults.get(position);
        holder.textWhat.setText(drop.getWhat());

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class DropHolder extends RecyclerView.ViewHolder{

        TextView textWhat;

        public DropHolder(View itemView) {
            super(itemView);
            textWhat = (TextView)itemView.findViewById(R.id.tv_what);
        }
    }
}
