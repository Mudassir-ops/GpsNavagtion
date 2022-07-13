package com.codesk.gpsnavigation.model.datamodels

import com.codesk.gpsnavigation.R
import java.util.*

object ChildDataFactory {

    private val random = Random()

    private val titles = arrayListOf("Niagra Falls", "Giligt", "Chuitral", "Kashmir", "Waterfall")

    private fun randomTitle(): String {
        val index = random.nextInt(titles.size)
        return titles[index]
    }

    private fun randomImage(): Int {
        return R.drawable.niagra_fall
    }

    fun getChildren(count: Int): List<ChildModel> {
        val children = mutableListOf<ChildModel>()
        repeat(count) {
            val child = ChildModel(randomImage(), randomTitle())
            children.add(child)
        }
        return children
    }


}