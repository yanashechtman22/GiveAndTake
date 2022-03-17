package com.example.giveandtake.ui.home;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.giveandtake.R;
import com.example.giveandtake.common.PostsListLoadingState;
import com.example.giveandtake.model.AppLocalDB;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.main,menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_posts).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.search_posts) {
            return true;
        }
        return false;
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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public int getItemCount() {
            if(homeViewModel.getPosts().getValue() == null){
                return 0;
            }
            return homeViewModel.getPosts().getValue().size() ;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void filter(String query) {
            if(query.isEmpty()){
                AppModel.instance.refreshPostsList();
            }else{
                List<Post> newPosts = AppModel.instance.getByQuery(query);
                homeViewModel.setPosts(newPosts);
            }
            notifyDataSetChanged();
        }
    }
}
