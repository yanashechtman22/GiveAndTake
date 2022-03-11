package com.example.giveandtake.ui.home;

import android.content.Context;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.giveandtake.R;
import com.example.giveandtake.common.PostsListLoadingState;
import com.example.giveandtake.databinding.FragmentHomeBinding;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeFragment extends Fragment {

    ItemAdapter adapter;
    HomeViewModel homeViewModel;
    SwipeRefreshLayout swipeRefresh;
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

        swipeRefresh = view.findViewById(R.id.postsList_swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> AppModel.instance.refreshPostsList());

        RecyclerView postsList = view.findViewById(R.id.posts_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ItemAdapter();
        postsList.setAdapter(adapter);
        setHasOptionsMenu(true);

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
            View view = getLayoutInflater().inflate(R.layout.single_ad_item, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

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
                Navigation.findNavController(v).navigate(HomeFragmentDirections.actionHomeToAdDetailsFragment(id));
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
