package com.matejhacin.ispogovor.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by matejhacin on 09/01/16.
 */
public class Message {

    /*
    Variables
     */

    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("Time")
    @Expose
    private String timestamp;
    @SerializedName("Username")
    @Expose
    private String username;

    /*
    Constructor
     */

    public Message(String text, String username) {
        this.text = removeSpaces(text);
        this.username = username;
        this.timestamp = createTimestamp();
    }

    /*
    Getters
     */

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    /*
    Methods
     */

    private String createTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd-hh-mm").format(new Date());
    }

    private String removeSpaces(String string) {
        return string.replace(" ", "123space123");
    }
}
