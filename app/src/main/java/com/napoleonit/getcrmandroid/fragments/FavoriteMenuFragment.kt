package com.napoleonit.getcrmandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.napoleonit.getcrmandroid.databinding.FragmentFavoriteBinding
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.data.models.http.ContextEvent

class FavoriteMenuFragment : Fragment() {
    companion object {
        fun getInstance() = FavoriteMenuFragment()
    }

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UXRocket.logEvent(
            itemIdentificator = "FavoritePage",
            itemName = "Favorite page",
            event = ContextEvent.OPEN_PAGE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        val customizingItems = listOf(binding.allFavoriteButton, binding.removeAllFavoriteButton)
        val params = listOf(AttributeParameter(id = "1", value = "190"))
        val fragmentName = "FavoritePage"
        UXRocket.getUIConfiguration(
            activityOrFragmentName = fragmentName,
            parameters = params,
            callback = {
                UXRocket.customizeItems(
                    items = customizingItems,
                    campaigns = it,
                    activityOrFragmentName = fragmentName
                )
                UXRocket.processActions(
                    items = customizingItems,
                    campaigns = it,
                    activityOrFragmentName = fragmentName,
                    parameters = params
                )
            }
        )
        return binding.root
    }
}