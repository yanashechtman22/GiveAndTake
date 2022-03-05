package com.example.giveandtake.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giveandtake.R;
import com.example.giveandtake.databinding.FragmentHomeBinding;
import com.example.giveandtake.model.Ad;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ItemAdapter adapter;
    HomeViewModel homeViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView postsList = view.findViewById(R.id.posts_rv);
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(homeViewModel.getPosts());
        postsList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.item_image);
        }

        void bind(Ad ad) {
            textView.setText(ad.getText());
            imageView.setImageResource(ad.getImage());
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{
        private ArrayList<Ad> posts;
        public ItemAdapter(ArrayList<Ad> posts) {
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
            Ad ad = homeViewModel.getPosts().get(position);
            holder.bind(ad);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }


    }
}