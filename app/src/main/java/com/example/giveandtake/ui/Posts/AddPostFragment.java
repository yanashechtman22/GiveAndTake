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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.giveandtake.MyApplication;
import com.example.giveandtake.R;
import com.example.giveandtake.model.Post;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;
import java.util.UUID;

public class AddPostFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 100;
    EditText contentEt;
    CheckBox cb;
    Button saveBtn;
    Button cancelBtn;
    Bitmap imageBitmap;
    ImageView avatarImv;
    ImageButton camBtn;
    ImageButton galleryBtn;
    View view;
    UserInfo userInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_post,container, false);
        contentEt = view.findViewById(R.id.content_et);
        saveBtn = view.findViewById(R.id.main_save_btn);
        cancelBtn = view.findViewById(R.id.main_cancel_btn);
        avatarImv = view.findViewById(R.id.baseImage_iv);
        userInfo = AuthenticationModel.instance.getUserInfo();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        camBtn = view.findViewById(R.id.main_cam_btn);
        galleryBtn = view.findViewById(R.id.main_gallery_btn);

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });
        return view;
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            avatarImv.setImageBitmap(imageBitmap);
        } else if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
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
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        String content = contentEt.getText().toString();
        Long lud = new Long(0);
        String userId = userInfo.getUid();

        //TODO: this is mock! this line shouldn't be here
        Post post = new Post(content,0, "beer sheva");

        if (imageBitmap == null){
            AppModel.instance.addPost(post,()->{
                Snackbar.make(view, "without image, "+ post.getContent(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });
        } else {
            String adImageId = UUID.randomUUID().toString();
            AppModel.instance.saveImage(imageBitmap, adImageId + ".jpg", url -> {
                post.setImageUrl(url);
                AppModel.instance.addPost(post,()->{
                    Snackbar.make(view, "with image, " + post.getContent(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
            });
        }
    }
}