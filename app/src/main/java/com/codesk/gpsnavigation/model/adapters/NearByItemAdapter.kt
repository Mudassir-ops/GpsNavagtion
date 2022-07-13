package com.codesk.gpsnavigation.model.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesk.gpsnavigation.databinding.NearbyItemLayoutBinding
import com.codesk.gpsnavigation.model.datamodels.NearByItemDataModel
import com.codesk.gpsnavigation.utill.commons.NearByItemDiffCallback
import java.util.*

class NearByItemAdapter(mContext: Context, val callback: (Int) -> Unit) :
    RecyclerView.Adapter<NearByItemAdapter.ViewHolder>() {

    private var TAG = "SearchItemAdapter"
    private var nearByArrayList = ArrayList<NearByItemDataModel>()
    private var nearByArrayListSecond: ArrayList<NearByItemDataModel>? = null
    var context = mContext


    fun setNearByitemList(_fightList: ArrayList<NearByItemDataModel>) {
        nearByArrayList = _fightList
        nearByArrayListSecond = ArrayList()
        nearByArrayListSecond!!.addAll(nearByArrayList)

        val diffCallback = NearByItemDiffCallback()
        diffCallback.fightDiffCallback(this.nearByArrayList, _fightList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }


    inner class ViewHolder(
        private val binding: NearbyItemLayoutBinding,
        val callback: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: NearByItemDataModel, context: Context) {
            binding.apply {
                nearbyPlaceName.text = dataModel.cityName
                Glide.with(context)
                    .load(dataModel.imageResource)
                    .into(nearbyImageview)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = NearbyItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, callback)
    }

    override fun getItemCount(): Int {
        if (nearByArrayList.size > 0) {
            return nearByArrayList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fight = nearByArrayList[position]
        holder.bind(fight, context)
        holder.setIsRecyclable(false)
    }

    fun filter(charText1: String) {
        var charText = charText1
        try {
            charText = charText.toLowerCase(Locale.getDefault())
            nearByArrayList.clear()
            if (charText.isEmpty()) {
                nearByArrayListSecond?.let { nearByArrayList.addAll(it) }
            } else {
                for (fight in this.nearByArrayListSecond!!) {
                    if (fight.cityName.toLowerCase(Locale.getDefault())
                            .contains(charText)
                    ) {
                        nearByArrayList.add(fight)
                    }
                }
            }
            notifyDataSetChanged()
        } catch (ex: Exception) {
            Log.d(TAG, "filter: $ex")
        }
    }
}