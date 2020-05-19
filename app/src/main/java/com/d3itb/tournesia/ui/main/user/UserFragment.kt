package com.d3itb.tournesia.ui.main.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.d3itb.tournesia.R
import com.d3itb.tournesia.utils.TokenPreference
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[UserViewModel::class.java]

        viewModel.getUser().observe(this.viewLifecycleOwner, Observer { user ->
            if (user != null) {
                when (user.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        val data = user.data
                        tv_name.text = data?.name
                        tv_address.text = data?.address
                        tv_email.text = data?.email
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(user.message.toString())
                    }
                }
            }
        })

        btn_logout.setOnClickListener {
            TokenPreference.getInstance(requireContext()).removeToken()
            this.activity?.finish()
        }
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
