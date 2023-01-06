package com.max.mediaselector.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

        Glide.with(requireActivity())
            .load(mediaFile?.path)
            .thumbnail(0.1f)
            .apply(RequestOptions().frame(0))
            .into(binding.mediaSelectorVideoCover)

        binding.mediaSelectorVideoView.setZOrderOnTop(true)

        binding.mediaSelectorVideoCover.setOnClickListener {
            binding.mediaSelectorVideoView.requestFocus()
            binding.mediaSelectorVideoView.start()
        }

        binding.mediaSelectorVideoView.setOnPreparedListener {
            it.setOnInfoListener { _, what, _ ->
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    binding.mediaSelectorVideoCoverContainer.visibility = View.INVISIBLE
                    return@setOnInfoListener true
                }
                return@setOnInfoListener false
            }
        }

        binding.mediaSelectorVideoView.setOnCompletionListener {
            binding.mediaSelectorVideoCoverContainer.visibility = View.VISIBLE
        }
        binding.mediaSelectorVideoView.setVideoPath(mediaFile?.path)
    }

    override fun onPause() {
        super.onPause()
        binding.mediaSelectorVideoView.suspend()
    }

    override fun onResume() {
        super.onResume()
        binding.mediaSelectorVideoView.resume()
    }


}