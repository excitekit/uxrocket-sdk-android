package com.napoleonit.getcrmandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.napoleonit.crmlibrary.UXRocket
import com.napoleonit.crmlibrary.data.models.SaveAppParamBuilder

class MainTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_test)

        findViewById<Button>(R.id.save_app_params_button).apply {
            val tempBuilder = SaveAppParamBuilder.Builder("test_item", "test_item_name", "test_captured_date").build()
            setOnClickListener { UXRocket.saveAppParams(tempBuilder) }
        }
    }
}