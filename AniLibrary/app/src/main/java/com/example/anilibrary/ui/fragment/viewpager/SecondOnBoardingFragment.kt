package com.example.anilibrary.ui.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.anilibrary.R
import com.example.anilibrary.databinding.FragmentSecondOnBoardingBinding

class SecondOnBoardingFragment : Fragment() {

    private var _binding: FragmentSecondOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondOnBoardingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.next.setOnClickListener {
            viewPager?.currentItem = 2
        }

        binding.skip.setOnClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionNavigationOnBoardingToNavigationLogin())
        }

        return root
    }
}