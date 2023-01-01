package com.max.mediaselector.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.max.mediaselector.MediaFile
import com.max.mediaselector.databinding.MediaSelectorFragmentPreviewVideoBinding

class MediaVideoPreviewFragment : Fragment {

    companion object {
        fun getInstance(mediaFile: MediaFile): MediaVideoPreviewFragment {
            return MediaVideoPreviewFragment(mediaFile)
        }
    }

    constructor()

    private constructor(mediaFile: MediaFile) {
        this.mediaFile = mediaFile
    }

    private val binding: MediaSelectorFragmentPreviewVideoBinding by lazy {
        MediaSelectorFragmentPreviewVideoBinding.inflate(layoutInflater)
    }

    private var mediaFile: MediaFile? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (mediaFile == null) {
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).setSupportActionBar(binding.mediaSelectorToolBar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mediaSelectorToolBar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.videoView.setVideoPath(mediaFile?.path)
    }

}