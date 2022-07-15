package com.napoleonit.getcrmandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.napoleonit.getcrmandroid.databinding.FragmentFavoriteBinding

class FavoriteMenuFragment : Fragment() {
    companion object {
        fun getInstance() = FavoriteMenuFragment()
    }

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }
}