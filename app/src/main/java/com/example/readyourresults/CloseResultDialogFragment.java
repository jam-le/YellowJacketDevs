package com.example.readyourresults;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.example.readyourresults.Home.HomeFragment;

public class CloseResultDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Close Result Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_lose_result)
                .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Stay on the results screen - don't do anything
                    }
                })
                .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Fragment homeFragment = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content_main, homeFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
