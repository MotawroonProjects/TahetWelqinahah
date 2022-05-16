package com.lost_found_it.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lost_found_it.R;
import com.lost_found_it.databinding.BrandRowBinding;
import com.lost_found_it.model.CategoryModel;
import com.lost_found_it.model.SubCategoryModel;
import com.lost_found_it.uis.activity_home.fragments.FragmentFound;
import com.lost_found_it.uis.activity_home.fragments.FragmentLost;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SubCategoryModel> list;
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
        SubCategoryModel model = list.get(position);
        myHolder.binding.setModel(model);
        Log.e("model",model.getId()+"__"+model.isSelected());

        if (model.isSelected()){
            if (oldHolder==null){
                oldHolder = myHolder;
            }
        }

        myHolder.itemView.setOnClickListener(v -> {
            if (oldHolder!=null){
                SubCategoryModel oldModel = list.get(oldHolder.getAdapterPosition());
                if (oldModel.isSelected()){
                    oldModel.setSelected(false);
                    list.set(oldHolder.getAdapterPosition(),oldModel);
                    oldHolder.binding.setModel(oldModel);
                }
            }
            SubCategoryModel subCategoryModel = list.get(myHolder.getAdapterPosition());
            if (!subCategoryModel.isSelected()){
                subCategoryModel.setSelected(true);
                list.set(myHolder.getAdapterPosition(),subCategoryModel);
                myHolder.binding.setModel(subCategoryModel);
                oldHolder = myHolder;

                if (fragment instanceof FragmentFound){
                    FragmentFound fragmentFound = (FragmentFound) fragment;
                    fragmentFound.setSubCategoryItemSelected(subCategoryModel);
                }else if (fragment instanceof FragmentLost){
                    FragmentLost fragmentLost = (FragmentLost) fragment;
                    fragmentLost.setSubCategoryItemSelected(subCategoryModel);
                }

            }




        });


    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public BrandRowBinding binding;

        public MyHolder(BrandRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<SubCategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
