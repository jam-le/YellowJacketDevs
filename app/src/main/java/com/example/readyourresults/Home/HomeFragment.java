package com.example.readyourresults.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.readyourresults.Camera.CamFragment;
import com.example.readyourresults.Help.HelpFragment;
import com.example.readyourresults.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button fab = getView().findViewById(R.id.button_read_my_results);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Fragment helpFragment = new CamFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, helpFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        Button help = getView().findViewById(R.id.help_button);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Help", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Fragment helpFragment = new HelpFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, helpFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
