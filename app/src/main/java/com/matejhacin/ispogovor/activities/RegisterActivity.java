package com.matejhacin.ispogovor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.matejhacin.ispogovor.R;
import com.matejhacin.ispogovor.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.usernameEditText)
    EditText usernameEditText;
    @Bind(R.id.firstnameEditText)
    EditText firstnameEditText;
    @Bind(R.id.lastnameEditText)
    EditText lastnameEditText;
    @Bind(R.id.passwordEditText)
    EditText passwordEditText;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.registerButton)
    public void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

        RestClient.getInstance().registerUser(
                getApplicationContext(),
                usernameEditText.getText().toString(),
                firstnameEditText.getText().toString(),
                lastnameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                new RestClient.RequestListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(RegisterActivity.this, ChatActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Oops, something went wrnog. Please try again.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
