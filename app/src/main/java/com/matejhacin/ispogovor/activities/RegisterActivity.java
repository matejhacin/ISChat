package com.matejhacin.ispogovor.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.matejhacin.ispogovor.R;
import com.matejhacin.ispogovor.helpers.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    /*
    Variables
     */

    @Bind(R.id.usernameEditText) EditText usernameEditText;
    @Bind(R.id.firstnameEditText) EditText firstnameEditText;
    @Bind(R.id.lastnameEditText) EditText lastnameEditText;
    @Bind(R.id.passwordEditText) EditText passwordEditText;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    /*
    Lifecycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    /*
    Callbacks
     */

    @OnClick(R.id.registerButton)
    public void registerNewUser() {
        closeKeyboard();
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

    /*
    Methods
     */

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
