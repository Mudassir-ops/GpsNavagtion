package com.volumecontrol.volumepanel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "style_panel_table")
data class StylePanelTable(

    @NotNull
    @PrimaryKey
    @SerializedName("stylePanelId")
    var stylePanelId: Int? = 0,

    @SerializedName("stylePanelType")
    var stylePanelType: String? = "",

    @SerializedName("slider_Color")
    var slider_Color: String? = "",

    @SerializedName("slider_Background_Color")
    var slider_Background_Color: String? = "",

    @SerializedName("slider_Type")
    var slider_Type: String? = "",

    @SerializedName("slider_Height")
    var slider_Height: String? = "",

    @SerializedName("slider_CornerRadius")
    var slider_CornerRadius: String? = "",

    @SerializedName("panel_position")
    var panel_position: String? = "",

    @SerializedName("panel_spacing")
    var panel_spacing: String? = "",

    @SerializedName("panel_arrow_position")
    var panel_arrow_position: String? = "",

    @SerializedName("panel_background_color")
    var panel_background_color: String? = "",

    @SerializedName("gradeint_start_color")
    var gradeint_start_color: String? = "",

    @SerializedName("gradeint_center_color")
    var gradeint_center_color: String? = "",

    @SerializedName("gradeint_end_color")
    var gradeint_end_color: String? = "",


    @SerializedName("panel_gradeint_start_color")
    var panel_gradeint_start_color: String? = "",

    @SerializedName("panel_gradeint_center_color")
    var panel_gradeint_center_color: String? = "",

    @SerializedName("panel_gradeint_end_color")
    var panel_gradeint_end_color: String? = "",


    @SerializedName("this_layout_selected")
    var this_layout_selected: Boolean? =false
) {}