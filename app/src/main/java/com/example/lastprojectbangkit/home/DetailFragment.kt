package com.example.lastprojectbangkit.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.example.lastprojectbangkit.databinding.FragmentDetailBinding

class DetailFragment : DialogFragment() {

    private var _detailbinding : FragmentDetailBinding?= null
    private val binding get() = _detailbinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _detailbinding = FragmentDetailBinding.inflate(inflater,container,false)
       return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initLoadDetail()

    }
    private fun initLoadDetail(){
        var name = binding.tvFaceDisease
        var description = binding.tvDetailDesc
        val bundle = DetailFragmentArgs.fromBundle(arguments as Bundle)
        val move= TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.slide_bottom).setDuration(60L)

        Glide.with(requireActivity())
            .load(bundle.image)
            .into(binding.ivDetailAvatar)
        description.text = bundle.detailDescription
        name.text = bundle.detailName


        sharedElementEnterTransition = move
        sharedElementReturnTransition = move
    }

}