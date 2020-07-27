package com.rohit2810.leo_kotlin.ui.emergencyContacts

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.databinding.FragmentEmergencyContactsBinding
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.showToast
import timber.log.Timber

class EmergencyContactsFragment : Fragment() {

    private lateinit var viewModel: EmergencyContactsViewModel

    private lateinit var user: User

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = EmergencyContactsFragmentArgs.fromBundle(arguments!!).registeredUser
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEmergencyContactsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        val toUpdate = EmergencyContactsFragmentArgs.fromBundle(requireArguments()).isFromMain

        requireActivity().title = "Leo"

//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        val repository = TroubleRepository.getInstance(activity?.applicationContext!!)
        val viewModelFactory = EmergencyContactsViewModelFactory(
            user,
            repository,
            activity?.applicationContext!!,
            toUpdate
        )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(EmergencyContactsViewModel::class.java)

        binding.viewmodel = viewModel

        requireActivity().title = "Emergency Contacts"

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
//                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                activity?.showToast(it)
                viewModel.doneShowingToastMsg()
            }
        })

        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!markSMSPermission() || !markLocationPermission()) {
                    this.findNavController()
                        .navigate(EmergencyContactsFragmentDirections.actionEmergencyContactsFragmentToPermissionRequestFragment2())
                } else if (!toUpdate) {
                    this.findNavController()
                        .navigate(EmergencyContactsFragmentDirections.actionEmergencyContactsFragmentToMainFragment())
                } else {
                    this.findNavController().navigateUp()
                }
                viewModel.doneNavigateToMain()
            }
        })

        viewModel.snackbarMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewModel.doneShowingSnackbar()
            }
        })

        viewModel.isPickingFromPhone.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (markContactsPermission()) {
                    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                    startActivityForResult(intent, 1000 + it)
                    viewModel.donePickingFromPhone()
                }else {
                    requestCallPermission()
                }
            }
        })




        return binding.root
    }

    private fun markSMSPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun markLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun markContactsPermission(): Boolean{
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestCallPermission() {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_CONTACTS
                    ), 1
                )
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS), 1
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode > 1000 && requestCode < 1006) {
                    var contactData: Uri = data?.data!!
                    var cursor = activity?.contentResolver?.query(contactData, null, null, null, null)
                    if(cursor?.moveToFirst()!!) {
                        val id: String =
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))

                        val hasPhone: String =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                        if (hasPhone.equals("1", ignoreCase = true)) {
                            val phones: Cursor = activity?.contentResolver!!?.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null
                            )!!
                            phones.moveToFirst()
                            var cNumber = phones.getString(phones.getColumnIndex("data1"))
                            if(cNumber.startsWith("+91")) {
                                cNumber = cNumber.replaceFirst("+91", "")
                            }
                            cNumber = cNumber.replace("\\s".toRegex(), "")
                            Timber.d("Phone number: ${cNumber}")
                            when(requestCode) {
                                1001 -> viewModel.phone1.value = cNumber
                                1002 -> viewModel.phone2.value = cNumber
                                1003 -> viewModel.phone3.value = cNumber
                                1004 -> viewModel.phone4.value = cNumber
                                1005 -> viewModel.phone5.value = cNumber
                            }
                        }
                    }
            }
        }
    }


}