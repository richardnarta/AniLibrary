package com.example.anilibrary.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.anilibrary.AniLibrary
import com.example.anilibrary.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth = AniLibrary.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        itemOnClickListener()

        return root
    }

    private fun changePassword() {
        val user = auth.currentUser
        val userEmail = user?.email!!
        val currentPassword = binding.currentPassword.text.toString()
        val newPassword = binding.newPassword.text.toString()
        val credential = EmailAuthProvider.getCredential(userEmail, currentPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener {update->
                            if (update.isSuccessful) {
                                requireActivity().supportFragmentManager.popBackStack()
                                Toast.makeText(requireContext(), "Password successfully updated.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Failed to change password, try Again", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(),"Current password is incorrect. Please enter the correct current password.", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun passwordFilterCondition(input: String): Boolean {
        return input.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
    }
    private fun itemOnClickListener() {
        binding.buttonSave.setOnClickListener {
            val newPassword = binding.newPassword.text.toString()
            if (passwordFilterCondition(newPassword)) {
                changePassword()
            } else {
                Toast.makeText(requireContext(), "Password must be 8 characters or more and contain letters and numbers", Toast.LENGTH_SHORT).show()
            }
        }
    }
}