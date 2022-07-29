package com.codesk.gpsnavigation.utill.commons

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.codesk.gpsnavigation.model.datamodels.TravellingModeDataModel

class TravellingModeItemDiffCallback : DiffUtil.Callback(){
    private var mOldFightList: List<TravellingModeDataModel>? = null
    private var mNewFightList: List<TravellingModeDataModel>? = null

    fun fightDiffCallback(
        oldFightList: List<TravellingModeDataModel>?,
        newFightList: List<TravellingModeDataModel>?
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
        return mOldFightList!![oldItemPosition].travellingModeTitle === mNewFightList!![newItemPosition].travellingModeTitle
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPatient: TravellingModeDataModel = mOldFightList!![oldItemPosition]
        val newPatient: TravellingModeDataModel = mNewFightList!![newItemPosition]
        return oldPatient.travellingModeTitle == newPatient.travellingModeTitle
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}