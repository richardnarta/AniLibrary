package com.example.anilibrary.ui.fragment.viewpager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.anilibrary.MainActivity
import com.example.anilibrary.databinding.FragmentOnBoardingBinding
import com.example.anilibrary.ui.adapter.ViewPagerAdapter
import kotlinx.coroutines.launch

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding?=null
    private val binding get() = _binding!!

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            (activity as MainActivity).supportActionBar?.setShowHideAnimationEnabled(false)
            (activity as MainActivity).supportActionBar?.hide()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fragmentList = arrayListOf(
            FirstOnBoardingFragment(),
            SecondOnBoardingFragment(),
            ThirdOnBoardingFragment()
        )

        val onBoardingAdapter = ViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = onBoardingAdapter

        return root
    }
}