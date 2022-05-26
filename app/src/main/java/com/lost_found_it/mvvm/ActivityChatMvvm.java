package com.lost_found_it.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.lost_found_it.model.MessageModel;
import com.lost_found_it.model.MessagesDataModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityChatMvvm extends AndroidViewModel {
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<MessageModel>> onDataSuccess;
    private MutableLiveData<MessagesDataModel> roomId;

    private CompositeDisposable disposable = new CompositeDisposable();


    public ActivityChatMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<List<MessageModel>> onDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<MessagesDataModel> getRoomId() {
        if (roomId == null) {
            roomId = new MutableLiveData<>();
        }
        return roomId;
    }

    public void getChatMessages(UserModel userModel, String country, String ad_id, String room_id) {

        getIsLoading().setValue(true);
        Api.getService(Tags.base_url).getChatMessages("Bearer " + userModel.getData().getAccess_token(), country, ad_id, room_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MessagesDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<MessagesDataModel> response) {
                        getIsLoading().setValue(false);
                        Log.e("kdkkdkk", response.code() + " " + response.body().getCode());

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getCode() == 200) {
                                getRoomId().setValue(response.body());
                                onDataSuccess().setValue(response.body().getData().getMessages());
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("chatError", e.toString());
                    }
                });

    }

    public void addNewMessage(MessageModel messageModel) {
        onDataSuccess().getValue().add(messageModel);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
