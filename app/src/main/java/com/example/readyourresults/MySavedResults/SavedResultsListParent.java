package com.example.readyourresults.MySavedResults;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

public class SavedResultsListParent implements ParentObject {

    private List<Object> childrenList;
    private UUID id_;
    private String title;

    public SavedResultsListParent(String title) {
        this.title = title;
        id_ = UUID.randomUUID();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId_() {
        return id_;
    }

    public void setId_(UUID id_) {
        this.id_ = id_;
    }

    @Override
    public List<Object> getChildObjectList() {
        return childrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        childrenList = list;
    }
}