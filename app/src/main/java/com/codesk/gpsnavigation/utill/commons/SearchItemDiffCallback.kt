package com.codesk.gpsnavigation.utill.commons

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.codesk.gpsnavigation.model.datamodels.SearchItemDataModel

class SearchItemDiffCallback : DiffUtil.Callback(){
    private var mOldFightList: List<SearchItemDataModel>? = null
    private var mNewFightList: List<SearchItemDataModel>? = null

    fun fightDiffCallback(
        oldFightList: List<SearchItemDataModel>?,
        newFightList: List<SearchItemDataModel>?
    ) {
        mOldFightList = oldFightList
        mNewFightList = newFightList
    }

    override fun getOldListSize(): Int {
        return mOldFightList!!.size
    }

    override fun getNewListSize(): Int {
        return mNewFightList!!.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFightList!![oldItemPosition].cityName === mNewFightList!![newItemPosition].cityName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPatient: SearchItemDataModel = mOldFightList!![oldItemPosition]
        val newPatient: SearchItemDataModel = mNewFightList!![newItemPosition]
        return oldPatient.cityName == newPatient.cityName
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}