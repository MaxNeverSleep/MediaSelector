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
        const val MAX_SELECT_COUNT = "max_select_count"
    }

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            finish()
            SecurityException("media selector need read external storage permission").printStackTrace()
        }

        setSupportActionBar(binding.mediaSelectorToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mediaSelectorToolBar.setNavigationOnClickListener {
            finish()
        }

        MediaSelectorResult.init(intent.getIntExtra(MAX_SELECT_COUNT, 9))

        binding.mediaSelectorRecyclerView.init(
            enableImage = true,
            enableVideo = true,
            enableSelect = true,
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
                val intent =
                    Intent(this@MediaSelectorListActivity, MediaSelectorPreviewActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.media_selector_fragment_start_enter_anim,
                    R.anim.media_selector_fragment_start_exit_anim
                )
            }
        })

        binding.mediaSelectorSelectConfirmButton.setOnClickListener {
            val outIntent = Intent()
            outIntent.putExtra(
                SELECTED_MEDIA_FILES,
                MediaSelectorResult.getResult()
            )
            setResult(RESULT_OK, outIntent)
            MediaSelectorResult.clear()
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