package com.datb;

import android.content.Context;

public interface DatbCommunicator {
    void register(Context applicationContext, String partnerKey);

    void fetchRecommendations(DatbRequest request, DatbListener handler);


    void setTestMode(boolean testMode);

    void testRTB(boolean testRTB);

    void testLocation(String location);

    String getDatbAboutURL();
}
