package com.d3itb.tournesia.ui.main.form

import android.content.Context
import android.widget.ArrayAdapter
import com.d3itb.tournesia.model.Province

class ProvinceAdapter(context: Context) : ArrayAdapter<Province>(context, android.R.layout.simple_spinner_dropdown_item) {

    private var data = ArrayList<Province>()

    fun setData(list: List<Province>) {
        data.clear()
        data.addAll(list)
        super.addAll(data)
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }
}