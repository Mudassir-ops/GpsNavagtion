package com.codesk.gpsnavigation.ui.fragments.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.FragmentSearchBottomNavigationBinding
import com.codesk.gpsnavigation.model.adapters.SearchItemAdapter
import com.codesk.gpsnavigation.model.datamodels.SearchItemDataModel
import java.io.File
import java.io.IOException
import java.util.*

@RequiresApi(api = Build.VERSION_CODES.M)
class SearchBottomNavFragment : Fragment() {


    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileName: String? = null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val RECORD_AUDIO_REQUEST_CODE = 101
    private var isPlaying = false


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
                        getPermissionToRecordAudio()
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
                //  Toast.makeText(requireContext(), "Permession Granted", Toast.LENGTH_SHORT).show()
                startVoiceRecognition()
            } else {
                Toast.makeText(
                    requireContext(),
                    "You must give permissions to use this app. App is exiting.",
                    Toast.LENGTH_SHORT
                ).show()

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






}