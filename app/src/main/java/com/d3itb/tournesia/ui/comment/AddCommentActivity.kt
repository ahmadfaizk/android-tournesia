package com.d3itb.tournesia.ui.comment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.d3itb.tournesia.R
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.model.Comment
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.activity_add_comment.*
import kotlinx.android.synthetic.main.activity_add_comment.container
import kotlinx.android.synthetic.main.activity_add_comment.progress_bar
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddCommentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_EDIT = "extra_edit"
        const val EXTRA_VOTES = "extra_votes"
        const val REQUEST_IMAGE_CAMERA = 1
        const val REQUEST_IMAGE_MEDIA = 2

        const val REQUEST_ADD = 11
        const val REQUEST_UPDATE = 12
        const val RESULT_ADD = 21
        const val RESULT_UPDATE = 22
        const val RESULT_DELETE = 23
    }

    private lateinit var viewModel: AddCommentViewModel

    private var currentPhotoPath: String? = null
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AddCommentViewModel::class.java]

        val id = intent.extras?.getInt(EXTRA_ID)
        if (id != null) {
            viewModel.setPostId(id)
        }
        val isEdit = intent.extras?.getBoolean(EXTRA_EDIT)
        if (isEdit != null) {
            this.isEdit = isEdit
        }
        if (this.isEdit) {
            requestData()
            btn_add.text = "Update"
        }
        val votes = intent.extras?.getFloat(EXTRA_VOTES)
        if (votes != null) {
            rb_votes.rating = votes
        }
        img_place.setOnClickListener {
            showDialogSelectImage()
        }
        btn_add.setOnClickListener {
            if (validateForm()) {
                if (this.isEdit) {
                    updateComment()
                } else {
                    createComment()
                }
            }
        }
    }

    private fun validateForm() : Boolean {
        var ready = true
        val ratings = rb_votes.rating
        val comment = edt_comment.text.toString()

        if (ratings == 0f || comment.isEmpty()) {
            ready = false
            showMessage("Anda Belum Mengisi Semua Datanya")
        }
        return ready
    }

    private fun requestData() {
        viewModel.comment.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        response.data?.let { populateData(it) }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.comment_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete -> {
                showDialogDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogDelete() {
        AlertDialog.Builder(this)
            .setMessage("Apakah anda yakin menghapus komentar ini ?")
            .setPositiveButton("Ya"
            ) { _, _ -> deleteComment() }
            .setNegativeButton("Batal"
            ) { dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }

    private fun populateData(comment: Comment) {
        rb_votes.rating = comment.votes.toFloat()
        edt_comment.setText(comment.comment)
        if (comment.images.isNotEmpty()) {
            Glide.with(this)
                .load(ApiClient.getImageCommentUrl(comment.images[0].name))
                .into(img_place)
        }
    }

    private fun showDialogSelectImage() {
        AlertDialog.Builder(this)
            .setTitle("Pilih Gambar Dari")
            .setItems(R.array.arr_media) { _, which ->
                when(which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> getImageFromMedia()
                }
            }
            .create()
            .show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = createImageFile()
                photoFile?.also {
                    val photoUri: Uri = FileProvider.getUriForFile(
                        this,
                        "com.d3itb.tournesia.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }

    }

    private fun getImageFromMedia() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_MEDIA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
            Glide.with(this)
                .load(File(currentPhotoPath))
                .into(img_place)
        }
        else if (requestCode == REQUEST_IMAGE_MEDIA && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                currentPhotoPath = getRealPathFromUri(uri)
            }
            Glide.with(this)
                .load(uri)
                .into(img_place)
        }
    }

    private fun getRealPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, uri, projection, null, null, null)
        val cursor = loader.loadInBackground()
        val index = cursor?.getColumnIndexOrThrow(projection[0])
        cursor?.moveToFirst()
        val result = index?.let { cursor?.getString(it) }
        cursor?.close()
        return result ?: "not found"
    }

    private fun createComment() {
        var images: MultipartBody.Part? = null
        if (currentPhotoPath != null) {
            val file = File(currentPhotoPath)
            val requestBody = RequestBody.create(MediaType.parse("images/*"), file)
            images = MultipartBody.Part.createFormData("images[0]", file.name, requestBody)
        }
        val params = HashMap<String, RequestBody>()
        params["votes"] = createPartFromString(rb_votes.rating.toString())
        params["comment"] = createPartFromString(edt_comment.text.toString())
        viewModel.createComment(images, params)?.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        setResult(RESULT_ADD)
                        finish()
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    private fun updateComment() {
        var images: MultipartBody.Part? = null
        if (currentPhotoPath != null) {
            val file = File(currentPhotoPath)
            val requestBody = RequestBody.create(MediaType.parse("images/*"), file)
            images = MultipartBody.Part.createFormData("images[0]", file.name, requestBody)
        }
        val params = HashMap<String, RequestBody>()
        params["votes"] = createPartFromString(rb_votes.rating.toString())
        params["comment"] = createPartFromString(edt_comment.text.toString())
        viewModel.updateComment(images, params)?.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        setResult(RESULT_UPDATE)
                        finish()
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    private fun deleteComment() {
        viewModel.deleteComment()?.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        setResult(RESULT_DELETE)
                        finish()
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    private fun createPartFromString(text: String) : RequestBody {
        return RequestBody.create(MultipartBody.FORM, text)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progress_bar.visibility = View.VISIBLE
            container.visibility = View.GONE
        } else {
            progress_bar.visibility = View.GONE
            container.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}