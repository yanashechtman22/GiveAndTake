package com.example.giveandtake.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.giveandtake.R;
import com.example.giveandtake.common.PostsListLoadingState;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    ItemAdapter adapter;
    HomeViewModel homeViewModel;
    SwipeRefreshLayout swipeRefresh;
    FloatingActionButton addPostButton;
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

        addPostButton = view.findViewById(R.id.add_new_post);
        swipeRefresh = view.findViewById(R.id.postsList_swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> AppModel.instance.refreshPostsList());

        RecyclerView postsList = view.findViewById(R.id.posts_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ItemAdapter();
        postsList.setAdapter(adapter);
        setHasOptionsMenu(true);

        addPostButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomeFragmentDirections.actionNavHomeToNavAdAdd()));

        homeViewModel.getPosts().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(AppModel.instance.getStudentListLoadingState().getValue() == PostsListLoadingState.loading);
        AppModel.instance.getStudentListLoadingState().observe(getViewLifecycleOwner(), postsListLoadingState -> {
            if (postsListLoadingState == PostsListLoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }else{
                swipeRefresh.setRefreshing(false);
            }

        });
        return view;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView postContent;
        private ImageView postImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            postContent = itemView.findViewById(R.id.item_title);
            postImage = itemView.findViewById(R.id.item_image);
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.single_post_item, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Post post = homeViewModel.getPosts().getValue().get(position);
            holder.postContent.setText(post.getContent());
            holder.postImage.setImageResource(R.drawable.image2);
            String postImageUrl = post.getImageUrl();
            if (postImageUrl != null && postImageUrl.length()>0) {
                Picasso.get()
                        .load(postImageUrl)
                        .into(holder.postImage);
            }
            holder.itemView.setOnClickListener(v -> {
                String id = post.getId();
                Navigation.findNavController(v).navigate(HomeFragmentDirections.actionHomeToPostDetailsFragment(id));
            });
        }

        @Override
        public int getItemCount() {
            if(homeViewModel.getPosts().getValue() == null){
                return 0;
            }
            return homeViewModel.getPosts().getValue().size() ;
        }
    }
}
