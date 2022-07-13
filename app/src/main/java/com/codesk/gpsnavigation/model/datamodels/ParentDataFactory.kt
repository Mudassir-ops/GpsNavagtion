package com.codesk.gpsnavigation.model.datamodels

import java.util.*

object ParentDataFactory {
    private val random = Random()

    private val titles =
        arrayListOf("Pakistan", "India","NewYork", "Canada")

    private fun randomTitle(): String {
        val index = random.nextInt(titles.size)
        return titles[index]
    }

    private fun randomChildren(): List<ChildModel> {
        return ChildDataFactory.getChildren(5)
    }

    fun getParents(count: Int): List<ParentModel> {
        val parents = mutableListOf<ParentModel>()
        repeat(count) {
            val parent = ParentModel(randomTitle(), randomChildren())
            parents.add(parent)
        }
        return parents
    }
}