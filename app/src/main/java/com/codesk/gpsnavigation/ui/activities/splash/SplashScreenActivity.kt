package com.codesk.gpsnavigation.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.ActivitySplashScreenBinding
import com.codesk.gpsnavigation.ui.activities.home.MainActivity
import com.codesktech.volumecontrol.utills.commons.CommonFunctions.Companion.showDialog


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding:ActivitySplashScreenBinding

    private val viewModel: SplashScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.content.apply {
            nextBtn.postDelayed(Runnable {
                binding.content.nextBtn.visibility = View.VISIBLE
                binding.content.circularProgressIndicatr.visibility = View.GONE
            }, 3000)
            nextBtn.setOnClickListener {
                val mIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(mIntent)
                finish()
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
}