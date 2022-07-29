package com.codesk.gpsnavigation.utill.commons


import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.codesk.gpsnavigation.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class CommonFunctions {

    companion object {
        private var progressDialog: ProgressDialog? = null


        // stylesMuiLayout.stylesLayout.background= CommonFunctions.getDrawable(resources,R.drawable.styles_item_checked_background,null)
        @Throws(NotFoundException::class)
        fun getDrawable(res: Resources, id: Int, theme: Theme?): Drawable? {
            val version = Build.VERSION.SDK_INT
            return if (version >= 21) {
                ResourcesCompat.getDrawable(res, id, theme)
            } else {
                res.getDrawable(id)
            }
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
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.privacy_policy_layout)
            val dialogTitle = dialog.findViewById(R.id.tv_privacy) as TextView
            val dialogDescription = dialog.findViewById(R.id.privacypolicy) as TextView
            val dialogPositiveButton = dialog.findViewById(R.id.imgclose) as TextView

            dialogTitle.text = title
            dialogDescription.text = description
//            val s: String = format(this.resources.getString(com.codesktech.volumecontrol.R.string.text_privacy_policy))
//            dialogDescription.text = getSpannedText(s)


            titleOfPositiveButton?.let { dialogPositiveButton.text = it }
                ?: dialogPositiveButton.isGone
            //  titleOfNegativeButton?.let { dialogNegativeButton.text = it } ?: dialogNegativeButton.isGone
            dialogPositiveButton.setOnClickListener {
                positiveButtonFunction?.invoke()
                dialog.dismiss()
            }
//            dialogNegativeButton.setOnClickListener {
//                negativeButtonFunction?.invoke()
//                dialog.dismiss()
//            }
            dialog.show()
        }


        fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            observe(lifecycleOwner, object : Observer<T> {
                override fun onChanged(t: T?) {
                    observer.onChanged(t)
                    removeObserver(this)
                }
            })
        }

        fun showMessage(view: View, message: String) {
            val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snack.show()
        }

        fun convertServerDateToAppFormat(dateFormat: String): String {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM")
            return formatter.format(parser.parse(dateFormat))
        }

        fun convertDateOfBirthDateFormat(dateFormat: String): String {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM, yyyy")
            return formatter.format(parser.parse(dateFormat))
        }

        private fun getSpannedText(text: String): Spanned? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(text)
            }
        }

        fun localAppDateFormat(dateFormat: String): String {
            return if (dateFormat.contains("-")) {
                val parser = SimpleDateFormat("dd-MMM-yyyy")
                val formatter = SimpleDateFormat("dd MMM")
                //    formatter.isLenient=true
                formatter.format(parser.parse(dateFormat))
            } else if (dateFormat.contains(",")) {
                val parser = SimpleDateFormat("MMM dd,yyyy")
                val formatter = SimpleDateFormat("dd MMM")
                //    formatter.isLenient=true
                formatter.format(parser.parse(dateFormat))
            } else {
                val parser = SimpleDateFormat("dd MMM yyyy")
                val formatter = SimpleDateFormat("dd MMM")
                formatter.format(parser.parse(dateFormat))
            }
        }

        inline fun <reified T : Activity> Activity.lunchActivity(
            options: Bundle? = null, noinline init: Intent.() -> Unit = {}
        ) {
            val intent = Intent(this, T::class.java)
            intent.init()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent, options)
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        inline fun <reified T : Any> newIntent(context: Activity): Intent =
            Intent(context, T::class.java)

        inline fun <reified T : Any> Context.launchActivity(
            options: Bundle? = null,
            noinline init: Intent.() -> Unit = {}
        ) {

            val intent = newIntent<T>(this)
            intent.init()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options)
            } else {
                startActivity(intent)
            }
        }

        inline fun <reified T : Any> newIntent(context: Context): Intent =
            Intent(context, T::class.java)


        fun showProgressDialog(
            mContext: Context?,
            message: String?
        ) {
            progressDialog = ProgressDialog(mContext)
            progressDialog!!.setMessage(message)
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        fun hideProgressDialog() {
            progressDialog!!.dismiss()
        }


        suspend fun deleteAllTable() {

        }


        fun getAdddressByLatLng(context: Context, MyLat: Double, MyLong: Double): List<Address> {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(MyLat, MyLong, 1)

            val cityName: String = addresses[0].getAddressLine(0)
            val locality: String = addresses[0].locality
            val countryName: String = addresses[0].countryName

            val stringBuilder = StringBuilder()
            stringBuilder.append("cityName")
            stringBuilder.append(cityName)
            stringBuilder.append("locality")
            stringBuilder.append(locality)
            stringBuilder.append("countryName")
            stringBuilder.append(countryName)
            return addresses
        }

        fun checkForInternet(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false

                val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
                return when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                @Suppress("DEPRECATION") val networkInfo =
                    connectivityManager.activeNetworkInfo ?: return false
                @Suppress("DEPRECATION")
                return networkInfo.isConnected
            }
        }

        fun Context.showNoInternetDialog(
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
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.no_internet_connection_dialouge_layout)

            val dialogPositiveButton = dialog.findViewById(R.id.btn_save) as AppCompatButton

            dialogPositiveButton.setOnClickListener {
                positiveButtonFunction!!.invoke()
                dialog.dismiss()
            }

            dialog.show()
        }



    }
}