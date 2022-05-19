package com.lost_found_it.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.lost_found_it.R;
import com.lost_found_it.databinding.MyAdsRowBinding;
import com.lost_found_it.databinding.NotificationRowBinding;
import com.lost_found_it.model.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotificationModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public NotificationAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.notification_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public NotificationRowBinding binding;

        public MyHolder(NotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<NotificationModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
