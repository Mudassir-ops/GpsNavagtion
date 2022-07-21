package com.codesk.gpsnavigation.model.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesk.gpsnavigation.databinding.FamosPlacesItemLayoutBinding
import com.codesk.gpsnavigation.model.datamodels.FamousPlacesDetailDataModel
import com.codesk.gpsnavigation.utill.commons.FamousPlacesDetailItemDiffCallback
import java.util.*

class FamousPlacesItemAdapter(mContext: Context, val callback: (String,Double,Double) -> Unit) :
    RecyclerView.Adapter<FamousPlacesItemAdapter.ViewHolder>() {

    private var TAG = "FamousPlacesItemAdapter"
    private var famousplacesDeatilArrayList = ArrayList<FamousPlacesDetailDataModel>()
    private var famousPlacesDetailArrayListSecond: ArrayList<FamousPlacesDetailDataModel>? = null
    var context = mContext


    fun setFamousPlacesitemList(_fightList: ArrayList<FamousPlacesDetailDataModel>) {
        famousplacesDeatilArrayList = _fightList
        famousPlacesDetailArrayListSecond = ArrayList()
        famousPlacesDetailArrayListSecond!!.addAll(famousplacesDeatilArrayList)

        val diffCallback = FamousPlacesDetailItemDiffCallback()
        diffCallback.fightDiffCallback(this.famousplacesDeatilArrayList, _fightList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: FamosPlacesItemLayoutBinding,
        val callback: (String,Double,Double) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: FamousPlacesDetailDataModel, context: Context) {
            binding.apply {
                famousPlacesName.text = dataModel.cityName
                Glide.with(context)
                    .load(dataModel.imageResource)
                    .into(famousPlacesImageview)


                famousPlacesImageview.setOnClickListener {
                    callback.invoke(dataModel.cityName,dataModel.latitude!!,dataModel.longitude!!)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            FamosPlacesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, callback)
    }

    override fun getItemCount(): Int {
        if (famousplacesDeatilArrayList.size > 0) {
            return famousplacesDeatilArrayList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fight = famousplacesDeatilArrayList[position]
        holder.bind(fight, context)
        holder.setIsRecyclable(false)
    }

    fun filter(charText1: String) {
        var charText = charText1
        try {
            charText = charText.toLowerCase(Locale.getDefault())
            famousplacesDeatilArrayList.clear()
            if (charText.isEmpty()) {
                famousPlacesDetailArrayListSecond?.let { famousplacesDeatilArrayList.addAll(it) }
            } else {
                for (fight in this.famousPlacesDetailArrayListSecond!!) {
                    if (fight.cityName.toLowerCase(Locale.getDefault())
                            .contains(charText)
                    ) {
                        famousplacesDeatilArrayList.add(fight)
                    }
                }
            }
            notifyDataSetChanged()
        } catch (ex: Exception) {
            Log.d(TAG, "filter: $ex")
        }
    }
}