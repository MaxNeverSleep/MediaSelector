package com.max.mediaselector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.max.mediaselector.databinding.MediaSelectorActivityMediaListBinding
import com.max.mediaselector.view.MediaSelectorRecyclerView
import kotlin.math.max

class MediaSelectorListActivity : AppCompatActivity() {

    private val binding: MediaSelectorActivityMediaListBinding by lazy {
        MediaSelectorActivityMediaListBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        if (binding.mediaSelectorRecyclerView.isInitialized) {
            binding.mediaSelectorRecyclerView.notifyDataSetChanged()
            refreshSelectedCountText(
                MediaSelectorResult.getSelectedCount(),
                MediaSelectorResult.getMaxCount()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaSelectorResult.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.mediaSelectorToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mediaSelectorToolBar.setNavigationOnClickListener {
            finish()
        }

        MediaSelectorResult.init(intent.getIntExtra(MediaSelectorExtras.MAX_SELECT_COUNT, 1))

        binding.mediaSelectorRecyclerView.init(
            enableImage = intent.getBooleanExtra(MediaSelectorExtras.IMAGE_ENABLE, true),
            enableVideo = intent.getBooleanExtra(MediaSelectorExtras.VIDEO_ENABLE, true),
            enableSelect = intent.getBooleanExtra(MediaSelectorExtras.SELECT_ENABLE, true),
        )

        refreshSelectedCountText(0, MediaSelectorResult.getMaxCount())

        binding.mediaSelectorRecyclerView.setOnSelectCountChangedListener(object :
            MediaSelectorRecyclerView.OnSelectMediaFileListener {

            override fun onSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    MediaSelectorResult.getSelectedCount(),
                    MediaSelectorResult.getMaxCount()
                )
            }

            override fun onUnSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    MediaSelectorResult.getSelectedCount(),
                    MediaSelectorResult.getMaxCount()
                )
            }
        })

        binding.mediaSelectorRecyclerView.setOnMediaItemClickListener(object :
            MediaSelectorRecyclerView.OnMediaItemClickListener {
            override fun onMediaItemClick(position: Int, mediaFile: MediaFile) {
                MediaSelectorResult.addMediaFile(mediaFile)
                val outIntent = Intent()
                outIntent.putExtra(
                    MediaSelectorExtras.SELECTED_MEDIA_FILES,
                    MediaSelectorResult.getResult()
                )
                setResult(RESULT_OK, outIntent)
                MediaSelectorResult.clear()
                finish()
            }
        })

        binding.mediaSelectorSelectConfirmButton.setOnClickListener {
            val outIntent = Intent()
            outIntent.putExtra(
                MediaSelectorExtras.SELECTED_MEDIA_FILES,
                MediaSelectorResult.getResult()
            )
            setResult(RESULT_OK, outIntent)
            MediaSelectorResult.clear()
            finish()
        }

    }

    private fun refreshSelectedCountText(currentSelectCount: Int, maxSelectCount: Int) {
        if (maxSelectCount == 1) {
            binding.mediaSelectorSelectConfirmButton.text =
                getString(
                    R.string.media_selector_single_confirm_button
                )
        } else {
            binding.mediaSelectorSelectConfirmButton.text =
                getString(
                    R.string.media_selector_confirm_button_pattern,
                    currentSelectCount,
                    maxSelectCount
                )
        }
    }


}