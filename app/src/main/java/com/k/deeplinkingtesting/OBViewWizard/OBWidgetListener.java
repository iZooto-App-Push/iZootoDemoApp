package com.k.deeplinkingtesting.OBViewWizard;

import com.outbrain.OBSDK.Entities.OBRecommendation;


public interface OBWidgetListener {
    void onRecommendationClick(OBRecommendation document);

    void onAdChoicesClick(String url);
}
