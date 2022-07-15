package com.codesk.gpsnavigation.ui.fragments.search

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentSearchBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.SearchItemAdapter
import com.codesk.gpsnavigation.model.datamodels.SearchItemDataModel
import com.codesk.gpsnavigation.ui.fragments.nearby.NearbyBottomNavFragment.Companion.TAG
import com.codesk.gpsnavigation.utill.commons.AppConstants
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.util.*


@RequiresApi(api = Build.VERSION_CODES.M)
class SearchBottomNavFragment : Fragment() {


    private val RECORD_AUDIO_REQUEST_CODE = 101

    private var mCurrentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mRequestingLocationUpdates: Boolean? = false
    private var locationRequest: LocationRequest? = null

    private var locationCallback1: LocationCallback? = null

    private var _binding: FragmentSearchBottomNavigationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterSearchItemAdapter: SearchItemAdapter
    var searcItemList = ArrayList<SearchItemDataModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomNavigationBinding.inflate(inflater, container, false)
        binding.headerLayout.backImageview.visibility = View.VISIBLE

        binding.apply {
            adapterSearchItemAdapter = SearchItemAdapter(requireContext()) {
                findNavController().navigate(R.id.navigation_search_place_map)

            }
            rvSearch.apply {
                adapter = adapterSearchItemAdapter
            }

            headerLayout.micImageviewOuterLayout.setOnClickListener {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.RECORD_AUDIO
                            )
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            startVoiceRecognition()
                        } else {
                            //--first dialouge appear
                            requireContext().showDialog(
                                title = "Privacy Policy",
                                description = "We need to acess Your Voice Recroder to use voice recognition \n Please allow us to acess your Voice Recorder",
                                titleOfPositiveButton = "Allow",
                                titleOfNegativeButton = "Cancel",
                                positiveButtonFunction = {
                                    getPermissionToRecordAudio()
                                },

                                )


                        }


                    }
            }


            headerLayout.searchTextview.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun afterTextChanged(editable: Editable) {
                    val text: String = headerLayout.searchTextview.text.toString()
                        .lowercase(Locale.getDefault())
                    adapterSearchItemAdapter.filter(text)

                }
            })
            btnRecentSearch.setOnClickListener {
                adapterSearchItemAdapter.setSearchitemList(gerSearchData())
            }
            btnClearSearch.setOnClickListener {
                searcItemList.clear()
                adapterSearchItemAdapter.setSearchitemList(searcItemList)
            }

            headerLayout.backImageview.setOnClickListener {
                //------initial fragment
            }

        }
        adapterSearchItemAdapter.setSearchitemList(gerSearchData())
        return binding.root
    }

    private fun gerSearchData(): ArrayList<SearchItemDataModel> {
        searcItemList.clear()
        searcItemList.add(SearchItemDataModel(cityName = "Rawalpindi"))
        searcItemList.add(SearchItemDataModel(cityName = "Islamabad"))
        searcItemList.add(SearchItemDataModel(cityName = "Lahore"))
        searcItemList.add(SearchItemDataModel(cityName = "Karachi"))
        searcItemList.add(SearchItemDataModel(cityName = "Faislabad"))

        return searcItemList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if(AppConstants.mCurrentLocation==null){
            setUpLocationListener()
        }

    }

    private fun getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid checking the build version since Context.checkSelfPermission(...) is only available in Marshmallow
        // 2) Always check for permission (even if permission has already been granted) since the user can revoke permissions at any time through Settings


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), RECORD_AUDIO_REQUEST_CODE
            )
        }
    }

    companion object {
        val RECORD_AUDIO_REQUEST_CODE = 101
    }

    // Callback with the request from calling requestPermissions(...)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startVoiceRecognition()
            } else if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                val permission = permissions[1]
                val showRationale = shouldShowRequestPermissionRationale(permission)
                if (!showRationale) {
                    val showRationale = shouldShowRequestPermissionRationale(permission)
                    if (!showRationale) {
                        requireContext().showDialog(
                            title = "Privacy Policy",
                            description = "You Have denied permession twice \nSo now you have to provide \n Permession manually through settings.\n Go to settings by clicking go",
                            titleOfPositiveButton = "Go",
                            titleOfNegativeButton = "Cancel",
                            positiveButtonFunction = {
                                val i = Intent()
                                i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                i.addCategory(Intent.CATEGORY_DEFAULT)
                                i.data = Uri.parse("package:" + requireContext().packageName)
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                requireContext().startActivity(i)
                            })
                    }
                }


            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 3012 && resultCode === AppCompatActivity.RESULT_OK) {
            val matches: ArrayList<String> = data!!.getStringArrayListExtra("android.speech.extra.RESULTS")!!
            val result = matches[0]
            //Consume result

            binding.headerLayout.searchTextview.setText("$result")
            Log.d("TextENter", "onActivityResult: $result")
        }
    }

    fun startVoiceRecognition() {
        val intent = Intent("android.speech.action.RECOGNIZE_SPEECH")
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form")
        intent.putExtra("android.speech.extra.PROMPT", "Speak Now")
        startActivityForResult(intent, 3012)
    }


    fun Context.showDialog(
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
        val dialogDescription = dialog.findViewById(R.id.edt_save_title) as TextView

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

    private fun setUpLocationListener() {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (mCurrentLocation == null) {

            locationRequest = LocationRequest.create().apply {
                interval = 100
                fastestInterval = 50
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 100
                smallestDisplacement = 0f

            }

            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest!!).build()

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

             locationCallback1= object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    mCurrentLocation = locationResult.lastLocation
                    mRequestingLocationUpdates = true
                    AppConstants.mCurrentLocation=mCurrentLocation
                    Log.d("CurrentLocation", "onLocationResult:$mCurrentLocation ")

                }
            }
            stopLocationUpdates()
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest!!,
                locationCallback1!!,
                Looper.myLooper()
            );


            /*      fusedLocationProviderClient.requestLocationUpdates(
                      locationRequest!!, object : LocationCallback() {
                          override fun onLocationResult(locationResult: LocationResult) {
                              super.onLocationResult(locationResult)
                              mCurrentLocation = locationResult.lastLocation
                              mRequestingLocationUpdates = true

                              Log.d("CurrentLocation", "onLocationResult:$mCurrentLocation ")

                          }
                      },
                      Looper.myLooper()!!
                  )*/

        }

    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        if(AppConstants.mCurrentLocation!=null){
            try {
                val voidTask: Task<Void> =
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback1!!)
                if (voidTask.isSuccessful()) {
                    Log.d(TAG, "StopLocation updates successful! ")
                } else {
                    Log.d(TAG, "StopLocation updates unsuccessful! " + voidTask.toString())
                }
            } catch (exp: SecurityException) {
                Log.d(TAG, " Security exception while removeLocationUpdates")
            }
        }

    }
}