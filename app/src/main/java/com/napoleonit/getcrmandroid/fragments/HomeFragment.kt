package com.napoleonit.getcrmandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.napoleonit.getcrmandroid.databinding.FragmentHomeBinding
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent

class HomeFragment : Fragment() {
    companion object {
        fun getInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UXRocket.logEvent(
            itemIdentificator = "HomePage",
            itemName = "Home page",
            event = ContextEvent.OPEN_PAGE,
            parameters = listOf(
                AttributeParameter("1", value = "Value sample 1"),
                AttributeParameter("7", value = "Value sample 2")
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val params = listOf(AttributeParameter(id = "1", value = "190"))
        val customizingItems = listOf(binding.homeBannersButton, binding.addCustomBannerButton)
        val fragmentName = "HomePage"
        UXRocket.getUIConfiguration(
            activityOrFragmentName = fragmentName,
            parameters = params,
            callback = {
                UXRocket.customizeItems(
                    items = customizingItems,
                    it,
                    activityOrFragmentName = fragmentName
                )
                UXRocket.processActions(
                    items = customizingItems,
                    it,
                    activityOrFragmentName = fragmentName,
                    parameters = params
                )
            })
        return binding.root
    }
}