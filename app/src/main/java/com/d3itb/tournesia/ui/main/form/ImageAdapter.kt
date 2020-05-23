package com.d3itb.tournesia.ui.main.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d3itb.tournesia.R
import com.d3itb.tournesia.model.Image
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val listImage = ArrayList<Image>()

    fun setListImage(list: List<Image>) {
        if (listImage.isNotEmpty()) {
            listImage.clear()
        }
        listImage.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listImage.size

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        holder.bind(listImage[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(image: Image) {
            Glide.with(itemView)
                .load(image.name)
                .into(itemView.imageView)
        }
    }
}