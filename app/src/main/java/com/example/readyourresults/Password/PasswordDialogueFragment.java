package com.example.readyourresults.Password;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.readyourresults.CloseResultDialogFragment;
import com.example.readyourresults.MainActivity;
import com.example.readyourresults.MySavedResults.MySavedResultsFragment;
import com.example.readyourresults.R;
import com.example.readyourresults.SaveResultDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

public class PasswordDialogueFragment extends AppCompatDialogFragment {

    private TextInputLayout editPassword;
    private PasswordDialogueListener listener;
    private boolean validationSuccessful = false;
    String purpose = "";

    public PasswordDialogueFragment() {
        super();
    }

    public PasswordDialogueFragment(String msg) {
        super();
        this.purpose = msg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("ok", null);
        builder.setNegativeButton("cancel", null);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.password_fragment, null);

        builder.setView(view);
        editPassword = view.findViewById(R.id.password);

        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button okButton = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String passwordInput = editPassword.getEditText().getText().toString().trim();

                        SharedPreferences settings = getContext().getSharedPreferences(
                                "PREFS", 0);
                        String savedPassword = settings.getString("password", null);

                        if(passwordInput.equals("")) {
                            editPassword.setError("Field Can't be empty");
                        } else if(!savedPassword.equals(passwordInput)){
                            editPassword.setError("Password Incorrect");
                        } else {
                            if (purpose.equals("Navigation Bar")) {
                                Fragment fragment = new MySavedResultsFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.content_main, fragment)
                                        .addToBackStack(null)
                                        .commit();
                                mAlertDialog.dismiss();
                                Toast toast = Toast.makeText(getContext(), purpose, Toast.LENGTH_SHORT);
                                toast.show();
                            } else {

                                //SaveResultDialogFragment fragment = new SaveResultDialogFragment();
                                //fragment.show(getActivity().getSupportFragmentManager(), "Saved Dialogue");

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                Toast toast = Toast.makeText(getContext(), purpose, Toast.LENGTH_SHORT);
                                toast.show();
                                startActivity(intent);
                                SaveResultDialogFragment fragment = new SaveResultDialogFragment();
                                fragment.show(getActivity().getSupportFragmentManager(), "Saved Dialogue");
                                getActivity().overridePendingTransition(0, 0);
                                mAlertDialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
        return mAlertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (PasswordDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement NewPasswordDialougue Listener");
        }

    }

    public interface PasswordDialogueListener {
        void applyText(String password);
    }

    private boolean validatePassword() {
        String passwordInput = editPassword.getEditText().getText().toString().trim();

        if(passwordInput.equals("1")) {
            editPassword.setError("Field Can't be empty");
            return false;
        } else {
            editPassword.setError(null);
            return true;
        }
    }

    public boolean getValidationSuccessful() {
        return validationSuccessful;
    }

    public void confirmInput(View view) {
        if (!validatePassword()) {
            return;
        }

        String input = "Password: " + editPassword.getEditText().getText().toString();

        Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
    }
}

