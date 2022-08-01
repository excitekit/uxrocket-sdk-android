package com.napoleonit.uxrocket.data.models.http

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


@Serializable
@JsonClassDiscriminator("attribute")
sealed class Attribute

@Serializable
@SerialName("text-color")
data class TextColorAttribute(val value: String) : Attribute()

@Serializable
@SerialName("text-size")
data class TextSizeAttribute(val value: String) : Attribute()

@Serializable
@SerialName("text")
data class TextAttribute(val value: String) : Attribute()

@Serializable
@SerialName("enabled")
data class EnabledAttribute(val value: Boolean) : Attribute()

@Serializable
@SerialName("scale-type")
data class ScaleTypeAttribute(val value: ScaleTypeAttributeValue) : Attribute() {
    @Serializable
    enum class ScaleTypeAttributeValue(val nativeValue: ImageView.ScaleType) {
        @SerialName("center")
        CENTER(ImageView.ScaleType.CENTER),

        @SerialName("center_crop")
        CENTER_CROP(ImageView.ScaleType.CENTER_CROP),

        @SerialName("center_inside")
        CENTER_INSIDE(ImageView.ScaleType.CENTER_INSIDE),

        @SerialName("fit_center")
        FIT_CENTER(ImageView.ScaleType.FIT_CENTER),

        @SerialName("fit_end")
        FIT_END(ImageView.ScaleType.FIT_END),

        @SerialName("fit_start")
        FIT_START(ImageView.ScaleType.FIT_START),

        @SerialName("fit_xy")
        FIT_XY(ImageView.ScaleType.FIT_XY),

        @SerialName("matrix")
        MATRIX(ImageView.ScaleType.MATRIX),

    }
}

@Serializable
@SerialName("visibility")
data class VisibilityAttribute(val value: VisibilityAttributeValue) : Attribute() {
    @Serializable
    enum class VisibilityAttributeValue(val nativeValue: Int) {
        @SerialName("visible")
        VISIBLE(View.VISIBLE),

        @SerialName("invisible")
        INVISIBLE(View.INVISIBLE),

        @SerialName("gone")
        GONE(View.GONE),
    }
}

@Serializable
@SerialName("image")
data class ImageAttribute(val value: String) : Attribute()

@Serializable
@SerialName("background-color")
data class BackgroundColorAttribute(val value: String) : Attribute()

@Serializable
@SerialName("alpha")
data class AlphaAttribute(val value: Float) : Attribute()

@Serializable
@SerialName("layout")
data class LayoutAttribute(val value: LayoutAttributeModel) : Attribute() {
    @Serializable
    data class LayoutAttributeModel(val width: String, val height: String)
}

@Serializable
@SerialName("hint-color")
data class HintColorAttribute(val value: String) : Attribute()

@Serializable
@SerialName("hint")
data class HintAttribute(val value: String) : Attribute()

@Serializable
@SerialName("gravity")
data class GravityAttribute(val value: List<GravityAttributeValue>) : Attribute() {

    @Serializable
    enum class GravityAttributeValue(val nativeValue: Int) {
        @SerialName("left")
        LEFT(Gravity.LEFT),

        @SerialName("right")
        RIGHT(Gravity.RIGHT),

        @SerialName("top")
        TOP(Gravity.TOP),

        @SerialName("bottom")
        BOTTOM(Gravity.BOTTOM),

        @SerialName("center")
        CENTER(Gravity.CENTER),

        @SerialName("center-vertical")
        CENTER_VERTICAL(Gravity.CENTER_VERTICAL),

        @SerialName("center-horizontal")
        CENTER_HORIZONTAL(Gravity.CENTER_HORIZONTAL)
    }

}


