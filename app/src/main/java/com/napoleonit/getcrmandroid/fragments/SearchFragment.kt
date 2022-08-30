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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        val params = listOf(AttributeParameter(id = "1", value = "190"))
        val fragmentName = "SearchPage"
        val screenElements = listOf(
            binding.editText,
            binding.textView,
            binding.imageView,
            binding.searchByNameButton
        )
        UXRocket.getUIConfiguration(
            activityOrFragmentName = "SearchPage",
            parameters = params,
            callback = {
                UXRocket.customizeItems(
                    items = screenElements,
                    campaigns = it,
                    activityOrFragmentName = fragmentName
                )
                UXRocket.processActions(
                    items = screenElements,
                    campaigns = it,
                    activityOrFragmentName = fragmentName,
                    parameters = params,
                    totalValue = {
                        binding.textView.text.toString().toIntOrNull()
                    }
                )
            })

        return binding.root
    }
}