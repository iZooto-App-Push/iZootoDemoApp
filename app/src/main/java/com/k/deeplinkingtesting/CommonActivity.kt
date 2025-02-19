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
import com.google.android.gms.ads.MobileAds
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
import com.k.deeplinkingtesting.appopen.OnAdsCallbackListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private var bannerAdUnitId: String = ""


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

        loadNativeAd(nativeAdView)
        iZooto.promptForPushNotifications()

        initializeRemoteConfig()
        loadBannerAds()


        iZooto.enablePulse(this,nestedScrollView, mainLayout, true)

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

    private fun loadBannerAds() {

        val bannerAdUnit = resources.getString(R.string.gam_banner)
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

            GAMAdManager.showInterstitialAd(
                this,
                resources.getString(R.string.gam_interstitial),
                object : OnAdsCallbackListener {
                    override fun onComplete() {
                        super.onComplete()
                        Log.d(TAG, "onComplete.")
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        Log.d(TAG, "onAdImpression.")

                    }

                    override fun onError(var1: Int, var2: String?) {
                        super.onError(var1, var2)
                        Log.e(TAG, "Error: $var1, $var2")
                    }
                })

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
        ///23206713921/izooto_demo/com.k.deeplinkingtesting_native
        //ca-app-pub-9298860897894361/4531740244
        val adLoader = AdLoader.Builder(
            this,
            resources.getString(R.string.gam_native)
        ) // Replace with your ad unit ID
            .forNativeAd { nativeAd ->
                nativeAdView.visibility = View.VISIBLE
                // Populate the native ad into the native ad view
                populateNativeAdView(nativeAd, nativeAdView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    nativeAdView.visibility = View.GONE

                    Log.e("NativeAd", "Failed to load native ad: ${error.message}")

                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set headline
//        adView.findViewById<TextView>(R.id.native_ad_headline).text = nativeAd.headline
//        adView.headlineView = adView.findViewById(R.id.native_ad_headline)


        // Set media
        val mediaView = adView.findViewById<MediaView>(R.id.native_ad_media)
        adView.mediaView = mediaView
        mediaView.setMediaContent(nativeAd.mediaContent)

//        // Set call to action
//        nativeAd.callToAction?.let {
//            val callToActionView = adView.findViewById<Button>(R.id.native_ad_call_to_action)
//            callToActionView.text = it
//            callToActionView.visibility = View.VISIBLE
//            adView.callToActionView = callToActionView
//        } ?: run {
//           // adView.findViewById<Button>(R.id.native_ad_call_to_action).visibility = View.GONE
//        }

        // Set the NativeAd object
        adView.setNativeAd(nativeAd)
    }


}
