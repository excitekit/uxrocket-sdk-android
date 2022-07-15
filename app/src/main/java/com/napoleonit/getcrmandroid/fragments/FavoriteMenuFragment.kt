package com.napoleonit.getcrmandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.napoleonit.getcrmandroid.databinding.FragmentFavoriteBinding
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.data.models.local.LogModel

class FavoriteMenuFragment : Fragment() {
    companion object {
        fun getInstance() = FavoriteMenuFragment()
    }

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UXRocket.logEvent(
            LogModel(
                item = "FavoritePage",
                itemName = "Favorite page",
                event = ContextEvent.OPEN_PAGE
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() = with(binding) {
        allFavoriteButton.setOnClickListener {
            UXRocket.logEvent(
                LogModel(
                    item = "all_favorite_button",
                    itemName = "All favorite button pressed",
                    event = ContextEvent.BUTTONS
                )
            )
        }
        removeAllFavoriteButton.setOnClickListener {
            UXRocket.logEvent(
                LogModel(
                    item = "remove_all_favorite_button",
                    itemName = "Remove all favorite button pressed",
                    event = ContextEvent.BUTTONS
                )
            )
        }
    }
}