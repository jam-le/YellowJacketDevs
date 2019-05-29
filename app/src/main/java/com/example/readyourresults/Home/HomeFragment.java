package com.example.readyourresults.Home;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.readyourresults.Help.HelpFragment;
import com.example.readyourresults.MainActivity;
import com.example.readyourresults.R;

public class HomeFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    Fragment fragment;
    FragmentManager f = getFragmentManager();
//    if(f == null) {
//
//    }
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
            }
        });
        Button help = getView().findViewById(R.id.help_button);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"1");
                fragment = new HelpFragment();
                Log.d(TAG,"2");
                FragmentTransaction transaction = f.beginTransaction();
                Log.d(TAG,"3");
                transaction.replace(R.id.content_main, fragment)
                        .addToBackStack(null)
                        .commit();
                Log.d(TAG,"4");
//                Snackbar.make(view, "Help", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//                Intent intent = new Intent(getActivity(), com.example.readyourresults.Help.HelpActivity.class);
//                startActivity(intent);
            }
        });
        Log.d(TAG,"5");
    }
}
