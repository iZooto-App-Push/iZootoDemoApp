package com.k.deeplinkingtesting.admob;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DATBErrorReporting {

    private static DATBErrorReporting mInstance = null;

    private static final String ERROR_REPORTING_URL = "https://aerr.izooto.com/aerr";
    private static final String SDK_ERROR_NAME = "ANDROID_SDK_ERROR";

    private String pid;
    private String token;
    private String izRequestUrlParamValue;

    private String appVersion;
    private OkHttpClient httpClient;

    private DATBErrorReporting() {

    }
    public static DATBErrorReporting getInstance(){
        if (mInstance == null)
        {
            throw new RuntimeException("OBErrorReporting Not initialized, call OBErrorReporting.init() before calling getInstance");
        }
        return mInstance;
    }
    public static void init(Context appContext) {
        if (mInstance == null) {
            mInstance = new DATBErrorReporting();
            mInstance.httpClient = new OkHttpClient.Builder().build();
            mInstance.pid = "49267";

            mInstance.appVersion = "";

            try {
                PackageInfo pInfo = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
                mInstance.appVersion = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException ignored) {

            }
        }
    }
    public void reportErrorToServer(String errorMessage) {

        final String formData = prepareErrorReportingFormData(errorMessage);
        RequestBody body = RequestBody.create(formData, MediaType.get("application/x-www-form-urlencoded"));
Log.e("RequestBody",body.toString());
        Request request = new Request.Builder()
                .url(ERROR_REPORTING_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.err.println("Error in response: " + response.code());
                }
            }
        });
    }

    private String prepareErrorReportingFormData(String msg) {
        StringBuilder formData = new StringBuilder();

        try {
            formData.append("name=").append(SDK_ERROR_NAME);

            // Create a JSONObject to hold extra params
            JSONObject extraParams = new JSONObject();

            // Add parameters to extraParams (with null checks)
            extraParams.put("partnerKey", (pid != null) ? pid : "(null)");
            extraParams.put("token", token);
            extraParams.put("sdk_version", "1.0.0");  // Replace with actual SDK version
            extraParams.put("dm", URLEncoder.encode(Build.MODEL, "utf-8"));
            extraParams.put("app_ver", appVersion);
            extraParams.put("dosv", URLEncoder.encode(String.valueOf(Build.VERSION.SDK_INT), "utf-8"));
            extraParams.put("rand", Integer.toString(new Random().nextInt(10000)));

            // Add extra params as URL-encoded string
            formData.append("&extra=").append(URLEncoder.encode(extraParams.toString(), "utf-8"));

            // Additional optional parameters (URL, SID, PID)
            if (izRequestUrlParamValue != null) {
                formData.append("&url=").append(URLEncoder.encode(izRequestUrlParamValue, "utf-8"));
            }

            // Error message
            formData.append("&message=").append(URLEncoder.encode(msg != null ? msg : "(null)", "utf-8"));
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return formData.toString();
    }







}
