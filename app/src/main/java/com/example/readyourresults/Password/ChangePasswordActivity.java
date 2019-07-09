package com.example.readyourresults.Password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.readyourresults.MainActivity;
import com.example.readyourresults.R;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputLayout editNewPassword, editRepeatPassword, editCurrentPassword;
    Button confirmButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editCurrentPassword = (TextInputLayout) findViewById(R.id.current_pass_cp);
        editNewPassword = (TextInputLayout) findViewById(R.id.new_password_cp);
        editRepeatPassword = (TextInputLayout) findViewById(R.id.repeat_password_cp);
        confirmButton = (Button) findViewById(R.id.confirm_button_cp);
        cancelButton = (Button) findViewById(R.id.cancel_button_cp);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = editCurrentPassword.getEditText().getText().toString().trim();
                String newPassword = editNewPassword.getEditText().getText().toString().trim();
                String repeatPassword = editRepeatPassword.getEditText().getText().toString().trim();

                if (currentPassword.equals("")) {
                    editCurrentPassword.setError("Field Can't be empty");
                }

                if (!getCurrentPassword().equals(currentPassword)) {
                    //Current Password is incorrect
                    editCurrentPassword.setError("Password Incorrect");
                }

                if (newPassword.equals("")) {
                    //there is no password
                    editNewPassword.setError("Field Can't be empty");
                }

                if (repeatPassword.equals("")) {
                    editRepeatPassword.setError("Field Can't be empty");
                } else {
                    //save password
                    if(newPassword.equals(repeatPassword)
                            && getCurrentPassword().equals(currentPassword)) {

                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password", newPassword);
                        editor.apply();

                        Toast.makeText(ChangePasswordActivity.this,
                                "Password Saved Successfully",
                                Toast.LENGTH_SHORT)
                                .show();
                        //PasswordDialogueFragment passwordDialogueFragment = new PasswordDialogueFragment();
                        //passwordDialogueFragment.show(getSupportFragmentManager(), "password Dialogue");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        //finish();
                    }

                    //there is no match for the passwords
                    if (!newPassword.equals(repeatPassword)) {
                        editRepeatPassword.setError("Passwords Don't Match");
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public String getCurrentPassword() {
        SharedPreferences settings = getSharedPreferences(
                "PREFS", 0);
        String currentPassword = settings.getString("password", null);
        return currentPassword;
    }
}
