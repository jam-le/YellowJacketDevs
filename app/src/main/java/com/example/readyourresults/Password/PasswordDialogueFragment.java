package com.example.readyourresults.Password;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.readyourresults.R;

public class PasswordDialogueFragment extends AppCompatDialogFragment {

    private TextInputLayout editPassword;
    private PasswordDialogueListener listener;

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

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmInput(view);
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

    public void confirmInput(View view) {
        if (!validatePassword()) {
            return;
        }

        String input = "Password: " + editPassword.getEditText().getText().toString();

        Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
    }
}

