package com.d3itb.tournesia.ui.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.d3itb.tournesia.R
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[HomeViewModel::class.java]

        viewModel.getUser().observe(this, Observer { user ->
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
            removeToken()
            finish()
        }
    }

    private fun removeToken() {
        val sf = getSharedPreferences(getString(R.string.preferense_file_key), Context.MODE_PRIVATE)
        sf.edit().remove("token").apply()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state)
            progress_bar.visibility = View.VISIBLE
        else
            progress_bar.visibility = View.GONE
    }
}
