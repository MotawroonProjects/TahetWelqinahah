package com.lost_found_it.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.lost_found_it.R;
import com.lost_found_it.databinding.SpinnerRowBinding;
import com.lost_found_it.model.CategoryModel;
import com.lost_found_it.model.SubCategoryModel;

import java.util.List;

public class SpinnerSubCategoryAdapter extends BaseAdapter {
    private List<SubCategoryModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerSubCategoryAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row, parent, false);
        binding.setTitle(list.get(position).getTitle());
        return binding.getRoot();
    }

    public void updateList(List<SubCategoryModel> list) {
        if (list != null) {
            this.list = list;

        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}
