package com.k.deeplinkingtesting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdSize.BANNER
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.izooto.AppConstant
import com.izooto.PreferenceUtil
import com.izooto.iZooto
import com.k.deeplinkingtesting.admob.AdMobActivity
import com.k.deeplinkingtesting.inmobi.sample.BannerBase
import com.k.deeplinkingtesting.iron.IronSourceActivity
import com.unity3d.ads.UnityAds.initialize
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt


class CommonActivity : AppCompatActivity() {
    private val TAG = "CommonActivity"
    private var permissionFile: Button? = null
    private var beginDebugFile: Button? = null
    private var sendDebugFile: Button? = null
    private var deleteDebugFile: Button? = null
    private var yandexAdsFile: Button? = null


    private var trackEvents : Button? = null
    private var mainLayout: LinearLayout? = null
    private var doubleBackToExitPressedOnce = false
    private val handler = Handler()
    private  var ad_container_admob : LinearLayout?=null
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private  var nestedScrollView : NestedScrollView? = null
    private var bannerAdUnitId: String = ""

    //yandex mediation
    private var banner_ad_view_container : FrameLayout? = null
    private var bannerAd: BannerAdView? = null
    private lateinit var adFormatManager: AdFormatManager

    // unity ads
    private var unity_ads_banner : FrameLayout? = null
    private var topBanner: BannerView? = null

    //adManagerView
    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.native_pulse)

        try {
            val backgroundScope = CoroutineScope(Dispatchers.IO)
            backgroundScope.launch {
                MobileAds.initialize(this@CommonActivity) {}
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Ads execution failure " + ex.message)
        }
        permissionFile = findViewById(R.id.btn_permissionFIle)
        beginDebugFile = findViewById(R.id.btn_beginDebugFile)
        sendDebugFile = findViewById(R.id.btn_sendDebugFile)
        deleteDebugFile = findViewById(R.id.btn_deleteDebugFile)
        permissionFile = findViewById(R.id.btn_permissionFIle)
        banner_ad_view_container = findViewById(R.id.banner_ad_view_container)
        trackEvents=findViewById(R.id.trackEvents);
        nestedScrollView = findViewById(R.id.nestedScrollView)
        mainLayout = findViewById(R.id.mainView)
        ad_container_admob = findViewById(R.id.ad_container_admob)
        unity_ads_banner= findViewById(R.id.unity_ads_banner)

        adFormatManager = AdFormatManager(this)
        // Initialize Unity Ads:
        initialize(
            applicationContext, getString(R.string.unity_game_id),
            false
        )
             loadUnityBannerAds()
             loadBannerYandexAds()

      //  loadNativeAd(nativeAdView)
          iZooto.promptForPushNotifications()

       // initializeRemoteConfig()

       // loadBannerAds("")



    //iZooto.enablePulse(this,nestedScrollView, mainLayout, true)
//        try {
//            linearLayout = findViewById(R.id.adLayout)
//            remoteConfig = Firebase.remoteConfig
//            val configSettings = remoteConfigSettings {
//                minimumFetchIntervalInSeconds = 0 // Set to 0 for testing to always fetch fresh data
//            }
//            remoteConfig.setConfigSettingsAsync(configSettings)
//            remoteConfig.setDefaultsAsync(R.xml.remote_config_default)
//            //setAdUnitId(this)
//        } catch (ex: Exception) {
//            Log.e(TAG, "AdUnit execution failure " + ex.message)
//        }

        permissionFile?.setOnClickListener { view ->
            (view as? Button)?.let {
                requestPermission()

            }
        }

        beginDebugFile?.setOnClickListener { view ->
            (view as? Button)?.let {
                val builder1 = AlertDialog.Builder(this@CommonActivity)
                builder1.setMessage("Are you begin the debug?")
                builder1.setCancelable(true)
                builder1.setPositiveButton(
                    "Yes"
                ) { dialog: DialogInterface, _: Int ->
                    iZooto.createDirectory(this@CommonActivity)
                    dialog.cancel()
                }
                builder1.setNegativeButton(
                    "No"
                ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                val alert11 = builder1.create()
                alert11.show()
            }
        }
        sendDebugFile?.setOnClickListener { view ->
            (view as Button).let {
                val builder1 = AlertDialog.Builder(this@CommonActivity)
                builder1.setMessage("Are you share the debug info?")
                builder1.setCancelable(true)
                builder1.setPositiveButton(
                    "Yes"
                ) { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }
                builder1.setNegativeButton(
                    "No"
                ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                val alert11 = builder1.create()
                alert11.show()
            }
        }
        deleteDebugFile?.setOnClickListener { view ->
            (view as Button).let {
                val builder1 = AlertDialog.Builder(this@CommonActivity)
                builder1.setMessage("Are you end the debug?")
                builder1.setCancelable(true)
                builder1.setPositiveButton(
                    "Yes"
                ) { dialog: DialogInterface, _: Int ->
                    iZooto.deleteDirectory(this@CommonActivity)
                    dialog.cancel()
                }
                builder1.setNegativeButton(
                    "No"
                ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                val alert11 = builder1.create()
                alert11.show()

            }
        }
        val sendButton = findViewById<Button>(R.id.btn_news_hub)
        sendButton.setOnClickListener { _: View? -> sendEmail() }
//        yandexAdsFile?.setOnClickListener { _: View? ->
//            val intent = Intent(this@CommonActivity, YadexAdsActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun loadUnityBannerAds() {
        topBanner =
            BannerView(this, getString(R.string.banner_ad_unit_id), UnityBannerSize.getDynamicSize(this)) //UnityBannerSize(320, 50)
        // Set the listener for banner lifecycle events:
        topBanner!!.listener = bannerListener
        topBanner?.load()
        // Associate the banner view object with the banner view:
        unity_ads_banner?.addView(topBanner)

    }



    private val bannerListener: BannerView.IListener = object : BannerView.IListener {
        override fun onBannerLoaded(bannerAdView: BannerView) {
            // Called when the banner is loaded.
            Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.placementId)
        }

        override fun onBannerShown(bannerAdView: BannerView?) {
            // Enable the correct button to hide the ad
            unity_ads_banner?.visibility = View.VISIBLE
            Log.v("UnityAdsExample", "onBannerShown: " + bannerAdView?.isShown)

        }

        override fun onBannerFailedToLoad(bannerAdView: BannerView, errorInfo: BannerErrorInfo) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load banner for " + bannerAdView.placementId + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage
            )
            // Note that the BannerErrorInfo object can indicate a no fill (refer to the API documentation).
        }

        override fun onBannerClick(bannerAdView: BannerView) {
            // Called when a banner is clicked.
            Log.v("UnityAdsExample", "onBannerClick: " + bannerAdView.placementId)
        }

        override fun onBannerLeftApplication(bannerAdView: BannerView) {
            // Called when the banner links out of the application.
            Log.v("UnityAdsExample", "onBannerLeftApplication: " + bannerAdView.placementId)
        }
    }



    private fun loadBannerAds(bannerAdsUnitID: String) {
    val defaultAdUnit = "ca-app-pub-9298860897894361/3941078262"
    val bannerAdUnit = if (bannerAdsUnitID.isNotEmpty()) bannerAdsUnitID else defaultAdUnit
    val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, getScreenWidthInDp())
    val adManagerAdView = AdManagerAdView(this).apply {
        adUnitId = bannerAdUnit
        setAdSize(adSize)
    }
    ad_container_admob?.removeAllViews() // Ensure only one ad is shown
    ad_container_admob?.addView(adManagerAdView)
    val adRequest = AdManagerAdRequest.Builder().build()
    adManagerAdView.loadAd(adRequest)
    var hasRetried = false
    adManagerAdView.adListener = object : com.google.android.gms.ads.AdListener() {
        override fun onAdLoaded() {
            Log.d("AdManager", "Ad loaded successfully: $bannerAdsUnitID")
        }
        override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
            Log.e("AdManager", "Failed to load ad: ${adError.message}")
            if (!hasRetried) {
                hasRetried = true
                fetchRemoteConfig()
                loadBannerAds(defaultAdUnit)
            }
        }
    }
}

    private fun initializeRemoteConfig() {
        try {
            remoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // Fetch interval set to 1 hour
                .build()
            remoteConfig.setConfigSettingsAsync(configSettings)
            Log.d("RemoteConfig", "RemoteConfig initialized successfully.")
        } catch (e: Exception) {
            Log.e("RemoteConfig", "Error initializing RemoteConfig: ${e.message}")
        }
    }

    private fun fetchRemoteConfig() {
        try {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("RemoteConfig", "Fetch and activate succeeded.")
                        bannerAdUnitId = remoteConfig.getString("banner_ad_unit_id")
                        Log.d("RemoteConfig", "Banner Ad Unit ID: $bannerAdUnitId")

                        // Uncomment to load banner ads dynamically
                        // loadBannerAds(bannerAdUnitId)
                    } else {
                        Log.e("RemoteConfig", "Fetch failed: ${task.exception?.message}")
                    }
                }
        } catch (e: Exception) {
            Log.e("RemoteConfig", "Error fetching RemoteConfig: ${e.message}")
        }
    }


    private fun getScreenWidthInDp(): Int {
        val displayMetrics = resources.displayMetrics
        return (displayMetrics.widthPixels / displayMetrics.density).toInt()
    }
    // Get the adaptive ad size based on the screen width

    private val adSize: AdSize
        get() {
            try {
                val display = windowManager.defaultDisplay
                val outMetrics = DisplayMetrics()
                display.getMetrics(outMetrics)
                val density = outMetrics.density
                val adWidthPixels = outMetrics.widthPixels.toFloat()
                val adWidth = (adWidthPixels / density).toInt()
                return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
            } catch (ex: Exception) {
                return BANNER
            }
        }

    private fun trackAdLoadedEvent() {
        FirebaseAnalytics.getInstance(this).logEvent("banner_ad_loaded", null)
    }


    private fun sendEmail() {
        val to = arrayOf("amit@datability.co")
        val preferenceUtil = PreferenceUtil.getInstance(this)
        if (preferenceUtil.getStringData(AppConstant.FCM_DEVICE_TOKEN).isNotEmpty()) {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.setType("message/rfc822")
            emailIntent.setData(Uri.parse("mailto:"))
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Device Token ")
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Device token -- " + preferenceUtil.getStringData(AppConstant.FCM_DEVICE_TOKEN)
            )
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this@CommonActivity,
                    "There is no email client installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //new code
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.setData(
                    Uri.parse(
                        String.format(
                            "package:%s",
                            applicationContext.packageName
                        )
                    )
                )
                startActivityForResult(intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, 2296)
            }
        } else {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXIST)
            askForReadPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXIST)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@CommonActivity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@CommonActivity,
                    permission
                )
            ) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(
                    this@CommonActivity,
                    arrayOf(permission),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@CommonActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }

    private fun askForReadPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@CommonActivity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@CommonActivity,
                    permission
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@CommonActivity,
                    arrayOf(permission),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@CommonActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }


    companion object {
        const val WRITE_EXIST = 0x3
        const val READ_EXIST = 0x5
        fun setLocale(activity: Activity, lCode: String?) {
            val locale = Locale(lCode)
            Locale.setDefault(locale)
            val resources = activity.resources
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }

override fun onBackPressed() {
    if (doubleBackToExitPressedOnce) {
        super.onBackPressed()
        return
    }

    this.doubleBackToExitPressedOnce = true

    // Inflate the custom layout containing the native ad
    val dialogView = layoutInflater.inflate(R.layout.ad_dialog, null)
//    val nativeAdView: NativeAdView = dialogView.findViewById(R.id.nativeAdView)
//
//    // Load the native ad
//    val adLoader = AdLoader.Builder(this, "ca-app-pub-9298860897894361/3941078262")  // Replace with your Ad Unit ID
//        .forNativeAd { nativeAd ->
//            // Populate the native ad into the native ad view
//            populateNativeAdView(nativeAd, nativeAdView)
//        }
//        .withAdListener(object : com.google.android.gms.ads.AdListener() {
//            override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
//                Log.e("Failed","Ads")
//                // Handle the failure by showing an appropriate message to the user
//            }
//        })
//        .withNativeAdOptions(NativeAdOptions.Builder().build())
//        .build()
//
//    adLoader.loadAd(AdRequest.Builder().build())

    // Create and show the AlertDialog
    val builder1 = AlertDialog.Builder(this@CommonActivity)
    builder1.setView(dialogView)
    builder1.setCancelable(true)
    builder1.setPositiveButton("Yes") { dialog, _ ->
        finishAffinity()  // Close the app
        dialog.cancel()
    }
    builder1.setNegativeButton("No") { dialog, _ ->
        dialog.cancel()
    }
    val alert11 = builder1.create()
    alert11.show()

    handler.postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
}

    // Helper method to populate native ad into the NativeAdView

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.applovin -> {
                val intent = Intent(this@CommonActivity, AdMobActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.iron -> {
                val intent = Intent(this@CommonActivity, IronSourceActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.inMobi -> {
                val intent = Intent(this@CommonActivity, BannerBase::class.java)
                startActivity(intent)
                true
            }

//            R.id.notification_settings -> {
//                val intent = Intent(this@CommonActivity, AdMobActivity::class.java)
//                startActivity(intent)
//                true
//
//            }
//            R.id.not_found -> {
//               // val intent = Intent(this@CommonActivity, OutBrainContentActivity::class.java)
//               // startActivity(intent)
//                true
//
//            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Handle AdView lifecycle properly
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun loadNativeAd(nativeAdView : NativeAdView) {
        ///23206713921/izooto_demo/com.k.deeplinkingtesting_native
        //ca-app-pub-9298860897894361/4531740244
        val adLoader = AdLoader.Builder(this, "ca-app-pub-9298860897894361/4531740244") // Replace with your ad unit ID
            .forNativeAd { nativeAd ->
                nativeAdView.visibility = View.VISIBLE
                // Populate the native ad into the native ad view
              //  populateNativeAdView(nativeAd, nativeAdView)
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    nativeAdView.visibility = View.GONE

                    Log.e("NativeAd", "Failed to load native ad: ${error.message}")

                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

//    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
//        // Set headline
////        adView.findViewById<TextView>(R.id.native_ad_headline).text = nativeAd.headline
////        adView.headlineView = adView.findViewById(R.id.native_ad_headline)
//
//
//        // Set media
//        val mediaView = adView.findViewById<MediaView>(R.id.native_ad_media)
//        adView.mediaView = mediaView
//        mediaView.setMediaContent(nativeAd.mediaContent)
//
////        // Set call to action
////        nativeAd.callToAction?.let {
////            val callToActionView = adView.findViewById<Button>(R.id.native_ad_call_to_action)
////            callToActionView.text = it
////            callToActionView.visibility = View.VISIBLE
////            adView.callToActionView = callToActionView
////        } ?: run {
////           // adView.findViewById<Button>(R.id.native_ad_call_to_action).visibility = View.GONE
////        }
//
//        // Set the NativeAd object
//        adView.setNativeAd(nativeAd)
//    }
//yandex

    private fun loadBannerYandexAds() {
        if (bannerAd != null) {
            bannerAd?.destroy()
            bannerAd?.removeAllViews()
        }

        if (banner_ad_view_container == null) {
            Log.e("Yandex", "banner_ad_view_container is null")
            return
        }

        if (bannerAd == null) {
            bannerAd = BannerAdView(this)
        }

        bannerAd?.setAdSize(adaptiveInlineBannerSize(this, banner_ad_view_container!!))
        bannerAd?.setAdUnitId("R-M-13859675-1") // Consider replacing with a constant
        Log.d("Yandex", "Using AdUnitId: R-M-13859675-1")

        bannerAd?.setBannerAdEventListener(object : BannerAdEventListener {
            override fun onAdLoaded() {
                Log.i("Yandex", "onAdLoaded")
                if (isFinishing || isDestroyed) {
                    bannerAd?.destroy()
                    return
                }
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                loadBannerAds("")
                Log.e("Yandex", "Ad failed to load: ${error.code} - ${error.description}")
            }

            override fun onAdClicked() {
                Log.i("Yandex", "Ad clicked")
            }

            override fun onLeftApplication() {
                Log.i("Yandex", "User left application")
            }

            override fun onReturnedToApplication() {
                Log.i("Yandex", "User returned to application")
            }

            override fun onImpression(impressionData: ImpressionData?) {
                Log.i("ImpressionData", "${impressionData?.rawData}")
            }
        })

        bannerAd?.loadAd(com. yandex. mobile. ads. common. AdRequest.Builder().build())
        banner_ad_view_container?.addView(bannerAd)
    }

    private fun adaptiveInlineBannerSize(context: Context, binding: FrameLayout): BannerAdSize
    {
        val screenHeight = context.resources.displayMetrics.run { heightPixels / density }.roundToInt()
        // Calculate the width of the ad, taking into account the padding in the ad container.
        var adWidthPixels = binding.width
        if (adWidthPixels == 0) {
            // If the ad hasn't been laid out, default to the full screen width
            adWidthPixels = context.resources.displayMetrics.widthPixels
        }
        val adWidth = (adWidthPixels / context.resources.displayMetrics.density).roundToInt()
        // Determine the maximum allowable ad height. The current value is given as an example.
        val maxAdHeight = screenHeight / 2

        return BannerAdSize.inlineSize(context, adWidth, maxAdHeight)
    }

}
