package com.codesk.gpsnavigation.model.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.model.datamodels.ParentModel
import com.codesk.gpsnavigation.utill.commons.FamousPlacesItemDiffCallback
import java.util.*

class ParentAdapter(mContext: Context, val callback: (Int) -> Unit) :
    RecyclerView.Adapter<ParentAdapter.ViewHolder>() {

    private var TAG = "ParentAdapterFamousPlaces"
    private var searchArrayList = ArrayList<ParentModel>()
    private var searchArrayListSecond: ArrayList<ParentModel>? = null
    var context = mContext
    private val viewPool = RecyclerView.RecycledViewPool()


    fun setSearchitemList(_fightList: ArrayList<ParentModel>) {
        searchArrayList = _fightList
        searchArrayListSecond = ArrayList()
        searchArrayListSecond!!.addAll(searchArrayList)

        val diffCallback = FamousPlacesItemDiffCallback()
        diffCallback.fightDiffCallback(this.searchArrayList, _fightList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_recylerview, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return searchArrayList.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val parent = searchArrayList[position]
        holder.textView.text = parent.title
        val childLayoutManager =
            LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
        childLayoutManager.initialPrefetchItemCount = 4
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = ChildAdapter(parent.children)
            setRecycledViewPool(viewPool)
        }
        holder.seeAllBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(SELECTED_POSITION, position)
            it.findNavController().navigate(R.id.navigation_famous_places_detail,bundle)
        }
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
                    if (fight.title.toLowerCase(Locale.getDefault())
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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.rv_child)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val seeAllBtn: TextView = itemView.findViewById(R.id.see_all_btn)
    }

    companion object {

        val SELECTED_POSITION = "SELECTED_POSITION"
    }

}