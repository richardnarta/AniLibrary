package com.example.anilibrary.ui.fragment.viewpager

import android.content.Context
import android.content.SharedPreferences
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
    private lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnBoardingBinding.inflate(inflater, container, false)
        val root:View = binding.root

        sp = requireContext().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        binding.next.setOnClickListener {
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("isFirstOpen", false)
            editor.apply()
            findNavController().navigate(OnBoardingFragmentDirections.actionNavigationOnBoardingToNavigationLogin())
        }

        return root
    }
}