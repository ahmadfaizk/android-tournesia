package com.d3itb.tournesia.ui.main.form

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.d3itb.tournesia.R
import com.d3itb.tournesia.api.ApiClient
import com.d3itb.tournesia.model.Category
import com.d3itb.tournesia.model.Post
import com.d3itb.tournesia.model.Regency
import com.d3itb.tournesia.model.Province
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.activity_form.container
import kotlinx.android.synthetic.main.activity_form.img_post
import kotlinx.android.synthetic.main.activity_form.progress_bar
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FormActivity : AppCompatActivity() {

    private lateinit var categoryAdapter: ArrayAdapter<Category>
    private lateinit var provinceAdapter: ArrayAdapter<Province>
    private lateinit var regencyAdapter: ArrayAdapter<Regency>
    private lateinit var viewModel: FormViewModel

    private var currentPhotoPath: String? = null
    private var categoryId = 0
    private var provinceId = 0
    private var regencyId = 0
    private var isEdit = false

    companion object {
        const val EXTRA_ID = "extra_id"
        const val REQUEST_IMAGE_CAMERA = 1
        const val REQUEST_IMAGE_MEDIA = 2

        const val REQUEST_UPDATE = 102
        const val RESULT_UPDATE = 202
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        supportActionBar?.title = "Add New Post"

        categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        regencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[FormViewModel::class.java]

        act_category.setAdapter(categoryAdapter)
        act_city.setAdapter(regencyAdapter)
        act_province.setAdapter(provinceAdapter)

        act_province.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                regencyAdapter.clear()
                act_city.text.clear()
                val id = provinceAdapter.getItem(position)?.id
                if (id != null) {
                    provinceId = id
                    viewModel.setProvinceId(id)
                }
            }
        act_category.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val id = categoryAdapter.getItem(position)?.id
                if (id != null) {
                    categoryId = id
                }
            }
        act_city.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val id = regencyAdapter.getItem(position)?.id
                if (id != null) {
                    regencyId = id
                }
            }
        img_post.setOnClickListener {
            showDialogSelectImage()
        }
        btn_add.setOnClickListener {
            if (validateForm()) {
                if (isEdit) {
                    updatePost()
                } else {
                    createPost()
                }
            }
        }

        viewModel.category.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        val categories = response.data
                        if (categories != null) {
                            categoryAdapter.addAll(categories)
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
        viewModel.province.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        val provinces = response.data
                        if (provinces != null) {
                            provinceAdapter.addAll(provinces)
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
        viewModel.regency.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        val cities = response.data
                        if (cities != null) {
                            regencyAdapter.addAll(cities)
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })

        val id = intent.getIntExtra(EXTRA_ID, 0)
        if (id != 0) {
            isEdit = true
            viewModel.setPostId(id)
            getPostData()
            supportActionBar?.title = "Edit Post"
            btn_add.text = "Update"
        }
    }

    private fun validateForm(): Boolean {
        var ready = true
        val name = edt_name.text.toString()
        val address = edt_address.text.toString()
        val description = edt_description.text.toString()
        val province = act_province.text.toString()
        val city = act_city.text.toString()
        if (name.isEmpty() || address.isEmpty() || description.isEmpty() || province.isEmpty() || city.isEmpty() || categoryId == 0) {
            ready = false
            showMessage("Anda Belum Mengisi Semua Datanya")
        }
        if (currentPhotoPath == null && !isEdit) {
            ready = false
            showMessage("Anda Belum Memilih Foto")
        }
        return ready
    }

    private fun getPostData() {
        viewModel.post.observe(this, Observer { response ->
            if (response!= null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        response.data?.let { populatePost(it) }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    private fun populatePost(post: Post) {
        Glide.with(this)
            .load(ApiClient.getImagePostUrl(post.images[0].name))
            .into(img_post)
        edt_name.setText(post.name)
        edt_address.setText(post.address)
        edt_description.setText(post.description)
        act_category.setText(post.category)
        act_province.setText(post.province)
        act_city.setText(post.regency)
        categoryId = post.idCategory
        provinceId = post.idProvince
        regencyId = post.idRegency
        viewModel.setProvinceId(post.idProvince)
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
                .into(img_post)
        }
        else if (requestCode == REQUEST_IMAGE_MEDIA && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                currentPhotoPath = getRealPathFromUri(uri)
            }
            Glide.with(this)
                .load(uri)
                .into(img_post)
        }
    }

    private fun createPost() {
        val file = File(currentPhotoPath)
        val requestBody = RequestBody.create(MediaType.parse("images/*"), file)
        val images = MultipartBody.Part.createFormData("images[0]", file.name, requestBody)
        val params = HashMap<String, RequestBody>()
        params["name"] = createPartFromString(edt_name.text.toString())
        params["id_category"] = createPartFromString(categoryId.toString())
        params["address"] = createPartFromString(edt_address.text.toString())
        params["description"] = createPartFromString(edt_description.text.toString())
        params["id_province"] = createPartFromString(provinceId.toString())
        params["id_regency"] = createPartFromString(regencyId.toString())
        viewModel.createPost(images, params).observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        showMessage("Berhasil Menambahkan Tempat Baru")
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

    private fun updatePost() {
        var images: MultipartBody.Part? = null
        if (currentPhotoPath != null) {
            val file = File(currentPhotoPath)
            val requestBody = RequestBody.create(MediaType.parse("images/*"), file)
            images = MultipartBody.Part.createFormData("images[0]", file.name, requestBody)
        }
        val params = HashMap<String, RequestBody>()
        params["name"] = createPartFromString(edt_name.text.toString())
        params["id_category"] = createPartFromString(categoryId.toString())
        params["address"] = createPartFromString(edt_address.text.toString())
        params["description"] = createPartFromString(edt_description.text.toString())
        params["id_province"] = createPartFromString(provinceId.toString())
        params["id_regency"] = createPartFromString(regencyId.toString())
        viewModel.updatePost(images, params).observe(this, Observer { response ->
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

    private fun createPartFromString(text: String) : RequestBody {
        return RequestBody.create(MultipartBody.FORM, text)
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
