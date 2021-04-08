package com.nmrc.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.nmrc.note.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       initButtonItemIcons()
       confNav()
    }

    private fun initButtonItemIcons() {

        with(binding.bnvMainNavigation) {
            itemIconTintList = null
            itemTextAppearanceActive = R.style.ButtonNavViewItems
        }
    }

    private fun confNav() {
        NavigationUI.setupWithNavController(binding.bnvMainNavigation, Navigation.findNavController(this,R.id.fragmentMainContent))
    }
 }