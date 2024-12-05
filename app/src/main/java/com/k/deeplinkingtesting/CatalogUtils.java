package com.k.deeplinkingtesting;

import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;


/**
 * Created by odedre on 10/18/17.
 */

public class CatalogUtils {

    public static void openURLInBrowser(String url, final Context ctx) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(ctx, Uri.parse(url));
    }
}
