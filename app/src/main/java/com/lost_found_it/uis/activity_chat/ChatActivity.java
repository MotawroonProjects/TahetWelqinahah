package com.lost_found_it.uis.activity_chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.lost_found_it.BuildConfig;
import com.lost_found_it.R;
import com.lost_found_it.adapter.ChatAdapter;
import com.lost_found_it.chat_service.ChatService;
import com.lost_found_it.databinding.ActivityChatBinding;
import com.lost_found_it.model.AddChatMessageModel;
import com.lost_found_it.model.ChatUserModel;
import com.lost_found_it.model.MessageModel;
import com.lost_found_it.mvvm.ActivityChatMvvm;
import com.lost_found_it.uis.activity_base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private ActivityChatMvvm mvvm;
    private String imagePath = "";
    private ChatAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private ChatUserModel model;
    private int req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (ChatUserModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(ActivityChatMvvm.class);
        mvvm.getIsLoading().observe(this, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
        });


        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.setModel(model);
        binding.setLang(getLang());
        binding.edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.send.setImageResource(R.drawable.ic_file);
                } else {
                    binding.send.setImageResource(R.drawable.ic_send);

                }
            }
        });
        adapter = new ChatAdapter(this, binding.recView,getUserModel().getData().getUser().getId());
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recView.setAdapter(adapter);

        mvvm.onDataSuccess().observe(this, list -> {
            if (adapter != null) {
                adapter.updateList(list);
            }
        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK) {
                sendMessage("file", "", imagePath);

            }
        });

        binding.llBack.setOnClickListener(v -> {
            finish();
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mvvm.getChatMessages(getUserModel(), getUserSetting().getCountry(), model.getAd_id(),model.getRoom_id());

        binding.send.setOnClickListener(v -> {
            if (binding.edtMessage.getText().toString().length() > 0) {
                sendMessage("text", binding.edtMessage.getText().toString(), null);
                binding.edtMessage.setText(null);
            } else {
                checkCameraFilePermission();
            }


        });

        binding.cardLastMsg.setOnClickListener(v -> {
            binding.setMsg("");
            binding.cardLastMsg.setVisibility(View.GONE);
            binding.recView.scrollToPosition(mvvm.onDataSuccess().getValue().size() - 1);
            adapter.notifyDataSetChanged();
            //binding.recView.post(() -> );
        });

        binding.swipeRefresh.setOnRefreshListener(() ->mvvm.getChatMessages(getUserModel(), getUserSetting().getCountry(), model.getAd_id(),model.getRoom_id()));
        setRoomId(model);


    }

    private void sendMessage(String type, String msg, String image_url) {
        AddChatMessageModel addChatMessageModel = new AddChatMessageModel(type, msg, image_url, model.getAd_id(),model.getRoom_id(),getUserModel().getData().getUser().getId());
        Intent intent = new Intent(this, ChatService.class);
        intent.putExtra("data", addChatMessageModel);
        startService(intent);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessage(MessageModel messageModel) {
        imagePath = "";
        mvvm.addNewMessage(messageModel);
        adapter.notifyItemInserted(mvvm.onDataSuccess().getValue().size() - 1);

    }

    private void checkCameraFilePermission() {
        if (ActivityCompat.checkSelfPermission(this, BaseActivity.WRITE_REQ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, BaseActivity.CAM_REQ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{BaseActivity.WRITE_REQ, BaseActivity.CAM_REQ}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 2) {
                openCamera();
            }
        }
    }

    private void openCamera() {

        req = 1;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = getImageFile();

        if (imageFile != null) {
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            launcher.launch(intent);
        }


    }

    private File getImageFile() {
        File imageFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm", Locale.ENGLISH).format(new Date());
        String imageName = "JPEG_" + timeStamp + "_";
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Etbo5lyClientImages");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            imageFile = File.createTempFile(imageName, ".jpg", file);
            imagePath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearRoomId();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void displayLastMessage(MessageModel messageModel) {
        if (messageModel.getType().equals("file")) {
            binding.setMsg(getString(R.string.attachment_sent));
        } else {
            binding.setMsg(messageModel.getMessage());
        }
        binding.cardLastMsg.setVisibility(View.VISIBLE);


    }

    public void hideLastMessageView() {
        binding.setMsg("");
        binding.cardLastMsg.setVisibility(View.GONE);
    }
}