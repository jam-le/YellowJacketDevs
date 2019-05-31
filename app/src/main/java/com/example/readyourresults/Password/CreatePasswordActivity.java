package com.example.readyourresults.Password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.readyourresults.MainActivity;
import com.example.readyourresults.R;

public class CreatePasswordActivity extends AppCompatActivity {

    EditText editNewPassword, editRepeatPassword;
    Button confirmButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editNewPassword = (EditText) findViewById(R.id.new_password);
        editRepeatPassword = (EditText) findViewById(R.id.repeat_password);
        confirmButton = (Button) findViewById(R.id.confirm_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editNewPassword.getText().toString();
                String repeatPassword = editRepeatPassword.getText().toString();

                if(newPassword.equals("") || repeatPassword.equals("")) {
                    //there is no password
                    Toast.makeText(CreatePasswordActivity.this,
                            "No Password Entered!",
                            Toast.LENGTH_SHORT)
                            .show();
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

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //there is no match for the passwords
                        Toast.makeText(CreatePasswordActivity.this,
                                "Passwords don't match",
                                Toast.LENGTH_SHORT)
                                .show();
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
