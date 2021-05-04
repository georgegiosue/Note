package com.nmrc.note.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nmrc.note.databinding.FragmentInfoAppBinding

private const val LINK_REPOSITORY = "https://github.com/16george/note"

class InfoAppFragment : Fragment() {

    private var _binding: FragmentInfoAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInfoAppBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //confIconsTint()
        sourceCodeListener()
    }


    /*private fun confIconsTint() {
        with(binding) {
            btnSourceCode.iconTint = null
            btnLicense.iconTint = null
        }
    }*/

    private fun sourceCodeListener() {
        binding.btnSourceCode.setOnClickListener {
            val intentSC = Intent(Intent.ACTION_VIEW, Uri.parse(LINK_REPOSITORY))
            startActivity(intentSC)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}