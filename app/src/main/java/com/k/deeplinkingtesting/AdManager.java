package com.k.deeplinkingtesting;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowMetrics;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener;
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener;
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdEventListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.rewarded.Reward;
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener;
import com.yandex.mobile.ads.rewarded.RewardedAdLoader;

import org.json.JSONObject;

public class AdManager {

    private static InterstitialAd mInterstitialAd;
    private static RewardedAd mRewardedAd;
    private static AppOpenAd mAppOpenAd;
    private static AdView mAdView;
    static com.google.android.gms.ads.nativead.NativeAd mNativeAd;

    private static com.yandex.mobile.ads.interstitial.InterstitialAd yInterstitialAd;
    private static com.yandex.mobile.ads.rewarded.RewardedAd yRewardedAd;
    private static com.yandex.mobile.ads.appopenad.AppOpenAd yAppOpenAd;
    private static com.yandex.mobile.ads.banner.BannerAdView yBannerAdView;
    static NativeAd yNativeAd;

    // load interstitial ad method
    static void loadInterstitialAd(Context context, JSONObject adUnitIds) {
        try {
            loadYInterstitialAd(context, adUnitIds);
        } catch (Exception ex) {
            Log.e("AdManager", "failed to load interstitial ad: " + ex.getMessage());
        }
    }

    // load Yandex interstitial ads
    private static void loadYInterstitialAd(Context context, JSONObject adUnitIds) {
        try {
            final String yInterstitialAdUnitId = adUnitIds.optString(Constants.Y_INTERSTITIAL_ADUNIT_ID);
            Log.d("ABC", "yInterstitialAdUnitId: " + yInterstitialAdUnitId);

            final AdRequestConfiguration adRequestConfiguration = new AdRequestConfiguration.Builder(yInterstitialAdUnitId).build();
            InterstitialAdLoader interstitialAdLoader = new InterstitialAdLoader(context);

            if (yInterstitialAd != null) {
                Log.d("ABC", "Yandex Interstitial Ad was already loaded.");
                return;
            }

            interstitialAdLoader.setAdLoadListener(new InterstitialAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull com.yandex.mobile.ads.interstitial.InterstitialAd interstitialAd) {
                    Log.d("ABC", "Yandex Interstitial onAdLoaded");

                    yInterstitialAd = interstitialAd;

                    interstitialAd.setAdEventListener(new InterstitialAdEventListener() {
                        @Override
                        public void onAdShown() {
                            Log.d("ABC", "Yandex Interstitial onAdShown");
                        }

                        @Override
                        public void onAdFailedToShow(@NonNull com.yandex.mobile.ads.common.AdError adError) {
                            Log.d("ABC", "Yandex Interstitial onAdFailedToShow");
                            yInterstitialAd = null;
                            loadGAMInterstitialAd(context, adUnitIds);
                        }

                        @Override
                        public void onAdDismissed() {
                            Log.d("ABC", "Yandex Interstitial onAdDismissed");
                            yInterstitialAd = null;
                        }

                        @Override
                        public void onAdClicked() {
                            Log.d("ABC", "Yandex Interstitial onAdClicked");

                        }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) {
                            Log.d("ABC", "Yandex Interstitial onAdImpression");

                        }
                    });

                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.d("ABC", "Yandex Interstitial onAdFailedToLoad");
                    yInterstitialAd = null;
                    loadGAMInterstitialAd(context, adUnitIds);

                }

            });

            interstitialAdLoader.loadAd(adRequestConfiguration);

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load Yandex interstitial ad: " + ex.getMessage());
        }
    }

    // load GAM interstitial ads
    private static void loadGAMInterstitialAd(Context context, JSONObject adUnitIds) {
        try {
            final String interstitialAdUnitId = adUnitIds.optString(Constants.INTERSTITIAL_ADUNIT_ID);
            final AdRequest adRequest = new AdRequest.Builder().build();

            Log.d("ABC", "interstitialAdUnitId: " + interstitialAdUnitId);

            if (mInterstitialAd != null) {
                Log.d("ABC", "Interstitial Ad was already loaded.");
                return;
            }

            InterstitialAd.load(context, interstitialAdUnitId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until

                    mInterstitialAd = interstitialAd;
                    Log.d("ABC", "Interstitial onAdLoaded");

                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d("ABC", "Interstitial Ad was clicked.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d("ABC", "Interstitial Ad dismissed fullscreen content.");
                            mInterstitialAd = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Called when ad fails to show.
                            Log.e("ABC", "Interstitial Ad failed to show fullscreen content.");
                            mInterstitialAd = null;
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d("ABC", "Interstitial Ad recorded an impression.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d("ABC", "Interstitial Ad showed fullscreen content.");
                        }
                    });

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.d("ABC", "GAM Interstitial error ");
                    mInterstitialAd = null;

                }
            });

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load GAM interstitial ad: " + ex.getMessage());
        }
    }

    // load rewarded ad method
    static void loadRewardedAd(Context context, JSONObject adUnitIds) {
        try {
            loadYRewardedAd(context, adUnitIds);
        } catch (Exception ex) {
            Log.e("AdManager", "failed to load rewarded ad: " + ex.getMessage());
        }
    }

    // load Yandex rewarded ads
    private static void loadYRewardedAd(Context context, JSONObject adUnitIds) {
        try {
            final String yRewardedAdUnitId = adUnitIds.optString(Constants.Y_REWARDED_ADUNIT_ID);
            Log.d("ABC", "yRewardedAdUnitId: " + yRewardedAdUnitId);

            final RewardedAdLoader rewardedAdLoader = new RewardedAdLoader(context);
            final AdRequestConfiguration adRequestConfiguration = new AdRequestConfiguration.Builder(yRewardedAdUnitId).build();

            if (yRewardedAd != null) {
                Log.d("ABC", "Yandex Rewarded Ad was already loaded.");
                return;
            }

            rewardedAdLoader.setAdLoadListener(new RewardedAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull com.yandex.mobile.ads.rewarded.RewardedAd rewardedAd) {
                    yRewardedAd = rewardedAd;

                    Log.d("ABC", "Yandex Rewarded Ad was loaded.");

                    rewardedAd.setAdEventListener(new com.yandex.mobile.ads.rewarded.RewardedAdEventListener() {

                        @Override
                        public void onRewarded(@NonNull Reward reward) {
                            Log.d("ABC", "Yandex Rewarded Ad was rewarded.");
                        }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) {
                            Log.d("ABC", "Yandex Rewarded Ad was impression.");

                        }

                        @Override
                        public void onAdDismissed() {
                            Log.d("ABC", "Yandex Rewarded Ad was dismissed.");
                            yRewardedAd = null;

                        }

                        @Override
                        public void onAdFailedToShow(@NonNull com.yandex.mobile.ads.common.AdError adError) {
                            Log.d("ABC", "Yandex Rewarded Ad failed to show.");
                            yRewardedAd = null;
                            loadGAMRewardedAd(context, adUnitIds);

                        }

                        @Override
                        public void onAdShown() {
                            Log.d("ABC", "Yandex Rewarded Ad was shown.");

                        }

                        @Override
                        public void onAdClicked() {
                            Log.d("ABC", "Yandex Rewarded Ad was clicked.");

                        }
                    });

                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.d("ABC", "Yandex Rewarded Ad failed to load.");
                    yRewardedAd = null;
                    loadGAMRewardedAd(context, adUnitIds);

                }
            });

            rewardedAdLoader.loadAd(adRequestConfiguration);

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load Yandex rewarded ad: " + ex.getMessage());
        }
    }

    // load GAM rewarded ads
    private static void loadGAMRewardedAd(Context context, JSONObject adUnitIds) {
        try {
            final String rewardedAdUnitId = adUnitIds.optString(Constants.REWARDED_ADUNIT_ID);
            Log.d("ABC", "rewardedAdUnitId: " + rewardedAdUnitId);

            final AdRequest adRequest = new AdRequest.Builder().build();

            if (mRewardedAd != null) {
                Log.d("ABC", "Rewarded Ad was already loaded.");
                return;
            }

            RewardedAd.load(context, rewardedAdUnitId, adRequest, new RewardedAdLoadCallback() {

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    mRewardedAd = rewardedAd;
                    Log.d("ABC", "Rewarded Ad was loaded.");

                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d("ABC", "Rewarded Ad was clicked.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d("ABC", "Rewarded Ad dismissed fullscreen content.");
                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Called when ad fails to show.
                            Log.e("ABC", "Rewarded Ad failed to show fullscreen content.");
                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d("ABC", "Rewarded Ad recorded an impression.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d("ABC", "Rewarded Ad showed fullscreen content.");
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error.
                    Log.d("ABC", "Rewarded Ad failed to load.");
                    mRewardedAd = null;
                }

            });

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load GAM rewarded ad: " + ex.getMessage());
        }
    }

    // load appOpen ad method
    static void loadAppOpenAd(Context context, JSONObject adUnitIds) {
        try {
            loadYAppOpenAd(context, adUnitIds);
        } catch (Exception ex) {
            Log.e("AdManager", "failed to load appOpen ad: " + ex.getMessage());
        }
    }

    // load Yandex appOpen ads
    private static void loadYAppOpenAd(Context context, JSONObject adUnitIds) {
        try {
            final String yAppOpenAdUnitId = adUnitIds.optString(Constants.Y_APPOPEN_ADUNIT_ID);
            Log.d("ABC", "yAppOpenAdUnitId: " + yAppOpenAdUnitId);

            final AdRequestConfiguration adRequestConfiguration = new AdRequestConfiguration.Builder(yAppOpenAdUnitId).build();
            final AppOpenAdLoader appOpenAdLoader = new AppOpenAdLoader(context);

            if (yAppOpenAd != null) {
                Log.d("ABC", "Yandex AppOpen Ad was already loaded.");
                return;
            }

            appOpenAdLoader.setAdLoadListener(new AppOpenAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull com.yandex.mobile.ads.appopenad.AppOpenAd appOpenAd) {
                    yAppOpenAd = appOpenAd;
                    Log.d("ABC", "Yandex AppOpen Ad was loaded.");

                    appOpenAd.setAdEventListener(new AppOpenAdEventListener() {
                        @Override
                        public void onAdShown() {
                            Log.d("ABC", "Yandex AppOpen Ad was shown.");

                        }

                        @Override
                        public void onAdFailedToShow(@NonNull com.yandex.mobile.ads.common.AdError adError) {
                            Log.d("ABC", "Yandex AppOpen Ad failed to show.");
                            yAppOpenAd = null;
                            loadGAMAppOpenAd(context, adUnitIds);

                        }

                        @Override
                        public void onAdDismissed() {
                            Log.d("ABC", "Yandex AppOpen Ad was dismissed.");
                            yAppOpenAd = null;

                        }

                        @Override
                        public void onAdClicked() {
                            Log.d("ABC", "Yandex AppOpen Ad was clicked.");

                        }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) {
                            Log.d("ABC", "Yandex AppOpen Ad was impression.");

                        }
                    });

                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.d("ABC", "Yandex AppOpen Ad failed to load.");
                    yAppOpenAd = null;
                    loadGAMAppOpenAd(context, adUnitIds);
                }

            });

            appOpenAdLoader.loadAd(adRequestConfiguration);

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load Yandex appOpen ad: ");
        }
    }

    // load GAM appOpen ad
    private static void loadGAMAppOpenAd(Context context, JSONObject adUnitIds) {
        try {
            final String appOpenAdUnitId = adUnitIds.optString(Constants.APPOPEN_ADUNIT_ID);
            Log.d("ABC", "appOpenAdUnitId: " + appOpenAdUnitId);

            final AdRequest adRequest = new AdRequest.Builder().build();

            if (mAppOpenAd != null) {
                Log.d("ABC", "AppOpen Ad was already loaded.");
                return;
            }

            AppOpenAd.load(context, appOpenAdUnitId, adRequest, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    Log.d("ABC", "AppOpen Ad was loaded.");
                    mAppOpenAd = ad;


                    ad.setFullScreenContentCallback(new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            Log.d("ABC", "AppOpen Ad dismissed fullscreen content.");
                            mAppOpenAd = null;

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            mAppOpenAd = null;
                            Log.d("ABC", "AppOpen Ad failed to show fullscreen content.");

                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d("ABC", "AppOpen Ad showed fullscreen content.");
                        }
                    });

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.d("ABC", "AppOpen Ad failed to load.");
                    mAppOpenAd = null;
                }

            });

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load GAM appOpen ad: " + ex.getMessage());
        }
    }

    // load banner Ads
    static void loadBannerAd(Activity activity, JSONObject adUnitIds, FrameLayout bannerContainer) {
        try {
            loadYBannerAd(activity, adUnitIds, bannerContainer);
        } catch (Exception ex) {
            Log.e("AdManager", "failed to load banner ad: " + ex.getMessage());
        }
    }

    // load Yandex banner ads
    private static void loadYBannerAd(Activity activity, JSONObject adUnitIds, FrameLayout bannerContainer) {
        try {
            final String yBannerAdUnitId = adUnitIds.optString(Constants.Y_BANNER_ADUNIT_ID);
            Log.d("ABC", "yBannerAdUnitId: " + yBannerAdUnitId);

            final com.yandex.mobile.ads.common.AdRequest adRequest = new com.yandex.mobile.ads.common.AdRequest.Builder().build();

            yBannerAdView = new BannerAdView(activity);
            yBannerAdView.setAdUnitId(yBannerAdUnitId);
            yBannerAdView.setAdSize(adaptiveYStickyBannerSize(activity, bannerContainer));
            yBannerAdView.setBannerAdEventListener(new BannerAdEventListener() {

                @Override
                public void onImpression(@Nullable ImpressionData impressionData) {
                    Log.d("ABC", "Yandex banner onImpression");

                }

                @Override
                public void onReturnedToApplication() {
                    Log.d("ABC", "Yandex banner onReturnedToApplication");

                }

                @Override
                public void onLeftApplication() {
                    Log.d("ABC", "Yandex banner onLeftApplication");

                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.d("ABC", "Yandex banner onAdFailedToLoad");
                    yBannerAdView = null;
                    loadGAMBannerAd(activity, adUnitIds, bannerContainer);

                }

                @Override
                public void onAdLoaded() {
                    Log.d("ABC", "Yandex banner onAdLoaded");
                    try {
                        if (yBannerAdView.getParent() != null) {
                            ((ViewGroup) yBannerAdView.getParent()).removeView(yBannerAdView);
                        }
                        bannerContainer.addView(yBannerAdView);

                    } catch (Exception ex) {
                        Log.d("ABC", "Yandex banner add to container error: " + ex.getMessage());
                    }
                }

                @Override
                public void onAdClicked() {
                    Log.d("ABC", "Yandex banner onAdClicked");
                }

            });

            yBannerAdView.loadAd(adRequest);

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load Yandex banner ad: " + ex.getMessage());
        }
    }

    // load GAM banner ads
    private static void loadGAMBannerAd(Activity activity, JSONObject adUnitIds, FrameLayout bannerContainer) {
        try {
            final String bannerAdUnitId = adUnitIds.optString(Constants.BANNER_ADUNIT_ID);
            Log.d("ABC", "bannerAdUnitId: " + bannerAdUnitId);

            final AdRequest adRequest = new AdRequest.Builder().build();

            mAdView = new AdView(activity);
            mAdView.setAdUnitId(bannerAdUnitId);
            mAdView.setAdSize(adaptiveGAMBannerSize(activity));
            mAdView.setAdListener(new AdListener() {

                @Override
                public void onAdClicked() {
                    Log.d("ABC", "GAM banner onAdClicked");

                }

                @Override
                public void onAdClosed() {
                    Log.d("ABC", "GAM banner onAdClosed");
                    mAdView = null;

                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    Log.d("ABC", "GAM banner onAdFailedToLoad");
                    mAdView = null;

                }

                @Override
                public void onAdImpression() {
                    Log.d("ABC", "GAM banner onAdImpression");

                }

                @Override
                public void onAdLoaded() {
                    Log.d("ABC", "GAM banner onAdLoaded");

                    try {
                        if (mAdView.getParent() != null) {
                            ((ViewGroup) mAdView.getParent()).removeView(mAdView);
                        }
                        bannerContainer.addView(mAdView);

                    } catch (Exception ex) {
                        Log.d("ABC", "GAM banner add to container error: " + ex.getMessage());
                    }

                }

                @Override
                public void onAdOpened() {
                    Log.d("ABC", "GAM banner onAdOpened");

                }

            });

            mAdView.loadAd(adRequest);

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load GAM banner ad: " + ex.getMessage());
        }
    }

    // yandex adaptive sticky banner size
    private static BannerAdSize adaptiveYStickyBannerSize(Context context, FrameLayout adContainer) {
        int adWidthPixels = adContainer.getWidth();

        if (adWidthPixels == 0) {
            adWidthPixels = context.getResources().getDisplayMetrics().widthPixels;
        }

        float density = context.getResources().getDisplayMetrics().density;
        int adWidth = Math.round(adWidthPixels / density);

        return BannerAdSize.stickySize(context, adWidth);
    }

    // yandex adaptive banner size
    static BannerAdSize adaptiveYInlineBannerSize(Context context, FrameLayout adContainer) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        int screenHeight = Math.round(displayMetrics.heightPixels / displayMetrics.density);

        int adWidthPixels = adContainer.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = displayMetrics.widthPixels;
        }

        int adWidth = Math.round(adWidthPixels / displayMetrics.density);

        int maxAdHeight = screenHeight / 2;

        return BannerAdSize.inlineSize(context, adWidth, maxAdHeight);
    }

    // mAdView adaptive size
    private static AdSize adaptiveGAMBannerSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int adWidthPixels = displayMetrics.widthPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity) context).getWindowManager().getCurrentWindowMetrics();
            adWidthPixels = windowMetrics.getBounds().width();
        }

        float density = displayMetrics.density;
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    // load native ads
    static void loadNativeAd(Context context, JSONObject adUnitIds) {
        try {
            loadYNativeAd(context, adUnitIds);
        } catch (Exception ex) {
            Log.e("AdManager", "failed to load native ad: " + ex.getMessage());
        }
    }

    // load Yandex native ads
    private static void loadYNativeAd(Context context, JSONObject adUnitIds) {
        try {
            final String yNativeAdUnitId = adUnitIds.optString(Constants.Y_NATIVE_ADUNIT_ID);
            Log.d("ABC", "yNativeAdUnitId: " + yNativeAdUnitId);

            final NativeAdLoader nativeAdLoader = new NativeAdLoader(context);
            final NativeAdRequestConfiguration nativeAdRequestConfiguration = new NativeAdRequestConfiguration.Builder(yNativeAdUnitId).build();

            if (yNativeAd != null) {
                Log.d("ABC", "Yandex native ad was already loaded.");
                return;
            }

            nativeAdLoader.setNativeAdLoadListener(new NativeAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull NativeAd nativeAd) {
                    yNativeAd = nativeAd;
                    Log.d("ABC", "Yandex native ad was loaded.");

                    nativeAd.setNativeAdEventListener(new NativeAdEventListener() {
                        @Override
                        public void onAdClicked() {
                            Log.d("ABC", "Yandex native ad was clicked.");

                        }

                        @Override
                        public void onLeftApplication() {
                            Log.d("ABC", "Yandex native ad was left application.");

                        }

                        @Override
                        public void onReturnedToApplication() {
                            Log.d("ABC", "Yandex native ad was returned to application.");

                        }

                        @Override
                        public void onImpression(@Nullable ImpressionData impressionData) {
                            Log.d("ABC", "Yandex native ad was impression.");

                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.d("ABC", "Yandex native ad failed to load.");
                    yNativeAd = null;
                    loadGAMNativeAd(context, adUnitIds);
                }

            });

            nativeAdLoader.loadAd(nativeAdRequestConfiguration);

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load Yandex native ad: " + ex.getMessage());
        }
    }

    // load GAM native ads
    private static void loadGAMNativeAd(Context context, JSONObject adUnitIds) {
        try {
            final String nativeAdUnitId = adUnitIds.optString(Constants.NATIVE_ADUNIT_ID);
            Log.d("ABC", "nativeAdUnitId: " + nativeAdUnitId);

            final AdManagerAdRequest adRequestConfiguration = new AdManagerAdRequest.Builder().build();

            if (mNativeAd != null) {
                Log.d("ABC", "GAM native ad was already loaded.");
                return;
            }

            AdLoader adLoader = new AdLoader.Builder(context, nativeAdUnitId)
                    .forNativeAd(nativeAd -> {
                        Log.d("ABC", "GAM native ad was loaded.");
                        mNativeAd = nativeAd;

                    })

                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d("ABC", "GAM native ad failed to load.");
                            mNativeAd = null;
                        }
                    })

                    .build();

            adLoader.loadAd(adRequestConfiguration);

        } catch (Exception ex) {
            Log.e("AdManager", "failed to load GAM native ad: " + ex.getMessage());
        }

    }

    static InterstitialAd getInterstitialAd() {
        return mInterstitialAd;
    }

    static RewardedAd getRewardedAd() {
        return mRewardedAd;
    }

    static AppOpenAd getAppOpenAd() {
        return mAppOpenAd;
    }

    static com.yandex.mobile.ads.interstitial.InterstitialAd getYInterstitialAd() {
        return yInterstitialAd;
    }

    static com.yandex.mobile.ads.rewarded.RewardedAd getYRewardedAd() {
        return yRewardedAd;
    }

    static com.yandex.mobile.ads.appopenad.AppOpenAd getYAppOpenAd() {
        return yAppOpenAd;
    }

    static BannerAdView getBannerAdView() {
        return yBannerAdView;
    }

    static AdView getAdView() {
        return mAdView;
    }

    static NativeAd getYNativeAd() {
        return yNativeAd;
    }

    static com.google.android.gms.ads.nativead.NativeAd getNativeAd() {
        return mNativeAd;
    }

}
