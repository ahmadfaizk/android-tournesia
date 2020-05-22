package com.d3itb.tournesia.ui.main.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.d3itb.tournesia.data.TournesiaRepository
import com.d3itb.tournesia.model.Category
import com.d3itb.tournesia.model.City
import com.d3itb.tournesia.model.Province
import com.d3itb.tournesia.vo.Resource

class FormViewModel(private val tournesiaRepository: TournesiaRepository): ViewModel() {

    private var provinceId = MutableLiveData<Int>()

    fun setProvinceId(id: Int) {
        provinceId.value = id
    }

    var category: LiveData<Resource<List<Category>>> = tournesiaRepository.getCategory()

    var province: LiveData<Resource<List<Province>>> = tournesiaRepository.getProvince()

    var city: LiveData<Resource<List<City>>> = Transformations.switchMap(provinceId) { provinceId ->
        tournesiaRepository.getCity(provinceId)
    }
}