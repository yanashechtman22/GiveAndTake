package com.example.giveandtake.auth;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.giveandtake.MyApplication;
import com.example.giveandtake.R;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.utils.EmailValidator;
import com.example.giveandtake.utils.InputValidator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class RegisterFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 100;
    EditText displayName;
    EditText email;
    EditText password;
    Button registerBtn;
    CardView validationCard;
    ProgressBar progressBar;
    View view;
    ImageButton galleryBtn;
    ImageButton cameraBtn;
    ImageView userImageImv;
    Bitmap imageBitmap;


    private final int MIN_PASS_LENGTH = 6;
    boolean nameNotEmpty = false;
    boolean emailValid = false;
    boolean passwordValid = false;
    boolean imageNotEmpty = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        displayName = view.findViewById(R.id.et_name);
        email = view.findViewById(R.id.et_email);
        password = view.findViewById(R.id.et_password);
        registerBtn = view.findViewById(R.id.btn_register);
        validationCard = view.findViewById(R.id.card1);
        progressBar = view.findViewById(R.id.simpleProgressBar);
        galleryBtn = view.findViewById(R.id.register_gallery_button);
        cameraBtn = view.findViewById(R.id.register_camera_button);
        userImageImv = view.findViewById(R.id.register_image_imv);


        cameraBtn.setOnClickListener(v -> {
            imageNotEmpty=true;
            openCam();
            checkInputValidation();
        });
        galleryBtn.setOnClickListener(v -> {
            imageNotEmpty=true;
            openGallery();
            checkInputValidation();
        });

        displayName.addTextChangedListener(new InputValidator(displayName) {
            @Override
            public void validate(TextView textView, String text) {
                nameNotEmpty = text.length() > 0;
                checkInputValidation();
            }
        });

        email.addTextChangedListener(new InputValidator(email) {
            @Override
            public void validate(TextView textView, String text) {
                emailValid = validateEmail(text);
                checkInputValidation();
            }
        });

        password.addTextChangedListener(new InputValidator(password) {
            @SuppressLint("ResourceType")
            @Override
            public void validate(TextView textView, String text) {
                if (text.length() >= MIN_PASS_LENGTH) {
                    validationCard.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
                    passwordValid = true;
                } else {
                    validationCard.setCardBackgroundColor(Color.parseColor("#dcdcdc"));
                    passwordValid = false;
                }
                checkInputValidation();
            }
        });

        registerBtn.setOnClickListener(view1 -> handleRegisterNewUser());
        return view;
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            userImageImv.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri imageUrl = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(MyApplication.getContext()
                        .getContentResolver(), imageUrl);
                userImageImv.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean validateEmail(String emailAddress) {
        return EmailValidator.validateEmail(emailAddress);
    }

    private void checkInputValidation() {
        registerBtn.setEnabled(passwordValid && nameNotEmpty && emailValid);
    }

    private void handleRegisterNewUser() {
        progressBar.setVisibility(View.VISIBLE);
        String displayNameText = displayName.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        RegisterAuthListener registerListener = new RegisterAuthListener();
        AppModel.instance.saveImage(imageBitmap,emailText + ".jpg",imageUriString -> {
            Uri imageUri = Uri.parse(imageUriString);
            AuthenticationModel.instance.registerNewUser(displayNameText, emailText, passwordText, imageUri, registerListener);
        });
    }

    public class RegisterAuthListener implements AuthenticationModel.AuthListener {

        @Override
        public void onComplete(FirebaseUser user) {
            String successMessage = String.format("User %s added successfully", user.getDisplayName());
            Snackbar.make(view, successMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Navigation.findNavController(view).navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            );
        }

        @Override
        public void onFailure(String message) {
            String failedMessage = "Failed to add user" + message;
            Snackbar.make(view, failedMessage, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}