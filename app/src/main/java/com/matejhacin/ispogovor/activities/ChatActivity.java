package com.matejhacin.ispogovor.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.matejhacin.ispogovor.helpers.LoginManager;
import com.matejhacin.ispogovor.objects.Message;
import com.matejhacin.ispogovor.MessageRecyclerAdapter;
import com.matejhacin.ispogovor.R;
import com.matejhacin.ispogovor.helpers.RestClient;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class ChatActivity extends AppCompatActivity {

    /*
    Variables
     */

    private final int QR_REQUEST_CODE = 0;

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.editText) EditText editText;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    /*
    Lifecycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        initRecyclerView();
    }

    /*
    Callbacks
     */

    @OnClick(R.id.sendImageView)
    public void sendMessage() {
        showLoading();

        RestClient.getInstance().postMessage(
                getApplicationContext(),
                new Message(editText.getText().toString(), LoginManager.getInstance(getApplicationContext()).getUsername()),
                new RestClient.RequestListener() {
                    @Override
                    public void onSuccess() {
                        initRecyclerView();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Oops, something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }
                }
        );
    }

    @OnClick(R.id.qrImageView)
    public void scanQr() {
        RxPermissions.getInstance(this)
                .request(
                        Manifest.permission.CAMERA)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean granted) {
                                        if (granted) {
                                            // Permission granted, start activity
                                            startActivityForResult(new Intent(ChatActivity.this, QRScannerActivity.class), QR_REQUEST_CODE);
                                        } else {
                                            // Do nothing
                                        }
                                    }
                                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_REQUEST_CODE && resultCode == QRScannerActivity.QR_RESULT_CODE) {
            String scannedText = data.getExtras().getString(QRScannerActivity.QR_RESULT);
            editText.setText(scannedText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editProfileItem:
                startActivity(new Intent(ChatActivity.this, EditProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    Private methods
     */

    private void initRecyclerView() {
        showLoading();

        RestClient.getInstance().getMessages(
                getApplicationContext(),
                new RestClient.MessageListener() {
                    @Override
                    public void onSuccess(ArrayList<Message> messageList) {
                        if (messageList != null) {
                            MessageRecyclerAdapter adapter = new MessageRecyclerAdapter(getApplicationContext(), messageList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            recyclerView.scrollToPosition(messageList.size()-1);
                        }

                        editText.setText("");
                        hideLoading();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Oops, something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }
                }
        );
    }

    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
