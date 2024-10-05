package com.palgao.menu.modules.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// HomeViewModel.java
public class WebViewViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mUrl;

    public WebViewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Attompo.Shop");
        mUrl = new MutableLiveData<>();
        mUrl.setValue("https://www.google.com/maps/place/Attenpo.Shop/@19.3693714,-99.1801109,17z/data=!4m14!1m7!3m6!1s0x85d1ff40733c47e1:0x81ac29fb4b4ad5bb!2sAttenpo.Shop!8m2!3d19.3700643!4d-99.1796933!16s%2Fg%2F11w1pvq4s5!3m5!1s0x85d1ff40733c47e1:0x81ac29fb4b4ad5bb!8m2!3d19.3700643!4d-99.1796933!16s%2Fg%2F11w1pvq4s5?entry=ttu/");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getUrl() {
        return mUrl;
    }
}
