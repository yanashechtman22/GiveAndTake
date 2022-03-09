package com.example.giveandtake.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giveandtake.R;
import com.example.giveandtake.auth.LoginFragmentDirections;
import com.example.giveandtake.auth.RegisterFragment;
import com.example.giveandtake.auth.RegisterFragmentDirections;
import com.example.giveandtake.databinding.FragmentHomeBinding;
import com.example.giveandtake.model.AuthenticationModel;
import com.example.giveandtake.model.Post;
import com.example.giveandtake.ui.profile.UserProfileFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ItemAdapter adapter;
    HomeViewModel homeViewModel;
    View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView postsList = view.findViewById(R.id.posts_rv);
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(homeViewModel.getPosts());
        postsList.setAdapter(adapter);

        Button profile = view.findViewById(R.id.home_to_profile_button);
        profile.setOnClickListener(handleMoveToProfile());
        //clicking on an add to go to ads details
        .setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String noteId = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(MyNotesFragmentDirections.actionNavMyNotesToNoteDetailsFragment(noteId));
            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private ArrayList<Post> posts;

        public ItemAdapter(ArrayList<Post> posts) {
            this.posts = posts;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.single_ad_item, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Post post = homeViewModel.getPosts().get(position);
            holder.textView.setText(ad.getContent());
            holder.imageView.setImageResource(ad.getImage());

            holder.itemView.setOnClickListener(v -> {
                String id = ad.getId();
                Navigation.findNavController(v).navigate(HomeFragmentDirections.actionHomeToAdDetailsFragment(id));
            });
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }


    }


    private View.OnClickListener handleMoveToProfile() {
//        Bundle bundle = new Bundle();
//        bundle.putString("email", "amount");
        return Navigation.createNavigateOnClickListener(
                R.id.action_nav_home_to_userProfileFragment2);

    }

}
