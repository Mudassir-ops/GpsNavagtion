package com.codesk.gpsnavigation.model.datamodels

object ParentDataFactory {
    private val titles =
        arrayListOf("Pakistan", "Australia", "Canada", "England", "Switzerland")


    private fun randomChildren(position: Int): List<ChildModel> {
        return ChildDataFactory.getChildren(position)
    }

    fun getParents(count: Int): List<ParentModel> {
        val parents = mutableListOf<ParentModel>()
        parents.clear()
        (0..4).forEach { i -> parents.add(ParentModel(titles[i], randomChildren(i))) }
        return parents
    }
}