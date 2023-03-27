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
    private var selectedCount: Int = 0
    private var maxSelectCount: Int = 9
    private var mediaFiles = ArrayList<MediaFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.hasExtra("position")) {
            mediaFiles = loadMediaFilesCaches()
            position = intent.getIntExtra("position", 0)
            selectedCount = intent.getIntExtra("selected_count", 0)
            maxSelectCount = intent.getIntExtra("max_select_count", 0)
        } else {
            finish()
        }

        setSupportActionBar(binding.mediaSelectorToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // position/total count
        binding.mediaSelectorToolBar.title =
            getString(R.string.media_selector_confirm_indicator, position + 1, mediaFiles.size)

        binding.mediaSelectorCheckText.text =
            getString(R.string.media_selector_select_button, selectedCount, maxSelectCount)

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
            }
        })

        binding.mediaSelectorViewPager.offscreenPageLimit = 3
        binding.mediaSelectorViewPager.adapter = adapter
        binding.mediaSelectorViewPager.setCurrentItem(position, false)

        binding.mediaSelectorCheckBox.isSelected = mediaFiles[position].checked
        binding.mediaSelectorPreviewCheckbox.setOnClickListener {
            mediaFiles[position].checked = !mediaFiles[position].checked
            binding.mediaSelectorCheckText.text =
                getString(R.string.media_selector_select_button, selectedCount, maxSelectCount)
            binding.mediaSelectorCheckBox.isSelected = mediaFiles[position].checked
        }
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