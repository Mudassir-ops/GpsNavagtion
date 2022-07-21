package com.volumecontrol.volumepanel.datasource.local.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.volumecontrol.volumepanel.model.StylePanelTable



@Dao
interface StylePanelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStylePanel(stylePanelTable: StylePanelTable)

    @Query("SELECT * FROM style_panel_table")
    suspend fun getAllStylePanel(): List<StylePanelTable>

    @Query("DELETE FROM style_panel_table")
    suspend fun deleteStylePanelTable(): Void

    @Query("UPDATE style_panel_table SET stylePanelType=:panelType WHERE stylePanelId = :stylePanelId")
    fun updateStylePanelType(panelType: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET slider_Type=:SliderType WHERE stylePanelId = :stylePanelId")
    fun updateSliderType(SliderType: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET slider_Height=:SliderHeight WHERE stylePanelId = :stylePanelId")
    fun updateSliderHeight(SliderHeight: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET slider_CornerRadius=:SliderCornerRadius WHERE stylePanelId = :stylePanelId")
    fun updateSliderCornerRadius(SliderCornerRadius: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET panel_position=:panelPosition WHERE stylePanelId = :stylePanelId")
    fun updatePanelPosition(panelPosition: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET panel_spacing=:panelSpacing WHERE stylePanelId = :stylePanelId")
    fun updatePanelSpacing(panelSpacing: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET panel_arrow_position=:arrowPostion WHERE stylePanelId = :stylePanelId")
    fun updatePanelArrowPosition(arrowPostion: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET slider_Color=:sliderColor WHERE stylePanelId = :stylePanelId")
    fun updateSliderColor(sliderColor: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET slider_Background_Color=:slideBackgroundColor WHERE stylePanelId = :stylePanelId")
    fun updateSliderBackgroundColor(slideBackgroundColor: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET panel_background_color=:panelbackgroundColor WHERE stylePanelId = :stylePanelId")
    fun updatePanelBackgroundColor(panelbackgroundColor: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET gradeint_start_color=:gradeint_start_color WHERE stylePanelId = :stylePanelId")
    fun updateSliderGrdientStartCOlor(gradeint_start_color: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET gradeint_center_color=:gradeint_center_color WHERE stylePanelId = :stylePanelId")
    fun updateSliderGrdientCenterColor(gradeint_center_color: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET gradeint_end_color=:gradeint_end_color WHERE stylePanelId = :stylePanelId")
    fun updateSliderGrdientEndColor(gradeint_end_color: String?, stylePanelId: Int)


    @Query("UPDATE style_panel_table SET panel_gradeint_start_color=:gradeint_start_color WHERE stylePanelId = :stylePanelId")
    fun updatePanelGrdientStartCOlor(gradeint_start_color: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET panel_gradeint_center_color=:gradeint_center_color WHERE stylePanelId = :stylePanelId")
    fun updatePanelGrdientCenterColor(gradeint_center_color: String?, stylePanelId: Int)

    @Query("UPDATE style_panel_table SET panel_gradeint_end_color=:gradeint_end_color WHERE stylePanelId = :stylePanelId")
    fun updatepanelGrdientEndColor(gradeint_end_color: String?, stylePanelId: Int)

    //---panel gradient colors
    @Query("SELECT panel_gradeint_start_color ,panel_gradeint_center_color,panel_gradeint_end_color,gradeint_start_color,gradeint_center_color,gradeint_end_color FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getPanelGraeintColors(stylePanelId: Int): LiveData<StylePanelTable>


    //---slider color and background color
    @Query("SELECT slider_Color ,slider_Background_Color FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getSliderColors(stylePanelId: Int): LiveData<StylePanelTable>

    //---slider Type
    @Query("SELECT slider_Type  FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getSliderType(stylePanelId: Int): LiveData<StylePanelTable>

    //---slider Height
    @Query("SELECT slider_Height  FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getSliderHeight(stylePanelId: Int): LiveData<StylePanelTable>

    //---slider Height
    @Query("SELECT slider_CornerRadius  FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getSliderCornerRadius(stylePanelId: Int): LiveData<StylePanelTable>

    //---slider Height
    @Query("SELECT panel_background_color  FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getPanelbackground(stylePanelId: Int): LiveData<StylePanelTable>

    //---slider Height
    @Query("SELECT panel_position  FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getPanelPosition(stylePanelId: Int): LiveData<StylePanelTable>

    //---slider Height
    @Query("SELECT panel_arrow_position  FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getArrowPosition(stylePanelId: Int): LiveData<StylePanelTable>

    //---slider Height
    @Query("SELECT panel_spacing  FROM style_panel_table WHERE stylePanelId = :stylePanelId")
    fun getLayoutSpacing(stylePanelId: Int): LiveData<StylePanelTable>


    @Query("UPDATE style_panel_table SET this_layout_selected=:isSelected WHERE stylePanelId = :stylePanelId")
    fun isCurrentLayoutSelcetd(isSelected: Boolean?, stylePanelId: Int)


    @Query("SELECT * FROM style_panel_table  ")
    fun getCurrentLayoutSelcetd(): LiveData<List<StylePanelTable>>


    @Query("SELECT * FROM style_panel_table WHERE this_layout_selected ")
    fun getAnyLayoutSelectedOrNot(): LiveData<List<StylePanelTable>>


    @Query("UPDATE style_panel_table SET stylePanelId=:stylePanelId, stylePanelType=:stylePanelType,slider_Color=:slider_Color,slider_Background_Color=:slider_Background_Color,slider_Type=:slider_Type,slider_Height=:slider_Height,slider_CornerRadius=:slider_CornerRadius,panel_position=:panel_position,panel_spacing=:panel_spacing,panel_arrow_position=:panel_arrow_position,panel_background_color=:panel_background_color,gradeint_start_color=:gradient_start_color,gradeint_center_color=:gradient_center_color,gradeint_end_color=:gradient_end_color WHERE stylePanelId = :stylePanelId")
    fun resetPanelValues(
        stylePanelId: Int,
        stylePanelType: String,
        slider_Color: String,
        slider_Background_Color: String,
        slider_Type: String,
        slider_Height: String,
        slider_CornerRadius: String,
        panel_position: String,
        panel_spacing: String,
        panel_arrow_position: String,
        panel_background_color: String,
        gradient_start_color: String,
        gradient_center_color: String,
        gradient_end_color: String,
    )


}