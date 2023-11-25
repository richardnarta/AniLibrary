package com.example.anilibrary.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anilibrary.databinding.FragmentWatchedListBinding

class WatchedListFragment : Fragment() {
    private var _binding: FragmentWatchedListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchedListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

}