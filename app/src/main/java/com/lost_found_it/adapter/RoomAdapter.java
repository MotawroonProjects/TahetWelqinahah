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
import com.lost_found_it.databinding.RoomRowBinding;
import com.lost_found_it.model.RoomModel;
import com.lost_found_it.model.SubCategoryModel;
import com.lost_found_it.uis.activity_home.fragments.FragmentFound;
import com.lost_found_it.uis.activity_home.fragments.FragmentLost;
import com.lost_found_it.uis.activity_rooms.RoomsActivity;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RoomModel> list;
    private Context context;
    private LayoutInflater inflater;
    private RoomsActivity activity;

    public RoomAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (RoomsActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RoomRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.room_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        RoomModel model = list.get(position);
        myHolder.binding.setModel(model);

        myHolder.itemView.setOnClickListener(v -> {
            RoomModel roomModel = list.get(myHolder.getAdapterPosition());
            activity.setRoomItem(roomModel);
        });

    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public RoomRowBinding binding;

        public MyHolder(RoomRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<RoomModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
