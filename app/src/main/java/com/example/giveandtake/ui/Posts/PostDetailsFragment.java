package com.example.giveandtake.ui.Posts;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.model.Post;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;


public class PostDetailsFragment extends Fragment {
    TextView contentTv;
    TextView phoneNumberTv;
    TextView locationTv;
    Post post;
    String postId;
    ImageView imageView;
    ProgressBar progressBar;
    boolean isUserPost;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_details, container, false);
        UserInfo userInfo = AuthenticationModel.instance.getUserInfo();
        postId = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostId();

        contentTv = view.findViewById(R.id.idTVContent);
        phoneNumberTv = view.findViewById(R.id.idTVPhone);
        locationTv = view.findViewById(R.id.idTVLocation);
        imageView = view.findViewById(R.id.idIVPostImage);
        progressBar = view.findViewById(R.id.postDetailsProgressBar2);

         AppModel.instance.getPostById(postId, post -> {
             isUserPost = post.getUserId().equals(userInfo.getUid());
             contentTv.setText(post.getContent());
             phoneNumberTv.setText(post.getPhone());
             locationTv.setText(post.getLocation());
             progressBar.setVisibility(View.INVISIBLE);
             if (post.getImageUrl() != null && post.getImageUrl().length() > 0) {
                 Picasso.get().load(post.getImageUrl()).into(imageView);
             }
             setHasOptionsMenu(true);
         });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.details_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.app_bar_delete:
                handleDeletePost();
                return super.onOptionsItemSelected(item);
            case R.id.app_bar_edit:
                Navigation.findNavController(view).
                        navigate(PostDetailsFragmentDirections.
                                actionPostDetailsFragmentToEditPostFragment(postId));
            default: return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        MenuItem editButton = menu.findItem(R.id.app_bar_edit);
        MenuItem deleteButton = menu.findItem(R.id.app_bar_delete);
        editButton.setVisible(isUserPost);
        deleteButton.setVisible(isUserPost);
    }

    private void handleDeletePost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Delete", this::deletePostFromDb)
                .setNegativeButton("Cancel", (dialog, id) -> {});
        builder.create().show();
    }

    private void deletePostFromDb(DialogInterface dialog, int id) {
        progressBar.setVisibility(View.VISIBLE);
        AppModel.instance.deletePostById(postId, (success) -> {
            if (success) {
                Navigation.findNavController(view).navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToHome());
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(view, "unable to delete post", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}