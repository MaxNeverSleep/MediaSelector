package com.max.mediaselector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.max.mediaselector.databinding.MediaSelectorActivityPreviewBinding
import com.max.mediaselector.fragment.MediaImagePreviewFragment
import com.max.mediaselector.fragment.MediaVideoPreviewFragment

class MediaSelectorPreviewActivity : AppCompatActivity() {

    private val binding: MediaSelectorActivityPreviewBinding by lazy {
        MediaSelectorActivityPreviewBinding.inflate(layoutInflater)
    }

    private var position: Int = 0
    private var mediaFiles = ArrayList<MediaFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.hasExtra("media_files") && intent.hasExtra("position")) {
            mediaFiles = intent.getParcelableArrayListExtra("media_files")!!
            position = intent.getIntExtra("position", 0)
        } else {
            finish()
        }

        setSupportActionBar(binding.mediaSelectorToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // position/total count
        binding.mediaSelectorIndicator.text =
            getString(R.string.media_selector_confirm_indicator, position + 1, mediaFiles.size)

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

        binding.mediaSelectorViewPager.offscreenPageLimit = 3
        binding.mediaSelectorViewPager.adapter = adapter
        binding.mediaSelectorViewPager.setCurrentItem(position, false)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.media_selector_fragment_finish_enter_anim,
            R.anim.media_selector_fragment_finish_exit_anim
        )
    }


}