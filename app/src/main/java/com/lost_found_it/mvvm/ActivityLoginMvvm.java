package com.lost_found_it.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.lost_found_it.R;
import com.lost_found_it.model.LoginModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.share.Common;
import com.lost_found_it.uis.activity_home.HomeActivity;
import com.lost_found_it.uis.activity_login.LoginActivity;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityLoginMvvm extends AndroidViewModel {
    private MutableLiveData<String> onSmsCodeSuccess;
    private MutableLiveData<String> onTimeStarted;
    private MutableLiveData<Boolean> onCanResend;
    private MutableLiveData<UserModel> onUserDataSuccess;
    private FirebaseAuth mAuth;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityLoginMvvm(@NonNull Application application) {
        super(application);


    }

    public MutableLiveData<String> getSmsCode() {
        if (onSmsCodeSuccess == null) {
            onSmsCodeSuccess = new MutableLiveData<>();
        }
        return onSmsCodeSuccess;
    }

    public MutableLiveData<String> getTime() {
        if (onTimeStarted == null) {
            onTimeStarted = new MutableLiveData<>();
        }
        return onTimeStarted;
    }

    public MutableLiveData<Boolean> canResend() {
        if (onCanResend == null) {
            onCanResend = new MutableLiveData<>();
        }
        return onCanResend;
    }

    public MutableLiveData<UserModel> getUserData() {
        if (onUserDataSuccess == null) {
            onUserDataSuccess = new MutableLiveData<>();
        }
        return onUserDataSuccess;
    }


    public void sendSmsCode(LoginModel model, LoginActivity activity) {
        startTimer();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String smsCode = phoneAuthCredential.getSmsCode();
                Log.e("sms", smsCode + "");
                getSmsCode().setValue(smsCode);
                checkValidCode(smsCode, activity, model);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                verificationId = verification_id;
                ActivityLoginMvvm.this.forceResendingToken = forceResendingToken;
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("dkdkdk", e.toString());
                if (e.getMessage() != null) {
                } else {

                }
            }
        };
        mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = new PhoneAuthOptions.Builder(mAuth)
                .setForceResendingToken(forceResendingToken)
                .setActivity(activity)
                .setPhoneNumber(model.getPhone_code() + model.getPhone())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallBack)
                .build();


        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void reSendSmsCode(LoginModel model, LoginActivity activity) {
        startTimer();
        sendSmsCode(model, activity);

    }

    private void startTimer() {
        canResend().setValue(false);
        Observable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        long diff = 60 - aLong;
                        int min = ((int) diff / 60);
                        int sec = ((int) diff % 60);
                        String time = String.format(Locale.ENGLISH, "%02d:%02d", min, sec);
                        getTime().setValue(time);


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("onErrorVerCode", e.getMessage() + "_");
                    }

                    @Override
                    public void onComplete() {
                        getTime().setValue("00:00");
                        canResend().setValue(true);

                    }
                });

    }

    public void checkValidCode(String code, LoginActivity activity, LoginModel loginModel) {
        Log.e("code", code + "__");
        ProgressDialog dialog = Common.createProgressDialog(activity, activity.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        login(activity, loginModel, dialog);
                    }).addOnFailureListener(e -> {
                        dialog.dismiss();
                        if (e.getMessage() != null) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();

                        } else {

                            dialog.dismiss();
                        }
                    });
        } else {
            Toast.makeText(activity, "Wait sms maybe take a few minutes", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            // Toast.makeText(context, "wait sms", Toast.LENGTH_SHORT).show();
        }

    }


    public void login(Context context, LoginModel loginMode, ProgressDialog dialogl) {
        dialogl.dismiss();
        /*  ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).login(phone_code, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<UserModel> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            Log.e("status", response.body().getStatus() + "");
                            if (response.body() != null) {

                                if (response.body().getStatus() == 200) {

                                    getUserData().setValue(response.body());
                                } else if (response.body().getStatus() == 400) {
                                    getUserData().setValue(null);

                                }
                            }

                        } else {
                            try {
                                Log.e("error", response.errorBody().string() + "__" + response.code());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();

                    }
                });*/
    }


    public void stopTimer() {
        disposable.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
