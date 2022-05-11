package com.lost_found_it.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;


import com.lost_found_it.R;
import com.lost_found_it.databinding.CountrySpinnerRowBinding;
import com.lost_found_it.model.CountryCodeModel;

import java.util.List;

public class CountryCodeAdapter extends BaseAdapter {
    private List<CountryCodeModel> list;
    private Context context;
    private LayoutInflater inflater;

    public CountryCodeAdapter(Context context) {
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
        @SuppressLint("ViewHolder") CountrySpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.country_spinner_row, parent, false);
        binding.image.setImageResource(list.get(position).getFlag());
        binding.setModel(list.get(position));
        return binding.getRoot();
    }

    public void updateList(List<CountryCodeModel> list) {
        if (list != null) {
            this.list = list;

        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}
