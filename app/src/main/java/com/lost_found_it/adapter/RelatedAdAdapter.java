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
import com.lost_found_it.databinding.RelatedAdRowBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.uis.activity_home.fragments.FragmentAdDetails;

import java.util.List;

public class RelatedAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AdModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public RelatedAdAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelatedAdRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.related_ad_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            if (fragment instanceof FragmentAdDetails){
                FragmentAdDetails fragmentAdDetails = (FragmentAdDetails) fragment;
                fragmentAdDetails.setAdItemData(list.get(myHolder.getAdapterPosition()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public RelatedAdRowBinding binding;

        public MyHolder(RelatedAdRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<AdModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
