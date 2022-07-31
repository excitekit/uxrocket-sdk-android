package com.napoleonit.uxrocket.shared

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import coil.load

fun View.getIdName(): String = resources.getResourceName(this.id)

fun View.customize(attributes: List<Attribute>) {
    when (this) {
        is TextView -> customizeTextView(attributes)
        is EditText -> customizeEditText(attributes)
        is ImageView -> customizeImageView(attributes)
        is Button -> customizeButton(attributes)
        else -> customizeView(attributes)
    }
}

private fun View.customizeView(attributes: List<Attribute>) {
    attributes.forEach { attribute ->
        try {
            when (attribute) {
                is AlphaAttribute -> {
                    alpha = attribute.value
                }
                is BackgroundColorAttribute -> {
                    setBackgroundColor(Color.parseColor(attribute.value))
                }
                is EnabledAttribute -> {
                    isEnabled = attribute.value
                }
                is LayoutAttribute -> {
                    val lp = layoutParams
                    lp.width = attribute.value.width.convertToPx().toInt()
                    lp.height = attribute.value.height.convertToPx().toInt()

                    layoutParams = lp
                }
                is VisibilityAttribute -> {
                    visibility = attribute.value.nativeValue
                }
                else -> {
                    /*ignore*/
                }
            }
        } catch (e: Exception) {
            e.logError()
        }
    }
}

private fun TextView.customizeTextView(attributes: List<Attribute>) {
    attributes.forEach { attribute ->
        try {
            when (attribute) {
                is AlphaAttribute -> {
                    alpha = attribute.value
                }
                is BackgroundColorAttribute -> {
                    setBackgroundColor(Color.parseColor(attribute.value))
                }
                is TextAttribute -> {
                    text = attribute.value
                }
                is TextColorAttribute -> {
                    setTextColor(Color.parseColor(attribute.value))
                }
                is TextSizeAttribute -> {
                    textSize = attribute.value.convertToPx()
                }
                is VisibilityAttribute -> {
                    visibility = attribute.value.nativeValue
                }
                is GravityAttribute -> {
                    var top: Int = -1
                    var bottom: Int = -1
                    var right: Int = -1
                    var left: Int = -1
                    var centerHorizontal: Int = -1
                    var centerVertical: Int = -1
                    var center: Int = -1

                    attribute.value.forEach { gravityType ->
                        when (gravityType) {
                            GravityAttribute.GravityAttributeValue.LEFT -> left = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.RIGHT -> right = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.TOP -> top = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.BOTTOM -> bottom = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.CENTER -> center = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.CENTER_VERTICAL -> centerVertical = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.CENTER_HORIZONTAL -> centerHorizontal = gravityType.nativeValue
                        }
                    }

                    gravity = top or bottom or right or left or centerHorizontal or centerVertical or center
                }
                is LayoutAttribute -> {
                    val lp = layoutParams
                    lp.width = attribute.value.width.convertToPx().toInt()
                    lp.height = attribute.value.height.convertToPx().toInt()

                    layoutParams = lp
                }
                else -> {
                    /*ignore*/
                }
            }
        } catch (e: Exception) {
            e.logError()
        }
    }
}

private fun EditText.customizeEditText(attributes: List<Attribute>) {
    attributes.forEach { attribute ->
        try {
            when (attribute) {
                is AlphaAttribute -> {
                    alpha = attribute.value
                }
                is BackgroundColorAttribute -> {
                    setBackgroundColor(Color.parseColor(attribute.value))
                }
                is TextAttribute -> {
                    setText(attribute.value)
                }
                is TextColorAttribute -> {
                    setTextColor(Color.parseColor(attribute.value))
                }
                is TextSizeAttribute -> {
                    textSize = attribute.value.convertToPx()
                }

                is GravityAttribute -> {
                    var top: Int = -1
                    var bottom: Int = -1
                    var right: Int = -1
                    var left: Int = -1
                    var centerHorizontal: Int = -1
                    var centerVertical: Int = -1
                    var center: Int = -1

                    attribute.value.forEach { gravityType ->
                        when (gravityType) {
                            GravityAttribute.GravityAttributeValue.LEFT -> left = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.RIGHT -> right = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.TOP -> top = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.BOTTOM -> bottom = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.CENTER -> center = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.CENTER_VERTICAL -> centerVertical = gravityType.nativeValue
                            GravityAttribute.GravityAttributeValue.CENTER_HORIZONTAL -> centerHorizontal = gravityType.nativeValue
                        }
                    }

                    gravity = top or bottom or right or left or centerHorizontal or centerVertical or center
                }
                is LayoutAttribute -> {
                    val lp = layoutParams
                    lp.width = attribute.value.width.convertToPx().toInt()
                    lp.height = attribute.value.height.convertToPx().toInt()

                    layoutParams = lp
                }
                is VisibilityAttribute -> {
                    visibility = attribute.value.nativeValue
                }
                is HintAttribute -> {
                    hint = attribute.value
                }
                is HintColorAttribute -> {
                    setHintTextColor(Color.parseColor(attribute.value))
                }
                else -> {
                    /*ignore*/
                }
            }
        } catch (e: Exception) {
            e.logError()
        }
    }
}

private fun ImageView.customizeImageView(attributes: List<Attribute>) {
    attributes.forEach { attribute ->
        try {
            when (attribute) {
                is BackgroundColorAttribute -> {
                    setBackgroundColor(Color.parseColor(attribute.value))
                }
                is ImageAttribute -> {
                    this.load(attribute.value)
                }
                is ScaleTypeAttribute -> {
                    this.scaleType = attribute.value.nativeValue
                }
                is LayoutAttribute -> {
                    val lp = layoutParams
                    lp.width = attribute.value.width.convertToPx().toInt()
                    lp.height = attribute.value.height.convertToPx().toInt()

                    layoutParams = lp
                }
                is VisibilityAttribute -> {
                    visibility = attribute.value.nativeValue
                }
                else -> {
                    /*ignore*/
                }
            }
        } catch (e: Exception) {
            e.logError()
        }
    }
}

private fun Button.customizeButton(attributes: List<Attribute>) {
    attributes.forEach { attribute ->
        try {
            when (attribute) {
                is AlphaAttribute -> {
                    alpha = attribute.value
                }
                is BackgroundColorAttribute -> {
                    setBackgroundColor(Color.parseColor(attribute.value))
                }
                is TextAttribute -> {
                    text = attribute.value
                }
                is TextColorAttribute -> {
                    setTextColor(Color.parseColor(attribute.value))
                }
                is TextSizeAttribute -> {
                    textSize = attribute.value.convertToPx()
                }
                is EnabledAttribute -> {
                    isEnabled = attribute.value
                }
                is LayoutAttribute -> {
                    val lp = layoutParams
                    lp.width = attribute.value.width.convertToPx().toInt()
                    lp.height = attribute.value.height.convertToPx().toInt()

                    layoutParams = lp
                }
                is VisibilityAttribute -> {
                    visibility = attribute.value.nativeValue
                }
                else -> {
                    /*ignore*/
                }
            }
        } catch (e: Exception) {
            e.logError()
        }
    }
}

fun findAndGetViewInItemsById(id: String, items: List<View>): View? {
    return items.find { it.getIdName().contains(id) }
}
