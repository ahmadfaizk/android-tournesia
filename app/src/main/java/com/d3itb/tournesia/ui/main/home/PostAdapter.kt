package com.d3itb.tournesia.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d3itb.tournesia.R
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.utils.ImageUtils
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val listPost = ArrayList<Post>()

    fun setListPost(listPost: List<Post>) {
        if (this.listPost.isNotEmpty()) {
            this.listPost.clear()
        }
        this.listPost.addAll(listPost)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listPost.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPost[position])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post) {
            with(itemView) {
                tv_name.text = post.name
                tv_description.text = post.description
                tv_address.text = "${post.city}, ${post.province}"
                Glide.with(itemView)
                    .load(ImageUtils.getImagePostUrl(post.images[0].name))
                    .into(img_post)
            }
        }
    }
}