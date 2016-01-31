package com.matejhacin.ispogovor.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by matejhacin on 30/01/16.
 */
public class Image {

    /*
    Variables
     */

    @SerializedName("Username")
    @Expose
    private String userName;
    @SerializedName("ImageString")
    @Expose
    private String base64;

    private File imageFile;

    /*
    Constructor
     */

    public Image(String userName, File imageFile) {
        this.userName = userName;
        this.imageFile = imageFile;

        createBase64(getBitmap());
    }

    public Image(String userName, String base64) {
        this.userName = userName;
        this.base64 = fixBase64(base64);
    }

    /*
    Private methods
     */

    private void createBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        this.base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Android encodes our base64 before sending it to server
     * (replacing new lines with \u000a, putting \ before /)
     * We need to get rid of these characters before base64 is usable again
     * @param base64 base64 string
     * @return fixed base64 string
     */
    private String fixBase64(String base64) {
        base64 = base64.replace("\\u000a", "");
        base64 = base64.replace("\\", "");
        base64 = base64.replace("\"", "");
        return base64;
    }

    /*
    Public methods
     */

    public Bitmap getBitmap() {
        try {
            Bitmap bitmap = null;

            if (base64 != null) {
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            } else if (imageFile != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);
                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            }

            return bitmap;
        } catch (IllegalArgumentException e) {
            Log.e(this.getClass().getSimpleName(), "Bad Base64: " + this.base64);
        }

        return null;
    }
}
