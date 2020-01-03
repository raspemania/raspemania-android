package br.com.raspemania.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends BaseViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        //mText = new MutableLiveData<>();
        //mText.setValue("Raspe Mania");
    }

    public LiveData<String> getText() {
        return mText;
    }
}