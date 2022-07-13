package com.codesk.gpsnavigation.model.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.model.datamodels.ParentModel

class ParentAdapter(private val parents: List<ParentModel>) :
    RecyclerView.Adapter<ParentAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_recylerview, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return parents.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val parent = parents[position]
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
            it.findNavController().navigate(R.id.navigation_famous_places_detail)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.rv_child)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val seeAllBtn: TextView = itemView.findViewById(R.id.see_all_btn)
    }

}