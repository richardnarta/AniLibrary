package com.example.anilibrary.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.anilibrary.AniLibrary
import com.example.anilibrary.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import java.io.File
import com.bumptech.glide.Glide
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth = AniLibrary.auth
    private val user: FirebaseUser = auth.currentUser!!
    private var database: DatabaseReference = AniLibrary.database.reference.child("users")
    private var storage: StorageReference = AniLibrary.storage.reference.child("images")
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private var username: String? = null
    private var gender: String? = null
    private var imageURL: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        itemOnClickListener()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.editUsername.text.isNotEmpty() || binding.editGender.text.isNotEmpty()) {
                        Log.d("log", binding.editUsername.text.toString())
                        showDiscardChangesDialog()
                    } else {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }
        )
        setHasOptionsMenu(true)

        CoroutineScope(Dispatchers.Main).launch {
            bindUserData()
        }
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.editUsername.text.isNotEmpty() || binding.editGender.text.isNotEmpty()) {
                    Log.d("log", binding.editUsername.text.toString())
                    showDiscardChangesDialog()
                } else {
                    requireActivity().supportFragmentManager.popBackStack()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDiscardChangesDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Discard Changes")
        alertDialogBuilder.setMessage("Do you want to discard changes?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            requireActivity().supportFragmentManager.popBackStack()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    if (data.data != null) {
                        imageUri = data.data!!
                    }
                    Glide.with(requireContext())
                        .load(imageUri)
                        .into(binding.ivProfile)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                imageUri = null
            }
        }
    }

    private suspend fun bindUserData() {
        val fetchDataDeferred = CompletableDeferred<Unit>()
        fetchUserDatabase { gender ->
            this@EditProfileFragment.gender = gender
            fetchDataDeferred.complete(Unit)
        }
        fetchDataDeferred.await()
        binding.apply {
            user.let {
                editUsername.hint = it.displayName
                if (it.photoUrl != null) {
                    Glide.with(requireContext()).load(it.photoUrl)
                        .into(ivProfile)
                }
            }
            if (gender != "") {
                editGender.hint = gender
            } else {
                editGender.hint = "Gender"
            }
        }
    }

    private fun fetchUserDatabase(onComplete: (String) -> Unit) {
        val userid = user.uid
        val userRef = database.child(userid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val gender = snapshot.child("gender").getValue(String::class.java)!!
                    onComplete(gender)
                } else {
                    onComplete("Gender")
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun itemOnClickListener () {
        binding.editImage.setOnClickListener {
            launchImagePicker()
        }
        binding.buttonSave.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                checkUserData()
            }
        }

        binding.changePw.setOnClickListener {
            findNavController().navigate(EditProfileFragmentDirections.actionNavigationEditProfileToNavigationChangePassword())
        }

        binding.changeMail.setOnClickListener {
            findNavController().navigate(EditProfileFragmentDirections.actionNavigationEditProfileToNavigationChangeEmail())
        }
    }

    private suspend fun checkUserData() {
        username = binding.editUsername.text.toString()
        gender = binding.editGender.text.toString()
        val checkUri = imageUri
        if (username!!.isNotEmpty() || gender!!.isNotEmpty() || checkUri != null) {
            Toast.makeText(requireContext(), "Updating profile, Please wait...", Toast.LENGTH_SHORT).show()
        }
        if (checkUri != null) {
            val imageUploadDeferred = CompletableDeferred<Unit>()

            uploadImageToStorage(imageUri!!) { imageURL ->
                this@EditProfileFragment.imageURL = imageURL
                imageUploadDeferred.complete(Unit)
            }
            imageUploadDeferred.await()
        }
        updateUserDatabase(gender!!)
        updateUserProfile()
        if (username!!.isNotEmpty() || gender!!.isNotEmpty() || imageURL != null) {
            delay(1000)
            requireActivity().supportFragmentManager.popBackStack()
            Toast.makeText(requireContext(), "Profile successfully updated.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserProfile() {
        val userProfileBuilder = UserProfileChangeRequest.Builder()
        if (username != null && username != "") {
            userProfileBuilder.displayName = username
        }
        if (imageURL != null) {
            userProfileBuilder.photoUri = imageURL
        }
        val userprofile = userProfileBuilder.build()
        user.updateProfile(userprofile)
    }

    private fun updateUserDatabase(updatedGender: String) {
        val userid = user.uid
        val userRef = database.child(userid)

        val updates = mutableMapOf<String, Any>().apply {
            if (updatedGender != "") {
                this["gender"] = updatedGender
            }
        }
        userRef.updateChildren(updates)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "$e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToStorage(imageUri: Uri, onComplete: (Uri) -> Unit) {
        val imagesRef = storage.child("images/${user.uid}.jpg")
        val uploadTask = imagesRef.putFile(imageUri)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    onComplete(uri)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to upload Images", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        val chooserIntent = Intent.createChooser(intent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(getCameraIntent()))
        getResult.launch(chooserIntent)
    }

    private fun getCameraIntent(): Intent {
        val file = createImageFile()
        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.anilibrary.fileprovider",
            file
        )
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
    }

    private fun createImageFile(): File {
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}",
            ".jpg",
            storageDir
        )
    }


}