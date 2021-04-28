package com.nmrc.note.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.nmrc.note.R
import com.nmrc.note.databinding.ActivityMainBinding
import com.nmrc.note.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()

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
        NavigationUI.setupWithNavController(binding.bnvMainNavigation, Navigation.findNavController(this, R.id.fragmentMainContent))
    }
 }