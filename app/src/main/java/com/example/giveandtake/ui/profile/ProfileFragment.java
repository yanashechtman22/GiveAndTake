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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.model.FireBaseUserModel;
import com.example.giveandtake.model.Post;
import com.example.giveandtake.model.User;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    EditText phone;
    TextView email;
    TextView name;
    Button registerBtn;
    CardView validationCard;
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


        return view;
    }

    private void initializeUserData(UserInfo user) {
        email = view.findViewById(R.id.profile_user_email_input);
        name = view.findViewById(R.id.profile_user_name_input);
        phone = view.findViewById(R.id.profile_user_phone_input);

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
//            holder.itemView.setOnClickListener(v -> {
//                String id = post.getId();
//                Navigation.findNavController(v).navigate(HomeFragmentDirections.actionHomeToPostDetailsFragment(id));
//            });
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