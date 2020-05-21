package com.d3itb.tournesia.ui.auth.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.d3itb.tournesia.R
import com.d3itb.tournesia.model.Token
import com.d3itb.tournesia.model.User
import com.d3itb.tournesia.utils.TokenPreference
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[RegisterViewModel::class.java]
        btn_register.setOnClickListener {
            checkInput()
        }
        btn_login.setOnClickListener {
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun checkInput() {
        val name = edt_name.text.toString()
        val address = edt_address.text.toString()
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()

        if (name.isEmpty()) {
            edt_name.error = "Nama harus diisi"
            return
        }

        if (address.isEmpty()) {
            edt_address.error = "Alamat harus diisi"
            return
        }

        if (email.isEmpty()) {
            edt_email.error = "Email harus diisi"
            return
        }
        if (password.isEmpty()) {
            edt_password.error = "Password harus disi"
            return
        }

        val user = User(0, name, email, address, null, password)

        viewModel.register(user).observe(this.viewLifecycleOwner, Observer { token ->
            if (token != null) {
                when (token.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> if (token.data != null) {
                        showLoading(false)
                        TokenPreference.getInstance(requireContext()).saveToken(token.data.token)
                        view?.findNavController()?.navigate(R.id.action_registerFragment_to_homeActivity)
                        this.activity?.finish()
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(token.message.toString())
                    }
                }
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state)
            progress_bar.visibility = View.VISIBLE
        else
            progress_bar.visibility = View.GONE
    }
}
