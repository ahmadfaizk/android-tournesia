package com.d3itb.tournesia.ui.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.d3itb.tournesia.R
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.model.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private val comments = ArrayList<Comment>()

    fun addComments(comments: List<Comment>) {
        if (this.comments.isNotEmpty()) {
            this.comments.clear()
        }
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        fun bind(comment: Comment) {
            with(itemView) {
                rb_votes.rating = comment.votes.toFloat()
                tv_comment.text = comment.comment
                tv_user_name.text = comment.userName
                if (comment.images.isNotEmpty()) {
                    Glide.with(itemView)
                        .load(comment.images[0].name.let { ApiClient.getImageCommentUrl(it) })
                        .apply(RequestOptions().override(160, 90))
                        .into(img_comment)
                } else {
                    img_comment.visibility = View.GONE
                }
            }
        }
    }
}