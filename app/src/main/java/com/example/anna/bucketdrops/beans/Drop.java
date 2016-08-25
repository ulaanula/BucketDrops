package com.example.anna.bucketdrops.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Anna on 24.08.2016.
 */
public class Drop extends RealmObject {

    private String what;
    @PrimaryKey
    private long added;
    private long when;
    private boolean completed;

    public Drop(){

    }

    public Drop(String what, long added, long when, boolean completed) {
        this.what = what;
        this.added = added;
        this.when = when;
        this.completed = completed;
    }


    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


}
