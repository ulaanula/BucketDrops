package com.example.anna.bucketdrops.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anna.bucketdrops.R;

import java.util.ArrayList;

/**
 * Created by Anna on 26.08.2016.
 */
public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private LayoutInflater mInflator;
    private ArrayList<String> mItems = new ArrayList();

    public AdapterDrops(Context context){

        mInflator = LayoutInflater.from(context);
        mItems = generateValues();
    }

    public static ArrayList<String> generateValues(){
        ArrayList<String> dummyValues = new ArrayList<>();

        for(int i=0; i<101;i++){
            dummyValues.add(i,"Item: " + i);
        }

        return dummyValues;
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view = mInflator.inflate(R.layout.row_drop,parent,false);

       DropHolder holder = new DropHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {

        holder.textWhat.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class DropHolder extends RecyclerView.ViewHolder{

        TextView textWhat;

        public DropHolder(View itemView) {
            super(itemView);
            textWhat = (TextView)itemView.findViewById(R.id.tv_what);
        }
    }
}
