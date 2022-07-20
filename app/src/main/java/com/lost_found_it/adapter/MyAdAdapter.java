package com.lost_found_it.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lost_found_it.R;
import com.lost_found_it.databinding.AdRowBinding;
import com.lost_found_it.databinding.MyAdsRowBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.uis.activity_home.fragments.FragmentMyAds;

import java.util.List;

public class MyAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AdModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public MyAdAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyAdsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.my_ads_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.binding.llView.setOnClickListener(view -> {
            if (fragment instanceof FragmentMyAds){
                FragmentMyAds fragmentMyAds=(FragmentMyAds) fragment;
                fragmentMyAds.navigateToDetails(list.get(myHolder.getAdapterPosition()));
            }
        });
        myHolder.binding.llEdit.setOnClickListener(view -> {
            if (fragment instanceof FragmentMyAds){
                FragmentMyAds fragmentMyAds=(FragmentMyAds) fragment;
                fragmentMyAds.editAdd(myHolder.getAdapterPosition(),list.get(position));
            }
        });

        myHolder.binding.llDelete.setOnClickListener(view -> {
            if (fragment instanceof FragmentMyAds){
                try {
                    if (list.size()>0){
                        FragmentMyAds fragmentMyAds=(FragmentMyAds) fragment;
                        fragmentMyAds.delete(myHolder.getAdapterPosition(),list.get(position));

                    }

                }catch (Exception e){

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public MyAdsRowBinding binding;

        public MyHolder(MyAdsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<AdModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
