package com.k.deeplinkingtesting.iron

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.BuildConfig
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.ads.nativead.LevelPlayMediaView
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAd
import com.ironsource.mediationsdk.ads.nativead.NativeAdLayout
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.model.Placement
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.databinding.ActivityIronBinding
import com.unity3d.mediation.LevelPlay
import com.unity3d.mediation.LevelPlayAdSize
import com.unity3d.mediation.LevelPlayInitRequest
import com.unity3d.mediation.banner.LevelPlayBannerAdView
import com.unity3d.mediation.interstitial.LevelPlayInterstitialAd
import com.unity3d.mediation.rewarded.LevelPlayReward
import com.unity3d.mediation.rewarded.LevelPlayRewardedAd


private const val TAG = "IronSourceActivity"
//private var APP_KEY = "85460dcd"
//private var INTERSTITIAL_AD_UNIT_ID = "aeyqi3vqlv6o8sh9"
//private var BANNER_AD_UNIT_ID = "thnfvcsog13bhn08"

private var APP_KEY = ""
private var INTERSTITIAL_AD_UNIT_ID = ""
private var BANNER_AD_UNIT_ID = ""


class IronSourceActivity : AppCompatActivity(R.layout.activity_iron), ActivityListener {

    private var rewardedVideoPlacementInfo: Placement? = null
    private var levelPlayRewardedInfo: LevelPlayReward? = null
    private var interstitialAd: LevelPlayInterstitialAd? = null
    private var nativeAd: LevelPlayNativeAd? = null
    private var rewardedAd: LevelPlayRewardedAd? = null
    private var bannerParentLayout: FrameLayout? = null
    private var bannerAd: LevelPlayBannerAdView? = null
    private lateinit var binding: ActivityIronBinding
    private val mNativeAds: MutableList<LevelPlayNativeAd> = mutableListOf()


    companion object {
        internal fun logCallbackName(tag: String, fmt: String) {
            Log.d(tag, String.format("%s $fmt", getMethodName()))
        }

        private fun getMethodName(): String {
            return Thread.currentThread().stackTrace
                .takeIf { it.size >= 5 }
                ?.let { it[4].methodName }
                ?: ""
        }
    }

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIronBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = resources.getString(R.string.title)
        APP_KEY = resources.getString(R.string.apiKey)
        BANNER_AD_UNIT_ID = resources.getString(R.string.banner_ad_iron)
        INTERSTITIAL_AD_UNIT_ID = resources.getString(R.string.interstitial_ad_iron)
        setupUI()
        setupIronSourceSdk()
    }

    override fun onResume() {
        super.onResume()
        // call the IronSource onResume method
        IronSource.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        // call the IronSource onPause method
        IronSource.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerAd?.destroy()
        nativeAd?.destroyAd()
    }
    //endregion

    //region Initialization Methods
    private fun setupUI() {

        val versionTV = findViewById<TextView>(R.id.version_txt)
        val versionName = getVersionName()

        "${resources.getString(R.string.version)} $versionName".also {
            versionTV.text = it
        }
        bannerParentLayout = findViewById(R.id.banner_footer)
    }

    private fun setupIronSourceSdk() {
        // The integrationHelper is used to validate the integration.
        // Remove the integrationHelper before going live!
        if (BuildConfig.DEBUG) {
            IntegrationHelper.validateIntegration(this)
        }

        // Before initializing any of our legacy products (Rewarded video, Interstitial or Banner) you must set
        // their listeners. Take a look at each of these listeners method and you will see that they each implement a product
        // protocol. This is our way of letting you know what's going on, and if you don't set the listeners
        // we will not be able to communicate with you.
        // We're passing 'this' to our listeners because we want
        // to be able to enable/disable buttons to match ad availability.
        IronSource.setLevelPlayRewardedVideoListener(RewardedVideoAdListener(this))
        IronSource.addImpressionDataListener(ImpressionDataListener())

        // After setting the listeners you can go ahead and initialize the SDK.
        // Once the initialization callback is returned you can start loading your ads
        val legacyAdFormats = listOf(LevelPlay.AdFormat.REWARDED,LevelPlay.AdFormat.NATIVE_AD)
        val initRequest = LevelPlayInitRequest.Builder(APP_KEY)
            .withLegacyAdFormats(legacyAdFormats)
            .build()

        log("init ironSource SDK with appKey: $APP_KEY")
        LevelPlay.init(this, initRequest, InitializationListener(this))

        // Scroll down the file to find out what happens when you tap a button...
    }
    //endregion
    fun loadRewardedButtonTapped(view: View){
        rewardedAd?.loadAd()
    }

    fun showRewardedButtonTapped(view: View){
        if (rewardedAd?.isAdReady() == true){
            showInterstitialAdWithPlacement(resources.getString(R.string.rewarded_placement))
        }else{
            // load a new ad before calling show
        }

    }

    //region Interstitial Methods
    override fun createInterstitialAd() {
        interstitialAd = LevelPlayInterstitialAd(INTERSTITIAL_AD_UNIT_ID)
        interstitialAd?.setListener(InterstitialAdListener(this))
        setEnablementForButton(ButtonIdentifiers.LOAD_INTERSTITIAL_BUTTON_IDENTIFIER, true)
    }

    fun loadInterstitialButtonTapped(view: View) {
        // This will load an Interstitial ad
        log("loadAd for interstitial")
        interstitialAd?.loadAd()
    }

    private fun showInterstitialAdWithPlacement(placementName: String) {
        // Check that ad is ready and that the placement is not capped
        if (interstitialAd!!.isAdReady() && !LevelPlayInterstitialAd.isPlacementCapped(placementName)) {
            // Show ad with placement
            log("showAd for interstitial")
            interstitialAd!!.showAd(this, placementName)
        }else{
            // Unlike Interstitial Videos there are no placements.
            interstitialAd?.showAd(this)
        }

        if (rewardedAd!!.isAdReady() && !LevelPlayInterstitialAd.isPlacementCapped(placementName)) {
            // Show ad with placement
            log("showAd for interstitial")
            rewardedAd!!.showAd(this, placementName)
        }else{
            // Unlike Interstitial Videos there are no placements.
            rewardedAd?.showAd(this)
        }
    }

    fun showInterstitialButtonTapped(view: View) {
        // It is advised to make sure there is available ad that isn't capped before attempting to show it
        if (interstitialAd?.isAdReady() == true) {
            // This will present the Interstitial.
            showInterstitialAdWithPlacement(resources.getString(R.string.interstitial_placement))
           // showInterstitialAdWithPlacement("")
        } else {
            // load a new ad before calling show
        }
    }
    //endregion

    //region Banner Methods
    override fun createBannerAd() {
        // choose banner size

        // 1. recommended - Adaptive ad size that adjusts to the screen width
        val adSize = LevelPlayAdSize.createAdaptiveAdSize(this)

        // 2. Adaptive ad size using fixed width ad size
//        val  adSize = LevelPlayAdSize.createAdaptiveAdSize(this, 400)

        // 3. Specific banner size - BANNER, LARGE, MEDIUM_RECTANGLE
//        val adSize = LevelPlayAdSize.BANNER

        // Create the banner view and set the ad unit id and ad size
        adSize?.let {
            bannerAd = LevelPlayBannerAdView(this, BANNER_AD_UNIT_ID)
            bannerAd?.setAdSize(adSize)

            // set the banner listener
            bannerAd?.setBannerListener(BannerAdListener(this))
            // add LevelPlayBannerAdView to your container
            val layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            bannerParentLayout?.addView(bannerAd, 0, layoutParams)
            // Set the placement name
            bannerAd?.setPlacementName(resources.getString(R.string.banner_placement))

            setEnablementForButton(ButtonIdentifiers.LOAD_BANNER_BUTTON_IDENTIFIER, true)
        } ?: run {
            log("Failed to create banner ad")
        }
    }

    override fun createRewardedAd() {
        rewardedAd = LevelPlayRewardedAd(resources.getString(R.string.rewarded_ad_iron))
        rewardedAd?.setListener(RewardedAdListener(this))
        setEnablementForButton(ButtonIdentifiers.LOAD_REWARDED_BUTTON_IDENTIFIER, true)
    }

    override fun createNativeAd() {
       // IronSource.init(this, resources.getString(R.string.apiKey), IronSource.AD_UNIT.NATIVE_AD)
         nativeAd = LevelPlayNativeAd.Builder()
            .withPlacementName(resources.getString(R.string.native_placement))
            .withListener(NativeAdListener(this))
            .build()
        nativeAd?.loadAd() ?: Log.e("IronSourceNative", "Native ad is not initialized!")

       // setEnablementForButton(ButtonIdentifiers.LOAD_NATIVE_BUTTON_IDENTIFIER, true)
    }

    private fun showNativeAd(adContainer: ViewGroup) {
        if (nativeAd == null) {
            Log.e("IronSourceNative", "Native ad is not loaded yet!")
            return
        }

        val inflater = adContainer.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val nativeAdLayout = inflater.inflate(R.layout.native_ad, adContainer, false) as NativeAdLayout

        // ** Fix: Remove the nativeAdLayout from any previous parent **
        nativeAdLayout.parent?.let { parent ->
            (parent as ViewGroup).removeView(nativeAdLayout)
        }

        // ** Clear old views from adContainer before adding a new one **
        adContainer.removeAllViews()

        // Set up ad views
        val titleView = nativeAdLayout.findViewById<TextView>(R.id.ad_title_i)
        val iconView = nativeAdLayout.findViewById<ImageView>(R.id.ad_app_icon_i)
        val mediaView = nativeAdLayout.findViewById<LevelPlayMediaView>(R.id.ad_media_i)
        val ctaView = nativeAdLayout.findViewById<Button>(R.id.ad_call_to_action_i)

        nativeAd?.let { ad ->
            titleView.text = ad.title ?: "Sponsored Ad"
            ad.icon?.drawable?.let { iconView.setImageDrawable(it) }

            // Register views with IronSource
            nativeAdLayout.setTitleView(titleView)
            nativeAdLayout.setIconView(iconView)
            nativeAdLayout.setMediaView(mediaView)
            nativeAdLayout.setCallToActionView(ctaView)

            try {
                // Register the ad views with IronSource (No unregister function available)
                nativeAdLayout.registerNativeAdViews(ad)
            } catch (e: IllegalStateException) {
                Log.e("IronSourceNative", "Error registering native ad views: ${e.message}")
            }
        }

        // ** Finally, add the prepared native ad layout to the container **
        adContainer.addView(nativeAdLayout)
    }

    fun loadNativeButtonTapped(view: View){
      showNativeAd(binding.adContainer)

    }

    fun loadBannerButtonTapped(view: View) {
        // Load a banner ad. If the "refresh" option is enabled in the LevelPlay dashboard settings, the banner will automatically refresh at the specified interval,
        // otherwise, the banner will remain static until manually destroyed
        log("loadAd for banner")
        bannerAd?.loadAd()
    }

    override fun setBannerViewVisibility(visibility: Int) {
        bannerParentLayout?.visibility = visibility
    }

    override fun setNativeViewVisibility(visibility: Int) {
        binding.nativeLayout.visibility = visibility
    }
    //endregion

    //region Rewarded Video Methods
    fun showRewardedVideoButtonTapped(view: View) {
        // It is advised to make sure there is available ad before attempting to show an ad
        if (IronSource.isRewardedVideoAvailable()) {
            // This will present the Rewarded Video.
            log("showRewardedVideo")
            IronSource.showRewardedVideo()
        } else {
            // wait for the availability of rewarded video to change to true before calling show
        }
    }

    override fun setPlacementInfo(placementInfo: Placement) {
        // Setting the rewarded video placement info, an object that contains the placement's reward name and amount
        rewardedVideoPlacementInfo = placementInfo
    }

    override fun setPlacementInfo(placementInfo: LevelPlayReward) {
        levelPlayRewardedInfo = placementInfo
    }

    override fun showRewardDialog() {
        // Showing a graphical indication of the reward name and amount after the user closed the rewarded video ad
        rewardedVideoPlacementInfo?.let {
            AlertDialog.Builder(this)
                .setPositiveButton("ok") { dialog, _ -> dialog.dismiss() }
                .setTitle(resources.getString(R.string.rewarded_dialog_header))
                .setMessage("${resources.getString(R.string.rewarded_dialog_message)} ${rewardedVideoPlacementInfo!!.rewardAmount} ${rewardedVideoPlacementInfo!!.rewardName}")
                .setCancelable(false)
                .create()
                .show()
        }

        levelPlayRewardedInfo?.let {
            AlertDialog.Builder(this)
                .setPositiveButton("ok") { dialog, _ -> dialog.dismiss() }
                .setTitle(resources.getString(R.string.rewarded_dialog_header))
                .setMessage("${resources.getString(R.string.rewarded_dialog_message)} ${levelPlayRewardedInfo!!.amount} ${levelPlayRewardedInfo!!.name}")
                .setCancelable(false)
                .create()
                .show()
        }

        rewardedVideoPlacementInfo = null
        levelPlayRewardedInfo = null
    }
    //endregion

    //region Utility Methods
    override fun setEnablementForButton(buttonIdentifier: ButtonIdentifiers, enable: Boolean) {
        var text: String? = null
        val color = if (enable) Color.WHITE else Color.LTGRAY
        var buttonToModify: Button? = null

        when (buttonIdentifier) {
            ButtonIdentifiers.SHOW_REWARDED_VIDEO_BUTTON_IDENTIFIER -> {
                text =
                    if (enable) resources.getString(R.string.show) else resources.getString(R.string.initializing)
                buttonToModify = binding.rvButton
            }

            ButtonIdentifiers.LOAD_INTERSTITIAL_BUTTON_IDENTIFIER -> buttonToModify =
                binding.interstitialLoadButton
            ButtonIdentifiers.SHOW_INTERSTITIAL_BUTTON_IDENTIFIER -> buttonToModify =
                binding.interstitialShowButton
            ButtonIdentifiers.LOAD_BANNER_BUTTON_IDENTIFIER -> buttonToModify =
                binding.bannerLoadButton
            ButtonIdentifiers.LOAD_REWARDED_BUTTON_IDENTIFIER -> buttonToModify =
                binding.rewardedLoadButton
            ButtonIdentifiers.SHOW_REWARDED_BUTTON_IDENTIFIER -> buttonToModify =
                binding.rewardedShowButton
            ButtonIdentifiers.LOAD_NATIVE_BUTTON_IDENTIFIER -> buttonToModify =
                binding.nativeLoadButton

            else -> {}
        }

        val finalButtonToModify = buttonToModify
        val finalText = text

        runOnUiThread {
            finalButtonToModify?.apply {
                setTextColor(color)
                finalText?.let {
                    setText(it)
                }
                isEnabled = enable
            }
        }
    }

    private fun log(log: String) {
        Log.d(TAG, log)
    }
    //endregion

    private fun getVersionName(): String {
        return try {
            val packageInfo =
                applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

}
