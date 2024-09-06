package com.datb;

import android.annotation.SuppressLint;
import android.content.Context;

import okhttp3.OkHttpClient;

public class DatbServices  implements DatbCommunicator{

    private static final String LOG_TAG = "DatbSDK";
    @SuppressLint("StaticFieldLeak")
    private static DatbServices mInstance = null;
    private RegistrationService registrationService;
    private Context applicationContext;
    private OkHttpClient httpClient;
    private OBLocalSettings localSettings;

    private DatbServices() {

    }

    public static DatbServices getInstance(){
        if (mInstance == null)
        {
            mInstance = new DatbServices();
            mInstance.localSettings = new OBLocalSettings();
            mInstance.registrationService = RegistrationService.getInstance();
            mInstance.registrationService.setLocalSettings(mInstance.localSettings);
        }
        return mInstance;
    }

    private Context getApplicationContext() {
        return applicationContext;
    }




    @Override
    public void register(Context applicationContext, String partnerKey) {

    }

    @Override
    public void fetchRecommendations(DatbRequest request, DatbListener handler) {

    }

    @Override
    public void setTestMode(boolean testMode) {

    }

    @Override
    public void testRTB(boolean testRTB) {

    }

    @Override
    public void testLocation(String location) {

    }

    @Override
    public String getDatbAboutURL() {
        return "";
    }
}
