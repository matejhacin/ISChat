package com.matejhacin.ispogovor.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.matejhacin.ispogovor.LoginManager;
import com.matejhacin.ispogovor.Message;
import com.matejhacin.ispogovor.MessageRecyclerAdapter;
import com.matejhacin.ispogovor.R;
import com.matejhacin.ispogovor.RestClient;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.editText)
    EditText editText;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        initRecyclerView();
    }

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
                        Toast.makeText(getApplicationContext(), "Oops, something went wrnog. Please try again.", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }
                }
        );
    }

    private void initRecyclerView() {
        showLoading();

        RestClient.getInstance().getMessages(
                getApplicationContext(),
                new RestClient.MessageListener() {
                    @Override
                    public void onSuccess(ArrayList<Message> messageList) {
                        if (messageList != null) {
                            MessageRecyclerAdapter adapter = new MessageRecyclerAdapter(getApplicationContext(), messageList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);
                        }

                        editText.setText("");
                        hideLoading();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Oops, something went wrnog. Please try again.", Toast.LENGTH_SHORT).show();
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
