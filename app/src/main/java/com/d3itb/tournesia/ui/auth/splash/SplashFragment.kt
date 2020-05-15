package com.d3itb.tournesia.ui.auth.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.d3itb.tournesia.R

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            val sf = context?.getSharedPreferences(getString(R.string.preferense_file_key), Context.MODE_PRIVATE)
            val token = sf?.getString("token", null)
            if (token == null) {
                view.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                view.findNavController().navigate(R.id.action_splashFragment_to_homeActivity)
            }
        }, 3000)
    }
}
