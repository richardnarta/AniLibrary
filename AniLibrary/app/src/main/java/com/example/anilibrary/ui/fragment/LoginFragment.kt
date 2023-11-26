package com.example.anilibrary.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.anilibrary.AniLibrary
import com.example.anilibrary.MainActivity
import com.example.anilibrary.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth = AniLibrary.auth

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            (activity as MainActivity).supportActionBar?.setShowHideAnimationEnabled(false)
            (activity as MainActivity).supportActionBar?.hide()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        itemOnClickListener()

        return binding.root
    }

    private fun itemOnClickListener() {
        binding.registerPage.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToNavigationRegister())
        }
        binding.loginButton.setOnClickListener{
            if (binding.inputEmail.text.isNotEmpty() && binding.inputEmail.text.isNotEmpty()) {
                auth.signInWithEmailAndPassword(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToNavigationHome())
                        } else {
                            Toast.makeText(requireContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Fill all the fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}