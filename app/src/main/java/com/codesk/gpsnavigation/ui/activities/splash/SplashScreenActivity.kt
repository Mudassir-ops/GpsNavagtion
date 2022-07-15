package com.codesk.gpsnavigation.ui.activities.splash

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.ActivitySplashScreenBinding
import com.codesk.gpsnavigation.ui.activities.home.MainActivity
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showDialog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding
    private lateinit var locationManager: LocationManager


    private val viewModel: SplashScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        binding.content.apply {
            nextBtn.postDelayed(Runnable {
                binding.content.nextBtn.visibility = View.VISIBLE
                binding.content.circularProgressIndicatr.visibility = View.GONE
            }, 3000)
            nextBtn.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@SplashScreenActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    if (isGpsEnabled()) {
                        val mIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        startActivity(mIntent)
                        finish()
                    } else {
                        itWillEnableGps()
                    }

                } else {
                    //--first dialouge appear
                    this@SplashScreenActivity.showDialog1(
                        title = "Privacy Policy",
                        description = "We need to acess Your Location to use app map feature accurately\n Please allow us to acess your Location",
                        titleOfPositiveButton = "Allow",
                        titleOfNegativeButton = "Cancel",
                        positiveButtonFunction = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE
                                )
                            }
                        },
                    )
                }
            }


            tvSplashPrivacy.setOnClickListener {
                this@SplashScreenActivity.showDialog(
                    title = "Privacy Policy",
                    description = resources.getString(R.string.text_privacy_policy),
                    titleOfPositiveButton = "yes",
                    titleOfNegativeButton = "No",
                    positiveButtonFunction = {

                    })
            }
        }

    }

    fun Context.showDialog1(
        title: String,
        description: String,
        titleOfPositiveButton: String? = null,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.permession_denyied_dialouge)

        val dialogPositiveButton = dialog.findViewById(R.id.btn_save) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.btn_cancel) as AppCompatButton
        val dialogDescription = dialog.findViewById(R.id.edt_save_title) as AppCompatTextView

        dialogDescription.text = description

        titleOfPositiveButton?.let { dialogPositiveButton.text = it }
            ?: dialogPositiveButton.isGone
        titleOfNegativeButton?.let { dialogNegativeButton.text = it }
            ?: dialogNegativeButton.isGone

        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialogNegativeButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        private const val REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 34
        const val REQUEST_CHECK_SETTINGS = 100
    }

    fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGpsEnabled()) {
                    val mIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(mIntent)
                    finish()
                } else {
                    itWillEnableGps()
                }
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                val permission = permissions[0]
                val showRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                if (!showRationale) {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                    if (!showRationale) {
                        this.showDialog1(
                            title = "Privacy Policy",
                            description = "You Have denied permession twice \nSo now you have to provide \n Permession manually through settings.\n Go to settings by clicking go",
                            titleOfPositiveButton = "Go",
                            titleOfNegativeButton = "Cancel",
                            positiveButtonFunction = {
                                val i = Intent()
                                i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                i.addCategory(Intent.CATEGORY_DEFAULT)
                                i.data = Uri.parse("package:" + packageName)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                startActivity(i)
                            })
                    }
                }

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {

                    val mIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }

                AppCompatActivity.RESULT_CANCELED ->
                    Toast.makeText(
                        this,
                        "User has clicked on NO, THANKS - So GPS is still off.",
                        Toast.LENGTH_SHORT
                    ).show()
                else -> {}
            }

        }


    }

    fun itWillEnableGps() {
        val locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

        // task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        val task = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())
        task.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
                Log.d("sadasda", "onCreate: $response")
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().

                            resolvable.startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }

    }

}