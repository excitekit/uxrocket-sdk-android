package com.napoleonit.getcrmandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.napoleonit.getcrmandroid.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    companion object {
        fun getInstance() = SearchFragment()
    }

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }
}