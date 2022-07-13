package com.codesk.gpsnavigation.utill.commons

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.codesk.gpsnavigation.model.datamodels.FamousPlacesDetailDataModel
import com.codesk.gpsnavigation.model.datamodels.NearByItemDataModel
import com.codesk.gpsnavigation.model.datamodels.SearchItemDataModel

class FamousPlacesDetailItemDiffCallback : DiffUtil.Callback(){
    private var mOldFightList: List<FamousPlacesDetailDataModel>? = null
    private var mNewFightList: List<FamousPlacesDetailDataModel>? = null

    fun fightDiffCallback(
        oldFightList: List<FamousPlacesDetailDataModel>?,
        newFightList: List<FamousPlacesDetailDataModel>?
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
        val oldPatient: FamousPlacesDetailDataModel = mOldFightList!![oldItemPosition]
        val newPatient: FamousPlacesDetailDataModel = mNewFightList!![newItemPosition]
        return oldPatient.cityName == newPatient.cityName
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}