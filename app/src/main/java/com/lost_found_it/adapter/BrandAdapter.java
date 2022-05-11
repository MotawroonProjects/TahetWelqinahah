package com.lost_found_it.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lost_found_it.R;
import com.lost_found_it.databinding.BrandRowBinding;
import com.lost_found_it.databinding.CategoryRowBinding;
import com.lost_found_it.model.BrandModel;
import com.lost_found_it.model.CategoryModel;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BrandModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private MyHolder oldHolder;

    public BrandAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BrandRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.brand_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
       /* BrandModel model = list.get(position);
        if (model.isSelected()){
            if (oldHolder==null){
                oldHolder = myHolder;
            }
        }*/

    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public BrandRowBinding binding;

        public MyHolder(BrandRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<BrandModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
