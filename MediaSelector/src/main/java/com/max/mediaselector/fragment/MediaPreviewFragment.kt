package com.max.mediaselector.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.max.mediaselector.MediaFile
import com.max.mediaselector.databinding.MediaSelectorFragmentPreviewBinding
import com.max.mediaselector.databinding.MediaSelectorFragmentPreviewImageBinding

class MediaPreviewFragment : Fragment {

    companion object {
        fun getInstance(position: Int, mediaFiles: ArrayList<MediaFile>): MediaPreviewFragment {
            return MediaPreviewFragment(position, mediaFiles)
        }
    }

    constructor()

    private constructor(position: Int, mediaFiles: ArrayList<MediaFile>) {
        this.position = position
        this.mediaFiles = mediaFiles
    }

    private val binding: MediaSelectorFragmentPreviewBinding by lazy {
        MediaSelectorFragmentPreviewBinding.inflate(layoutInflater)
    }

    private var position: Int = 0
    private var mediaFiles = ArrayList<MediaFile>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).setSupportActionBar(binding.mediaSelectorToolBar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mediaSelectorToolBar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int {
                return mediaFiles.size
            }

            override fun createFragment(position: Int): Fragment {
                return when (mediaFiles[position].mediaType) {
                    MediaFile.MediaType.IMAGE -> {
                        MediaImagePreviewFragment.getInstance(mediaFiles[position])
                    }
                    MediaFile.MediaType.VIDEO -> {
                        MediaVideoPreviewFragment.getInstance(mediaFiles[position])
                    }
                }
            }

        }
        binding.mediaSelectorViewPager.adapter = adapter
        binding.mediaSelectorViewPager.setCurrentItem(position, false)
    }

}