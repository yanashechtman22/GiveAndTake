package com.example.giveandtake.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.example.giveandtake.model.FireBaseUserModel;
import com.example.giveandtake.model.Post;
import com.example.giveandtake.model.User;
import com.example.giveandtake.ui.Posts.EditPostFragmentDirections;
import com.example.giveandtake.ui.home.HomeFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    SwipeRefreshLayout swipeRefresh;
    EditText phone;
    TextView email;
    TextView name;
    Button logout;
    FloatingActionButton addNewPost;
    ProgressBar progressBar;
    View view;
    FireBaseUserModel fireBaseUserModel = new FireBaseUserModel();
    ItemAdapter adapter;
    ProfileViewModel profileViewModel;
    UserInfo userInfo;

    private final int MIN_PASS_LENGTH = 6;
    boolean nameNotEmpty = false;
    boolean emailValid = false;
    boolean passwordValid = false;
    User user = null;


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

//        profileViewModel.getPosts(userInfo.).observe(getViewLifecycleOwner(), list1 -> refresh());
        AppModel.instance.getStudentListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            if (postsListLoadingState == PostsListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }

        });

        logout.setOnClickListener(v -> {
            logOutActions();
        });

        addNewPost.setOnClickListener(v -> {
            addNewPostActions();
        });


        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
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
        phone = view.findViewById(R.id.profile_user_phone_input);
        logout = view.findViewById(R.id.profile_user_logout_button);
        addNewPost = view.findViewById(R.id.profile_user_add_new_post);

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
    }

    private void initializePostList() {
        RecyclerView postsList = view.findViewById(R.id.posts_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ItemAdapter();
        postsList.setAdapter(adapter);
        setHasOptionsMenu(true);

        swipeRefresh = view.findViewById(R.id.postsList_swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> AppModel.instance.refreshPostsList());

        profileViewModel.getPosts(userInfo.getUid());
        //.observe(getViewLifecycleOwner(), list1 -> refresh());
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.single_post_item_user_profile, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Post post = profileViewModel.getPosts(userInfo.getUid()).get(position);
            holder.postContent.setText(post.getContent());
            holder.postImage.setImageResource(R.drawable.image2);
            String postImageUrl = post.getImageUrl();
            if (postImageUrl != null && postImageUrl.length() > 0) {
                Picasso.get()
                        .load(postImageUrl)
                        .into(holder.postImage);
            }
            holder.editButton.setOnClickListener(v -> {
                String id = post.getId();
                Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionUserProfilePageToEditPostFragment(id));
            });

            holder.deleteButton.setOnClickListener(v -> {
                String postId = post.getId();
                profileViewModel.deletePost(postId);
                refresh();
            });
        }

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

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView postContent;
        private ImageView postImage;
        private ImageView editButton;
        private ImageView deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            postContent = itemView.findViewById(R.id.item_title_user_profile);
            postImage = itemView.findViewById(R.id.item_image_user_profile);
            editButton = itemView.findViewById(R.id.item_edit_user_profile);
            deleteButton = itemView.findViewById(R.id.item_delete_user_profile);
        }
    }
}