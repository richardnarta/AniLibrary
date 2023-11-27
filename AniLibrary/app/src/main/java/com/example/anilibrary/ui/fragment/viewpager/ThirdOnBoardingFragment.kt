package com.example.anilibrary.ui.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.anilibrary.databinding.FragmentThirdOnBoardingBinding

class ThirdOnBoardingFragment : Fragment() {

    private var _binding:FragmentThirdOnBoardingBinding? = null
    private val binding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnBoardingBinding.inflate(inflater, container, false)
        val root:View = binding.root

        binding.next.setOnClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionNavigationOnBoardingToNavigationLogin())
        }

        return root
    }
}