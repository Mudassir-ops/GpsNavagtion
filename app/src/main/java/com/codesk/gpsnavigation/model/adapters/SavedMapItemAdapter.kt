package com.codesk.gpsnavigation.model.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codesk.gpsnavigation.databinding.FamosPlacesItemLayoutBinding
import com.codesk.gpsnavigation.model.datamodels.SavedMapDataModel
import com.codesk.gpsnavigation.utill.commons.SavedMapItemDiffCallback

class SavedMapItemAdapter(mContext: Context, val callback: (Int) -> Unit) :
    RecyclerView.Adapter<SavedMapItemAdapter.ViewHolder>() {

    private var TAG = "FamousPlacesItemAdapter"
    private var famousplacesDeatilArrayList = ArrayList<SavedMapDataModel>()
    var context = mContext

    fun setFamousPlacesitemList(_fightList: ArrayList<SavedMapDataModel>) {
        famousplacesDeatilArrayList = _fightList
        val diffCallback = SavedMapItemDiffCallback()
        diffCallback.fightDiffCallback(this.famousplacesDeatilArrayList, _fightList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: FamosPlacesItemLayoutBinding,
        val callback: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: SavedMapDataModel, context: Context) {
            binding.apply {
                famousPlacesName.text = dataModel.cityName
                Glide.with(context)
                    .load(dataModel.imageResource)
                    .into(famousPlacesImageview)


                famousPlacesImageview.setOnClickListener {
                    callback.invoke(adapterPosition)
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


}