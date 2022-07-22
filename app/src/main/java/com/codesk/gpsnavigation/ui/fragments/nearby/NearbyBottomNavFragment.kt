package com.codesk.gpsnavigation.ui.fragments.nearby

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentNearbyBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.NearByItemAdapter
import com.codesk.gpsnavigation.model.datamodels.NearByItemDataModel
import com.codesk.gpsnavigation.utill.commons.AppConstants
import com.codesktech.volumecontrol.utills.commons.CommonFunctions
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showNoInternetDialog
import java.util.*

class NearbyBottomNavFragment : Fragment() {

    private var _binding: FragmentNearbyBottomNavigationBinding? = null
    private val binding get() = _binding!!


    private lateinit var nearByItemAdapter: NearByItemAdapter
    var searcItemList = ArrayList<NearByItemDataModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val nearbyBottomNavViewModel =
            ViewModelProvider(this).get(NearbyBottomNavViewModel::class.java)

        _binding = FragmentNearbyBottomNavigationBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.apply {

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


            nearByItemAdapter =
                NearByItemAdapter(requireContext()) { _position: Int, _placeName: String ->
                    Log.d(TAG, "onCreateView: $_position")
                    if (CommonFunctions.checkForInternet(requireContext())){
                        val bundle = Bundle()
                        bundle.putString(SELECTEDTYPE, _placeName)
                        bundle.putInt(SELECTEDPPOSITION, _position)
                        findNavController().navigate(R.id.navigation_nearby_place_detail, bundle)
                    }else{
                        requireContext().showNoInternetDialog(
                            title = "Privacy Policy",
                            description = "",
                            titleOfPositiveButton = "",
                            titleOfNegativeButton = "",
                            positiveButtonFunction = {
                                val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                                startActivityForResult(panelIntent,402)
                            }
                        )
                    }


                }
            rvNearByItem.apply {
                adapter = nearByItemAdapter
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
                    nearByItemAdapter.filter(text)

                }
            })


        }
        nearByItemAdapter.setNearByitemList(getNearByData())



        return root
    }

    private fun getNearByData(): ArrayList<NearByItemDataModel> {
        searcItemList.clear()
        searcItemList.add(NearByItemDataModel(cityName = "Airport", R.drawable.airplane))
        searcItemList.add(NearByItemDataModel(cityName = "PetrolStation", R.drawable.petrol_stattion))
        searcItemList.add(NearByItemDataModel(cityName = "Hospital", R.drawable.hospital))
        searcItemList.add(NearByItemDataModel(cityName = "Bank", R.drawable.bank))
        searcItemList.add(NearByItemDataModel(cityName = "RealEstate", R.drawable.realestate))
        searcItemList.add(NearByItemDataModel(cityName = "Furniture", R.drawable.furniture))
        searcItemList.add(NearByItemDataModel(cityName = "Computer", R.drawable.computer))
        searcItemList.add(NearByItemDataModel(cityName = "Electronics", R.drawable.electronics))
        searcItemList.add(NearByItemDataModel(cityName = "Restaurant", R.drawable.resturant))
        searcItemList.add(NearByItemDataModel(cityName = "Pet", R.drawable.pet))
        searcItemList.add(NearByItemDataModel(cityName = "Forest", R.drawable.forest))
        searcItemList.add(NearByItemDataModel(cityName = "Cafeteria", R.drawable.cafeteria))
        searcItemList.add(NearByItemDataModel(cityName = "Court", R.drawable.court))
        searcItemList.add(NearByItemDataModel(cityName = "Bakery", R.drawable.bakery))
        searcItemList.add(NearByItemDataModel(cityName = "Shop", R.drawable.shop))
        searcItemList.add(NearByItemDataModel(cityName = "Library", R.drawable.library))
        searcItemList.add(NearByItemDataModel(cityName = "GlassShop", R.drawable.glass_shop))
        searcItemList.add(NearByItemDataModel(cityName = "GYM", R.drawable.gym))
        searcItemList.add(NearByItemDataModel(cityName = "SalonShop", R.drawable.salon_shop))
        searcItemList.add(NearByItemDataModel(cityName = "Carpenter", R.drawable.carpenter))
        searcItemList.add(NearByItemDataModel(cityName = "PostOffice", R.drawable.post_office))
        searcItemList.add(NearByItemDataModel(cityName = "Hardware", R.drawable.hardware))
        searcItemList.add(NearByItemDataModel(cityName = "Pharmacy", R.drawable.pharmacy))
        searcItemList.add(NearByItemDataModel(cityName = "School", R.drawable.university))
        searcItemList.add(NearByItemDataModel(cityName = "Ambulance", R.drawable.ambulance))
        searcItemList.add(NearByItemDataModel(cityName = "Water", R.drawable.water))
        searcItemList.add(NearByItemDataModel(cityName = "College", R.drawable.university))
        searcItemList.add(NearByItemDataModel(cityName = "University", R.drawable.university))
        searcItemList.add(NearByItemDataModel(cityName = "Painter", R.drawable.painter))
        searcItemList.add(NearByItemDataModel(cityName = "Cinema", R.drawable.cineam))
        searcItemList.add(NearByItemDataModel(cityName = "Jewelry", R.drawable.jewelery))
        searcItemList.add(NearByItemDataModel(cityName = "Taxi", R.drawable.taxi))

        return searcItemList
    }

    companion object {
        val SELECTEDTYPE = "SELECTEDTYPE"
        val SELECTEDPPOSITION = "SELECTEDPPOSITION"
        val TAG = "NearbyFragmentTAG"
        val RECORD_AUDIO_REQUEST_CODE = 101
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${AppConstants.mCurrentLocation}")

    }

    private fun Context.showDialog(
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
        }else if(requestCode==402){

        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent("android.speech.action.RECOGNIZE_SPEECH")
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form")
        intent.putExtra("android.speech.extra.PROMPT", "Speak Now")
        startActivityForResult(intent, 3012)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3012 && resultCode == AppCompatActivity.RESULT_OK) {
            val matches: ArrayList<String> =
                data!!.getStringArrayListExtra("android.speech.extra.RESULTS")!!
            val result = matches[0]
            //Consume result
            binding.headerLayout.searchTextview.setText("$result")

        }
    }
}