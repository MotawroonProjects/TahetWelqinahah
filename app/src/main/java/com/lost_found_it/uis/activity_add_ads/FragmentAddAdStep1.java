package com.lost_found_it.uis.activity_add_ads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lost_found_it.BuildConfig;
import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.SliderAdapter;
import com.lost_found_it.databinding.DialogChooseImageBinding;
import com.lost_found_it.databinding.FragmentAdDetailsBinding;
import com.lost_found_it.databinding.FragmentHomeBinding;
import com.lost_found_it.databinding.FragmentPostAddStep1Binding;
import com.lost_found_it.model.AddAdModel;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.share.Common;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FragmentAddAdStep1 extends BaseFragment {
    private FragmentPostAddStep1Binding binding;
    private AddAdsActivity activity;
    private AddAdModel model;
    private ActivityResultLauncher<String[]> permissions;
    private ActivityResultLauncher<Intent> launcher;
    private Uri outPutUri = null;
    private String imagePath = "";
    private int req;
    private int type;
    private Map<Integer, String> uris;
    private List<String> images;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AddAdsActivity) context;
        permissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            if (req == 1) {
                if (permissions.containsValue(false)) {
                    Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
                } else {
                    openCamera();
                }
            } else if (req == 2) {
                if (permissions.containsValue(false)) {
                    Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
                } else {
                    openGallery();
                }
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                if (type == 1) {
                    Glide.with(this)
                            .asBitmap()
                            .load(outPutUri)
                            .into(binding.img1);
                    binding.icon1.setVisibility(View.GONE);
                    uris.put(0, outPutUri.toString());
                    addImages();

                } else if (type == 2) {
                    Glide.with(this)
                            .asBitmap()
                            .load(outPutUri)
                            .into(binding.img2);
                    binding.icon2.setVisibility(View.GONE);
                    uris.put(1, outPutUri.toString());
                    addImages();


                } else if (type == 3) {
                    Glide.with(this)
                            .asBitmap()
                            .load(outPutUri)
                            .into(binding.img3);
                    binding.icon3.setVisibility(View.GONE);
                    uris.put(2, outPutUri.toString());
                    addImages();

                } else if (type == 4) {
                    Glide.with(this)
                            .asBitmap()
                            .load(outPutUri)
                            .into(binding.img4);
                    binding.icon4.setVisibility(View.GONE);
                    uris.put(3, outPutUri.toString());
                    addImages();

                }


            } else if (req == 2 && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (type == 1) {
                    Glide.with(this)
                            .asBitmap()
                            .load(uri)
                            .into(binding.img1);
                    binding.icon1.setVisibility(View.GONE);
                    uris.put(0, uri.toString());
                    addImages();

                } else if (type == 2) {
                    Glide.with(this)
                            .asBitmap()
                            .load(uri)
                            .into(binding.img2);
                    binding.icon2.setVisibility(View.GONE);
                    uris.put(1, uri.toString());
                    addImages();

                } else if (type == 3) {
                    Glide.with(this)
                            .asBitmap()
                            .load(uri)
                            .into(binding.img3);
                    binding.icon3.setVisibility(View.GONE);
                    uris.put(2, uri.toString());
                    addImages();

                } else if (type == 4) {
                    Glide.with(this)
                            .asBitmap()
                            .load(uri)
                            .into(binding.img4);
                    binding.icon4.setVisibility(View.GONE);
                    uris.put(3, uri.toString());
                    addImages();
                }
            }
        });

    }

    public static FragmentAddAdStep1 newInstance(AddAdModel addAdModel) {
        FragmentAddAdStep1 fragmentAddAdStep1 = new FragmentAddAdStep1();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", addAdModel);
        fragmentAddAdStep1.setArguments(bundle);
        return fragmentAddAdStep1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (AddAdModel) getArguments().getSerializable("data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_add_step1, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        images = new ArrayList<>();
        uris = new HashMap<>();
        binding.setModel(model);
        model.setCountry(getUserSetting().getCountry());

        if (!model.getAd_id().isEmpty()){
            for (int index =0;index<model.getOnlineImages().size();index++){
                String onlineUrl = model.getOnlineImages().get(index);
                if (index==0){
                    Glide.with(this)
                            .asBitmap()
                            .load(onlineUrl)
                            .into(binding.img1);
                    binding.icon1.setVisibility(View.GONE);

                }else if (index==1){
                    Glide.with(this)
                            .asBitmap()
                            .load(onlineUrl)
                            .into(binding.img2);
                    binding.icon2.setVisibility(View.GONE);
                }else if (index==2){
                    Glide.with(this)
                            .asBitmap()
                            .load(onlineUrl)
                            .into(binding.img3);
                    binding.icon3.setVisibility(View.GONE);
                }else if (index==3){
                    Glide.with(this)
                            .asBitmap()
                            .load(onlineUrl)
                            .into(binding.img4);
                    binding.icon4.setVisibility(View.GONE);
                }
            }

            if (model.getAd_type().equals("lost")){
                updateAdTypeLost();
            }else if (model.getAd_type().equals("found")){
                updateAdTypeFound();
            }
        }

        if (getUserSetting().getCountry().equals("sa")) {
            binding.checkbox.setVisibility(View.VISIBLE);
            binding.checkbox.setText(R.string.thing_great_mosq);
            if (!model.getAd_id().isEmpty()){
                if (model.getAd_type().equals("special")){
                    binding.checkbox.setChecked(true);
                }else {
                    binding.checkbox.setChecked(false);

                }
            }

        } else if (getUserSetting().getCountry().equals("ae")) {
            binding.checkbox.setVisibility(View.VISIBLE);
            binding.checkbox.setText(R.string.thing_great_tower);
            if (!model.getAd_id().isEmpty()){
                if (model.getAd_type().equals("special")){
                    binding.checkbox.setChecked(true);
                }else {
                    binding.checkbox.setChecked(false);

                }
            }

        } else {
            binding.checkbox.setText("");
            binding.checkbox.setVisibility(View.GONE);
        }



        binding.flAdLost.setOnClickListener(v -> {
           updateAdTypeLost();
        });

        binding.flAdFound.setOnClickListener(v -> {
            updateAdTypeFound();
        });

        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                model.setPlace_type("special");
            } else {
                model.setPlace_type("main");

            }
        });

        binding.flImg1.setOnClickListener(v -> {
            type = 1;
            openSheet();
        });
        binding.flImg2.setOnClickListener(v -> {
            type = 2;
            openSheet();
        });
        binding.flImg3.setOnClickListener(v -> {
            type = 3;
            openSheet();
        });
        binding.flImg4.setOnClickListener(v -> {
            type = 4;
            openSheet();
        });
        binding.btnNext.setOnClickListener(v -> {
            activity.navigateToStep2(model);
        });
    }

    private void updateAdTypeLost() {
        model.setAd_type("lost");

        binding.flAdLost.setBackgroundResource(R.drawable.small_rounded_primary);
        binding.iconLost.setColorFilter(ContextCompat.getColor(activity, R.color.white));
        binding.tvLost.setTextColor(ContextCompat.getColor(activity, R.color.white));
        binding.tvLostNote.setTextColor(ContextCompat.getColor(activity, R.color.white));

        binding.flAdFound.setBackgroundResource(R.drawable.small_stroke_rounded_white_grey0);
        binding.iconFound.setColorFilter(ContextCompat.getColor(activity, R.color.color3));
        binding.tvFound.setTextColor(ContextCompat.getColor(activity, R.color.color3));
        binding.tvFoundNote.setTextColor(ContextCompat.getColor(activity, R.color.grey7));

    }

    private void updateAdTypeFound() {
        model.setAd_type("found");

        binding.flAdFound.setBackgroundResource(R.drawable.small_rounded_color3);
        binding.iconFound.setColorFilter(ContextCompat.getColor(activity, R.color.white));
        binding.tvFound.setTextColor(ContextCompat.getColor(activity, R.color.white));
        binding.tvFoundNote.setTextColor(ContextCompat.getColor(activity, R.color.white));

        binding.flAdLost.setBackgroundResource(R.drawable.small_stroke_rounded_white_grey0);
        binding.iconLost.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent));
        binding.tvLost.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
        binding.tvLostNote.setTextColor(ContextCompat.getColor(activity, R.color.grey7));

    }

    private void addImages() {
        images.clear();
        images.addAll(uris.values());
        model.setImages(images);
    }

    private void openSheet() {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        DialogChooseImageBinding imageBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_choose_image, null, false);
        dialog.setView(imageBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        imageBinding.tvCamera.setOnClickListener(v -> {
            checkCameraPermission();
            dialog.dismiss();
        });
        imageBinding.tvGallery.setOnClickListener(v -> {
            checkPhotoPermission();
            dialog.dismiss();
        });
        imageBinding.tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void checkCameraPermission() {
        req = 1;
        String[] permissions = new String[]{BaseActivity.WRITE_REQ, BaseActivity.CAM_REQ};
        if (ContextCompat.checkSelfPermission(activity, BaseActivity.WRITE_REQ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, BaseActivity.CAM_REQ) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            this.permissions.launch(permissions);
        }
    }

    public void checkPhotoPermission() {
        req = 2;
        String[] permissions = new String[]{BaseActivity.READ_REQ};
        if (ContextCompat.checkSelfPermission(activity, BaseActivity.READ_REQ) == PackageManager.PERMISSION_GRANTED

        ) {
            openGallery();
        } else {
            this.permissions.launch(permissions);
        }
    }

    private void openCamera() {
        req = 1;
        outPutUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", getCameraOutPutFile());

        if (outPutUri != null) {
            Intent intentCamera = new Intent();
            intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            launcher.launch(intentCamera);
        } else {
            Toast.makeText(activity, "You don't allow to access photos", Toast.LENGTH_SHORT).show();
        }


    }

    private void openGallery() {
        req = 2;
        Intent intentGallery = new Intent();
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentGallery.setType("image/*");
        launcher.launch(Intent.createChooser(intentGallery, "Choose photos"));


    }

    private File getCameraOutPutFile() {
        File file = null;
        String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageName = "JPEG_" + stamp + "_";

        File appFile = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            file = File.createTempFile(imageName, ".jpg", appFile);
            imagePath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}