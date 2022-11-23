package com.max.mediaselector

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.max.mediaselector.view.MediaSelectorRecyclerView

class MediaSelectorActivity : AppCompatActivity() {

    private lateinit var mediaSelectorRecyclerView: MediaSelectorRecyclerView
    private lateinit var mediaSelectConfirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_selector_activity_media_selector)
        val mediaSelectorToolbar = findViewById<Toolbar>(R.id.media_selector_tool_bar)

        setSupportActionBar(mediaSelectorToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val maxSelectCount = 9

        val mediaSelectCountTextView =
            findViewById<TextView>(R.id.media_selector_select_count_text_view)
        mediaSelectorRecyclerView = findViewById(R.id.media_selector_recycler_view)
        mediaSelectorRecyclerView.init(true, true, true, 9)
        mediaSelectorRecyclerView.setOnSelectCountChangedListener(object :
            MediaSelectorRecyclerView.OnSelectCountChangedListener {
            override fun onSelectCountChange(count: Int) {
                mediaSelectCountTextView.text =
                    getString(R.string.media_selector_confirm_button_pattern, count, maxSelectCount)
            }
        })

        mediaSelectConfirmButton = findViewById(R.id.media_selector_confirm_button)
        mediaSelectConfirmButton.setOnClickListener {
            val outBundle = Bundle()
            outBundle.putSerializable("1", mediaSelectorRecyclerView.getSelectedMediaFiles())
            val outIntent = Intent()
            outIntent.putExtra("1", outBundle)
            setResult(RESULT_OK, outIntent)
            finish()
        }
    }

}