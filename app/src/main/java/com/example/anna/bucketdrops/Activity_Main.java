package com.example.anna.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.anna.bucketdrops.adapters.AdapterDrops;
import com.example.anna.bucketdrops.adapters.AddListener;
import com.example.anna.bucketdrops.adapters.CompleteListener;
import com.example.anna.bucketdrops.adapters.Divider;
import com.example.anna.bucketdrops.adapters.Filter;
import com.example.anna.bucketdrops.adapters.MarkListener;
import com.example.anna.bucketdrops.adapters.SimpleTouchCallback;
import com.example.anna.bucketdrops.beans.Drop;
import com.example.anna.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class Activity_Main extends AppCompatActivity {

    Toolbar mToolbar;
    Button mBtnAdd;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    AdapterDrops mAdapter;
    View mEmptyView;

    private RealmChangeListener mChangedListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            mAdapter.update(mResults);
            Log.d(AdapterDrops.TAG, "onChange: was called!");
        }
    };
    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    private View.OnClickListener mBtnAddListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
           showDialogAdd();
        }
    };

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };

    private void showDialogMark(int position){
        DialogMark dialog = new DialogMark();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION",position);
        dialog.setArguments(bundle);
        dialog.setCompleteListener(mCompleteListener);
        dialog.show(getSupportFragmentManager(),"Mark");
    }

    private CompleteListener mCompleteListener= new CompleteListener() {
        @Override
        public void onComplete(int position) {

         mAdapter.markComplete(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);
        mRealm = mRealm.getDefaultInstance();

        int filterOption = AppBucketDrops.load(this);
        loadResults(filterOption);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mBtnAdd = (Button)findViewById(R.id.btn_add);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mEmptyView = findViewById(R.id.empty_drops);

        mRecycler = (BucketRecyclerView)findViewById(R.id.rv_drops);
        mRecycler.setLayoutManager(manager);
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mAdapter = new AdapterDrops(this,mRealm, mResults, mAddListener, mMarkListener);
        mAdapter.setHasStableIds(true);
        // here you can set any custom animator
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mRecycler.setAdapter(mAdapter);
        mBtnAdd.setOnClickListener(mBtnAddListener);
        setSupportActionBar(mToolbar);
        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycler);
        initBackgroundImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main,menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        boolean handled = true;
        int filterOption = Filter.NONE;

        switch(id){
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.action_none:
                AppBucketDrops.save(this,Filter.NONE);
                filterOption = Filter.NONE;
                break;
            case R.id.action_show_complete:
                AppBucketDrops.save(this,Filter.COMPLETE);
                filterOption = Filter.COMPLETE;
                break;
            case R.id.action_show_incomplete:
                AppBucketDrops.save(this,Filter.INCOMPLETE);
                filterOption = Filter.INCOMPLETE;
                break;
            case R.id.action_sort_ascending_date:
                AppBucketDrops.save(this,Filter.LEAST_TIME_LEFT);
                filterOption = Filter.LEAST_TIME_LEFT;
                break;
            case R.id.action_sort_descending_date:
                AppBucketDrops.save(this,Filter.MOST_TIME_LEFT);
                filterOption = Filter.MOST_TIME_LEFT;
                break;
            default:
                handled=false;
                break;
        }
        loadResults(filterOption);
        return handled;
    }

    private void loadResults(int filterOption){

        switch(filterOption){
            case Filter.NONE:
                mResults =  mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.COMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when");
                break;
            case Filter.MOST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when",Sort.DESCENDING);
                break;

        }
        mResults.addChangeListener(mChangedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangedListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangedListener);
    }

    private void initBackgroundImage() {
        ImageView background= (ImageView)findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);

    }
}
