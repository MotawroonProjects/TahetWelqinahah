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
import com.lost_found_it.databinding.CityRowBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.CityModel;
import com.lost_found_it.uis.activity_cities.CitiesActivity;
import com.lost_found_it.uis.activity_home.fragments.FragmentFound;
import com.lost_found_it.uis.activity_home.fragments.FragmentHome;
import com.lost_found_it.uis.activity_home.fragments.FragmentLost;
import com.lost_found_it.uis.activity_home.fragments.FragmentMecca;
import com.lost_found_it.uis.activity_home.fragments.FragmentTower;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CityModel> list;
    private Context context;
    private LayoutInflater inflater;
    private CitiesActivity activity;
    public CityAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (CitiesActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CityRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.city_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            activity.setItemData(list.get(myHolder.getAdapterPosition()));
        });
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public CityRowBinding binding;

        public MyHolder(CityRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<CityModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
