package com.matejhacin.ispogovor.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.matejhacin.ispogovor.helpers.LoginManager;
import com.matejhacin.ispogovor.R;
import com.matejhacin.ispogovor.helpers.RestClient;
import com.matejhacin.ispogovor.objects.Image;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import rx.functions.Action1;

public class EditProfileActivity extends AppCompatActivity {

    /*
    Variables
     */

    @Bind(R.id.usernameTextView) TextView usernameTextView;
    @Bind(R.id.profileCircleImageView) CircleImageView profileCircleImageView;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    private AppCompatActivity thisActivity;

    /*
    Lifecycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Init
        ButterKnife.bind(this);
        thisActivity = this;
        imageLoading(true);

        usernameTextView.setText(LoginManager.getInstance(getApplicationContext()).getUsername());
        RestClient.getInstance().getImage(LoginManager.getInstance(getApplicationContext()).getUsername(), new RestClient.ImageListener() {
            @Override
            public void onSuccess(Image image) {
                imageLoading(false);
                profileCircleImageView.setImageBitmap(image.getBitmap());
            }

            @Override
            public void onFailure() {
                imageLoading(false);
            }
        });
    }

    /*
    Callbacks
     */

    @OnClick(R.id.changeImageButton)
    public void changeImage() {
        new BottomSheet.Builder(this).title("Pick your image source").sheet(R.menu.menu_image_actions).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.fromDevice:
                        EasyImage.openGallery(thisActivity);
                        break;
                    case R.id.fromCamera:
                        RxPermissions.getInstance(thisActivity)
                                .request(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean granted) {
                                        if (granted) {
                                            // Permission granted, open camera
                                            EasyImage.openCamera(thisActivity);
                                        } else {
                                            // Do nothing
                                        }
                                    }
                                });
                        break;
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                Toast.makeText(EditProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                uploadImage(imageFile);
            }
        });
    }

    /*
    Methods
     */

    private void uploadImage(File file) {
        imageLoading(true);

        Image image = new Image(LoginManager.getInstance(getApplicationContext()).getUsername(), file);
        profileCircleImageView.setImageBitmap(image.getBitmap());

        RestClient.getInstance().uploadImage(getApplicationContext(), image, new RestClient.RequestListener() {
            @Override
            public void onSuccess() {
                imageLoading(false);
            }

            @Override
            public void onFailure() {
                imageLoading(false);
                Toast.makeText(EditProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                profileCircleImageView.setImageResource(R.drawable.icon_profile_placeholder);
            }
        });
    }

    private void imageLoading(boolean isLoading) {
        profileCircleImageView.setAlpha(isLoading ? 0.5f : 1f);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
