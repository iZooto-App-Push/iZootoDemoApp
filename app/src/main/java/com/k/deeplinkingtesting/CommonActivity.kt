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
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdSize.BANNER
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.izooto.AppConstant
import com.izooto.PreferenceUtil
import com.izooto.iZooto
import com.k.deeplinkingtesting.admob.AdMobActivity
import com.k.deeplinkingtesting.admob.AdUnitConfig
import com.k.deeplinkingtesting.remoteconfigData.InterstitialAdHandler
import com.k.deeplinkingtesting.remoteconfigData.RewardedAdManager
import java.util.Locale


class CommonActivity : AppCompatActivity() {
    private val TAG = "CommonActivity"
    private var permissionFile: Button? = null
    private var beginDebugFile: Button? = null
    private var sendDebugFile: Button? = null
    private var deleteDebugFile: Button? = null
    private var trackEvents: Button? = null
    private var mainLayout: LinearLayout? = null
    private var linearLayout: LinearLayout? = null
    private var ad_container_admob: LinearLayout? = null
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private var nestedScrollView: NestedScrollView? = null
    private lateinit var nativeAdView: NativeAdView
    private lateinit var adManagerAdView: AdManagerAdView

    private lateinit var interstitialAdHandler: InterstitialAdHandler



    //adManagerView
    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.native_pulse)

        try {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { showExitConfirmationDialog() }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // adManagerAdView = findViewById(R.id.adManagerView)

        permissionFile = findViewById(R.id.btn_permissionFIle)
        beginDebugFile = findViewById(R.id.btn_beginDebugFile)
        sendDebugFile = findViewById(R.id.btn_sendDebugFile)
        deleteDebugFile = findViewById(R.id.btn_deleteDebugFile)
        permissionFile = findViewById(R.id.btn_permissionFIle)
        trackEvents = findViewById(R.id.trackEvents);
        nestedScrollView = findViewById(R.id.nestedScrollView)
        mainLayout = findViewById(R.id.mainView)
        nativeAdView = findViewById(R.id.native_ad_view)
        ad_container_admob = findViewById(R.id.ad_container_admob)

        iZooto.promptForPushNotifications()

        initializeRemoteConfig()
        loadBannerAds()
        loadNativeAd(nativeAdView)

        interstitialAdHandler = InterstitialAdHandler(this)

        // 1. Load remote config and ad unit IDs
        interstitialAdHandler.loadFromRemoteConfig {

            // 2. Load AdMob interstitial first
            interstitialAdHandler.loadAdMobInterstitial(
                onAdLoaded = {
                    // Show AdMob ad when loaded
                    interstitialAdHandler.showInterstitialIfAvailable(this)
                },
                onAdFailed = {
                    // If AdMob fails, try loading AdManager ad
                    interstitialAdHandler.loadAdManagerInterstitial {
                        // Show AdManager ad if loaded
                        interstitialAdHandler.showInterstitialIfAvailable(this)
                    }
                }
            )
        }

// To show ad later



        iZooto.enablePulse(this,nestedScrollView, mainLayout, true)

        permissionFile?.setOnClickListener { view ->
            (view as? Button)?.let {
                requestPermission()
               // adHandler.showInterstitialIfAvailable(this)
                val rewardedAdManager = RewardedAdManager(this)
                rewardedAdManager.loadAndShowRewardedAd()

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
    }

    private fun loadBannerAds() {
        val bannerAdUnit = if ( AdConfig.r_bannerAdUnitId.isNotEmpty()) {
            AdConfig.r_bannerAdUnitId
        } else {
            resources.getString(R.string.gam_banner)
        }
        Log.e("Banner AdUnit ID",bannerAdUnit)

        val adSize =
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, getScreenWidthInDp())
        adManagerAdView = AdManagerAdView(this).apply {
            adUnitId = bannerAdUnit
            setAdSize(adSize)
        }
        ad_container_admob?.removeAllViews() // Ensure only one ad is shown
        ad_container_admob?.addView(adManagerAdView)
        val adRequest = AdManagerAdRequest.Builder().build()
        adManagerAdView.loadAd(adRequest)
        var hasRetried = false
        adManagerAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d(
                    TAG,
                    "Banner adapter class name:" + adManagerAdView.responseInfo?.mediationAdapterClassName
                )
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(TAG, "Failed to load ad: ${adError.message}")
                if (!hasRetried) {
                    hasRetried = true
                    // fetchRemoteConfig()
                    // loadBannerAds(defaultAdUnit)
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
            fetchRemoteConfig()
            Log.d("RemoteConfig", "RemoteConfig initialized successfully.")
        } catch (e: Exception) {
            Log.e("RemoteConfig", "Error initializing RemoteConfig: ${e.message}")
        }
    }
// fetch remote config data from firebase server
    private fun fetchRemoteConfig() {
        try {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("RemoteConfig", "Fetch and activate succeeded.")
                        AdConfig.r_bannerAdUnitId = remoteConfig.getString("banner_ad_unit_id")
                        AdConfig.r_interstitialID = remoteConfig.getString("interstitial_ad_unit_id")
                        AdConfig.r_rewarded_Ads = remoteConfig.getString("rewarded_ad_unit_id")
                        AdConfig.r_native_ad_unit_id = remoteConfig.getString("native_ad_unit_id")
                        AdConfig.r_app_open_id = remoteConfig.getString("app_open_ad_unit_id")

                        Log.d("AdConfig", "App Open Ad Unit ID: ${AdConfig.r_app_open_id}")
                        Log.d("AdConfig", "Native Ad Unit ID: ${AdConfig.r_native_ad_unit_id}")
                        Log.d("AdConfig", "Rewarded Ad Unit ID: ${AdConfig.r_rewarded_Ads}")
                        Log.d("AdConfig", "Interstitial Ad Unit ID: ${AdConfig.r_interstitialID}")
                        Log.d("AdConfig", "Banner Ad Unit ID: ${AdConfig.r_bannerAdUnitId}")


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

    private fun dynamicAdsView(context: Context) {
        try {
            val adView = AdView(context)
            adView.setAdSize(adSize)
            adView.adUnitId = AdUnitConfig.bannerAdUnitId
            adView.loadAd(AdRequest.Builder().build())
            linearLayout?.addView(adView)

            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.d(TAG, "Banner ad loaded successfully")
                    trackAdLoadedEvent()
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.e(TAG, "Banner ad failed to load: $p0")
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    Log.d(TAG, "Banner ad opened by user")
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.d(TAG, "Banner ad clicked by user")
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    Log.d(TAG, "Banner ad closed")
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Banner ad execution failure " + ex.message)
        }
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


    private fun showExitConfirmationDialog() {
        try {
            AlertDialog.Builder(this).apply {
                setTitle("Exit App")
                setMessage("Are you sure you want to exit?")
                setPositiveButton("Yes") { _, _ ->
                    finishAffinity() // Close all activities and exit
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss() // Close the dialog
                }
                setCancelable(false) // Prevent closing by tapping outside
                show()
            }

            val gam_interstitial = if (AdConfig.r_interstitialID.isNotEmpty()) {
                AdConfig.r_interstitialID
            } else {
                resources.getString(R.string.gam_interstitial)
            }
            Log.e("GAM Rewarded AdUnit ID",gam_interstitial)

          

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


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
            R.id.action_notification -> {
                val intent = Intent(this@CommonActivity, AdMobActivity::class.java)
                startActivity(intent)
                true
            }

//            R.id.notification_settings -> {
//                val intent = Intent(this@CommonActivity, AdMobActivity::class.java)
//                startActivity(intent)
//                true
//
//            }
            R.id.not_found -> {
                val intent = Intent(this@CommonActivity, OutBrainContentActivity::class.java)
                startActivity(intent)
                true

            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        nativeAdView.destroy()
        adManagerAdView.destroy()
    }

    private fun loadNativeAd(nativeAdView: NativeAdView) {
        val gam_native = if (AdConfig.r_native_ad_unit_id.isNotEmpty()) {
            AdConfig.r_native_ad_unit_id
        } else {
            resources.getString(R.string.gam_native)
        }
        Log.e("GAM native AdUnit ID",gam_native)


        val adLoader = AdLoader.Builder(
            this,
            gam_native
        ) // Replace with your ad unit ID
            .forNativeAd { nativeAd ->
                nativeAdView.visibility = View.VISIBLE
                // Populate the native ad into the native ad view
                populateNativeAdView(nativeAd, nativeAdView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                   // loadNativeAd(nativeAdView)
                    nativeAdView.visibility = View.VISIBLE

                    Log.e("NativeAd", "Failed to load native ad: ${error.message}")

                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {

        val mediaView = adView.findViewById<MediaView>(R.id.native_ad_media)
        adView.mediaView = mediaView
        mediaView.setMediaContent(nativeAd.mediaContent)

        adView.setNativeAd(nativeAd)
    }


}
object AdConfig {
    var r_bannerAdUnitId: String = ""
    var r_interstitialID : String = ""
    var r_app_open_id :String = ""
    var r_rewarded_Ads : String = ""
    var r_native_ad_unit_id :String = ""

}
