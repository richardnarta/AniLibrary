package com.example.anilibrary.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.anilibrary.AniLibrary
import com.example.anilibrary.MainActivity
import com.example.anilibrary.databinding.FragmentProfileBinding
import com.example.anilibrary.model.data.database.AnimeListDatabase
import com.example.anilibrary.model.data.repository.AnimeListRepository
import com.example.anilibrary.viewmodel.DBViewModelFactory
import com.example.anilibrary.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ProfileFragment : Fragment() {

    private val database by lazy { AnimeListDatabase.getAnimeDB(requireContext()) }
    private val repository by lazy { AnimeListRepository(database.getDao()) }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        DBViewModelFactory(repository)
    }

    private val auth: FirebaseAuth = AniLibrary.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bindUserData()
        itemOnClickListener()

        return root
    }
    private suspend fun isInternetConnected(): Boolean {
        return try {
            val socket = Socket()
            withContext(Dispatchers.IO) {
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            }
            withContext(Dispatchers.IO) {
                socket.close()
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun bindUserData() {
        binding.apply {
            val user = auth.currentUser
            user?.let {
                Glide.with(requireContext()).load(it.photoUrl)
                    .into(ivProfile)
                tvProfile.text = it.displayName

            }
            viewModel.countAllAnime { allAnimeAmount->
                binding.allListNumber.text = allAnimeAmount.toString()
            }
            viewModel.countWatchedAnime { watchedAnimeAmount->
                binding.watchedListNumber.text = watchedAnimeAmount.toString()
            }
            viewModel.countPlannedAnime { plannedAnimeAmount->
                binding.plannedListNumber.text = plannedAnimeAmount.toString()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun itemOnClickListener() {
        binding.apply {
            editProfile.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    val isInternetAvailable = isInternetConnected()
                    launch(Dispatchers.Main) {
                        if (isInternetAvailable) {
                            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationEditProfile())
                        } else {
                            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            allListButton.setOnClickListener{
                findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationAllList())
            }
            watchedListButton.setOnClickListener{
                findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationWatchedList())
            }
            plannedListButton.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationPlannedList())
            }
            logOutButton.setOnClickListener {
                lifecycleScope.launch {
                    (activity as MainActivity).supportActionBar?.setShowHideAnimationEnabled(false)
                    (activity as MainActivity).supportActionBar?.hide()
                }
                auth.signOut()
                findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationLogin())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}