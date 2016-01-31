package com.matejhacin.ispogovor.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.matejhacin.ispogovor.objects.Image;
import com.matejhacin.ispogovor.objects.Message;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

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
    private static final String ENDPOINT_UPLOAD_IMAGE = "UploadImage";
    private static final String ENDPOINT_GET_IMAGE = "GetImage/%s"; // username

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

    public interface ImageListener{
        void onSuccess(Image image);
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
                ArrayList<Message> messageList = gson.fromJson(responseString, new TypeToken<List<Message>>() {
                }.getType());

                if (responseHandler != null)
                    responseHandler.onSuccess(messageList);
            }
        });
    }

    public void uploadImage(Context context, Image image, final RequestListener responseHandler) {
        asyncHttpClient.removeAllHeaders();
        asyncHttpClient.addHeader("Authorization", LoginManager.getInstance(context).getAuthorizationHeader());
        asyncHttpClient.addHeader("Content-Type", "application/json");

        StringEntity stringEntity = new StringEntity(gson.toJson(image), ContentType.APPLICATION_JSON);

        asyncHttpClient.post(context, BASE_URL + ENDPOINT_UPLOAD_IMAGE, stringEntity, ContentType.APPLICATION_JSON.toString(), new TextHttpResponseHandler() {
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

    public void getImage(final String userName, final ImageListener responseHandler) {
        asyncHttpClient.removeAllHeaders();

        asyncHttpClient.get(BASE_URL + String.format(ENDPOINT_GET_IMAGE, userName), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (responseHandler != null)
                    responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseHandler != null) {
                    if (responseString == null || ("").equals(responseString))
                        responseHandler.onFailure();
                    else
                        responseHandler.onSuccess(new Image(userName, responseString));
                }
            }
        });
    }
}
