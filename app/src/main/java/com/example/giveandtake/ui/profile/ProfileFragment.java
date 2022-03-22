package com.example.giveandtake.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.giveandtake.R;
import com.example.giveandtake.common.PostsListLoadingState;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    SwipeRefreshLayout swipeRefresh;
    TextView email;
    TextView name;
    ImageButton logout;
    FloatingActionButton addNewPost;
    View view;
    ItemAdapter adapter;
    ProfileViewModel profileViewModel;
    UserInfo userInfo;
    ImageView picture;
    ImageView editNameButton;
    ImageView editPhotoButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        userInfo = AuthenticationModel.instance.getUserInfo();

        initializeUserData(userInfo);
        initializePostList();

        AppModel.instance.getPostListLoadingState().observe(getViewLifecycleOwner(),
                postsListLoadingState -> swipeRefresh.setRefreshing(postsListLoadingState == PostsListLoadingState.loading));

        logout.setOnClickListener(v -> logOutActions());

        addNewPost.setOnClickListener(v -> addNewPostActions());

        editNameButton.setOnClickListener(v -> changeNameButton());

        return view;
    }

    private void changeNameButton() {
        if (name.getInputType() == InputType.TYPE_NULL) {
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            name.setEnabled(true);
            editNameButton.setImageResource(R.drawable.ic_save_button);
        } else {
            name.setInputType(InputType.TYPE_NULL);
            name.setEnabled(false);
            editNameButton.setImageResource(R.drawable.ic_edit_button);
            updateUserNameOnDb();
        }
    }

    private void updateUserNameOnDb() {
        userInfo = AuthenticationModel.instance.getUserInfo();
        FirebaseUser fireBaseUser = AuthenticationModel.instance.getFireBaseUser();

        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder()
                .setDisplayName(name.getText().toString());

        final UserProfileChangeRequest changeRequest = builder.build();
        fireBaseUser.updateProfile(changeRequest);
        userInfo = AuthenticationModel.instance.getUserInfo();

    }

    private void logOutActions() {
        AuthenticationModel.instance.logOut();
        Navigation.findNavController(view).navigate(
                ProfileFragmentDirections.actionUserProfilePageToLoginFragment2());
    }

    private void addNewPostActions() {
        Navigation.findNavController(view).navigate(
                ProfileFragmentDirections.actionUserProfilePageToNavAdAdd());
    }

    private void initializeUserData(UserInfo user) {
        email = view.findViewById(R.id.profile_user_email_input);
        name = view.findViewById(R.id.profile_user_name_input);
        logout = view.findViewById(R.id.profile_user_logout_button);
        addNewPost = view.findViewById(R.id.profile_user_add_new_post);
        picture = view.findViewById(R.id.profile_user_picture_profile);
        picture = view.findViewById(R.id.profile_user_picture_profile);
        editNameButton = view.findViewById(R.id.profile_user_edit_name_profile);
        editPhotoButton = view.findViewById(R.id.profile_user_edit_photo_profile);

        //todo change the image
        picture.setImageResource(R.drawable.image2);
        name.setText(user.getDisplayName());
        name.setInputType(InputType.TYPE_NULL);
        email.setText(user.getEmail());
    }

    private void initializePostList() {
        RecyclerView postsList = view.findViewById(R.id.posts_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ItemAdapter();
        postsList.setAdapter(adapter);
        setHasOptionsMenu(true);

        swipeRefresh = view.findViewById(R.id.postsList_swipeRefresh);
        swipeRefresh.setOnRefreshListener(AppModel.instance::refreshPostsList);

        profileViewModel.getPosts(userInfo.getUid());
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.single_post_item_user_profile, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Post post = profileViewModel.getPosts(userInfo.getUid()).get(position);
            String postId = post.getId();

            holder.postContent.setText(post.getContent());
            holder.postImage.setImageResource(R.drawable.image2);
            String postImageUrl = post.getImageUrl();
            if (postImageUrl != null && postImageUrl.length() > 0) {
                Picasso.get()
                        .load(postImageUrl)
                        .into(holder.postImage);
            }
            holder.editButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionUserProfilePageToEditPostFragment(postId)));

            holder.deleteButton.setOnClickListener(v -> {
                profileViewModel.deletePost(postId);
                refresh();
            });

            holder.postContent.setOnClickListener(v -> Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionUserProfilePageToPostDetailsFragment(postId)));

            holder.postImage.setOnClickListener(v -> Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionUserProfilePageToPostDetailsFragment(postId)));
        }

        @SuppressLint("NotifyDataSetChanged")
        private void refresh() {
            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        }

        @Override
        public int getItemCount() {
            if (profileViewModel.getPosts(userInfo.getUid()) == null) {
                return 0;
            }
            return profileViewModel.getPosts(userInfo.getUid()).size();
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView postContent;
        private final ImageView postImage;
        private final ImageView editButton;
        private final ImageView deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            postContent = itemView.findViewById(R.id.item_title_user_profile);
            postImage = itemView.findViewById(R.id.item_image_user_profile);
            editButton = itemView.findViewById(R.id.item_edit_user_profile);
            deleteButton = itemView.findViewById(R.id.item_delete_user_profile);
        }
    }
}