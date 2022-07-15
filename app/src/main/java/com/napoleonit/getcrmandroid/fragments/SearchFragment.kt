package com.napoleonit.getcrmandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.napoleonit.getcrmandroid.databinding.FragmentSearchBinding
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.ContextEvent
import com.napoleonit.uxrocket.data.models.local.LogModel

class SearchFragment : Fragment() {
    companion object {
        fun getInstance() = SearchFragment()
    }

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UXRocket.logEvent(
            LogModel(
                item = "SearchPage",
                itemName = "Search page",
                event = ContextEvent.OPEN_PAGE
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() = with(binding) {
        searchByName.setOnClickListener {
            UXRocket.logEvent(
                LogModel(
                    item = "search_by_name",
                    itemName = "Search by name button pressed",
                    event = ContextEvent.OPEN_PAGE
                )
            )
        }
    }
}