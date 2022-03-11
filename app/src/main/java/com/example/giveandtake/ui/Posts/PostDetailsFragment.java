package com.example.giveandtake.ui.Posts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.model.Post;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;


public class PostDetailsFragment extends Fragment {
    TextView contentTv;
    TextView phoneNumberTv;
    TextView locationTv;
    Post post;
    Button editBtn;
    Button deleteBtn;
    ImageView imageView;
    boolean isUserPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        UserInfo userInfo = AuthenticationModel.instance.getUserInfo();
        String postId = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostId();


        contentTv = view.findViewById(R.id.idTVContent);
        phoneNumberTv = view.findViewById(R.id.idTVPhone);
        locationTv = view.findViewById(R.id.idTVLocation);
        imageView = view.findViewById(R.id.idIVPostImage);


         AppModel.instance.getPostById(postId, post -> {
             isUserPost = post.getUserId().equals(userInfo.getUid());
             contentTv.setText(post.getContent());
             locationTv.setText(post.getLocation());
             //progressBar.setVisibility(View.INVISIBLE);
             if (post.getImageUrl() != null && post.getImageUrl().length() > 0) {
                 Picasso.get().load(post.getImageUrl()).into(imageView);
             }
             setHasOptionsMenu(true);
         });


        /*editBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(PostDetailsFragmentDirections
                    .actionNoteDetailsFragmentToHome());
        });
        deleteBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(PostDetailsFragmentDirections
                    .actionNoteDetailsFragmentToHome());
        });*/
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.details_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem editButton = menu.findItem(R.id.app_bar_edit);
        MenuItem deleteButton = menu.findItem(R.id.app_bar_delete);
        editButton.setVisible(isUserPost);
        deleteButton.setVisible(isUserPost);
    }
}