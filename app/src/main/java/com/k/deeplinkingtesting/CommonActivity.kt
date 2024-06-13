package com.k.deeplinkingtesting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.izooto.AppConstant
import com.izooto.PreferenceUtil
import com.izooto.iZooto
import java.util.Locale

// common activity add to launcher activity
class CommonActivity : AppCompatActivity() {
    var permissionFile: Button? = null
    var beginDebugFile: Button? = null
    var sendDebugFile: Button? = null
    var deleteDebugFile: Button? = null
    var mainLayout : CoordinatorLayout?=null
     var backPressedOnce = false
     var isChecked = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_common)

        permissionFile = findViewById(R.id.btn_permissionFIle)
        beginDebugFile = findViewById(R.id.btn_beginDebugFile)
        sendDebugFile = findViewById(R.id.btn_sendDebugFile)
        deleteDebugFile = findViewById(R.id.btn_deleteDebugFile)
        permissionFile = findViewById(R.id.btn_permissionFIle)
        mainLayout = findViewById(R.id.mainLayout);
        //pulse
        iZooto.enablePulse(this,mainLayout,true)
        permissionFile?.setOnClickListener { view ->
            (view as? Button)?.let {
                requestPermission()
            }
        }
      //  iZooto.requestOneTapActivity(this@CommonActivity)
       beginDebugFile?.setOnClickListener { view ->
           (view as? Button)?.let {
               val builder1 = AlertDialog.Builder(this@CommonActivity)
               builder1.setMessage("Are you begin the debug?")
               builder1.setCancelable(true)
               builder1.setPositiveButton(
                   "Yes"
               ) { dialog: DialogInterface, id: Int ->
                   iZooto.createDirectory(this@CommonActivity)
                   dialog.cancel()
               }
               builder1.setNegativeButton(
                   "No"
               ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
               val alert11 = builder1.create()
               alert11.show()
            }
       }
       sendDebugFile?.setOnClickListener { view ->
           (view as Button).let{
               val builder1 = AlertDialog.Builder(this@CommonActivity)
               builder1.setMessage("Are you share the debug info?")
               builder1.setCancelable(true)
               builder1.setPositiveButton(
                   "Yes"
               ) { dialog: DialogInterface, id: Int ->
                   iZooto.shareFile(this@CommonActivity,"Support Team","amit@datability.co")
                   dialog.cancel()
               }
               builder1.setNegativeButton(
                   "No"
               ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
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
               ) { dialog: DialogInterface, id: Int ->
                   iZooto.deleteDirectory(this@CommonActivity)
                   dialog.cancel()
               }
               builder1.setNegativeButton(
                   "No"
               ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
               val alert11 = builder1.create()
               alert11.show()

           }
       }
        val sendButton = findViewById<Button>(R.id.btn_news_hub)
        sendButton.setOnClickListener { view: View? -> sendEmail() }
    }



    private fun sendEmail() {
        val to = arrayOf("amit@datability.co")
        val preferenceUtil = PreferenceUtil.getInstance(this)
        if (!preferenceUtil.getStringData(AppConstant.FCM_DEVICE_TOKEN).isEmpty()) {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.setType("message/rfc822")
            emailIntent.setData(Uri.parse("mailto:"))
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Device Token ")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Device token -- " + preferenceUtil.getStringData(AppConstant.FCM_DEVICE_TOKEN))
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
    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            backPressedOnce = false
        } else if (!backPressedOnce && !isChecked) {
            backPressedOnce = false
            isChecked = true
             // iZooto.enablePulse(this, true)
        } else {
            // iZooto.closeDrawer()
            backPressedOnce = true
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
}
