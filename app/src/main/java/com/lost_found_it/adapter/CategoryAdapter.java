package com.lost_found_it.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lost_found_it.R;
import com.lost_found_it.databinding.CategoryRowBinding;
import com.lost_found_it.model.CategoryModel;
import com.lost_found_it.uis.activity_home.fragments.FragmentFound;
import com.lost_found_it.uis.activity_home.fragments.FragmentLost;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private MyHolder oldHolder;

    public CategoryAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CategoryModel model = list.get(position);
        myHolder.binding.setModel(model);
        if (model.isSelected()){
            if (oldHolder==null){
                oldHolder = myHolder;
            }
        }

        myHolder.itemView.setOnClickListener(v -> {
            if (oldHolder!=null){
                CategoryModel oldModel = list.get(oldHolder.getAdapterPosition());
                if (oldModel.isSelected()){
                    oldModel.setSelected(false);
                    list.set(oldHolder.getAdapterPosition(),oldModel);
                    oldHolder.binding.setModel(oldModel);
                }
            }
            CategoryModel categoryModel = list.get(myHolder.getAdapterPosition());
            if (!categoryModel.isSelected()){
                categoryModel.setSelected(true);
                list.set(myHolder.getAdapterPosition(),categoryModel);
                myHolder.binding.setModel(categoryModel);
                oldHolder = myHolder;
                if (fragment instanceof FragmentFound){
                    FragmentFound fragmentFound = (FragmentFound) fragment;
                    fragmentFound.setCategoryItemSelected(categoryModel);
                }else if (fragment instanceof FragmentLost){
                    FragmentLost fragmentLost = (FragmentLost) fragment;
                    fragmentLost.setCategoryItemSelected(categoryModel);
                }


            }




        });

    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CategoryRowBinding binding;

        public MyHolder(CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<CategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
