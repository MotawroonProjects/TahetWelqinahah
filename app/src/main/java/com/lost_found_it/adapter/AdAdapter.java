package com.lost_found_it.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.lost_found_it.R;
import com.lost_found_it.databinding.AdRowBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.uis.activity_home.fragments.FragmentHome;

import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AdModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public AdAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.ad_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.itemView.setOnClickListener(v -> {
            if (fragment instanceof FragmentHome){
                FragmentHome fragmentHome = (FragmentHome) fragment;
                fragmentHome.navigateToAdDetails();
            }
        });
    }



    @Override
    public int getItemCount() {
        return list != null ? list.size() : 4;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public AdRowBinding binding;

        public MyHolder(AdRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<AdModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
