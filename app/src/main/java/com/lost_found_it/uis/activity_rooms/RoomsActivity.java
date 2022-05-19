package com.lost_found_it.uis.activity_rooms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lost_found_it.R;
import com.lost_found_it.adapter.MyAdAdapter;
import com.lost_found_it.adapter.RoomAdapter;
import com.lost_found_it.databinding.ActivityCountryBinding;
import com.lost_found_it.databinding.ActivityRoomsBinding;
import com.lost_found_it.model.AppSettingModel;
import com.lost_found_it.model.ChatUserModel;
import com.lost_found_it.model.RoomModel;
import com.lost_found_it.mvvm.ActivityRoomsMvvm;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_chat.ChatActivity;

public class RoomsActivity extends BaseActivity {
    private ActivityRoomsBinding binding;
    private ActivityRoomsMvvm mvvm;
    private RoomAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rooms);
        initView();

    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(ActivityRoomsMvvm.class);
        setUpToolbar(binding.toolbar, getString(R.string.conversations), R.color.white, R.color.black);
        adapter = new RoomAdapter(this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.tvNoData.setText(R.string.no_conv);
        mvvm.getIsLoading().observe(this,isLoading->{
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);

        });
        mvvm.getOnDataSuccess().observe(this,list->{
            if (list.size()>0){
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            }else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

            }
            if (adapter!=null){
                adapter.updateList(list);
            }
        });

        binding.recViewLayout.swipeRefresh.setOnRefreshListener(()->{
            mvvm.getRooms(getUserSetting().getCountry(),getUserModel());
        });
        mvvm.getRooms(getUserSetting().getCountry(),getUserModel());
    }

    public void setRoomItem(RoomModel roomModel) {
        String phone = roomModel.getUser().getPhone_code()+roomModel.getUser().getPhone();
        ChatUserModel model = new ChatUserModel(roomModel.getUser().getId(),roomModel.getUser().getFirst_name()+" "+roomModel.getUser().getLast_name(),phone,roomModel.getUser().getImage(),roomModel.getAd_id(),roomModel.getId(),roomModel.getAd());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("data",model);
        startActivity(intent);
    }
}