package com.napoleonit.crmlibrary.data.models

class SaveAppParamBuilder(
    val itemName: String,
    val item: String,
    val capturedDate: String
) {

    private constructor(builder: Builder) : this(itemName = builder.itemName, item = builder.item, capturedDate = builder.capturedDate)

    class Builder(val item: String, val itemName: String, val capturedDate: String) {
        /*Need continue for any property's*/

        fun build() = SaveAppParamBuilder(this)
    }
}