package com.codesk.gpsnavigation.model.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.databinding.TravelingModeItemLayoutBinding
import com.codesk.gpsnavigation.model.datamodels.TravellingModeDataModel
import com.codesk.gpsnavigation.utill.commons.TravellingModeItemDiffCallback

class TrvellingModeItemAdapter(mContext: Context, val callback: (Int) -> Unit) :
    RecyclerView.Adapter<TrvellingModeItemAdapter.ViewHolder>() {

    private var TAG = "FamousPlacesItemAdapter"
    private var famousplacesDeatilArrayList = ArrayList<TravellingModeDataModel>()
    var context = mContext

    fun setTravellingModeitemList(_fightList: ArrayList<TravellingModeDataModel>) {
        famousplacesDeatilArrayList = _fightList
        val diffCallback = TravellingModeItemDiffCallback()
        diffCallback.fightDiffCallback(this.famousplacesDeatilArrayList, _fightList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: TravelingModeItemLayoutBinding,
        val callback: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: TravellingModeDataModel, context: Context) {
            binding.apply {
                tvTravelMode.text = dataModel.travellingModeTitle
                Glide.with(context)
                    .load(dataModel.imageResource)
                    .into(ivTravelMode)

                cardviewOuterLayout.setOnClickListener {
                    updatedapter(adapterPosition)
                    callback.invoke(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            TravelingModeItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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
        val travelModeImage = holder.itemView.findViewById<AppCompatImageView>(R.id.iv_travel_mode)
        val travelModeTitle = holder.itemView.findViewById<AppCompatTextView>(R.id.tv_travel_mode)
        if (famousplacesDeatilArrayList[position].isSelected) {
            travelModeTitle.visibility = View.VISIBLE
            travelModeImage.setColorFilter(
                ContextCompat.getColor(context, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
            travelModeTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            travelModeTitle.visibility = View.INVISIBLE
            travelModeTitle.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.white_less_opccity
                )
            )
            travelModeImage.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.white_less_opccity
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
        holder.bind(fight, context)
    }

    fun updatedapter(position: Int) {
        for (i in 0 until famousplacesDeatilArrayList.size) {
            famousplacesDeatilArrayList[i].isSelected = i == position
        }
        notifyDataSetChanged()
    }

}