package com.example.anilibrary.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.anilibrary.AniLibrary
import com.example.anilibrary.databinding.FragmentChangeEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ChangeEmailFragment : Fragment() {
    private var _binding: FragmentChangeEmailBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth = AniLibrary.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeEmailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        itemOnClickListener()

        return root
    }

    private fun changeEmail() {
        val user = auth.currentUser
        val currentEmail = user?.email!!
        val currentPassword = binding.currentPassword.text.toString()
        val newEmail = binding.newEmail.text.toString()
        val credential = EmailAuthProvider.getCredential(currentEmail, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                user.verifyBeforeUpdateEmail(newEmail)
                    .addOnCompleteListener { update->
                        if (update.isSuccessful) {
                            requireActivity().supportFragmentManager.popBackStack()
                            Toast.makeText(requireContext(), "Verification sent. Verify your email to confirm email change", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to change email, try Again", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(),"Current password is incorrect. Please enter the correct current password.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun emailFilterCondition(input: String): Boolean {
        return input.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"))
    }

    private fun itemOnClickListener() {
        binding.buttonSave.setOnClickListener {
            val newEmail = binding.newEmail.text.toString()
            if (emailFilterCondition(newEmail)) {
                changeEmail()
            } else {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
            }
        }
    }



}