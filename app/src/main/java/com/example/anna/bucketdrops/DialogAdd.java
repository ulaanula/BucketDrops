package com.example.anna.bucketdrops;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.anna.bucketdrops.beans.Drop;

import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Anna on 23.08.2016.
 */
public class DialogAdd extends DialogFragment {

    private Button mBtnAddIt;
    private DatePicker mInputWhen;
    private EditText mInputWhat;
    private ImageButton mBtnClose;

    public DialogAdd(){
    }

    private View.OnClickListener mBtnClickListener =  new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            int id = view.getId();

            switch(id){
                case R.id.btn_add_it:
                    addAction();
                    break;
            }
            dismiss();
        }
    };

    //TODO Process date
    private void addAction() {

        String what = mInputWhat.getText().toString();
        String date = mInputWhen.getDayOfMonth()+"/"+mInputWhen.getMonth()+"/"+mInputWhen.getYear();
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, mInputWhen.getDayOfMonth());
        calendar.set(Calendar.MONTH, mInputWhen.getMonth());
        calendar.set(Calendar.YEAR, mInputWhen.getYear());
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Drop drop = new Drop(what, now,calendar.getTimeInMillis() ,false);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add , container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnAddIt = (Button)view.findViewById(R.id.btn_add_it);
        mInputWhen = (DatePicker)view.findViewById(R.id.bpv_date);
        mInputWhat = (EditText)view.findViewById(R.id.et_drop);
        mBtnClose = (ImageButton)view.findViewById(R.id.btn_close);

        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnAddIt.setOnClickListener(mBtnClickListener);
    }
}
