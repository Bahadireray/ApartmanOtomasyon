package com.bahadireray.mvc30.model;

public class MyModel {

    private String _id;
    private String title;

    public MyModel(String _id, String title){
        this._id=_id;
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }
}
