package com.max.mediaselector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.max.mediaselector.databinding.MediaSelectorActivityMediaListBinding
import com.max.mediaselector.view.MediaSelectorRecyclerView

class MediaSelectorListActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_MEDIA_FILES = "selected_media_files"
    }

    private val binding: MediaSelectorActivityMediaListBinding by lazy {
        MediaSelectorActivityMediaListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            finish()
            SecurityException("media selector need read external storage permission").printStackTrace()
        }

        setSupportActionBar(binding.mediaSelectorToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val maxSelectCount = 9

        binding.mediaSelectorRecyclerView.init(
            enableImage = true,
            enableVideo = true,
            enableSelect = true,
            maxSelectCount = 9
        )

        refreshSelectedCountText(0, maxSelectCount)
        binding.mediaSelectorRecyclerView.setOnSelectCountChangedListener(object :
            MediaSelectorRecyclerView.OnSelectMediaFileListener {

            override fun onSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    binding.mediaSelectorRecyclerView.getSelectedMediaFiles().size,
                    maxSelectCount
                )
            }

            override fun onUnSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    binding.mediaSelectorRecyclerView.getSelectedMediaFiles().size,
                    maxSelectCount
                )
            }
        })

        binding.mediaSelectorRecyclerView.setOnMediaItemClickListener(object :
            MediaSelectorRecyclerView.OnMediaItemClickListener {
            override fun onMediaItemClick(position: Int, mediaFile: MediaFile) {
                val intent = Intent(this@MediaSelectorListActivity, MediaSelectorPreviewActivity::class.java)
                intent.putParcelableArrayListExtra(
                    "media_files",
                    binding.mediaSelectorRecyclerView.getMediaFiles()
                )
                intent.putExtra("position", position)
                startActivity(intent)

            }
        })

        binding.mediaSelectorSelectConfirmButton.setOnClickListener {
            val outIntent = Intent()
            outIntent.putExtra(
                SELECTED_MEDIA_FILES,
                binding.mediaSelectorRecyclerView.getSelectedMediaFiles()
            )
            setResult(RESULT_OK, outIntent)
            finish()
        }

    }

    private fun refreshSelectedCountText(currentSelectCount: Int, maxSelectCount: Int) {
        binding.mediaSelectorSelectConfirmButton.text =
            getString(
                R.string.media_selector_confirm_button_pattern,
                currentSelectCount,
                maxSelectCount
            )
    }


}