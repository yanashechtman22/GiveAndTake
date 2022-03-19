package com.example.giveandtake.ui.Posts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.giveandtake.MyApplication;
import com.example.giveandtake.R;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.utils.IsraeliCities;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;
import java.util.UUID;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import com.example.giveandtake.utils.InputValidator;
import com.squareup.picasso.Picasso;

import android.widget.ArrayAdapter;
import java.util.Arrays;

public class EditPostFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 100;
    boolean contentNotEmpty = true;
    boolean locationNotEmpty = true;
    boolean imageNotEmpty = true;
    boolean phoneNoteEmpty=true;

    EditText contentEt;
    EditText phoneEt;
    CheckBox cb;
    Button saveBtn;
    Button cancelBtn;
    Bitmap imageBitmap;
    ImageView avatarImv;
    ImageButton camBtn;
    ImageButton galleryBtn;
    AutoCompleteTextView autoComplete;
    View view;
    UserInfo userInfo;
    ProgressBar progressBar;
    String postId;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        contentEt = view.findViewById(R.id.createPost_descriptionInput);
        saveBtn = view.findViewById(R.id.main_save_btn);
        cancelBtn = view.findViewById(R.id.main_cancel_btn);
        avatarImv = view.findViewById(R.id.creatPost_UpImage);
        progressBar = view.findViewById(R.id.progressBar);
        camBtn = view.findViewById(R.id.main_cam_btn);
        galleryBtn = view.findViewById(R.id.main_gallery_btn);
        phoneEt = view.findViewById(R.id.createPost_phoneInput);

        autoComplete = view.findViewById(R.id.autoComplete_actv);
        String[] cities = IsraeliCities.CITIES;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item,cities);
        autoComplete.setThreshold(2);
        autoComplete.setAdapter(adapter);
        userInfo = AuthenticationModel.instance.getUserInfo();

        saveBtn.setEnabled(true);

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        AppModel.instance.getPostById(postId, post -> {
            contentEt.setText(post.getContent());
            autoComplete.setText(post.getLocation());
            phoneEt.setText(post.getPhone());
            if (post.getImageUrl() != null && post.getImageUrl().length() > 0) {
                Picasso.get().load(post.getImageUrl()).into(avatarImv);
            }
            setHasOptionsMenu(true);
        });

        contentEt.addTextChangedListener(new InputValidator(contentEt) {
            @Override
            public void validate(TextView textView, String text) {
                contentNotEmpty = text.length() > 0;
                checkInputValidation();
            }
        });

        phoneEt.addTextChangedListener(new InputValidator(phoneEt) {
            @Override
            public void validate(TextView textView, String text) {
                phoneNoteEmpty = text.length() > 0 && text.matches("[0-9]+") ;
                checkInputValidation();
            }
        });

        autoComplete.addTextChangedListener(new InputValidator(autoComplete) {
            @Override
            public void validate(TextView textView, String text) {
                locationNotEmpty = text.length() > 0 && Arrays.asList(cities).contains(text);
                checkInputValidation();
            }
        });

        cancelBtn.setOnClickListener(v -> navigateBack());
        saveBtn.setOnClickListener(v -> save());
        camBtn.setOnClickListener(v -> {
            imageNotEmpty=true;
            openCam();
            checkInputValidation();
        });
        galleryBtn.setOnClickListener(v -> {
            imageNotEmpty=true;
            openGallery();
            checkInputValidation();
        });
        return view;
    }
    private void checkInputValidation() {
        saveBtn.setEnabled(contentNotEmpty&&locationNotEmpty&&imageNotEmpty&&phoneNoteEmpty);
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
            avatarImv.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri imageUrl = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(MyApplication.getContext()
                        .getContentResolver(), imageUrl);
                avatarImv.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        AppModel.instance.getPostById(postId, post -> {
            post.setContent(contentEt.getText().toString());
            post.setPhone(phoneEt.getText().toString());
            post.setLocation(autoComplete.getText().toString());


            if (imageBitmap == null){
                AppModel.instance.editPost(post, (boolean success)-> navigateBack());
            } else {
                String postImageId = UUID.randomUUID().toString();
                AppModel.instance.saveImage(imageBitmap, postImageId + ".jpg", url -> {
                    post.setImageUrl(url);
                    AppModel.instance.editPost(post, success -> navigateBack());
                    //navigateBack();
                });
            }

//        if (imageBitmap == null){
//            AppModel.instance.addPost(post, (boolean success)-> navigateBack());
//        } else {
//            String adImageId = UUID.randomUUID().toString();
//            AppModel.instance.saveImage(imageBitmap, adImageId + ".jpg", url -> {
//                post.setImageUrl(url);
//                navigateBack();
//                //AppModel.instance.addPost(post, (boolean success)-> navigateBack());
//            });
//        }
        });
    }

    private void navigateBack() {
        Navigation.findNavController(view).navigate(
                EditPostFragmentDirections.actionEditPostFragmentToNavHome());
    }
}