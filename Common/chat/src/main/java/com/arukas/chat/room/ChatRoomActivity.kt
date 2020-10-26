package com.arukas.chat.room

import android.Manifest
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arukas.base.BaseActivity
import com.arukas.chat.R
import com.arukas.chat.adapter.message.MessageAdapter
import com.arukas.chat.databinding.ActivityChatRoomBinding
import com.arukas.common.ChatConstant
import com.arukas.network.model.Single
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding, ChatRoomActivityViewModel>(),
    TextWatcher {
    private val layoutManager by lazy {
        LinearLayoutManager(this@ChatRoomActivity).apply {
            stackFromEnd = true
        }
    }
    private val adapter by lazy { MessageAdapter() }
    private val easyImage by lazy {
        EasyImage.Builder(this).setCopyImagesToPublicGalleryFolder(false).build()
    }
    private val cameraCallback by lazy { CameraCallback() }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, room: Single): Intent {
            return Intent(context, ChatRoomActivity::class.java).apply {
                putExtra(ChatConstant.EXTRA_ROOM, room)
            }
        }
    }

    override val binding: ActivityChatRoomBinding by lazy {
        ActivityChatRoomBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: ChatRoomActivityViewModel by viewModels()

    override fun initView() {
        observeMessages()
        initData()
        initToolbar()
        initInput()
        initOptions()
        initMessages()

        viewModel.loadRoomDetail()
    }

    private fun initOptions() {
        binding.imgCamera.setOnClickListener {
            requestPermissions(this::openCamera)
        }

        binding.imgPhoto.setOnClickListener {
            requestPermissions(this::openPicker)
        }
    }

    private fun openCamera() {
        easyImage.openCameraForImage(this)
    }

    private fun openPicker() {
        easyImage.openGallery(this)
    }

    private fun requestPermissions(action: () -> Unit) {
        Dexter.withContext(this@ChatRoomActivity)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        action.invoke()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showAlert(getString(R.string.request_camera_permission)) {
                        token?.continuePermissionRequest()
                    }
                }

            }).check()
    }

    private fun initInput() {
        binding.tbxMessage.setOnClickListener {
            hideOptions()
        }

        binding.tbxMessage.addTextChangedListener(this)

        binding.imgShowOption.setOnClickListener {
            showOptions()
        }

        binding.imgSend.setOnClickListener {
            val text = binding.tbxMessage.text?.toString().orEmpty()
            viewModel.sendTextMessage(text)

            binding.tbxMessage.setText("")
        }
    }

    private fun showOptions() {
        if (binding.imgShowOption.visibility == View.VISIBLE) {
            binding.imgCamera.visibility = View.VISIBLE
            binding.imgPhoto.visibility = View.VISIBLE
            binding.imgShowOption.visibility = View.GONE
        }
    }

    private fun hideOptions() {
        if (binding.imgShowOption.visibility == View.GONE) {
            binding.imgCamera.visibility = View.GONE
            binding.imgPhoto.visibility = View.GONE
            binding.imgShowOption.visibility = View.VISIBLE
        }
    }

    private fun observeMessages() {
        viewModel.getMessages().observe(this, Observer {
            adapter.setData(it)
            if (it.isNotEmpty()) {
                binding.rcvMessage.post {
                    binding.rcvMessage.scrollToPosition(it.size - 1)
                }
            }
        })
    }

    private fun initMessages() {
        binding.rcvMessage.setHasFixedSize(false)
        binding.rcvMessage.layoutManager = layoutManager
        binding.rcvMessage.adapter = adapter
    }

    private fun initData() {
        intent.getParcelableExtra<Single>(ChatConstant.EXTRA_ROOM)?.let {
            viewModel.setRoom(it)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = viewModel.getRoomName()
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(requestCode, resultCode, data, this, cameraCallback)

    }

    inner class CameraCallback : DefaultCallback() {
        override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
            //
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        hideOptions()
    }

    override fun afterTextChanged(s: Editable?) {
        //
    }
}