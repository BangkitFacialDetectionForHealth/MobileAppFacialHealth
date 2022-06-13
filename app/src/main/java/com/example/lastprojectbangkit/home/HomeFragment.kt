package com.example.lastprojectbangkit.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lastprojectbangkit.R
import com.example.lastprojectbangkit.camera.CameraActivity
import com.example.lastprojectbangkit.databinding.HomeFragmentBinding
import com.example.lastprojectbangkit.setting.SettingViewModel
import com.example.lastprojectbangkit.utilities.reduceFileImage
import com.example.lastprojectbangkit.utilities.rotateBitmap
import com.example.lastprojectbangkit.utilities.uriToFile
import com.example.lastprojectbangkit.view.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class HomeFragment : Fragment() {
    private var _homeFragmentBinding: HomeFragmentBinding? = null
    private val binding get() = _homeFragmentBinding!!
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: HomeViewModel by activityViewModels { factory }
    private val authenticationViewModel : AuthenticationViewModel by activityViewModels {factory}
    private val viewModelSetting: SettingViewModel by activityViewModels{factory}
    private lateinit var result: Bitmap
    private var navView: BottomNavigationView? = null
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeFragmentBinding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navView = requireActivity().findViewById(R.id.nav_view)
        binding.previewImage.setOnClickListener { startCameraX() }
        binding.cameraButton.setOnClickListener { startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.detecButton.setOnClickListener { uploadStory() }
        initObserve()
        initAction()
    }

    private fun initObserve() {
        viewModel.loading.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                loading(it)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (!error) {
                    Toast.makeText(activity, getString(R.string.upload_success), Toast.LENGTH_LONG)
                        .show()
                    startActivity(Intent(activity, DashboardActivity::class.java))
                    activity?.finish()
                }
            }
        }
        viewModel.message.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { msg ->
                val message = getString(R.string.upload_failed)
                Toast.makeText(activity, "$msg: $message", Toast.LENGTH_LONG).show()
            }
        }

        viewModelSetting.getUserName().observe(viewLifecycleOwner){username ->
            binding.greetingText.text = username
        }
    }

    private fun startCameraX() {
        val intent = Intent(activity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            startActivity(Intent(activity, ProfileFragment::class.java))
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.detecButton.isEnabled = false
            binding.tvUploading.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.detecButton.isEnabled = true
            binding.tvUploading.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.detecButton.isEnabled = true
            binding.previewImage.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, requireActivity())

            getFile = myFile

            binding.detecButton.isEnabled = true
            binding.previewImage.setImageURI(selectedImg)
        }
    }

    private fun initAction() {
        binding.toolbar.apply {

            inflateMenu(R.menu.nav_setting)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navigation_setting -> {
                        findNavController().navigate(R.id.action_navigation_home_to_settingFragment)
                        true

                    }
                    else -> false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragmentBinding = null
    }
}