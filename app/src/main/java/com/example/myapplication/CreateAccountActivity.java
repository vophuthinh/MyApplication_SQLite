package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccountActivity extends AppCompatActivity {
    EditText edt_user;
    EditText edt_password;
    EditText edt_confirm_password;
    Button btn_create;
    Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        edt_user = findViewById(R.id.edt_user);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_create = findViewById(R.id.btn_create);
        btn_close = findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePassword();
            }
        });

    }
    private boolean validatePassword() {
        String passwordInput = edt_password.getText().toString().trim();
        String confirmPasswordInput = edt_confirm_password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            showToast("Field can't be empty");
            return false;
        } else if (!passwordInput.equals(confirmPasswordInput)) {
            showToast("Passwords do not match");
            return false;
        } else {
            saveToSharedPreferences();
            return true;
        }
    }

    private void saveToSharedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myfile", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user", edt_user.getText().toString());
        editor.putString("password", edt_password.getText().toString());
        editor.apply();
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}