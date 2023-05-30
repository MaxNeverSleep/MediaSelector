package com.max.mediaselector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
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

        if (intent.hasExtra("position")) {
            mediaFiles = loadMediaFilesCaches()
            position = intent.getIntExtra("position", 0)
        } else {
            finish()
        }

        setSupportActionBar(binding.mediaSelectorToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mediaSelectorToolBar.setNavigationOnClickListener {
            finish()
        }

        // position/total count
        binding.mediaSelectorToolBar.title =
            getString(R.string.media_selector_confirm_indicator, position + 1, mediaFiles.size)

        if (MediaSelectorResult.getMaxCount() == 1) {
            binding.mediaSelectorCheckText.text =
                getString(
                    R.string.media_selector_single_select_button,
                )
        } else {
            binding.mediaSelectorCheckText.text =
                getString(
                    R.string.media_selector_select_button,
                    MediaSelectorResult.getSelectedCount(),
                    MediaSelectorResult.getMaxCount()
                )
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

        binding.mediaSelectorViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.mediaSelectorToolBar.title =
                    getString(
                        R.string.media_selector_confirm_indicator,
                        position + 1,
                        mediaFiles.size
                    )
                binding.mediaSelectorCheckBox.isSelected = mediaFiles[position].checked
                binding.mediaSelectorPreviewCheckbox.setOnClickListener {
                    // if changed to checked
                    if (!mediaFiles[position].checked) {
                        if (MediaSelectorResult.addMediaFile(mediaFiles[position])) {
                            binding.mediaSelectorCheckBox.isSelected = true
                            mediaFiles[position].checked = true
                        }
                    } else {
                        MediaSelectorResult.removeMediaFile(mediaFiles[position])
                        binding.mediaSelectorCheckBox.isSelected = false
                        mediaFiles[position].checked = false
                    }
                    if (MediaSelectorResult.getMaxCount() == 1) {
                        binding.mediaSelectorCheckText.text =
                            getString(
                                R.string.media_selector_single_select_button,
                            )
                    } else {
                        binding.mediaSelectorCheckText.text =
                            getString(
                                R.string.media_selector_select_button,
                                MediaSelectorResult.getSelectedCount(),
                                MediaSelectorResult.getMaxCount()
                            )
                    }
                }
            }
        })

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

    private fun loadMediaFilesCaches(): java.util.ArrayList<MediaFile> {
        mediaFiles.clear()

        val images = MediaScanner.getLoadedImagesCache()
        mediaFiles.addAll(images)

        val videos = MediaScanner.getLoadedVideoCache()
        mediaFiles.addAll(videos)

        mediaFiles.sortWith { p0, p1 ->
            if (p0 != null && p1 != null) {
                return@sortWith p1.timeStamp.compareTo(p0.timeStamp)
            }
            return@sortWith 0
        }
        return mediaFiles
    }

}