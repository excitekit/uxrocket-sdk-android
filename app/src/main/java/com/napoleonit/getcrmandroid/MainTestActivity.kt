package com.napoleonit.getcrmandroid


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.napoleonit.getcrmandroid.databinding.ActivityMainTestBinding
import com.napoleonit.getcrmandroid.fragments.FavoriteMenuFragment
import com.napoleonit.getcrmandroid.fragments.HomeFragment
import com.napoleonit.getcrmandroid.fragments.SearchFragment
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.ContextEvent

class MainTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainTestBinding
    private var currentSelectedMenu = Menus.NON
    private var ignoreHomepageLog = true

    enum class Menus {
        HOME,
        SEARCH,
        FAVORITES,
        NON
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNav()

        binding.bottomNavView.selectedItemId = R.id.nav_home_button
    }

    private fun initBottomNav() = with(binding.bottomNavView) {
        setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_button -> {
                    if (currentSelectedMenu != Menus.HOME) {
                        currentSelectedMenu = Menus.HOME
                        openFragment(HomeFragment.getInstance())

                        if (!ignoreHomepageLog) {
                            UXRocket.logEvent(
                                itemIdentificator = "nav_home_button",
                                itemName = "Nav home button pressed",
                                event = ContextEvent.MAIN_MENU
                            )
                        }else {
                            ignoreHomepageLog = false
                        }
                    }
                }
                R.id.nav_search_button -> {
                    if (currentSelectedMenu != Menus.SEARCH) {
                        currentSelectedMenu = Menus.SEARCH
                        openFragment(SearchFragment.getInstance())

                        UXRocket.logEvent(
                            itemIdentificator = "nav_search_button",
                            itemName = "Nav search button pressed",
                            event = ContextEvent.MAIN_MENU
                        )
                    }
                }
                R.id.nav_favorite_button -> {
                    if (currentSelectedMenu != Menus.FAVORITES) {
                        currentSelectedMenu = Menus.FAVORITES
                        openFragment(FavoriteMenuFragment.getInstance())

                        UXRocket.logEvent(
                            itemIdentificator = "nav_favorite_button",
                            itemName = "Nav favorite button pressed",
                            event = ContextEvent.MAIN_MENU
                        )
                    }
                }
                else -> 0
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment, fragment::class.java.simpleName)
        }
    }
}