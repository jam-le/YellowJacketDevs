package com.example.readyourresults.MySavedResults;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.readyourresults.MainActivity;
import com.example.readyourresults.R;

import java.util.ArrayList;
import java.util.List;

public class MySavedResultsFragment extends Fragment {
    RecyclerView recyclerView;
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ((SavedResultsAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mysavedresults, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SavedResultsAdapter adapter = new SavedResultsAdapter(getActivity(),initData());
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData() {
        SavedResultsListParentHelper savedResultsListParentHelper = SavedResultsListParentHelper.get(getActivity());
        List<SavedResultsListParent> titles = savedResultsListParentHelper.getAll();
        List<ParentObject> parentObjects = new ArrayList<>();
        for(SavedResultsListParent title: titles) {
            List<Object> childList = new ArrayList<>();
            childList.add(new SavedResultsListChild("Test Type:",
                    "Test Outcome:",
                    "Test Date:"));
            title.setChildObjectList(childList);
            parentObjects.add(title);
        }
        return parentObjects;
    }
}