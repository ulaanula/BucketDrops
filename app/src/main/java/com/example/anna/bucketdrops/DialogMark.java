package com.example.anna.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.anna.bucketdrops.adapters.CompleteListener;

/**
 * Created by Anna on 01.09.2016.
 */
public class DialogMark extends DialogFragment {

    ImageButton mBtnClose;
    Button mBtnCompleted;
    private CompleteListener mListener;
    private View.OnClickListener mBtnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {

            int id = view.getId();

            switch(id){
                case R.id.btn_completed:
                    //TODO handle the action here to mark the item as completed
                    markAsComplete();
                    break;
            }
            dismiss();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }


    private void markAsComplete() {

        Bundle arguments = getArguments();

        if(mListener!=null || arguments!=null){
            int position =arguments.getInt("POSITION");
            Toast.makeText(getActivity(), "position: " + position, Toast.LENGTH_SHORT).show();
            mListener.onComplete(position);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnClose= (ImageButton) view.findViewById(R.id.btn_close);
        mBtnCompleted = (Button)view.findViewById(R.id.btn_completed);
        mBtnClose.setOnClickListener(mBtnClickListener);
        mBtnCompleted.setOnClickListener(mBtnClickListener);


    }

    public void setCompleteListener(CompleteListener mCompleteListener) {

        mListener = mCompleteListener;


    }
}
