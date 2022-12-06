package com.max.mediaselector

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.max.mediaselector.view.MediaSelectorRecyclerView

class MediaSelectorActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_MEDIA_FILES = "selected_media_files"
    }

    private lateinit var mediaSelectorRecyclerView: MediaSelectorRecyclerView
    private lateinit var mediaSelectConfirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_selector_activity_media_selector)
        val mediaSelectorToolbar = findViewById<Toolbar>(R.id.media_selector_tool_bar)

        setSupportActionBar(mediaSelectorToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val maxSelectCount = 9

        mediaSelectorRecyclerView = findViewById(R.id.media_selector_recycler_view)
        mediaSelectorRecyclerView.init(
            enableImage = true,
            enableVideo = true,
            enableSelect = true,
            maxSelectCount = 9
        )

        mediaSelectConfirmButton = findViewById(R.id.media_selector_select_confirm_button)

        refreshSelectedCountText(0, maxSelectCount)
        mediaSelectorRecyclerView.setOnSelectCountChangedListener(object :
            MediaSelectorRecyclerView.OnSelectMediaFileListener {

            override fun onSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    mediaSelectorRecyclerView.getSelectedMediaFiles().size,
                    maxSelectCount
                )
            }

            override fun onUnSelect(mediaFile: MediaFile) {
                refreshSelectedCountText(
                    mediaSelectorRecyclerView.getSelectedMediaFiles().size,
                    maxSelectCount
                )
            }
        })

        mediaSelectConfirmButton.setOnClickListener {
            val outIntent = Intent()
            outIntent.putExtra(
                SELECTED_MEDIA_FILES,
                mediaSelectorRecyclerView.getSelectedMediaFiles()
            )
            setResult(RESULT_OK, outIntent)
            finish()
        }
    }

    private fun refreshSelectedCountText(currentSelectCount: Int, maxSelectCount: Int) {
        mediaSelectConfirmButton.text =
            getString(
                R.string.media_selector_confirm_button_pattern,
                currentSelectCount,
                maxSelectCount
            )
    }

}