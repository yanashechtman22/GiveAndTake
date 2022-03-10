package com.example.giveandtake.ui.Posts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.model.Post;
import com.google.firebase.auth.UserInfo;
//import com.squareup.picasso.Picasso;


public class PostDetailsFragment extends Fragment {
    TextView contentTv;
    Post post;
    Button editBtn;
    Button deleteBtn;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        UserInfo userInfo = AuthenticationModel.instance.getUserInfo();

        String postId = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostId();
        editBtn = view.findViewById(R.id.details_edit_btn);
        deleteBtn = view.findViewById(R.id.details_delete_btn);

        editBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setVisibility(View.INVISIBLE);

        contentTv = view.findViewById(R.id.details_content_tv);
        imageView = view.findViewById(R.id.details_image);

        contentTv.setText(postId);

         /* AppModel.instance.getNoteById(postId, new AppModel.GetNoteById() {
            @Override
            public void onComplete(Note note) {
                contentTv.setText(note.getContent());
                editBtn.setVisibility(note.getUserId().equals(userInfo.getUid())?
                        View.VISIBLE : View.INVISIBLE);
                deleteBtn.setVisibility(note.getUserId().equals(userInfo.getUid())?
                        View.VISIBLE : View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                if (note.getImageUrl() != null && note.getImageUrl().length() > 0) {
                    Picasso.get().load(note.getImageUrl()).into(imageView);
                }
            }
        });*/


        editBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(PostDetailsFragmentDirections
                    .actionNoteDetailsFragmentToHome());
        });
        return view;
    }
}