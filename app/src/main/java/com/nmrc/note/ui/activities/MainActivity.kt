package com.nmrc.note.ui.activities

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.nmrc.note.R
import com.nmrc.note.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private val navController by lazy { Navigation.findNavController(this, R.id.fragmentMainContent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

       confButtonItemIcons()
       confNav()
    }

    private fun confButtonItemIcons() {
        with(binding.bnvMainNavigation) {
            itemIconTintList = null
            itemTextAppearanceActive = R.style.ButtonNavViewItems
        }
    }

    private fun confNav() {
        NavigationUI.setupWithNavController(binding.bnvMainNavigation, navController)
    }
}