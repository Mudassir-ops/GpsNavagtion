package com.codesk.gpsnavigation.model.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codesk.gpsnavigation.databinding.SearchItemLayoutBinding
import com.codesk.gpsnavigation.model.datamodels.SearchItemDataModel
import com.codesk.gpsnavigation.utill.commons.SearchItemDiffCallback
import java.util.*

class SearchItemAdapter(mContext: Context, val callback: (Int,Double,Double,String) -> Unit) :
    RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

    private var TAG = "SearchItemAdapter"
    private var searchArrayList = ArrayList<SearchItemDataModel>()
    private var searchArrayListSecond: ArrayList<SearchItemDataModel>? = null
    var context = mContext


    fun setSearchitemList(_fightList: ArrayList<SearchItemDataModel>) {
        searchArrayList = _fightList
        searchArrayListSecond = ArrayList()
        searchArrayListSecond!!.addAll(searchArrayList)

        val diffCallback = SearchItemDiffCallback()
        diffCallback.fightDiffCallback(this.searchArrayList, _fightList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

/*    fun clear() {
        val size: Int = searchArrayList.size
        searchArrayList.clear()
        notifyItemRangeRemoved(0, size)
    }*/


    inner class ViewHolder(private val binding: SearchItemLayoutBinding, val callback: (Int,Double,Double,String) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: SearchItemDataModel,context:Context) {
            binding.apply {
                tvSearchView.text = dataModel.cityName
                cardviewOuterLayout.setOnClickListener {
                    callback.invoke(adapterPosition,dataModel.savedPlaceLatitude!!,dataModel.savedPlaceLongitude!!,dataModel.cityName)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, callback)
    }

    override fun getItemCount(): Int {
        if (searchArrayList.size > 0) {
            return searchArrayList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fight = searchArrayList[position]
        holder.bind(fight,context)
        holder.setIsRecyclable(false)
    }

    fun filter(charText1: String) {
        var charText = charText1
        try {
            charText = charText.toLowerCase(Locale.getDefault())
            searchArrayList.clear()
            if (charText.isEmpty()) {
                searchArrayListSecond?.let { searchArrayList.addAll(it) }
            } else {
                for (fight in this.searchArrayListSecond!!) {
                    if (fight.cityName.toLowerCase(Locale.getDefault())
                            .contains(charText)
                    ) {
                        searchArrayList.add(fight)
                    }
                }
            }
            notifyDataSetChanged()
        } catch (ex: Exception) {
            Log.d(TAG, "filter: $ex")
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}