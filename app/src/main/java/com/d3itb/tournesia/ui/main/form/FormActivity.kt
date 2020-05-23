package com.d3itb.tournesia.ui.main.form

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.d3itb.tournesia.model.Category
import com.d3itb.tournesia.model.City
import com.d3itb.tournesia.model.Province
import com.d3itb.tournesia.viewmodel.ViewModelFactory
import com.d3itb.tournesia.vo.Status
import kotlinx.android.synthetic.main.activity_form.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FormActivity : AppCompatActivity() {

    private lateinit var categoryAdapter: ArrayAdapter<Category>
    private lateinit var provinceAdapter: ArrayAdapter<Province>
    private lateinit var cityAdapter: ArrayAdapter<City>
    private lateinit var viewModel: FormViewModel
    private lateinit var loadingDialog: AlertDialog

    private var currentPhotoPath: String? = null
    private var categoryId = 0

    companion object {
        const val REQUEST_IMAGE_CAMERA = 1
        const val REQUEST_IMAGE_MEDIA = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[FormViewModel::class.java]
        loadingDialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_loading)
            .setCancelable(false)
            .create()

        act_category.setAdapter(categoryAdapter)
        act_city.setAdapter(cityAdapter)
        act_province.setAdapter(provinceAdapter)

        act_province.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                cityAdapter.clear()
                act_city.clearListSelection()
                val id = provinceAdapter.getItem(position)?.id
                if (id != null) {
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
        img_post.setOnClickListener {
            showDialogSelectImage()
        }
        btn_add.setOnClickListener {
            if (validateForm()) {
                createPost()
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
        viewModel.city.observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        val cities = response.data
                        if (cities != null) {
                            cityAdapter.addAll(cities)
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        showMessage(response.message.toString())
                    }
                }
            }
        })
    }

    private fun validateForm(): Boolean {
        var ready = true
        val name = edt_name.text.toString()
        val address = edt_address.text.toString()
        val description = edt_description.text.toString()
        val province = act_province.text.toString()
        val city = act_city.text.toString()
        if (name.isEmpty() || address.isEmpty() || description.isEmpty() || province.isEmpty() || city.isEmpty()) {
            ready = false
        }
        if (categoryId == 0 || currentPhotoPath == null) {
            ready = false
        }
        return ready
    }

    private fun clearForm() {
        currentPhotoPath = null
        Glide.with(this)
            .load(R.drawable.ic_image_outline_48dp)
            .into(img_post)
        edt_name.text = null
        edt_address.text = null
        edt_description.text = null
        act_category.clearListSelection()
        act_province.clearListSelection()
        act_city.clearListSelection()
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
        params["province"] = createPartFromString(act_province.text.toString())
        params["city"] = createPartFromString(act_city.text.toString())
        viewModel.createPost(images, params).observe(this, Observer { response ->
            if (response != null) {
                when(response.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        showMessage("Sukses Create Post")
                        clearForm()
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
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
