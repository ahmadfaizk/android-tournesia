package com.d3itb.tournesia.ui.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.d3itb.tournesia.R

class AddCommentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val REQUEST_IMAGE_CAMERA = 1
        const val REQUEST_IMAGE_MEDIA = 2

        const val REQUEST_ADD = 101
        const val REQUEST_UPDATE = 102
        const val RESULT_ADD = 201
        const val RESULT_UPDATE = 202
    }

    private lateinit var viewModel: AddCommentViewModel

    private var currentPhotoPath: String? = null
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
    }
}