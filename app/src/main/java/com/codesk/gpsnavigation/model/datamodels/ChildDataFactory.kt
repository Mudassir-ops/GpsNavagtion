package com.codesk.gpsnavigation.model.datamodels

import com.codesk.gpsnavigation.R

object ChildDataFactory {

    private val titlesPakistanFamousPlaces = arrayListOf("hunza valley", "neelam valley", "passu cone", "phanchder lake")
    private val imagesListPaksitanFamousPlaces = arrayListOf(R.drawable.hunzavalley, R.drawable.neelamvallery, R.drawable.passucone, R.drawable.phancder_lake)

    private val titlesAustraliaFamousPlaces = arrayListOf("Blue Mountains", "Broom Western", "DainTree National", "Sydney Bridge")
    private val imagesListAustraliaFamousPlaces = arrayListOf(R.drawable.blue_mountains_national_park, R.drawable.broome_western, R.drawable.daintree_nationalpark, R.drawable.sydneyharbourbridge)

    private val titlesCanadaFamousPlaces = arrayListOf("Niagra Fall", "Quebec City", "Tofino City", "whistler")
    private val imagesListCanadaFamousPlaces = arrayListOf(R.drawable.niagrafall, R.drawable.quebeccity, R.drawable.tofino, R.drawable.whistler)

    private val titlesEnglandFamousPlaces = arrayListOf("Edinburgh", "Inverness", "Roman era bath", "Royal windsor")
    private val imagesListEnglandFamousPlaces = arrayListOf(R.drawable.edinburgh, R.drawable.lochnessandinverness, R.drawable.romanerabath, R.drawable.royalwindsor)

    private val titlesSwitzerlandFamousPlaces = arrayListOf("Interlaken", "Lake Geneva", "Lucerne", "Matterhorn")
    private val imagesListSwitzerlandFamousPlaces = arrayListOf(R.drawable.interlaken, R.drawable.lakegeneva, R.drawable.lucerne, R.drawable.matterhorn)


    fun getChildren(postion: Int): List<ChildModel> {
        val children = mutableListOf<ChildModel>()
        children.clear()

        when (postion) {
            0 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListPaksitanFamousPlaces[i], titlesPakistanFamousPlaces[i])) }
            }
            1 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListAustraliaFamousPlaces[i], titlesAustraliaFamousPlaces[i])) }
            }
            2 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListCanadaFamousPlaces[i], titlesCanadaFamousPlaces[i])) }
            }
            3 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListEnglandFamousPlaces[i], titlesEnglandFamousPlaces[i])) }
            }
            else -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListSwitzerlandFamousPlaces[i], titlesSwitzerlandFamousPlaces[i])) }
            }
        }
        return children
    }


}