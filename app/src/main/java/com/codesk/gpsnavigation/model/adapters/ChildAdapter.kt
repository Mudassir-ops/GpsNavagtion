package com.codesk.gpsnavigation.model.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.codesk.gpsnavigation.R
import com.codesk.gpsnavigation.model.datamodels.ChildModel

class ChildAdapter(private val children: List<ChildModel>) :
    RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.child_recycyclerview, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val child = children[position]
        holder.imageView.setImageResource(child.image)
        holder.textView.text = child.title

        holder.imageView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(ParentAdapter.SELECTED_POSITION, position)
            it.findNavController().navigate(R.id.navigation_famous_places_detail, bundle)
        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.child_textView)
        val imageView: ImageView = itemView.findViewById(R.id.child_imageView)

    }

    interface AdapterCallback {
        fun onItemClicked(position: Int)
    }


}