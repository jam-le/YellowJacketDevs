package com.example.readyourresults.MySavedResults;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readyourresults.R;

import java.util.List;

public class MySavedResultsFragment extends Fragment {
    RecyclerView recyclerView;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //((SavedResultsAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
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

        SavedResultsAdapter adapter = new SavedResultsAdapter(initData());
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    private List<SavedResultsItem> initData() {
        SavedResultsListHelper savedResultsListHelper = SavedResultsListHelper.get(getActivity());
        List<SavedResultsItem> savedResultsItems = savedResultsListHelper.getAll();
        return  savedResultsItems;
    }
}