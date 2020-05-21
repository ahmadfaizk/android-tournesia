package com.d3itb.tournesia.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.d3itb.tournesia.R
import com.d3itb.tournesia.utils.TokenPreference
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[LoginViewModel::class.java]
        btn_login.setOnClickListener {
            checkInput()
        }
        btn_register.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun checkInput() {
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()

        if (email.isEmpty()) {
            edt_email.error = "Email harus diisi"
            return
        }
        if (password.isEmpty()) {
            edt_password.error = "Password harus disi"
            return
        }

        viewModel.login(email, password).observe(this.viewLifecycleOwner, Observer { token ->
            if (token != null) {
                when (token.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> if (token.data != null) {
                        showLoading(false)
                        TokenPreference.getInstance(requireContext()).saveToken(token.data.token)
                        view?.findNavController()?.navigate(R.id.action_loginFragment_to_homeActivity)
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
