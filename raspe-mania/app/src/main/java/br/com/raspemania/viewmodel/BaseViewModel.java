package br.com.raspemania.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class BaseViewModel extends ViewModel {

    static String TAG = "BaseViewModel";

    public MutableLiveData<String> error;
    //public MutableLiveData<String> sucess;

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
    }
}
