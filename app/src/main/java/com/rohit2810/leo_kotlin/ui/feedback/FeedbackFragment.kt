package com.rohit2810.leo_kotlin.ui.feedback

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rohit2810.leo_kotlin.databinding.FragmentFeedbackBinding
import com.rohit2810.leo_kotlin.utils.showToast
import timber.log.Timber
import java.lang.Exception


class FeedbackFragment : Fragment() {

    private lateinit var feedbackViewModel: FeedbackViewModel
    private lateinit var feedbackViewModelFactory: FeedbackViewModelFactory
    private lateinit var storageReference: StorageReference
    val PICK_IMAGE_REQUEST = 10001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        feedbackViewModelFactory = FeedbackViewModelFactory(requireContext())
        feedbackViewModel = ViewModelProvider(this, feedbackViewModelFactory).get(FeedbackViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewmodel = feedbackViewModel
        storageReference = FirebaseStorage.getInstance().getReference("uploads")


        feedbackViewModel.onSubmit.observe(viewLifecycleOwner, Observer {
            it?.let {

                feedbackViewModel.doneOnSubmit()
            }
        })

        feedbackViewModel.fileUpload.observe(viewLifecycleOwner, Observer {
            it?.let {
                pickImage()
                feedbackViewModel.doneFileUpload()
            }
        })

        return binding.root
    }

    fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )
    }

    fun uploadFile(uri: Uri) {
        var fileRef = storageReference.child(System.currentTimeMillis().toString() + "." + getFileExtension(uri))
        try {

            fileRef.putFile(uri).addOnSuccessListener {
                Timber.d("File Uploaded Download url ${it.uploadSessionUri.toString()}")
            }.addOnFailureListener {
                Timber.d("File Upload Failed ${it.localizedMessage}")
            }.addOnProgressListener {
                Timber.d("Work in progress")
            }
        }catch (e: Exception) {
            requireContext().showToast("File upload failed")
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR: ContentResolver = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                var image = data?.data
                Timber.d("Request success")
                if (image != null) {
                    feedbackViewModel.filename.value = "File Selected"
                    feedbackViewModel.fileSelected()
                    uploadFile(image)
                }else {
                    requireContext().showToast("Please select files to upload")
                }
            }
        }
    }
}