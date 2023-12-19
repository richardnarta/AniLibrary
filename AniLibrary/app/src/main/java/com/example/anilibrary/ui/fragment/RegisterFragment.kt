package com.example.anilibrary.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.anilibrary.AniLibrary
import com.example.anilibrary.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth = AniLibrary.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        itemOnClickListener()

        return binding.root
    }

    private fun itemOnClickListener() {
        binding.loginPage.setOnClickListener{
            findNavController().navigate(RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin())
        }

        binding.buttonRegister.setOnClickListener{
            checkRegister()
        }
    }

    private fun checkRegister() {
        val email = binding.inputEmail.text.toString()
        val username = binding.inputUsername.text.toString()
        val password = binding.inputPassword.text.toString()
        val passConf = binding.inputPasswordConf.text.toString()

        if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && passConf.isNotEmpty()) {
            if (emailFilterCondition(email)) {
                if (password == passConf) {
                    if (passwordFilterCondition(password)) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val user = auth.currentUser
                                    val userprofile = UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build()
                                    user?.updateProfile(userprofile)
                                    findNavController().navigate(RegisterFragmentDirections.actionNavigationRegisterToNavigationHome())
                                } else {
                                    Toast.makeText(requireContext(), "Failed to register", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(requireContext(), "Password must be 8 characters or more and contain letters and numbers", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(),"Fill all the fields!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun passwordFilterCondition(input: String): Boolean {
        return input.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
    }
    private fun emailFilterCondition(input: String): Boolean {
        return input.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}