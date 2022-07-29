package com.codesk.gpsnavigation.model.datamodels

import com.codesk.gpsnavigation.R

object ChildDataFactory {

    private val titlesPakistanFamousPlaces = arrayListOf("Hunza valley", "Neelam valley", "Passu cone", "Phanchder lake")
    private val imagesListPaksitanFamousPlaces = arrayListOf(R.drawable.hunzavalley, R.drawable.neelamvallery, R.drawable.passucone, R.drawable.phancder_lake)
    private val latitudeListPaksitanFamousPlaces = arrayListOf(  36.31957297517686, 34.59907892272706,36.45860546466207,36.173469457866155)
    private val lngitudeListPaksitanFamousPlaces = arrayListOf( 74.65071718764615, 73.90733948279369,74.8802507389999,72.94677263496762)

    private val titlesAustraliaFamousPlaces = arrayListOf("Blue Mountains", "Broom Western", "DainTree National", "Sydney Bridge")
    private val imagesListAustraliaFamousPlaces = arrayListOf(R.drawable.blue_mountains_national_park, R.drawable.broome_western, R.drawable.daintree_nationalpark, R.drawable.sydneyharbourbridge)
    private val latitudeListAustraliaFamousPlaces = arrayListOf(  -33.37225467233445, -17.95457190783468,-16.168850908071242,-33.863433909530876)
    private val lngitudeListAustraliaFamousPlaces = arrayListOf( 150.28814571240719, 122.23756801822948,145.41577950307257,151.21022028435164)


    private val titlesCanadaFamousPlaces = arrayListOf("Niagra Fall", "Quebec City", "Tofino City", "whistler")
    private val imagesListCanadaFamousPlaces = arrayListOf(R.drawable.niagrafall, R.drawable.quebeccity, R.drawable.tofino, R.drawable.whistler)
    private val latitudeListCanadaFamousPlaces = arrayListOf(  43.08287886692618, 46.82387386478944,49.13469022098501,50.11909844955745)
    private val lngitudeListCanadaFamousPlaces = arrayListOf( -79.07416290191135, -71.21329652859735,-125.89266792511567,-122.95345145414817)

    private val titlesEnglandFamousPlaces = arrayListOf("Edinburgh", "Inverness", "Roman era bath", "Royal windsor")
    private val imagesListEnglandFamousPlaces = arrayListOf(R.drawable.edinburgh, R.drawable.lochnessandinverness, R.drawable.romanerabath, R.drawable.royalwindsor)
    private val latitudeListEnglandFamousPlaces = arrayListOf(  55.94695623192066, 57.467068267840666,51.381212600997785,51.48859574594411 )
    private val lngitudeListEnglandFamousPlaces = arrayListOf( -3.215983133839371, -4.225806295608513,-2.3596941035007695,-0.6224845016494911)

    private val titlesSwitzerlandFamousPlaces = arrayListOf("Interlaken", "Lake Geneva", "Lucerne", "Matterhorn")
    private val imagesListSwitzerlandFamousPlaces = arrayListOf(R.drawable.interlaken, R.drawable.lakegeneva, R.drawable.lucerne, R.drawable.matterhorn)
    private val latitudeListSwitzerlandFamousPlaces = arrayListOf(  46.670119964531665, 42.592253087752574,47.05358004619934,45.97696062635796)
    private val lngitudeListSwitzerlandFamousPlaces = arrayListOf( 7.874476477225262, -88.4288311184804,8.271146636584225,7.65785105767387)

    fun getChildren(postion: Int): List<ChildModel> {
        val children = mutableListOf<ChildModel>()
        children.clear()

        when (postion) {
            0 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListPaksitanFamousPlaces[i], titlesPakistanFamousPlaces[i], latitudeListPaksitanFamousPlaces[i],lngitudeListPaksitanFamousPlaces[i])) }
            }
            1 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListAustraliaFamousPlaces[i], titlesAustraliaFamousPlaces[i], latitudeListAustraliaFamousPlaces[i],lngitudeListAustraliaFamousPlaces[i])) }
            }
            2 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListCanadaFamousPlaces[i], titlesCanadaFamousPlaces[i], latitudeListCanadaFamousPlaces[i],lngitudeListCanadaFamousPlaces[i])) }
            }
            3 -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListEnglandFamousPlaces[i], titlesEnglandFamousPlaces[i], latitudeListEnglandFamousPlaces[i],lngitudeListEnglandFamousPlaces[i])) }
            }
            else -> {
                (0..3).forEach { i -> children.add(ChildModel(imagesListSwitzerlandFamousPlaces[i], titlesSwitzerlandFamousPlaces[i], latitudeListSwitzerlandFamousPlaces[i],lngitudeListSwitzerlandFamousPlaces[i])) }
            }
        }
        return children
    }


}