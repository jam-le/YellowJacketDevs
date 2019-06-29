package com.example.readyourresults.Password;

import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.readyourresults.MainActivity;
import com.example.readyourresults.R;

public class CreatePasswordActivity extends AppCompatActivity {

    TextInputLayout editNewPassword, editRepeatPassword;
    Button confirmButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editNewPassword = (TextInputLayout) findViewById(R.id.new_password);
        editRepeatPassword = (TextInputLayout) findViewById(R.id.repeat_password);
        confirmButton = (Button) findViewById(R.id.confirm_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editNewPassword.getEditText().getText().toString().trim();

                String repeatPassword = editRepeatPassword.getEditText().getText().toString().trim();

                if (newPassword.equals("")) {
                    //there is no password
                    editNewPassword.setError("Field Can't be empty");
                }

                if (repeatPassword.equals("")) {
                    editRepeatPassword.setError("Field Can't be empty");
                } else {
                    //save password
                    if(newPassword.equals(repeatPassword)) {
                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password", newPassword);
                        editor.apply();

                        Toast.makeText(CreatePasswordActivity.this,
                                "Password Saved Successfully",
                                Toast.LENGTH_SHORT)
                                .show();

                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intent);
                        finish();

                    } else {
                        //there is no match for the passwords
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
}
