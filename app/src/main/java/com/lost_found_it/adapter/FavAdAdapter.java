package com.lost_found_it.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lost_found_it.R;
import com.lost_found_it.databinding.AdRowFavBinding;
import com.lost_found_it.databinding.RelatedAdRowBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.uis.activity_home.fragments.FragmentAdDetails;
import com.lost_found_it.uis.activity_home.fragments.FragmentMyFavorite;

import java.util.List;

public class FavAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AdModel> list;
    private Context context;
    private LayoutInflater inflater;
    private FragmentMyFavorite fragment;
    public FavAdAdapter(Context context, FragmentMyFavorite fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdRowFavBinding binding = DataBindingUtil.inflate(inflater, R.layout.ad_row_fav, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.fav.setOnClickListener(v -> {
            fragment.unFav(list.get(myHolder.getAdapterPosition()).getId());
        });
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public AdRowFavBinding binding;

        public MyHolder(AdRowFavBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<AdModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
