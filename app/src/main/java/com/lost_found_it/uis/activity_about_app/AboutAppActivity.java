package com.lost_found_it.uis.activity_about_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.lost_found_it.R;
import com.lost_found_it.databinding.ActivityAboutAppBinding;
import com.lost_found_it.model.SettingDataModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseActivity;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AboutAppActivity extends BaseActivity {
    private ActivityAboutAppBinding binding;
    private String type;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_app);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

    }

    private void initView() {
        String title = "";
        if (type.equals("1")) {
            title = getString(R.string.terms_and_conditions);
        } else {
            title = getString(R.string.privacy_policy);
        }
        setUpToolbar(binding.toolbar, title, R.color.white, R.color.black);

        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                binding.progBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progBar.setVisibility(View.GONE);

            }
        });
        getSettings();
    }

    private void getSettings(){
        Api.getService(Tags.base_url)
                .getSettings(getUserSetting().getCountry())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SettingDataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<SettingDataModel> response) {
                        if (response.isSuccessful()){
                            if (response.body()!=null){
                                if (response.body().getCode()==200){
                                    SettingDataModel.Data settingModel = response.body().getData();

                                    if (type.equals("1")){

                                        if (settingModel != null && settingModel.getTerms_condition() != null) {
                                            String endPoint = "";
                                            if (!settingModel.getTerms_condition().isEmpty()){
                                                endPoint = "webView?type=terms" ;
                                            }
                                            String url = Tags.base_url + endPoint;
                                            binding.webView.loadUrl(url);

                                        } else {
                                            Toast.makeText(AboutAppActivity.this, R.string.inv_link, Toast.LENGTH_SHORT).show();
                                        }


                                    }else {
                                        if (settingModel != null && settingModel.getPrivacy_policy() != null) {
                                            String endPoint = "";
                                            if (!settingModel.getPrivacy_policy().isEmpty()){
                                                endPoint = "webView?type=privacy";
                                            }
                                            String url = Tags.base_url + endPoint;
                                            binding.webView.loadUrl(url);
                                        } else {

                                            Toast.makeText(AboutAppActivity.this, R.string.inv_link, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error",e.getMessage());
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}