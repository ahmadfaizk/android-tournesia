package com.d3itb.tournesia.ui.main.form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.d3itb.tournesia.R
import com.d3itb.tournesia.model.Category
import com.d3itb.tournesia.model.City
import com.d3itb.tournesia.model.Province
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.fragment_form.*

/**
 * A simple [Fragment] subclass.
 */
class FormFragment : Fragment() {

    private lateinit var categoryAdapter: ArrayAdapter<Category>
    private lateinit var provinceAdapter: ProvinceAdapter
    private lateinit var cityAdapter: ArrayAdapter<City>
    private lateinit var viewModel: FormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        provinceAdapter = ProvinceAdapter(requireContext())
        cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[FormViewModel::class.java]

        act_category.setAdapter(categoryAdapter)
        act_city.setAdapter(cityAdapter)
        act_province.setAdapter(provinceAdapter)

        act_province.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, id ->
                cityAdapter.clear()
                act_city.clearListSelection()
                viewModel.setProvinceId(id.toInt())
            }

        viewModel.category.observe(this.viewLifecycleOwner, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.SUCCESS -> {
                        val categories = response.data
                        if (categories != null) {
                            categoryAdapter.addAll(categories)
                        }
                    }
                    Status.ERROR -> showMessage(response.message.toString())
                }
            }
        })
        viewModel.province.observe(this.viewLifecycleOwner, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.SUCCESS -> {
                        val provinces = response.data
                        if (provinces != null) {
                            provinceAdapter.setData(provinces)
                        }
                    }
                    Status.ERROR -> showMessage(response.message.toString())
                }
            }
        })
        viewModel.city.observe(this.viewLifecycleOwner, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.SUCCESS -> {
                        val cities = response.data
                        if (cities != null) {
                            cityAdapter.addAll(cities)
                        }
                    }
                    Status.ERROR -> showMessage(response.message.toString())
                }
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
