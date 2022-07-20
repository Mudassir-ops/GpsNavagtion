package com.codesk.gpsnavigation.utill.commons

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.codesk.gpsnavigation.model.datamodels.ParentModel

class FamousPlacesItemDiffCallback : DiffUtil.Callback() {
    private var mOldFightList: List<ParentModel>? = null
    private var mNewFightList: List<ParentModel>? = null

    fun fightDiffCallback(
        oldFightList: List<ParentModel>?,
        newFightList: List<ParentModel>?
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
        return mOldFightList!![oldItemPosition].title === mNewFightList!![newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPatient: ParentModel = mOldFightList!![oldItemPosition]
        val newPatient: ParentModel = mNewFightList!![newItemPosition]
        return oldPatient.title == newPatient.title
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}