package com.napoleonit.getcrmandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.napoleonit.getcrmandroid.databinding.FragmentSearchBinding
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent

class SearchFragment : Fragment() {
    companion object {
        fun getInstance() = SearchFragment()
    }

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UXRocket.logEvent(
            itemIdentificator = "SearchPage",
            itemName = "Search page",
            event = ContextEvent.OPEN_PAGE
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        initView()

        val params = listOf(AttributeParameter(id = "1", value = "190"))

        UXRocket.getUIConfiguration(
            forItem = "DemoController",
            parameters = params,
            callback = {
                UXRocket.customizeItems(items = listOf(binding.EditText, binding.TextView,binding.ImageView), it)
            })

        return binding.root
    }

    private fun initView() = with(binding) {
        searchByName.setOnClickListener {
            UXRocket.logEvent(
                itemIdentificator = "search_by_name",
                itemName = "Search by name button pressed",
                event = ContextEvent.OPEN_PAGE
            )
        }
    }
}