package com.nmrc.note.ui.fragments.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nmrc.note.databinding.FragmentNewTaskDialogBinding

class NewTaskDialogFragment : Fragment() {

    private var _binding: FragmentNewTaskDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentNewTaskDialogBinding.inflate(inflater,container,false)

        return binding.root
    }


}