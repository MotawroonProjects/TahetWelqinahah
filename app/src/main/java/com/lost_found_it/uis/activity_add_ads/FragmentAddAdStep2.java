package com.lost_found_it.uis.activity_add_ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lost_found_it.R;
import com.lost_found_it.adapter.SpinnerCategoryAdapter;
import com.lost_found_it.adapter.SpinnerGovernorateAdapter;
import com.lost_found_it.adapter.SpinnerSubCategoryAdapter;
import com.lost_found_it.background_service.ServicePostAd;
import com.lost_found_it.databinding.FragmentPostAddStep2Binding;
import com.lost_found_it.model.AddAdModel;
import com.lost_found_it.model.CategoryModel;
import com.lost_found_it.model.CityModel;
import com.lost_found_it.model.GovernorateModel;
import com.lost_found_it.model.SubCategoryModel;
import com.lost_found_it.mvvm.FragmentAddAdsMvvm;
import com.lost_found_it.uis.activity_about_app.AboutAppActivity;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_base.FragmentMapTouchListener;
import com.lost_found_it.uis.activity_cities.CitiesActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentAddAdStep2 extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private FragmentPostAddStep2Binding binding;
    private FragmentAddAdsMvvm mvvm;
    private AddAdsActivity activity;
    private AddAdModel model;
    private FragmentMapTouchListener fragmentMapTouchListener;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double lat;
    private double lng;
    private String address = "";
    private ActivityResultLauncher<String> permission;
    private SpinnerCategoryAdapter spinnerCategoryAdapter;
    private SpinnerSubCategoryAdapter spinnerSubCategoryAdapter;
    private SpinnerGovernorateAdapter spinnerGovernorateAdapter;
    private ActivityResultLauncher<Intent> launcher;
    private String governorate_id = null;

    @SuppressLint("MissingPermission")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AddAdsActivity) context;
        permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                mMap.setMyLocationEnabled(false);
                initGoogleApi();
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                CityModel cityModel = (CityModel) result.getData().getSerializableExtra("data");
                binding.setCityModel(cityModel);
                model.setCity_id(cityModel.getId());
            }
        });
    }

    public static FragmentAddAdStep2 newInstance() {
        return new FragmentAddAdStep2();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_add_step2, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mvvm = ViewModelProviders.of(this).get(FragmentAddAdsMvvm.class);


        binding.setModel(model);

        mvvm.getOnCategoryDataSuccess().observe(activity, list -> {
            if (spinnerCategoryAdapter != null) {

                if (list.size() > 0) {
                    spinnerCategoryAdapter.updateList(list);
                    if (model.getAd_id().isEmpty()) {
                        if (list.size() > 0) {
                            CategoryModel categoryModel = list.get(0);
                            model.setCategory_id(categoryModel.getId());
                            if (categoryModel.getSub_categories().size() > 0) {
                                binding.llSubCategory.setVisibility(View.VISIBLE);

                                SubCategoryModel subCategoryModel = categoryModel.getSub_categories().get(0);
                                model.setSub_category_id(subCategoryModel.getId());

                                model.setHasSubCategory(true);

                                if (spinnerSubCategoryAdapter != null) {
                                    spinnerSubCategoryAdapter.updateList(categoryModel.getSub_categories());

                                }
                            } else {
                                binding.llSubCategory.setVisibility(View.GONE);
                                model.setSub_category_id("");
                                model.setHasSubCategory(false);
                            }


                        }
                    } else {
                        int pos = getCategoryPos(model.getCategory_id());
                        binding.spinnerCategory.setSelection(pos);
                        if (!model.getSub_category_id().isEmpty()) {
                            if (mvvm.getOnCategoryDataSuccess().getValue() != null) {
                                CategoryModel categoryModel = mvvm.getOnCategoryDataSuccess().getValue().get(pos);
                                int sub_category_pos = getSubCategoryPos(model.getSub_category_id(), categoryModel.getSub_categories());
                                binding.spinnerSubCategory.setSelection(sub_category_pos);
                            }
                        }

                    }

                }

            }
        });

        mvvm.getOnGovernorateDataSuccess().observe(activity, list -> {
            spinnerGovernorateAdapter.updateList(list);
            governorate_id = null;
            if (model.getAd_id().isEmpty()) {
                if (list.size() > 0) {
                    GovernorateModel governorateModel = list.get(0);
                    governorate_id = governorateModel.getId();
                    model.setCity_id("");
                    binding.setCityModel(null);


                }

            } else {
                int pos = getCityPos(model.getCity_id());
                //binding.spinnerCity.setSelection(pos);


            }
        });

        spinnerCategoryAdapter = new SpinnerCategoryAdapter(activity);

        binding.spinnerCategory.setAdapter(spinnerCategoryAdapter);

        spinnerSubCategoryAdapter = new SpinnerSubCategoryAdapter(activity);
        binding.spinnerSubCategory.setAdapter(spinnerSubCategoryAdapter);

        spinnerGovernorateAdapter = new SpinnerGovernorateAdapter(activity);
        binding.spinnerGovernorate.setAdapter(spinnerGovernorateAdapter);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryModel categoryModel = (CategoryModel) parent.getSelectedItem();
                model.setCategory_id(categoryModel.getId());

                if (categoryModel.getSub_categories().size() > 0) {
                    binding.llSubCategory.setVisibility(View.VISIBLE);
                    SubCategoryModel subCategoryModel = categoryModel.getSub_categories().get(0);
                    model.setSub_category_id(subCategoryModel.getId());

                    model.setHasSubCategory(true);

                    if (spinnerSubCategoryAdapter != null) {
                        spinnerSubCategoryAdapter.updateList(categoryModel.getSub_categories());

                    }
                } else {
                    binding.llSubCategory.setVisibility(View.GONE);

                    spinnerSubCategoryAdapter.updateList(new ArrayList<>());
                    model.setSub_category_id("");
                    model.setHasSubCategory(false);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubCategoryModel subCategoryModel = (SubCategoryModel) parent.getSelectedItem();
                model.setSub_category_id(subCategoryModel.getId());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerGovernorate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GovernorateModel governorateModel = (GovernorateModel) parent.getSelectedItem();
                governorate_id = governorateModel.getId();
                model.setCity_id("");
                binding.setCityModel(null);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.setAgree_terms(isChecked);
            if (isChecked && model != null && model.getAd_id().isEmpty()) {
                Intent intent = new Intent(activity, AboutAppActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);


            }
        });
        binding.btnPublish.setOnClickListener(v -> {
            String action = "add";
            if (model.getAd_id().isEmpty()) {
                action = "add";
            } else {
                action = "update";

            }
            Intent intent = new Intent(activity, ServicePostAd.class);
            intent.putExtra("action", action);
            intent.putExtra("country", getUserSetting().getCountry());
            intent.putExtra("data", model);
            activity.startService(intent);
            activity.finish();
            Toast.makeText(activity, R.string.posting_ad, Toast.LENGTH_SHORT).show();
        });

        binding.llCity.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CitiesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("governorate_id",governorate_id);
            launcher.launch(intent);
        });
        binding.btnBack.setOnClickListener(v -> {
            activity.navigateToStep1();
        });

    }

    private int getCategoryPos(String category_id) {
        if (mvvm.getOnCategoryDataSuccess().getValue() != null) {
            List<CategoryModel> list = mvvm.getOnCategoryDataSuccess().getValue();
            for (int index = 0; index < list.size(); index++) {
                if (list.get(index).getId().equals(category_id)) {
                    return index;
                }
            }
        }
        return 0;
    }


    private int getSubCategoryPos(String sub_category_id, List<SubCategoryModel> list) {
        if (mvvm.getOnCategoryDataSuccess().getValue() != null) {
            for (int index = 0; index < list.size(); index++) {
                if (list.get(index).getId().equals(sub_category_id)) {
                    return index;
                }
            }
        }
        return 0;
    }

    private int getCityPos(String city_id) {
        /*if (mvvm.getOnGovernorateDataSuccess().getValue() != null) {
            List<CityModel> list = mvvm.getOnGovernorateDataSuccess().getValue();
            for (int index = 0; index < list.size(); index++) {
                if (list.get(index).getId().equals(city_id)) {
                    return index;
                }
            }
        }*/
        return 0;
    }

    public void updateModel(AddAdModel model) {
        this.model = model;
        String phone_code;
        if (getUserSetting().getCountry().equals("sa")) {
            phone_code = "+966";

        } else if (getUserSetting().getCountry().equals("eg")) {
            phone_code = "+20";

        } else {
            phone_code = "+971";
        }
        model.setPhone_code(phone_code);
        binding.setModel(model);
        setUpMapFragment();
        mvvm.getCategories(getUserSetting().getCountry());
        mvvm.getGovernorates(getUserSetting().getCountry(),getLang());


        if (model != null && !model.getAd_id().isEmpty()) {
            binding.checkbox.setChecked(true);
        }

    }

    private void setUpMapFragment() {
        fragmentMapTouchListener = (FragmentMapTouchListener) getChildFragmentManager().findFragmentById(R.id.map);
        if (fragmentMapTouchListener != null) {
            fragmentMapTouchListener.getMapAsync(this);

        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(activity, BaseActivity.fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            permission.launch(BaseActivity.fineLocPerm);
        } else {
            mMap.setMyLocationEnabled(false);
            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            fragmentMapTouchListener.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));

            checkPermission();
            mMap.setOnMapClickListener(latLng -> {
                addMarker(latLng);
                getAddress(latLng.latitude, latLng.longitude);
            });
        }
    }

    private void addMarker(LatLng latLng) {
        model.setLat(latLng.latitude);
        model.setLng(latLng.longitude);
        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else {
            marker.setPosition(latLng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    startLocationUpdate();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(activity, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        });


    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        getAddress(lat, lng);
        addMarker(new LatLng(lat, lng));
        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            startLocationUpdate();
        }
    }

    private void getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addressList = new ArrayList<>();
        geocoder = new Geocoder(activity, Locale.ENGLISH);
        try {
            addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                if (address != null) {
                    this.address = (address.getAddressLine(0) != null ? address.getAddressLine(0) : "") + (address.getSubLocality() != null ? "-" + address.getSubLocality() : "") + (address.getLocality() != null ? address.getLocality() : "") + (address.getSubAdminArea() != null ? address.getSubAdminArea() : "") + (address.getAdminArea() != null ? address.getAdminArea() : "") + (address.getCountryName() != null ? address.getCountryName() : "");
                } else {
                    this.address = "UnKnown";
                }
            } else {
                address = "UnKnown";
            }


            binding.tvAddress.setText(address);
            model.setAddress(address);
            model.setLng(lat);
            model.setLng(lng);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}