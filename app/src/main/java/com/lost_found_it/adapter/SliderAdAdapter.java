package com.lost_found_it.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.lost_found_it.R;
import com.lost_found_it.databinding.SliderAdBinding;
import com.lost_found_it.databinding.SliderBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.SliderImages;

import java.util.List;

public class SliderAdAdapter extends PagerAdapter {

    List<SliderImages> list;
    private LayoutInflater inflater;
    Context context;

    public SliderAdAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        SliderAdBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slider_ad,view,false);
        rowBinding.setModel(list.get(position));
        view.addView(rowBinding.getRoot());
        return rowBinding.getRoot();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void updateList(List<SliderImages> list){
        if (list!=null){
            this.list = list;
        }else {
            this.list.clear();
        }

        notifyDataSetChanged();

    }
}