package com.k.deeplinkingtesting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.izooto.AppConstant
import com.izooto.PreferenceUtil
import com.izooto.iZooto
import com.k.deeplinkingtesting.admob.AdMobActivity
import com.k.deeplinkingtesting.admob.AdUnitConfig
import com.k.deeplinkingtesting.admob.InLineBannerAdActivity
import com.k.deeplinkingtesting.productionOubtbrain.OutbrainSDKActivity
import com.k.deeplinkingtesting.storage.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.Locale


class CommonActivity : AppCompatActivity() {
    private val TAG = "CommonActivity"
    private var permissionFile: Button? = null
    private var beginDebugFile: Button? = null
    private var sendDebugFile: Button? = null
    private var deleteDebugFile: Button? = null
    private var mainLayout: CoordinatorLayout? = null
    private var doubleBackToExitPressedOnce = false
    private val handler = Handler()
    private var nativeAd: NativeAd? = null
    private var linearLayout: LinearLayout? = null
    private lateinit var remoteConfig: FirebaseRemoteConfig

    @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_common)

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
        mainLayout = findViewById(R.id.mainLayout)
        iZooto.promptForPushNotifications()

        iZooto.enablePulse(this, mainLayout, true)

        try {
            linearLayout = findViewById(R.id.adLayout)
            remoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // Set to 0 for testing to always fetch fresh data
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_default)
            setAdUnitId(this)
        } catch (ex: Exception) {
            Log.e(TAG, "AdUnit execution failure " + ex.message)
        }

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
    }

    private fun setAdUnitId(context: Context) {
        try {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        AdUnitConfig.bannerAdUnitId =
                            remoteConfig.getString(AdUnitConfig.BANNER_AD_UNIT_ID)
                        AdUnitConfig.inlineBannerAdUnitId =
                            remoteConfig.getString(AdUnitConfig.INLINE_BANNER_AD_UNIT_ID)
                        AdUnitConfig.nativeAdUnitId =
                            remoteConfig.getString(AdUnitConfig.NATIVE_AD_UNIT_ID)
                        dynamicAdsView(context)
                    } else {
                        Log.e(TAG, "AdUnit execution failure")
                    }
                }

            remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
                override fun onUpdate(configUpdate: ConfigUpdate) {
                    if (configUpdate.updatedKeys.contains(AdUnitConfig.BANNER_AD_UNIT_ID)) {
                        remoteConfig.activate().addOnCompleteListener {
                            AdUnitConfig.bannerAdUnitId =
                                remoteConfig.getString(AdUnitConfig.BANNER_AD_UNIT_ID)
                        }
                    }

                    if (configUpdate.updatedKeys.contains(AdUnitConfig.INLINE_BANNER_AD_UNIT_ID)) {
                        remoteConfig.activate().addOnCompleteListener {
                            AdUnitConfig.inlineBannerAdUnitId =
                                remoteConfig.getString(AdUnitConfig.INLINE_BANNER_AD_UNIT_ID)
                        }
                    }

                    if (configUpdate.updatedKeys.contains(AdUnitConfig.NATIVE_AD_UNIT_ID)) {
                        remoteConfig.activate().addOnCompleteListener {
                            AdUnitConfig.nativeAdUnitId =
                                remoteConfig.getString(AdUnitConfig.NATIVE_AD_UNIT_ID)
                        }
                    }
                }

                override fun onError(error: FirebaseRemoteConfigException) {
                    Log.e(TAG, "Config update error with code: " + error.code, error)
                }
            })
        } catch (ex: Exception) {
            Log.e(TAG, "AdUnit execution failure " + ex.message)
        }
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
                return AdSize.BANNER
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

//    override fun onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//
//        this.doubleBackToExitPressedOnce = true
//        val builder1 = AlertDialog.Builder(this@CommonActivity)
//        builder1.setMessage("Do you want to close the app?")
//        builder1.setCancelable(true)
//        builder1.setPositiveButton(
//            "Yes"
//        ) { dialog: DialogInterface, _: Int ->
//            finishAffinity()
//            dialog.cancel()
//        }
//        builder1.setNegativeButton(
//            "No"
//        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
//        val alert11 = builder1.create()
//        alert11.show()
//        handler.postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
//    }
override fun onBackPressed() {
    if (doubleBackToExitPressedOnce) {
        super.onBackPressed()
        return
    }

    this.doubleBackToExitPressedOnce = true

    // Inflate the custom layout containing the native ad
    val dialogView = layoutInflater.inflate(R.layout.ad_dialog, null)
    val nativeAdView: NativeAdView = dialogView.findViewById(R.id.nativeAdView)

    // Load the native ad
    val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")  // Replace with your Ad Unit ID
        .forNativeAd { nativeAd ->
            // Populate the native ad into the native ad view
            populateNativeAdView(nativeAd, nativeAdView)
        }
        .withAdListener(object : com.google.android.gms.ads.AdListener() {
            override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                // Handle the failure by showing an appropriate message to the user
            }
        })
        .withNativeAdOptions(NativeAdOptions.Builder().build())
        .build()

    adLoader.loadAd(AdRequest.Builder().build())

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
    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the headline
        adView.headlineView = adView.findViewById(R.id.ad_headline)

        // Set the media view
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.mediaView?.setMediaContent(nativeAd.mediaContent!!)




        // Register the native ad object with the view
        adView.setNativeAd(nativeAd)
    }

    override fun onDestroy() {
        nativeAd?.destroy()
        super.onDestroy()
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
                val intent = Intent(this@CommonActivity, InLineBannerAdActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.notification_settings -> {
                val intent = Intent(this@CommonActivity, AdMobActivity::class.java)
                startActivity(intent)
                true

            }
            R.id.not_found -> {
                val intent = Intent(this@CommonActivity, OutBrainContentActivity::class.java)
                startActivity(intent)
                true

            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    fun getOSVersion(): String {
        var androidVersion = ""
        try {
            androidVersion = URLEncoder.encode(Build.VERSION.SDK_INT.toString(), "utf-8")
        } catch (ignored: UnsupportedEncodingException) {
            Log.e("Exception",ignored.toString())
        }

        return androidVersion
    }
    fun getAppIdentifier(applicationContext: Context): String {
        return applicationContext.packageName
    }

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        if (stringId == 0 && applicationInfo.nonLocalizedLabel == null) {
            return applicationInfo.name
        }
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }

    fun isTablet(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

}