package com.rohit2810.leo_kotlin.ui.register

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.databinding.FragmentRegisterBinding
import com.rohit2810.leo_kotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var viewModelFactory: RegisterViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRegisterBinding.inflate(inflater, container, false)


        requireActivity().title = "Register"

        viewModelFactory = RegisterViewModelFactory(activity?.applicationContext!!)

        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel


        viewModel.registeredUser.observe(viewLifecycleOwner, Observer {
            it?.let {
//                this.findNavController().navigate()
                this.findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToVerifyOtpFragment2(it, false)
                )
                viewModel.doneNavigateToEmergencyNumbers()
            }
        })

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigateUp()
                viewModel.doneNavigateToLogin()
            }
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewModel.doneShowingToastMsg()
            }
        })

        viewModel.isTermsConditionsAgreed.observe(viewLifecycleOwner, Observer {it1 ->

            viewModel.isPrivacyPolicyAgreed.observe(viewLifecycleOwner, Observer { it2 ->
                it1?.let {it1 ->
                    it2?.let { it2 ->
                        button_register.isEnabled = it1 && it2
                    }
                }

            })

        })

        binding.tvTermsAndConditions.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1W5GDYjWCFIj_dfbctg0iA_9Unp8gkoKU/view?usp=sharing"))
            startActivity(intent)
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1auANTHv5d-9phggss4s0SRCl5_wmepu_/view?usp=sharing"))
            startActivity(intent)
        }


//        binding.buttonRegister.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
//            startActivityForResult(intent, 1)
//        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val s: Cursor = activity?.contentResolver?.query(
                        Phone.CONTENT_URI, null,
                        null, null, null
                    )!!

                    if (s.moveToFirst()) {
                        val phoneNum: String = s.getString(s.getColumnIndex(Phone.NUMBER))
                        Toast.makeText(activity?.applicationContext!!, phoneNum, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}