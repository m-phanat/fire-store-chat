package com.arukas.authen.register

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.arukas.authen.R
import com.arukas.authen.databinding.ActivityRegisterBinding
import com.arukas.authen.utils.AuthenConstants
import com.arukas.base.BaseActivity
import com.arukas.base.navigator.AppNavigator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yalantis.ucrop.UCrop
import java.io.File


class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterActivityViewModel>() {
    override val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: RegisterActivityViewModel by viewModels()

    override fun initView() {
        initRegisterListener()

        binding.btnConfirm.setOnClickListener {
            validateForm()
        }

        binding.imgAvatar.setOnClickListener {
            pickProfileImage()
        }
    }

    private fun initRegisterListener() {
        viewModel.setOnRegisterSuccessListener {
            (application as AppNavigator).onLogin()
        }
    }

    private fun pickProfileImage() {
        binding.imgAvatar.setOnClickListener {
            //requestPermission()
            chooseProfileImage()
        }
    }

    private fun chooseProfileImage() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayListOf("image/*"))
            }

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, AuthenConstants.REQUEST_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                AuthenConstants.REQUEST_PICK_IMAGE -> {
                    data?.data?.let { fileUri ->
                        val file = File(getDir("temp", Context.MODE_PRIVATE), "profile_image.png")
                        val cropUri = file.toUri()
                        UCrop.of(fileUri, cropUri)
                            .withAspectRatio(1f, 1f)
                            .withMaxResultSize(450, 450)
                            .withOptions(UCrop.Options().apply {
                                setCompressionFormat(Bitmap.CompressFormat.PNG)
                                setCompressionQuality(90)
                                setToolbarColor(
                                    ContextCompat.getColor(
                                        this@RegisterActivity,
                                        R.color.colorPrimary
                                    )
                                )
                                setStatusBarColor(
                                    ContextCompat.getColor(
                                        this@RegisterActivity,
                                        R.color.colorPrimaryDark
                                    )
                                )
                                setToolbarWidgetColor(
                                    ContextCompat.getColor(
                                        this@RegisterActivity,
                                        R.color.white
                                    )
                                )
                            })
                            .start(this@RegisterActivity)
                    }
                }

                UCrop.REQUEST_CROP -> {
                    data?.let {
                        val cropUri = UCrop.getOutput(data)

                        Glide.with(this@RegisterActivity)
                            .load(cropUri)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .centerCrop()
                            .placeholder(R.drawable.ic_add_user)
                            .error(R.drawable.ic_add_user)
                            .into(binding.imgAvatar)

                        viewModel.setProfileUri(cropUri)
                    }
                }
            }
        }
    }

    private fun validateForm() {
        val username = binding.tbxEmail.text?.toString().orEmpty()
        val password = binding.tbxPassword.text?.toString().orEmpty()
        val confirmPassword = binding.tbxCPassword.text?.toString().orEmpty()
        val firstName = binding.tbxFirstName.text?.toString().orEmpty()
        val lastName = binding.tbxLastName.text?.toString().orEmpty()

        viewModel.validateForm(username, password, confirmPassword, firstName, lastName)
    }
}