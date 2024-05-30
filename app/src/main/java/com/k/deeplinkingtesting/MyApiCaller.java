package com.k.deeplinkingtesting;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyApiCaller extends AsyncTask<Void, Void, String[]> {
    private static final String TAG = "MyApiCaller";
    
    private final OkHttpClient client = new OkHttpClient();
    private final String[] apiUrls;
    private final ApiResponseListener listener;

    public MyApiCaller(String[] apiUrls, ApiResponseListener listener) {
        this.apiUrls = apiUrls;
        this.listener = listener;
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        String[] responses = new String[apiUrls.length];

        for (int i = 0; i < apiUrls.length; i++) {
            String url = apiUrls[i];
            try {
                responses[i] = fetchDataFromApi(url);
            } catch (IOException e) {
                Log.e(TAG, "Error fetching data from API: " + e.getMessage());
            }
        }

        return responses;
    }

    @Override
    protected void onPostExecute(String[] responses) {
        listener.onResponseReceived(responses);
    }

    private String fetchDataFromApi(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public interface ApiResponseListener {
        void onResponseReceived(String[] responses);
    }
}
