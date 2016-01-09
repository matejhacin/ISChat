package com.matejhacin.ispogovor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by matejhacin on 09/01/16.
 */
public class RestClient {

    /*
    Variables
     */

    private static final String BASE_URL = "http://fri-is-63140070.azurewebsites.net/Service1.svc/";

    private static final String ENDPOINT_REGISTER = "Register/%s/%s/%s/%s"; // username, firstname, lastname, password
    private static final String ENDPOINT_LOGIN = "Login/%s/%s"; // username, password
    private static final String ENDPOINT_SEND_MESSAGE = "PostMessage/%s/%s/%s"; // username, timestamp, message
    private static final String ENDPOINT_GET_MESSAGES = "Messages";

    private static RestClient ourInstance = new RestClient();

    private AsyncHttpClient asyncHttpClient;
    private Gson gson;

    /*
    Singleton
     */

    public static RestClient getInstance() {
        return ourInstance;
    }

    /*
    Constructor
     */

    private RestClient() {
        asyncHttpClient = new AsyncHttpClient();
        gson = new Gson();
    }

    /*
    Interfaces
     */

    public interface RequestListener{
        void onSuccess();
        void onFailure();
    }

    public interface MessageListener{
        void onSuccess(ArrayList<Message> messageList);
        void onFailure();
    }

    /*
    Methods
     */

    public void registerUser(final Context context, final String username, String firstname, String lastname, final String password, final RequestListener responseHandler) {
        asyncHttpClient.removeAllHeaders();
        asyncHttpClient.post(BASE_URL + String.format(ENDPOINT_REGISTER, username, firstname, lastname, password), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (responseHandler != null)
                    responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseHandler != null) {
                    if (responseString.equals("true")) {
                        LoginManager.getInstance(context).saveUser(username, password);
                        responseHandler.onSuccess();
                    } else {
                        responseHandler.onFailure();
                    }
                }

            }
        });
    }

    public void loginUser(final Context context, final String username, final String password, final RequestListener responseHandler) {
        asyncHttpClient.removeAllHeaders();
        asyncHttpClient.post(BASE_URL + String.format(ENDPOINT_LOGIN, username, password), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (responseHandler != null)
                    responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseHandler != null) {
                    if (responseString.equals("true")) {
                        LoginManager.getInstance(context).saveUser(username, password);
                        responseHandler.onSuccess();
                    } else {
                        responseHandler.onFailure();
                    }
                }
            }
        });
    }

    public void postMessage(Context context, Message message, final RequestListener responseHandler) {
        asyncHttpClient.removeAllHeaders();
        asyncHttpClient.addHeader("Authorization", LoginManager.getInstance(context).getAuthorizationHeader());

        asyncHttpClient.post(BASE_URL + String.format(ENDPOINT_SEND_MESSAGE, message.getUsername(), message.getTimestamp(), message.getText()), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (responseHandler != null)
                    responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseHandler != null) {
                    if (responseString.equals("true")) {
                        responseHandler.onSuccess();
                    } else {
                        responseHandler.onFailure();
                    }
                }
            }
        });
    }

    public void getMessages(Context context, final MessageListener responseHandler) {
        asyncHttpClient.removeAllHeaders();
        asyncHttpClient.addHeader("Authorization", LoginManager.getInstance(context).getAuthorizationHeader());

        asyncHttpClient.get(BASE_URL + ENDPOINT_GET_MESSAGES, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (responseHandler != null)
                    responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<Message> messageList = gson.fromJson(responseString, new TypeToken<List<Message>>(){}.getType());

                if (responseHandler != null)
                    responseHandler.onSuccess(messageList);
            }
        });
    }
}
